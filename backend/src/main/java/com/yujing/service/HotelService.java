package com.yujing.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yujing.entity.Hotel;
import com.yujing.entity.Spot;
import com.yujing.mapper.HotelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HotelService {

    @Autowired
    private HotelMapper hotelMapper;

    /**
     * 基于主要游玩区域与预算推荐酒店
     * @param spots 行程中的去处
     * @param budget 预算：经济/适中/豪华
     * @return 推荐酒店列表
     */
    public List<Hotel> recommend(List<Spot> spots, String budget) {
        // 1. 统计出现频率最高的区域作为主要游玩区域
        Map<String, Long> regionCount = spots.stream()
                .filter(s -> s.getRegion() != null)
                .collect(Collectors.groupingBy(Spot::getRegion, Collectors.counting()));

        List<String> topRegions = regionCount.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        String priceRange = mapBudgetToPriceRange(budget);

        // 2. 优先按主要区域+预算查询
        for (String region : topRegions) {
            LambdaQueryWrapper<Hotel> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Hotel::getRegion, region)
                    .eq(Hotel::getPriceRange, priceRange);
            List<Hotel> hotels = hotelMapper.selectList(wrapper);
            if (!hotels.isEmpty()) {
                return limit(hotels, 5);
            }
        }

        // 3. 区域无匹配时，放宽区域条件，仅按预算查询
        LambdaQueryWrapper<Hotel> fallback = new LambdaQueryWrapper<>();
        fallback.eq(Hotel::getPriceRange, priceRange);
        List<Hotel> fallbackHotels = hotelMapper.selectList(fallback);
        if (!fallbackHotels.isEmpty()) {
            return limit(fallbackHotels, 5);
        }

        // 4. 预算也无匹配时，返回任意酒店
        return limit(hotelMapper.selectList(null), 5);
    }

    private String mapBudgetToPriceRange(String budget) {
        if (budget == null) return "适中";
        switch (budget) {
            case "经济": return "经济";
            case "豪华": return "豪华";
            default: return "适中";
        }
    }

    private List<Hotel> limit(List<Hotel> list, int n) {
        return list.size() > n ? list.subList(0, n) : list;
    }
}
