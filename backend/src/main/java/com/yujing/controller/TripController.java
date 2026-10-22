package com.yujing.controller;

import com.yujing.common.Result;
import com.yujing.common.UserContext;
import com.yujing.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 行程接口（需登录，未登录由 JwtInterceptor 返回 401）
 */
@RestController
@RequestMapping("/api/trip")
public class TripController {

    @Autowired
    private TripService tripService;

    @GetMapping("/list")
    public Result<Map<String, Object>> list() {
        return Result.success(tripService.detail(UserContext.getUserId()));
    }

    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody Map<String, Long> body) {
        boolean added = tripService.add(UserContext.getUserId(), body.get("spotId"));
        return Result.success(added);
    }

    @PostMapping("/remove")
    public Result<Void> remove(@RequestBody Map<String, Long> body) {
        tripService.remove(UserContext.getUserId(), body.get("spotId"));
        return Result.success();
    }
}
