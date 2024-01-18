package com.klapertart.expect;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import expect4j.Expect4j;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


/**
 * @author tritr
 * @since 7/24/2023
 */

@Slf4j
public class SimpleExpect {
    public static void main(String[] args) {
        String USER = "root";
        String HOST = "192.168.8.121";
        String PASSWD = "getpass";

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(USER, HOST);
            session.setPassword(PASSWD);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect(60 * 1000);
            Channel channel = session.openChannel("shell");
            Expect expect = new Expect(channel.getInputStream(),
                    channel.getOutputStream());
            channel.connect();
            expect.expect("#");
            log.info("LOG 1, \n< {} > ------------- < {} >", expect.before, expect.match);
            expect.send("ls\r");
            expect.expect("#");
            log.info("LOG 2, \n< {} > ------------- < {} >", expect.before, expect.match);
            expect.send("exit\r");
            expect.expectEOF();
            log.info("LOG 3, \n< {} > ------------- < {} >", expect.before, expect.match);
            expect.close();
            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
