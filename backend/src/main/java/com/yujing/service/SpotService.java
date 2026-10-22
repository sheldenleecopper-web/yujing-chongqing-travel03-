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

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SpotService {

    @Autowired
    private SpotMapper spotMapper;

    @Autowired
    private BrowseHistoryMapper browseHistoryMapper;

    @Autowired
    private FavoriteMapper favoriteMapper;

    public List<Spot> list(String type, String region, String sort) {
        LambdaQueryWrapper<Spot> wrapper = new LambdaQueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            wrapper.eq(Spot::getType, type);
        }
        if (region != null && !region.isEmpty() && !"全部".equals(region)) {
            wrapper.eq(Spot::getRegion, region);
        }
        if ("评分优先".equals(sort)) {
            wrapper.orderByDesc(Spot::getHeatIndex);
        } else if ("最新发布".equals(sort)) {
            wrapper.orderByDesc(Spot::getId);
        } else {
            wrapper.orderByDesc(Spot::getHeatIndex);
        }
        return spotMapper.selectList(wrapper);
    }

    public List<Spot> search(String keyword) {
        LambdaQueryWrapper<Spot> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Spot::getName, keyword).or().like(Spot::getRegion, keyword));
        }
        wrapper.orderByDesc(Spot::getHeatIndex);
        return spotMapper.selectList(wrapper);
    }

    public Spot detail(Long id) {
        Spot spot = spotMapper.selectById(id);
        // 已登录用户自动记录浏览历史
        Long userId = UserContext.getUserId();
        if (userId != null && spot != null) {
            recordBrowse(userId, id);
            // 查询是否已收藏
            LambdaQueryWrapper<Favorite> favWrapper = new LambdaQueryWrapper<>();
            favWrapper.eq(Favorite::getUserId, userId).eq(Favorite::getSpotId, id);
            spot.setIsFavorited(favoriteMapper.selectCount(favWrapper) > 0);
        }
        return spot;
    }

    private void recordBrowse(Long userId, Long spotId) {
        LambdaQueryWrapper<BrowseHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BrowseHistory::getUserId, userId)
                .eq(BrowseHistory::getSpotId, spotId);
        BrowseHistory history = browseHistoryMapper.selectOne(wrapper);
        if (history != null) {
            history.setBrowseTime(LocalDateTime.now());
            browseHistoryMapper.updateById(history);
        } else {
            history = new BrowseHistory();
            history.setUserId(userId);
            history.setSpotId(spotId);
            history.setBrowseTime(LocalDateTime.now());
            browseHistoryMapper.insert(history);
        }
    }
}
