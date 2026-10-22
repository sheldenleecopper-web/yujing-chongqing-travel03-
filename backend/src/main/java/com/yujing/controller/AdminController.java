package com.yujing.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yujing.common.Result;
import com.yujing.entity.Heritage;
import com.yujing.entity.Hotel;
import com.yujing.entity.Spot;
import com.yujing.entity.User;
import com.yujing.mapper.GuideMapper;
import com.yujing.mapper.HeritageMapper;
import com.yujing.mapper.HotelMapper;
import com.yujing.mapper.SpotMapper;
import com.yujing.mapper.UserMapper;
import com.yujing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SpotMapper spotMapper;

    @Autowired
    private HeritageMapper heritageMapper;

    @Autowired
    private HotelMapper hotelMapper;

    @Autowired
    private GuideMapper guideMapper;

    // ==================== 登录 ====================

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        try {
            String username = body.get("username");
            String password = body.get("password");
            Map<String, Object> data = userService.adminLogin(username, password);
            return Result.success(data);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    // ==================== 数据概览 ====================

    @GetMapping("/stats")
    public Result<Map<String, Long>> stats() {
        Map<String, Long> data = new HashMap<>();
        data.put("userCount", userMapper.selectCount(null));
        data.put("spotCount", spotMapper.selectCount(null));
        data.put("heritageCount", heritageMapper.selectCount(null));
        data.put("guideCount", guideMapper.selectCount(null));
        return Result.success(data);
    }

    // ==================== 去处管理 ====================

    @GetMapping("/spot/list")
    public Result<List<Spot>> spotList() {
        return Result.success(spotMapper.selectList(null));
    }

    @PostMapping("/spot")
    public Result<String> addSpot(@RequestBody Spot spot) {
        if (spot.getName() == null || spot.getName().trim().isEmpty()) {
            return Result.fail("去处名称不能为空");
        }
        LambdaQueryWrapper<Spot> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Spot::getName, spot.getName());
        if (spotMapper.selectCount(wrapper) > 0) {
            return Result.fail("去处名称已存在");
        }
        spotMapper.insert(spot);
        return Result.success("新增成功");
    }

    @PutMapping("/spot/{id}")
    public Result<String> updateSpot(@PathVariable Long id, @RequestBody Spot spot) {
        spot.setId(id);
        if (spot.getName() == null || spot.getName().trim().isEmpty()) {
            return Result.fail("去处名称不能为空");
        }
        spotMapper.updateById(spot);
        return Result.success("更新成功");
    }

    @DeleteMapping("/spot/{id}")
    public Result<String> deleteSpot(@PathVariable Long id) {
        spotMapper.deleteById(id);
        return Result.success("删除成功");
    }

    // ==================== 非遗管理 ====================

    @GetMapping("/heritage/list")
    public Result<List<Heritage>> heritageList() {
        return Result.success(heritageMapper.selectList(null));
    }

    @PostMapping("/heritage")
    public Result<String> addHeritage(@RequestBody Heritage heritage) {
        if (heritage.getName() == null || heritage.getName().trim().isEmpty()) {
            return Result.fail("非遗名称不能为空");
        }
        LambdaQueryWrapper<Heritage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Heritage::getName, heritage.getName());
        if (heritageMapper.selectCount(wrapper) > 0) {
            return Result.fail("非遗名称已存在");
        }
        heritageMapper.insert(heritage);
        return Result.success("新增成功");
    }

    @PutMapping("/heritage/{id}")
    public Result<String> updateHeritage(@PathVariable Long id, @RequestBody Heritage heritage) {
        heritage.setId(id);
        if (heritage.getName() == null || heritage.getName().trim().isEmpty()) {
            return Result.fail("非遗名称不能为空");
        }
        heritageMapper.updateById(heritage);
        return Result.success("更新成功");
    }

    @DeleteMapping("/heritage/{id}")
    public Result<String> deleteHeritage(@PathVariable Long id) {
        heritageMapper.deleteById(id);
        return Result.success("删除成功");
    }

    // ==================== 酒店管理 ====================

    @GetMapping("/hotel/list")
    public Result<List<Hotel>> hotelList() {
        return Result.success(hotelMapper.selectList(null));
    }

    @PostMapping("/hotel")
    public Result<String> addHotel(@RequestBody Hotel hotel) {
        if (hotel.getName() == null || hotel.getName().trim().isEmpty()) {
            return Result.fail("酒店名称不能为空");
        }
        LambdaQueryWrapper<Hotel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Hotel::getName, hotel.getName());
        if (hotelMapper.selectCount(wrapper) > 0) {
            return Result.fail("酒店名称已存在");
        }
        hotelMapper.insert(hotel);
        return Result.success("新增成功");
    }

    @PutMapping("/hotel/{id}")
    public Result<String> updateHotel(@PathVariable Long id, @RequestBody Hotel hotel) {
        hotel.setId(id);
        if (hotel.getName() == null || hotel.getName().trim().isEmpty()) {
            return Result.fail("酒店名称不能为空");
        }
        hotelMapper.updateById(hotel);
        return Result.success("更新成功");
    }

    @DeleteMapping("/hotel/{id}")
    public Result<String> deleteHotel(@PathVariable Long id) {
        hotelMapper.deleteById(id);
        return Result.success("删除成功");
    }

    // ==================== 用户管理（只读） ====================

    @GetMapping("/user/list")
    public Result<List<Map<String, Object>>> userList() {
        List<User> users = userMapper.selectList(null);
        List<Map<String, Object>> result = users.stream().map(u -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", u.getId());
            map.put("username", u.getUsername());
            map.put("role", u.getRole());
            map.put("createTime", u.getCreateTime());
            return map;
        }).collect(Collectors.toList());
        return Result.success(result);
    }
}
