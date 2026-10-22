package com.yujing.service;

import com.yujing.entity.Spot;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RouteService {

    /**
     * 路线规划：按区域聚类 + 同日内最近邻排序
     * @param spots 指定去处列表
     * @param days 出行天数
     * @return 每日去处顺序，外层按天，内层按游玩顺序
     */
    public List<List<Spot>> plan(List<Spot> spots, int days) {
        if (spots == null || spots.isEmpty()) {
            return Collections.emptyList();
        }

        // 1. 按区域分组
        Map<String, List<Spot>> regionGroups = spots.stream()
                .collect(Collectors.groupingBy(s -> s.getRegion() == null ? "未知" : s.getRegion()));

        // 2. 将区域组分配到各天
        List<List<Spot>> dailyPlan = new ArrayList<>();
        List<Map.Entry<String, List<Spot>>> entries = new ArrayList<>(regionGroups.entrySet());

        // 跨区县的单独占天，主城区域可合并
        Set<String> mainUrban = new HashSet<>(Arrays.asList(
                "渝中区", "江北区", "南岸区", "九龙坡区", "沙坪坝区",
                "大渡口区", "渝北区", "巴南区", "北碚区"
        ));

        List<List<Spot>> mainUrbanGroups = new ArrayList<>();
        List<List<Spot>> outerGroups = new ArrayList<>();

        for (Map.Entry<String, List<Spot>> entry : entries) {
            if (mainUrban.contains(entry.getKey())) {
                mainUrbanGroups.add(entry.getValue());
            } else {
                outerGroups.add(entry.getValue());
            }
        }

        // 主城区去处合并后按天数分配
        List<Spot> allMainUrban = mainUrbanGroups.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        int remainingDays = days - outerGroups.size();
        if (remainingDays < 1) remainingDays = 1;

        // 将主城区去处均分到 remainingDays 天
        List<List<Spot>> mainUrbanDaily = splitEvenly(allMainUrban, remainingDays);
        dailyPlan.addAll(mainUrbanDaily);

        // 区县去处每组占一天
        for (List<Spot> outer : outerGroups) {
            dailyPlan.add(outer);
        }

        // 3. 每天内部按最近邻排序
        for (List<Spot> daySpots : dailyPlan) {
            sortByNearestNeighbor(daySpots);
        }

        return dailyPlan;
    }

    private List<List<Spot>> splitEvenly(List<Spot> spots, int parts) {
        List<List<Spot>> result = new ArrayList<>();
        if (spots.isEmpty() || parts <= 0) {
            result.add(new ArrayList<>(spots));
            return result;
        }
        int size = spots.size();
        int perPart = (size + parts - 1) / parts;
        for (int i = 0; i < size; i += perPart) {
            result.add(new ArrayList<>(spots.subList(i, Math.min(i + perPart, size))));
        }
        return result;
    }

    /**
     * 最近邻排序：从第一个去处开始，每次选距离最近的下一个
     */
    private void sortByNearestNeighbor(List<Spot> spots) {
        if (spots.size() <= 2) return;

        List<Spot> sorted = new ArrayList<>();
        Spot current = spots.get(0);
        sorted.add(current);
        spots.remove(0);

        while (!spots.isEmpty()) {
            Spot nearest = null;
            double minDist = Double.MAX_VALUE;
            for (Spot s : spots) {
                double d = distance(current, s);
                if (d < minDist) {
                    minDist = d;
                    nearest = s;
                }
            }
            sorted.add(nearest);
            spots.remove(nearest);
            current = nearest;
        }

        spots.clear();
        spots.addAll(sorted);
    }

    private double distance(Spot a, Spot b) {
        if (a.getLng() == null || a.getLat() == null || b.getLng() == null || b.getLat() == null) {
            return 0;
        }
        BigDecimal lngDiff = a.getLng().subtract(b.getLng());
        BigDecimal latDiff = a.getLat().subtract(b.getLat());
        return Math.sqrt(lngDiff.doubleValue() * lngDiff.doubleValue()
                + latDiff.doubleValue() * latDiff.doubleValue());
    }
}
