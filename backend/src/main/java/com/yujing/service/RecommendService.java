package com.yujing.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yujing.common.UserContext;
import com.yujing.entity.BrowseHistory;
import com.yujing.entity.Favorite;
import com.yujing.entity.Spot;
import com.yujing.mapper.BrowseHistoryMapper;
import com.yujing.mapper.FavoriteMapper;
import com.yujing.mapper.SpotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendService {

    @Autowired
    private SpotMapper spotMapper;

    @Autowired
    private BrowseHistoryMapper browseHistoryMapper;

    @Autowired
    private FavoriteMapper favoriteMapper;

    public Map<String, Object> home() {
        Map<String, Object> result = new HashMap<>();
        Long userId = UserContext.getUserId();

        List<Spot> recommendSpots;
        List<Map<String, Object>> plans;

        if (userId != null) {
            recommendSpots = recommendByUserBehavior(userId);
            plans = buildPlansByBehavior(userId);
        } else {
            recommendSpots = getHotSpots(8);
            plans = buildDefaultPlans();
        }

        result.put("recommendSpots", recommendSpots);
        result.put("recommendPlans", plans);
        return result;
    }

    private List<Spot> recommendByUserBehavior(Long userId) {
        // 收集用户浏览和收藏的去处类别与区域
        Set<String> regions = new HashSet<>();
        Set<String> types = new HashSet<>();
        Set<Long> viewedSpotIds = new HashSet<>();

        LambdaQueryWrapper<BrowseHistory> bhWrapper = new LambdaQueryWrapper<>();
        bhWrapper.eq(BrowseHistory::getUserId, userId);
        List<BrowseHistory> histories = browseHistoryMapper.selectList(bhWrapper);
        for (BrowseHistory h : histories) {
            viewedSpotIds.add(h.getSpotId());
            Spot s = spotMapper.selectById(h.getSpotId());
            if (s != null) {
                if (s.getRegion() != null) regions.add(s.getRegion());
                if (s.getType() != null) types.add(s.getType());
            }
        }

        LambdaQueryWrapper<Favorite> favWrapper = new LambdaQueryWrapper<>();
        favWrapper.eq(Favorite::getUserId, userId);
        List<Favorite> favorites = favoriteMapper.selectList(favWrapper);
        for (Favorite f : favorites) {
            viewedSpotIds.add(f.getSpotId());
            Spot s = spotMapper.selectById(f.getSpotId());
            if (s != null) {
                if (s.getRegion() != null) regions.add(s.getRegion());
                if (s.getType() != null) types.add(s.getType());
            }
        }

        if (regions.isEmpty() && types.isEmpty()) {
            return getHotSpots(8);
        }

        // 查询同类同区域去处，排除已浏览的
        LambdaQueryWrapper<Spot> wrapper = new LambdaQueryWrapper<>();
        if (!regions.isEmpty()) {
            wrapper.in(Spot::getRegion, regions);
        }
        if (!types.isEmpty()) {
            wrapper.and(w -> w.in(Spot::getType, types));
        }
        if (!viewedSpotIds.isEmpty()) {
            wrapper.notIn(Spot::getId, viewedSpotIds);
        }
        wrapper.orderByDesc(Spot::getHeatIndex);
        List<Spot> spots = spotMapper.selectList(wrapper);

        if (spots.isEmpty()) {
            return getHotSpots(8);
        }
        return spots.size() > 8 ? spots.subList(0, 8) : spots;
    }

    private List<Spot> getHotSpots(int limit) {
        LambdaQueryWrapper<Spot> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Spot::getHeatIndex);
        List<Spot> spots = spotMapper.selectList(wrapper);
        return spots.size() > limit ? spots.subList(0, limit) : spots;
    }

    private List<Map<String, Object>> buildPlansByBehavior(Long userId) {
        // 简单方案：基于用户偏好类别生成推荐行程方案
        return buildDefaultPlans();
    }

    private List<Map<String, Object>> buildDefaultPlans() {
        List<Map<String, Object>> plans = new ArrayList<>();

        Map<String, Object> plan1 = new HashMap<>();
        plan1.put("name", "渝中区经典2日游");
        plan1.put("days", 2);
        plan1.put("spots", Arrays.asList("洪崖洞", "解放碑", "山城步道", "李子坝"));
        plan1.put("interests", Arrays.asList("人文历史", "城市休闲"));
        plans.add(plan1);

        Map<String, Object> plan2 = new HashMap<>();
        plan2.put("name", "非遗文化体验3日游");
        plan2.put("days", 3);
        plan2.put("spots", Arrays.asList("川剧变脸", "荣昌夏布", "梁平木版年画"));
        plan2.put("interests", Arrays.asList("非遗体验", "人文历史"));
        plans.add(plan2);

        Map<String, Object> plan3 = new HashMap<>();
        plan3.put("name", "区县自然风光5日游");
        plan3.put("days", 5);
        plan3.put("spots", Arrays.asList("武隆天生三桥", "大足石刻", "巫山小三峡"));
        plan3.put("interests", Arrays.asList("自然探索", "打卡摄影"));
        plans.add(plan3);

        return plans;
    }
}
