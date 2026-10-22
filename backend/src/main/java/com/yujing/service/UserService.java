package com.yujing.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yujing.common.JwtUtil;
import com.yujing.entity.User;
import com.yujing.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Map<String, Object> register(String username, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        if (userMapper.selectCount(wrapper) > 0) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");
        user.setCreateTime(LocalDateTime.now());
        userMapper.insert(user);

        String token = jwtUtil.generateToken(user.getId(), user.getRole());
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("role", user.getRole());
        return result;
    }

    public Map<String, Object> login(String username, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getRole());
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("role", user.getRole());
        return result;
    }

    public Map<String, Object> adminLogin(String username, String password) {
        Map<String, Object> data = login(username, password);
        if (!"ADMIN".equals(data.get("role"))) {
            throw new RuntimeException("无管理员权限");
        }
        return data;
    }
}
