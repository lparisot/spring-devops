package com.lpa.springdevops.bootstrap.profilesysout;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("default")
public class DefaultProfileSysOut {

    public DefaultProfileSysOut(@Value("${com.lpa.springdevops.profile.message}") String msg) {
        System.out.println("##################################");
        System.out.println("##################################");
        System.out.println("##            DEFAULT           ##");
        System.out.println(msg);
        System.out.println("##################################");
        System.out.println("##################################");
    }
}