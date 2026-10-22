package com.yujing.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yujing.common.UserContext;
import com.yujing.entity.BrowseHistory;
import com.yujing.entity.Spot;
import com.yujing.mapper.BrowseHistoryMapper;
import com.yujing.mapper.SpotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BrowseHistoryService {

    @Autowired
    private BrowseHistoryMapper browseHistoryMapper;

    @Autowired
    private SpotMapper spotMapper;

    public List<Spot> list() {
        Long userId = UserContext.getUserId();
        LambdaQueryWrapper<BrowseHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BrowseHistory::getUserId, userId)
                .orderByDesc(BrowseHistory::getBrowseTime);
        List<BrowseHistory> histories = browseHistoryMapper.selectList(wrapper);
        List<Spot> spots = new ArrayList<>();
        for (BrowseHistory h : histories) {
            Spot spot = spotMapper.selectById(h.getSpotId());
            if (spot != null) {
                spots.add(spot);
            }
        }
        return spots;
    }

    public void delete(Long spotId) {
        Long userId = UserContext.getUserId();
        LambdaQueryWrapper<BrowseHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BrowseHistory::getUserId, userId)
                .eq(BrowseHistory::getSpotId, spotId);
        browseHistoryMapper.delete(wrapper);
    }

    public void clear() {
        Long userId = UserContext.getUserId();
        LambdaQueryWrapper<BrowseHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BrowseHistory::getUserId, userId);
        browseHistoryMapper.delete(wrapper);
    }
}
