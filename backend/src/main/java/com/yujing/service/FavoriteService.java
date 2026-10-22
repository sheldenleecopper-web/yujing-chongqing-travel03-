package com.yujing.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yujing.common.UserContext;
import com.yujing.entity.Favorite;
import com.yujing.entity.Spot;
import com.yujing.mapper.FavoriteMapper;
import com.yujing.mapper.SpotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    private SpotMapper spotMapper;

    public void add(Long spotId) {
        Long userId = UserContext.getUserId();
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getSpotId, spotId);
        if (favoriteMapper.selectCount(wrapper) > 0) {
            return;
        }
        Favorite fav = new Favorite();
        fav.setUserId(userId);
        fav.setSpotId(spotId);
        fav.setCreateTime(LocalDateTime.now());
        favoriteMapper.insert(fav);
    }

    public void remove(Long spotId) {
        Long userId = UserContext.getUserId();
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getSpotId, spotId);
        favoriteMapper.delete(wrapper);
    }

    public List<Spot> list() {
        Long userId = UserContext.getUserId();
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .orderByDesc(Favorite::getCreateTime);
        List<Favorite> favorites = favoriteMapper.selectList(wrapper);
        List<Spot> spots = new ArrayList<>();
        for (Favorite fav : favorites) {
            Spot spot = spotMapper.selectById(fav.getSpotId());
            if (spot != null) {
                spots.add(spot);
            }
        }
        return spots;
    }
}
