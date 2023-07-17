package com.yang.lovemsg;

import com.yang.lovemsg.job.WxJob;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
@EnableFeignClients
public class LovemsgApplication {

    public static void main(String[] args) {
        SpringApplication.run(LovemsgApplication.class, args);
    }

}
