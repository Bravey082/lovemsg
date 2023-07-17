package com.yang.lovemsg.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "service", url = "https://api.likepoems.com")
public interface RandomPicFeignApi {

    @GetMapping(value = "/img/pe?type=json")
    Object getPic();
}
