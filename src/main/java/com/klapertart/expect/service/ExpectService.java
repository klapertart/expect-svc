package com.klapertart.expect.service;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.klapertart.expect.properties.NetworkProperties;
import expect4j.Closure;
import expect4j.Expect4j;
import expect4j.ExpectState;
import expect4j.matches.Match;
import expect4j.matches.RegExpMatch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.oro.text.regex.MalformedPatternException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * @author tritr
 * @since 7/24/2023
 */

@Service
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpectService {
    private static final int COMMAND_EXECUTION_SUCCESS_OPCODE = -2;
    private static String ENTER_CHARACTER = "\r";
    private List<String> lstCmds = new ArrayList<String>();
    private static String[] linuxPromptRegEx = new String[]{"\\>","#", "~#"};
    @Autowired
    private Expect4j expect4j;
    private StringBuilder buffer = new StringBuilder();
    @Autowired
    private NetworkProperties networkProperties;

    public String execute(List<String> cmdsToExecute) {
        this.lstCmds = cmdsToExecute;

        Closure closure = new Closure() {
            public void run(ExpectState expectState) throws Exception {
                buffer.append(expectState.getBuffer()); // string buffer for appending output of executed command
            }
        };

        List<Match> lstPattern =  new ArrayList<Match>();
        for (String regexElement : linuxPromptRegEx) {
            try {
                Match mat = new RegExpMatch(regexElement, closure);
                lstPattern.add(mat);
            } catch (MalformedPatternException e) {
                e.printStackTrace();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }


        try {
            //expect = SSH();
            boolean isSuccess = true;
            for(String strCmd : lstCmds) {
                isSuccess = isSuccess(lstPattern,strCmd);
                if (!isSuccess) {
                    isSuccess = isSuccess(lstPattern,strCmd);
                }
            }

            checkResult(expect4j.expect(lstPattern));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection();
        }

        return buffer.toString();
    }

    private boolean isSuccess(List<Match> objPattern,String strCommandPattern) {

        try {
            boolean isFailed = checkResult(expect4j.expect(objPattern));

            if (!isFailed) {
                expect4j.send(strCommandPattern);
                expect4j.send(ENTER_CHARACTER);
                return true;
            }
            return false;
        } catch (MalformedPatternException ex) {
            ex.printStackTrace();
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private boolean checkResult(int intRetVal) {
        if (intRetVal == COMMAND_EXECUTION_SUCCESS_OPCODE) {
            return true;
        }
        return false;
    }

    private void closeConnection() {
        if (expect4j!=null) {
            expect4j.close();
        }
    }
}
