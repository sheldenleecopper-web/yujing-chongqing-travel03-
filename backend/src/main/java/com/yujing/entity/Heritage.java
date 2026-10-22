package com.yujing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("heritage")
public class Heritage {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String level;       // 国家级 / 市级
    private String category;
    private String cover;
    private String intro;
    private String inheritor;
    private String experienceSpot;
    private Long relatedSpotId;
}
