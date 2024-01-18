package com.klapertart.expect.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tritr
 * @since 7/24/2023
 */

@Service
@Slf4j
public class ExpectRunner {
    @Autowired
    private ExpectService expectService;

    @PostConstruct
    public void run(){
        List<String> cmdsToExecute = new ArrayList<String>();
        cmdsToExecute.add("ls");
        cmdsToExecute.add("pwd");
        String executeLog = expectService.execute(cmdsToExecute);
        System.out.println("---------------------- LOG EXECUTE -----------------");
        System.out.println(executeLog);
    }
}
