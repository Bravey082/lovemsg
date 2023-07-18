package com.yang.lovemsg.job;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yang.lovemsg.Enum.SdEnum;
import com.yang.lovemsg.feign.RandomPicFeignApi;
import com.yang.lovemsg.feign.SdFeignApi;
import com.yang.lovemsg.feign.WeatherFeignApi;
import com.yang.lovemsg.feign.WxFeignApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.util.Date;

@Configuration
@EnableScheduling
public class WxJob {

    private static final Logger logger = LoggerFactory.getLogger(WxJob.class);

    @Autowired
    private WxFeignApi wxFeignApi;

    @Autowired
    private WeatherFeignApi weatherFeignApi;

    @Autowired
    private SdFeignApi sdFeignApi;

    @Autowired
    private RandomPicFeignApi randomPicFeignApi;

    @Autowired
    private Environment application;

    @Scheduled(cron = "*/5 * * * * ?")
    public void sendMsg() throws InterruptedException {
        sendMessage();
    }

    public void sendMessage(){
        //获取token
        JSONObject json = wxFeignApi.getToken(application.getProperty("wx.appId"), application.getProperty("wx.appsecret"));
        String token = json.get("access_token").toString();
        //获取关注用户
        JSONObject userJson = wxFeignApi.getUser(token);
        JSONArray openidArray = userJson.getJSONObject("data").getJSONArray("openid");
        for (int i = 0; i < openidArray.size(); i++) {
            String openid = openidArray.get(i).toString();
            JSONObject msg = new JSONObject();
            msg.put("touser", openid);
            msg.put("template_id", application.getProperty("wx.templateId"));
            msg.put("url", application.getProperty("wx.picUrl"));
            msg.put("data", assembly());
            JSONObject resp = wxFeignApi.sendMsg(msg, token);
            logger.info("----发送结果：" + resp);
        }
    }

    //组装数据
    public JSONObject assembly() {
        JSONObject date = getDate();
        JSONObject weather = getWeather();
        JSONObject result = new JSONObject();

        JSONObject province = new JSONObject();
        province.put("value", weather.get("province"));
        result.put("province", province);

        JSONObject city = new JSONObject();
        city.put("value", weather.get("city"));
        result.put("city", city);

        JSONObject today_weather = new JSONObject();
        today_weather.put("value", weather.get("weather"));
        result.put("today_weather", today_weather);

        JSONObject temperature = new JSONObject();
        temperature.put("value", weather.get("temperature_float"));
        result.put("temperature", temperature);

        JSONObject humidity = new JSONObject();
        humidity.put("value", weather.get("humidity_float"));
        result.put("humidity", humidity);

        JSONObject love_days = new JSONObject();
        love_days.put("value", date.get("loveDays"));
        result.put("love_days", love_days);

        JSONObject lover_birthday_left = new JSONObject();
        lover_birthday_left.put("value", date.get("LoverBirthdayLeft"));
        result.put("lover_birthday_left", lover_birthday_left);

        JSONObject mine_birthday_left = new JSONObject();
        mine_birthday_left.put("value", date.get("MineBirthdayLeft"));
        result.put("mine_birthday_left", mine_birthday_left);

        int ran = RandomUtil.randomInt(3);
        String sdType = null;
        switch (ran) {
            case 1:
                sdType = SdEnum.one.getType();
                break;
            case 2:
                sdType = SdEnum.two.getType();
                break;
            default:
                sdType = SdEnum.three.getType();
                break;
        }

        //每日一言
        JSONObject sd = sdFeignApi.getSd(sdType);
        String text = sd.getJSONObject("data").get("text").toString().replaceAll("\\s*|\r|\n|\t", "");
        StringBuilder builder = new StringBuilder();
        int length = text.length();
        for (int i = 0; i < length; i += 18) {
            if (i + 18 <= length) {
                builder.append(text, i, i + 18).append("|");
            } else {
                builder.append(text.substring(i)).append("|");
            }
        }
        String[] strArr = builder.toString().split("\\|");
        if (strArr.length > 0) {
            JSONObject one = new JSONObject();
            one.put("value", strArr[0]);
            result.put("one", one);
        }
        if (strArr.length > 1) {
            JSONObject two = new JSONObject();
            two.put("value", strArr[1]);
            result.put("two", two);
        }
        if (strArr.length > 2) {
            JSONObject three = new JSONObject();
            three.put("value", strArr[2]);
            result.put("three", three);
        }
        if (strArr.length > 3) {
            JSONObject four = new JSONObject();
            four.put("value", strArr[3]);
            result.put("four", four);
        }
        return result;
    }

    public JSONObject getDate() {
        //获取lovedays
        Date begin = DateUtil.parse("2023-03-27");
        Date now = DateUtil.date();
        long loveDays = DateUtil.between(begin, now, DateUnit.DAY);
        //组装日期
        JSONObject day = new JSONObject();
        day.put("loveDays", loveDays);
        day.put("LoverBirthdayLeft", getBirthdayLeft(1992, 5, 5));
        day.put("MineBirthdayLeft", getBirthdayLeft(1994, 8, 20));
        return day;
    }

    //获取天气信息
    public JSONObject getWeather() {
        //茅箭区
        String city = application.getProperty("dg.city");
        String key = application.getProperty("dg.key");
        JSONObject jsonObject = weatherFeignApi.getWeather(key, city);
        JSONObject lives = jsonObject.getJSONArray("lives").getJSONObject(0);
        //重新组装
        JSONObject info = new JSONObject();
        info.put("province", lives.get("province"));
        info.put("city", lives.get("city"));
        info.put("weather", lives.get("weather"));
        info.put("temperature_float", lives.get("temperature_float"));
        info.put("humidity_float", lives.get("humidity_float"));
        return info;
    }

    public long getBirthdayLeft(int year, int month, int day) {
        // 设置出生日期
        LocalDate birthday = LocalDate.of(year, month, day);
        // 获取今年的生日日期
        LocalDate thisYearBirthday = birthday.withYear(LocalDate.now().getYear());
        // 如果今年的生日已经过了，那么下次生日就是明年的生日
        if (thisYearBirthday.isBefore(LocalDate.now())) {
            thisYearBirthday = thisYearBirthday.plusYears(1);
        }
        LocalDate today = LocalDate.now();
        long days = Math.abs(thisYearBirthday.toEpochDay() - today.toEpochDay());
        return days;
    }

}
