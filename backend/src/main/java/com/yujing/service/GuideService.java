package com.yujing.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yujing.common.UserContext;
import com.yujing.dto.GuideGenerateRequest;
import com.yujing.entity.Guide;
import com.yujing.entity.Hotel;
import com.yujing.entity.Spot;
import com.yujing.mapper.GuideMapper;
import com.yujing.mapper.SpotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GuideService {

    @Autowired
    private GuideMapper guideMapper;

    @Autowired
    private SpotMapper spotMapper;

    @Autowired
    private RouteService routeService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private DeepseekService deepseekService;

    public Map<String, Object> generate(GuideGenerateRequest req) {
        Long userId = UserContext.getUserId();

        // 1. 查询指定去处
        List<Spot> spots = new ArrayList<>();
        if (req.getSpotIds() != null && !req.getSpotIds().isEmpty()) {
            spots = spotMapper.selectBatchIds(req.getSpotIds());
        }

        // 2. 路线规划
        List<List<Spot>> dailyPlan = routeService.plan(spots, req.getDays());

        // 3. 酒店推荐
        List<Hotel> hotels = hotelService.recommend(spots, req.getBudget());

        // 4. 拼接 Prompt
        String prompt = buildPrompt(req, dailyPlan, hotels);

        // 5. 调用大模型
        String markdown = deepseekService.chat(prompt);

        // 6. 写入数据库
        Guide guide = new Guide();
        guide.setUserId(userId);
        guide.setTitle("重庆" + req.getDays() + "日游攻略");
        guide.setParamsSummary(req.getDays() + "天 · " + req.getPeople() + "人 · " + req.getBudget());
        guide.setSpotIds(req.getSpotIds() == null ? "" : req.getSpotIds().stream().map(String::valueOf).collect(Collectors.joining(",")));
        guide.setMarkdownContent(markdown);
        guide.setIsSaved(0);
        guide.setCreateTime(LocalDateTime.now());
        guideMapper.insert(guide);

        Map<String, Object> result = new HashMap<>();
        result.put("guideId", guide.getId());
        result.put("title", guide.getTitle());
        result.put("markdownContent", markdown);
        return result;
    }

    private String buildPrompt(GuideGenerateRequest req, List<List<Spot>> dailyPlan, List<Hotel> hotels) {
        StringBuilder sb = new StringBuilder();
        sb.append("请为我生成一份重庆旅游攻略。\n\n");
        sb.append("【出行信息】\n");
        sb.append("- 出行天数：").append(req.getDays()).append("天\n");
        sb.append("- 出行人数：").append(req.getPeople()).append("人\n");
        sb.append("- 预算范围：").append(req.getBudget()).append("\n");
        if (req.getTravelDate() != null && !req.getTravelDate().isEmpty()) {
            sb.append("- 出行时间：").append(req.getTravelDate()).append("\n");
        }
        sb.append("- 兴趣偏好：").append(String.join("、", req.getInterests() == null ? Collections.emptyList() : req.getInterests())).append("\n");
        sb.append("- 交通方式：").append(req.getTransport()).append("\n");
        sb.append("- 住宿偏好：").append(req.getAccommodation()).append("\n");
        if (req.getSpecialNeed() != null && !req.getSpecialNeed().isEmpty()) {
            sb.append("- 特殊需求：").append(req.getSpecialNeed()).append("\n");
        }

        sb.append("\n【已优化的每日行程顺序】（请按此顺序展开每日行程）\n");
        for (int i = 0; i < dailyPlan.size(); i++) {
            sb.append("- Day ").append(i + 1).append("：");
            List<Spot> daySpots = dailyPlan.get(i);
            sb.append(daySpots.stream().map(Spot::getName).collect(Collectors.joining(" → ")));
            sb.append("\n");
        }

        if (!hotels.isEmpty()) {
            sb.append("\n【推荐酒店】（请在酒店推荐章节列出以下酒店）\n");
            for (Hotel h : hotels) {
                sb.append("- ").append(h.getName()).append("（").append(h.getRegion())
                        .append("，").append(h.getPriceRange()).append("）：").append(h.getIntro()).append("\n");
            }
        }

        sb.append("\n请按以下结构输出 Markdown：\n");
        sb.append("# 重庆X日游攻略\n");
        sb.append("## 行程概览\n## 路线说明\n## 每日行程\n### Day 1\n### Day 2\n...\n## 美食推荐\n## 酒店推荐\n## 交通与住宿建议\n## 注意事项\n");

        return sb.toString();
    }

    public Guide detail(Long id) {
        return guideMapper.selectById(id);
    }

    public void save(Long id, String posterUrl) {
        Guide guide = guideMapper.selectById(id);
        if (guide != null) {
            guide.setIsSaved(1);
            if (posterUrl != null && !posterUrl.isEmpty()) {
                guide.setPosterUrl(posterUrl);
            }
            guideMapper.updateById(guide);
        }
    }

    public void savePoster(Long id, String posterUrl) {
        Guide guide = guideMapper.selectById(id);
        if (guide != null) {
            guide.setPosterUrl(posterUrl);
            guideMapper.updateById(guide);
        }
    }

    public List<Guide> historyList() {
        Long userId = UserContext.getUserId();
        LambdaQueryWrapper<Guide> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Guide::getUserId, userId)
                .eq(Guide::getIsSaved, 1)
                .orderByDesc(Guide::getCreateTime);
        return guideMapper.selectList(wrapper);
    }

    public List<Guide> posterRecordList() {
        Long userId = UserContext.getUserId();
        LambdaQueryWrapper<Guide> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Guide::getUserId, userId)
                .isNotNull(Guide::getPosterUrl)
                .ne(Guide::getPosterUrl, "")
                .orderByDesc(Guide::getCreateTime);
        return guideMapper.selectList(wrapper);
    }

    public void delete(Long id) {
        guideMapper.deleteById(id);
    }
}
