package cn.gdiu.config;


import cn.gdiu.model.Message;
import cn.gdiu.repository.MessageRepository;
import cn.gdiu.util.RedisUtil;
import cn.gdiu.dto.MessageDTO;
import cn.gdiu.model.User;
import cn.gdiu.repository.UserRepository;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Component
@Slf4j
public class EventMonitor {

    @Resource
    private ClientCache clientCache;

    @Resource
    private UserRepository userRepository;

    @Resource
    private MessageRepository messageRepository;

    @Resource
    private RedisUtil redisUtil;


    /**
     * 客户端连接
     *
     * @param client
     */
    @OnConnect
    public void onConnect(SocketIOClient client) {
        String userId = client.getHandshakeData().getSingleUrlParam("userId");
        UUID sessionId = client.getSessionId();
        if (StrUtil.isNotEmpty(userId)) {
            clientCache.saveClient(userId, sessionId, client);
            log.info("{} 建立链接", userId);
            // 创建用户
            User user = new User();
            user.setUserId(Long.parseLong(userId));
            user.setLastTimeDate(new Date());
            final User save = userRepository.save(user);

            System.out.println(save);
        }
    }


    /**
     * 客户端断开
     *
     * @param client
     */
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        String userId = client.getHandshakeData().getSingleUrlParam("userId");
        if (StrUtil.isNotEmpty(userId)) {
            clientCache.deleteSessionClient(userId, client.getSessionId());
            log.info("{} 关闭连接", userId);
        }
    }


    @OnEvent("sendMessage")
    public void sendMessage(SocketIOClient client, MessageDTO messageDto) {
        log.info("收到消息-{}", messageDto.toString());
        messageDto.setTime(new Date().getTime());
        // 建立好友关系
        redisUtil.addZSet("Friend-" + messageDto.getReceiver(), messageDto.getSender());
        redisUtil.addZSet("Friend-" + messageDto.getSender(), messageDto.getReceiver());

        // 存储最后一条消息
        redisUtil.set("lastMessage-" + messageDto.getReceiver(), messageDto);
        redisUtil.set("lastMessage-" + messageDto.getSender(), messageDto);


        final HashMap<UUID, SocketIOClient> userClient = clientCache.getUserClient(messageDto.getReceiver());
        Message message = new Message();
        BeanUtil.copyProperties(messageDto, message);
        messageRepository.save(message);
        if (userClient == null) {
            // 用户不在线
        } else {
            // 用户在线
            for (SocketIOClient item : userClient.values()) {
                item.sendEvent("getMessageInfo", message);
            }
        }
    }


    // 发送好友列表
    @OnEvent("getFriendList")
    public void getFriendList(SocketIOClient client, String userId) {
        final Set<String> key = redisUtil.getZSetOrder("Friend-" + userId);

        List<Map> list = new ArrayList<>();
        for (String item : key) {
            Map<String, Object> map = new HashMap<>();
            MessageDTO messageDTO = redisUtil.get("lastMessage-" + item);
            map.put("userId", item);
            map.put("lastMessage", messageDTO);
            list.add(map);
        }
        client.sendEvent("getFriendList", list);
    }


}
