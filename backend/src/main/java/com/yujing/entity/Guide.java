package com.yujing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("guide")
public class Guide {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String title;
    private String paramsSummary;
    private String spotIds;
    private String markdownContent;
    private String posterUrl;
    private Integer isSaved;     // 0 未保存 1 已保存
    private LocalDateTime createTime;
}
