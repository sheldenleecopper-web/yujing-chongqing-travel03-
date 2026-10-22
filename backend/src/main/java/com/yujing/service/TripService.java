package com.yujing.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yujing.entity.Spot;
import com.yujing.entity.Trip;
import com.yujing.entity.TripSpot;
import com.yujing.mapper.SpotMapper;
import com.yujing.mapper.TripMapper;
import com.yujing.mapper.TripSpotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 行程服务：每个用户维护一条默认行程，行程内景点写入 trip_spot 关联表
 */
@Service
public class TripService {

    @Autowired
    private TripMapper tripMapper;

    @Autowired
    private TripSpotMapper tripSpotMapper;

    @Autowired
    private SpotMapper spotMapper;

    /** 获取（不存在则创建）当前用户的默认行程 */
    public Trip getOrCreate(Long userId) {
        LambdaQueryWrapper<Trip> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Trip::getUserId, userId).orderByAsc(Trip::getId).last("LIMIT 1");
        Trip trip = tripMapper.selectOne(wrapper);
        if (trip == null) {
            trip = new Trip();
            trip.setUserId(userId);
            trip.setTitle("我的重庆行程");
            trip.setCreateTime(LocalDateTime.now());
            tripMapper.insert(trip);
        }
        return trip;
    }

    /**
     * 加入行程
     * @return true 新增成功；false 该景点已在行程中
     */
    @Transactional
    public boolean add(Long userId, Long spotId) {
        if (spotId == null || spotMapper.selectById(spotId) == null) {
            throw new IllegalArgumentException("景点不存在");
        }
        Trip trip = getOrCreate(userId);
        LambdaQueryWrapper<TripSpot> exists = new LambdaQueryWrapper<>();
        exists.eq(TripSpot::getTripId, trip.getId()).eq(TripSpot::getSpotId, spotId);
        if (tripSpotMapper.selectCount(exists) > 0) {
            return false;
        }
        LambdaQueryWrapper<TripSpot> countWrapper = new LambdaQueryWrapper<>();
        countWrapper.eq(TripSpot::getTripId, trip.getId());
        long count = tripSpotMapper.selectCount(countWrapper);

        TripSpot item = new TripSpot();
        item.setTripId(trip.getId());
        item.setSpotId(spotId);
        item.setSortNo((int) count + 1);
        item.setCreateTime(LocalDateTime.now());
        tripSpotMapper.insert(item);
        return true;
    }

    /** 从行程中移除 */
    public void remove(Long userId, Long spotId) {
        Trip trip = getOrCreate(userId);
        LambdaQueryWrapper<TripSpot> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TripSpot::getTripId, trip.getId()).eq(TripSpot::getSpotId, spotId);
        tripSpotMapper.delete(wrapper);
    }

    /** 行程详情：返回行程基础信息与景点列表 */
    public Map<String, Object> detail(Long userId) {
        Trip trip = getOrCreate(userId);
        LambdaQueryWrapper<TripSpot> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TripSpot::getTripId, trip.getId()).orderByAsc(TripSpot::getSortNo).orderByAsc(TripSpot::getId);
        List<TripSpot> items = tripSpotMapper.selectList(wrapper);

        List<Spot> spots = new ArrayList<>();
        List<Long> spotIds = new ArrayList<>();
        for (TripSpot item : items) {
            Spot spot = spotMapper.selectById(item.getSpotId());
            if (spot != null) {
                spots.add(spot);
                spotIds.add(spot.getId());
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("id", trip.getId());
        result.put("title", trip.getTitle());
        result.put("spotIds", spotIds);
        result.put("spots", spots);
        return result;
    }
}
