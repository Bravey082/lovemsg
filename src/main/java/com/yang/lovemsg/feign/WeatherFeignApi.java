package com.yang.lovemsg.feign;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service", url = "https://restapi.amap.com/v3/weather")
public interface WeatherFeignApi {

    @GetMapping("/weatherInfo?key={key}&city={city}")
    JSONObject getWeather(@RequestParam("key") String key, @RequestParam("city") String city);
}
