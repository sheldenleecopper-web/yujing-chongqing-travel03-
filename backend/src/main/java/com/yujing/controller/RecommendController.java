package com.yujing.controller;

import com.yujing.common.Result;
import com.yujing.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/recommend")
public class RecommendController {

    @Autowired
    private RecommendService recommendService;

    @GetMapping("/home")
    public Result<Map<String, Object>> home() {
        return Result.success(recommendService.home());
    }
}
