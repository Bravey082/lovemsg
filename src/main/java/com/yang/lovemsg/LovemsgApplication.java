package com.yang.lovemsg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LovemsgApplication {

    public static void main(String[] args){
        SpringApplication.run(LovemsgApplication.class, args);
    }

}
