package com.yang.lovemsg.feign;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service", url = "https://api.shadiao.pro")
public interface SdFeignApi {

    @GetMapping("/{type}")
    JSONObject getSd(@RequestParam("type") String type);

}
