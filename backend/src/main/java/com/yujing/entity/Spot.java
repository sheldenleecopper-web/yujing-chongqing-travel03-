package com.yujing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("spot")
public class Spot {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String type;        // attraction / food / activity
    private String region;
    private String tags;
    private String cover;
    private String intro;
    private Integer heatIndex;
    private BigDecimal avgCost;
    private String openTime;
    private String address;
    private BigDecimal lng;
    private BigDecimal lat;

    @TableField(exist = false)
    private Boolean isFavorited;  // 当前用户是否已收藏（非数据库字段）
}
