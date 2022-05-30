package cn.gdiu.config;


import com.corundumstudio.socketio.SocketIOClient;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ClientCache {

    //本地缓存
    private static Map<String, HashMap<UUID, SocketIOClient>> concurrentHashMap = new ConcurrentHashMap<>();


    public void saveClient(String userId, UUID sessionId, SocketIOClient socketIOClient) {
        HashMap<UUID, SocketIOClient> sessionIdClientCache = concurrentHashMap.get(userId);
        if (sessionIdClientCache == null) {
            sessionIdClientCache = new HashMap<>();
        }
        sessionIdClientCache.put(sessionId, socketIOClient);
        concurrentHashMap.put(userId, sessionIdClientCache);
    }

    public HashMap<UUID, SocketIOClient> getUserClient(String userId) {
        return concurrentHashMap.get(userId);
    }


    public void deleteSessionClient(String userId, UUID sessionId) {
        concurrentHashMap.get(userId).remove(sessionId);
    }
}
