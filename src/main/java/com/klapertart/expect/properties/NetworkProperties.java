package com.klapertart.expect.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author tritr
 * @since 7/24/2023
 */

@Component
@ConfigurationProperties("network")
@Getter
@Setter
public class NetworkProperties {
    private String sshHost;
    private int sshPort;
    private String sshUser;
    private String sshPass;
    private int sessionTimeout;
}
