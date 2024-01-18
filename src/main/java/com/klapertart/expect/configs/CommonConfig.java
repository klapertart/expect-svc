package com.klapertart.expect.configs;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.klapertart.expect.properties.NetworkProperties;
import expect4j.Expect4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Hashtable;

/**
 * @author tritr
 * @since 7/27/2023
 */

@Configuration
public class CommonConfig {
    @Autowired
    private NetworkProperties networkProperties;

    @Bean
    public Expect4j expect4j() throws JSchException, IOException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(networkProperties.getSshUser(), networkProperties.getSshHost(), networkProperties.getSshPort());
        if (networkProperties.getSshPass() != null) {
            session.setPassword(networkProperties.getSshPass());
        }
        Hashtable<String,String> config = new Hashtable<String,String>();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect(60000);
        ChannelShell channel = (ChannelShell) session.openChannel("shell");
        Expect4j expect = new Expect4j(channel.getInputStream(), channel.getOutputStream());
        channel.connect();

        return expect;
    }
}
