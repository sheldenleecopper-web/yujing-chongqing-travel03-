package com.yujing.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yujing.common.PageResult;
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
            String kw = keyword.trim();
            wrapper.and(w -> w.like(Spot::getName, kw)
                    .or().like(Spot::getRegion, kw)
                    .or().like(Spot::getIntro, kw)
                    .or().like(Spot::getDescription, kw));
        }
        wrapper.orderByDesc(Spot::getHeatIndex);
        return spotMapper.selectList(wrapper);
    }

    /**
     * 景点榜列表：分类筛选 + 关键词搜索 + 排序 + 分页
     *
     * @param category all 全部 / hot 热门活动榜 / classic 经典必打卡 / nature 自然风光 / history 人文历史
     * @param sort     heat 热度优先 / priceAsc 价格从低到高 / priceDesc 价格从高到低
     */
    public PageResult<Spot> rank(String category, String sort, String keyword, int page, int size) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.min(Math.max(size, 1), 50);

        LambdaQueryWrapper<Spot> wrapper = new LambdaQueryWrapper<>();
        // 本页面为景点榜，固定只取景点类型
        wrapper.eq(Spot::getType, "attraction");

        if ("hot".equals(category)) {
            wrapper.eq(Spot::getActivityFlag, 1);
        } else if ("classic".equals(category)) {
            wrapper.eq(Spot::getIsClassic, 1);
        } else if ("nature".equals(category)) {
            wrapper.eq(Spot::getScene, "nature");
        } else if ("history".equals(category)) {
            wrapper.eq(Spot::getScene, "history");
        }

        if (keyword != null && !keyword.trim().isEmpty()) {
            String kw = keyword.trim();
            wrapper.and(w -> w.like(Spot::getName, kw)
                    .or().like(Spot::getIntro, kw)
                    .or().like(Spot::getDescription, kw));
        }

        // 价格未知的景点不参与“免费”排序，统一排在最后
        if ("priceAsc".equals(sort)) {
            wrapper.last("ORDER BY (price_type = 'unknown') ASC, CASE WHEN price_type = 'free' THEN 0 ELSE avg_cost END ASC, heat_index DESC");
        } else if ("priceDesc".equals(sort)) {
            wrapper.last("ORDER BY (price_type = 'unknown') ASC, CASE WHEN price_type = 'free' THEN 0 ELSE avg_cost END DESC, heat_index DESC");
        } else {
            wrapper.orderByDesc(Spot::getHeatIndex).orderByAsc(Spot::getId);
        }

        List<Spot> all = spotMapper.selectList(wrapper);
        long total = all.size();
        int from = Math.min((safePage - 1) * safeSize, all.size());
        int to = Math.min(from + safeSize, all.size());
        return PageResult.of(all.subList(from, to), total, safePage, safeSize);
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
