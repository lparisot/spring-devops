package com.lpa.springdevops.bootstrap.profilesysout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NonProfileBean {

    @Autowired
    public NonProfileBean(@Value("${com.lpa.springdevops.profile.message}") String msg) {
        System.out.println("**********"  + msg + "************");

    }
}