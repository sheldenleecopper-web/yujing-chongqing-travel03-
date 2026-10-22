package com.yujing.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yujing.entity.Heritage;
import com.yujing.mapper.HeritageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HeritageService {

    @Autowired
    private HeritageMapper heritageMapper;

    public List<Heritage> list(String category) {
        LambdaQueryWrapper<Heritage> wrapper = new LambdaQueryWrapper<>();
        if (category != null && !category.isEmpty()) {
            wrapper.eq(Heritage::getCategory, category);
        }
        return heritageMapper.selectList(wrapper);
    }

    public List<Heritage> search(String keyword) {
        LambdaQueryWrapper<Heritage> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Heritage::getName, keyword);
        }
        return heritageMapper.selectList(wrapper);
    }

    public Heritage detail(Long id) {
        return heritageMapper.selectById(id);
    }
}
