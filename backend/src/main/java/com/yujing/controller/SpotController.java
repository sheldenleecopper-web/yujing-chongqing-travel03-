package com.yujing.controller;

import com.yujing.common.Result;
import com.yujing.entity.Spot;
import com.yujing.service.SpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/spot")
public class SpotController {

    @Autowired
    private SpotService spotService;

    @GetMapping("/list")
    public Result<List<Spot>> list(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String sort) {
        return Result.success(spotService.list(type, region, sort));
    }

    @GetMapping("/search")
    public Result<List<Spot>> search(@RequestParam(required = false) String keyword) {
        return Result.success(spotService.search(keyword));
    }

    @GetMapping("/{id}")
    public Result<Spot> detail(@PathVariable Long id) {
        Spot spot = spotService.detail(id);
        if (spot == null) {
            return Result.fail("去处不存在");
        }
        return Result.success(spot);
    }
}
