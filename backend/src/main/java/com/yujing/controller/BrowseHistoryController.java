package com.yujing.controller;

import com.yujing.common.Result;
import com.yujing.entity.Spot;
import com.yujing.service.BrowseHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
public class BrowseHistoryController {

    @Autowired
    private BrowseHistoryService browseHistoryService;

    @GetMapping("/browse")
    public Result<List<Spot>> list() {
        return Result.success(browseHistoryService.list());
    }

    @DeleteMapping("/browse/{spotId}")
    public Result<Void> delete(@PathVariable Long spotId) {
        browseHistoryService.delete(spotId);
        return Result.success();
    }

    @DeleteMapping("/browse")
    public Result<Void> clear() {
        browseHistoryService.clear();
        return Result.success();
    }
}
