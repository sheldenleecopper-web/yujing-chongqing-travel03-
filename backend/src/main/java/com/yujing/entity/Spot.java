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
    private String description;   // 详细介绍（详情页）
    private String priceType;    // 价格状态：free 免费 / paid 收费 / unknown 待确认
    private Integer isClassic;   // 是否经典必打卡：1 是 / 0 否
    private String scene;        // 景观类型：nature 自然风光 / history 人文历史
    private Integer activityFlag; // 是否关联热门活动：1 是 / 0 否
    private String playTip;      // 游玩建议

    @TableField(exist = false)
    private Boolean isFavorited;  // 当前用户是否已收藏（非数据库字段）
}
