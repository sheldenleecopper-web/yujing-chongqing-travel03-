package com.yujing.controller;

import com.yujing.common.Result;
import com.yujing.entity.Spot;
import com.yujing.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/add")
    public Result<Void> add(@RequestBody Map<String, Long> body) {
        favoriteService.add(body.get("spotId"));
        return Result.success();
    }

    @PostMapping("/remove")
    public Result<Void> remove(@RequestBody Map<String, Long> body) {
        favoriteService.remove(body.get("spotId"));
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<Spot>> list() {
        return Result.success(favoriteService.list());
    }
}
