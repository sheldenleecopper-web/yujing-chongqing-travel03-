package com.yujing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 行程与景点的关联（同一行程内景点不可重复）
 */
@Data
@TableName("trip_spot")
public class TripSpot {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long tripId;
    private Long spotId;
    private Integer sortNo;
    private LocalDateTime createTime;
}
