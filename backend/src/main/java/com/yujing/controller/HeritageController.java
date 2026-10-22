package com.yujing.controller;

import com.yujing.common.Result;
import com.yujing.entity.Heritage;
import com.yujing.service.HeritageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/heritage")
public class HeritageController {

    @Autowired
    private HeritageService heritageService;

    @GetMapping("/list")
    public Result<List<Heritage>> list(@RequestParam(required = false) String category) {
        return Result.success(heritageService.list(category));
    }

    @GetMapping("/search")
    public Result<List<Heritage>> search(@RequestParam(required = false) String keyword) {
        return Result.success(heritageService.search(keyword));
    }

    @GetMapping("/{id}")
    public Result<Heritage> detail(@PathVariable Long id) {
        return Result.success(heritageService.detail(id));
    }
}
