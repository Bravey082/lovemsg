package com.yang.lovemsg.job;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.time.LocalTime;

@Configuration
@EnableScheduling
public class StockJob {

    private static final String stockCode = "sh000001,sh510050";

    private static final String url = "http://qt.gtimg.cn/q=";

    LocalDateTime time1 = LocalDateTime.now();

    @Value("${spring.mail.username}")
    private String mainUserName;

    //发送人昵称
    @Value("${spring.mail.nickname}")
    private String nickname;

    @Value("${spring.mail.receivers}")
    private String receivers;

    @Autowired
    private JavaMailSender javaMailSender;

    @Scheduled(cron = "*/5 * * * * ?")
    public void getStock() {
        String result = HttpUtil.get(url + stockCode);
        String[] stockArr = result.split(";");
        for(int i = 0; i < stockArr.length-1; i++) {
            stcokAnalysis(stockArr[i]);
        }
        System.out.println();
    }

    private static boolean isTimeInRange(LocalTime startTime, LocalTime endTime, LocalTime time) {
        // 判断待判断的时间是否在时间段内
        return !time.isBefore(startTime) && !time.isAfter(endTime);
    }

    public void stcokAnalysis (String result){
        String[] res = result.split("~");
        String priceNow = res[3];
        double percent = Double.parseDouble(res[32]);
        //当前
       System.out.print(priceNow + "  " + res[32]);

        // 设置时间段
        LocalTime startTime1 = LocalTime.of(9, 30);
        LocalTime endTime1 = LocalTime.of(11, 30);
        // 设置时间段
        LocalTime startTime2 = LocalTime.of(13, 0);
        LocalTime endTime2 = LocalTime.of(15, 0);

        // 获取当前时间
        LocalTime now = LocalTime.now();

        if (isTimeInRange(startTime1, endTime1, now)) {
            sengMail(priceNow, percent);
        } else if (isTimeInRange(startTime2, endTime2, now)) {
            sengMail(priceNow, percent);
        } else {
            System.out.println("    Not within trading hours -    " + now);
        }
    }


    public void sengMail(String priceNow, double percent) {
        if (Math.abs(percent) > 0.5 && Duration.between(time1, LocalDateTime.now()).toMinutes() > 5) {
            // 构建一个邮件对象
            SimpleMailMessage message = new SimpleMailMessage();
            // 设置邮件主题
            message.setSubject("BestNow");
            // 设置邮件发送者，昵称+<邮箱地址>
            message.setFrom(mainUserName);
            // 设置邮件接收者，可以有多个接收者，多个接受者参数需要数组形式
            message.setTo(receivers);
            message.setSentDate(new Date());
            message.setText(String.valueOf(percent));
            javaMailSender.send(message);
            time1 = LocalDateTime.now();
            System.out.println("    SendTime    " + DateUtil.format(time1, "yyyy-MM-dd HH:mm:ss"));
        } else {
            System.out.print("   |   ");
        }
    }
}
