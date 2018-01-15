package com.lpa.springdevops.bootstrap.profilesysout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DevProfileSysOut {

    @Autowired
    public DevProfileSysOut(@Value("${com.lpa.springdevops.profile.message}") String msg) {
        System.out.println("##################################");
        System.out.println("##################################");
        System.out.println("##              DEV             ##");
        System.out.println(msg);
        System.out.println("##################################");
        System.out.println("##################################");
    }
}