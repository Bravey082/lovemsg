package com.yang.lovemsg.feign;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service", url = "https://api.weixin.qq.com/cgi-bin")
public interface WxFeignApi {

    @GetMapping("/token?grant_type=client_credential&appid={appId}&secret={appsecret}")
    JSONObject getToken(@RequestParam("appId") String appId, @RequestParam("appsecret") String appsecret);

    @GetMapping("/user/get?access_token={token}")
    JSONObject getUser(@RequestParam("token") String token);

    @PostMapping("/message/template/send?access_token={token}")
    JSONObject sendMsg(@RequestBody JSONObject jsonObject, @RequestParam("token") String token);
}
