package com.yujing.controller;

import com.yujing.common.Result;
import com.yujing.common.UserContext;
import com.yujing.entity.User;
import com.yujing.mapper.UserMapper;
import com.yujing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/register")
    public Result<Map<String, Object>> register(@RequestBody Map<String, String> body) {
        try {
            String username = body.get("username");
            String password = body.get("password");
            Map<String, Object> data = userService.register(username, password);
            return Result.success(data);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        try {
            String username = body.get("username");
            String password = body.get("password");
            Map<String, Object> data = userService.login(username, password);
            return Result.success(data);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.success();
    }

    @GetMapping("/info")
    public Result<Map<String, Object>> info() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return Result.fail(401, "未登录");
        }
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.fail(401, "用户不存在");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("userId", user.getId());
        data.put("username", user.getUsername());
        return Result.success(data);
    }
}
