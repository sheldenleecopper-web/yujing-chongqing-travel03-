package com.yujing.controller;

import com.yujing.common.Result;
import com.yujing.dto.GuideGenerateRequest;
import com.yujing.entity.Guide;
import com.yujing.service.GuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/guide")
public class GuideController {

    @Autowired
    private GuideService guideService;

    @PostMapping("/generate")
    public Result<Map<String, Object>> generate(@RequestBody GuideGenerateRequest req) {
        try {
            Map<String, Object> data = guideService.generate(req);
            return Result.success(data);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result<Guide> detail(@PathVariable Long id) {
        return Result.success(guideService.detail(id));
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody Map<String, Object> body) {
        Long id = Long.valueOf(body.get("id").toString());
        String posterUrl = body.get("posterUrl") == null ? null : body.get("posterUrl").toString();
        guideService.save(id, posterUrl);
        return Result.success();
    }

    @PostMapping("/{id}/poster")
    public Result<Void> savePoster(@PathVariable Long id, @RequestBody Map<String, String> body) {
        guideService.savePoster(id, body.get("posterUrl"));
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<Guide>> historyList() {
        return Result.success(guideService.historyList());
    }

    @GetMapping("/poster/list")
    public Result<List<Guide>> posterRecordList() {
        return Result.success(guideService.posterRecordList());
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        guideService.delete(id);
        return Result.success();
    }
}
