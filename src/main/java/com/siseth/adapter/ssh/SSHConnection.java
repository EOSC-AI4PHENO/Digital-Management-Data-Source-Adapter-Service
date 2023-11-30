package com.siseth.adapter.ssh;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.siseth.adapter.constant.AppProperties;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;

@AllArgsConstructor
public class SSHConnection {

    private String username;

    private String password;

    private String host;

    private Integer port;

    public SSHConnection() {
        this.username = AppProperties.SSH_USER;
        this.password = AppProperties.SSH_PASSWORD;
        this.host = AppProperties.SSH_HOST;
        this.port = AppProperties.SSH_PORT;
    }

    @SneakyThrows
    public void sendCommand(String command) {

        Session session = null;
        ChannelExec channel = null;

        try {
            session = new JSch().getSession(username, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
            channel.setOutputStream(responseStream);
            channel.connect();

            while (channel.isConnected()) {
                Thread.sleep(100);
            }

            String responseString = new String(responseStream.toByteArray());
        } finally {
            if (session != null) {
                session.disconnect();
            }
            if (channel != null) {
                channel.disconnect();
            }
        }
    }
}
