package cn.gdiu.controller;

import cn.gdiu.R;
import cn.gdiu.model.Message;
import cn.gdiu.model.User;
import cn.gdiu.repository.MessageRepository;
import cn.gdiu.repository.UserRepository;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@RestController
public class UserController {

    @Resource
    private UserRepository userRepository;

    @Resource
    private MessageRepository messageRepository;


    /**
     * 根据用户查找当前用户
     *
     * @param userId
     * @return
     */
    @GetMapping("findUserByUserId")
    public R findUserByUserId(Long userId) {
        if (userId == null) return R.error("userId 不能为空");
        User user = userRepository.findById(userId).orElse(null);
        List<User> users = Arrays.asList(user);
        return user == null ? R.error("用户不存在") : R.ok(users);
    }


    /**
     * 获取聊天用户
     *
     * @return
     */
    @GetMapping("messageHistory")
    public R messageHistory(Long myId, Long chatId) {
        final List<Message> messageHistoryList = messageRepository.findMessageHistoryList(myId, chatId);
        return R.ok(messageHistoryList);
    }


}
