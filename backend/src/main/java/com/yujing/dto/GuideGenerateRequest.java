package com.yujing.dto;

import lombok.Data;

import java.util.List;

@Data
public class GuideGenerateRequest {
    private List<Long> spotIds;
    private Integer days;
    private Integer people;
    private String budget;
    private String travelDate;
    private List<String> interests;
    private String transport;
    private String accommodation;
    private String specialNeed;
}
