package com.wayn.spider.community.controller;

import com.wayn.spider.community.core.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private CommunityService communityService;

    @GetMapping("test")
    public String test() {

        return "test";
    }
}
