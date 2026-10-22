package com.yujing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("hotel")
public class Hotel {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String region;
    private String priceRange;   // 经济 / 适中 / 豪华
    private String intro;
}
