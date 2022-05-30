package cn.gdiu.config;

import com.corundumstudio.socketio.SocketIOServer;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;


@Data
@Slf4j
@Component
public class ServerRunner implements CommandLineRunner {

    @Autowired
    private SocketIOServer socketIOServer;

    @Override
    public void run(String... args) throws Exception {
        socketIOServer.start();
        log.info("socket.io启动成功！");
    }
}
