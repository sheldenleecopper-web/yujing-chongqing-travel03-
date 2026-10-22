package com.yujing.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class DeepseekService {

    @Value("${deepseek.api-key}")
    private String apiKey;

    @Value("${deepseek.base-url}")
    private String baseUrl;

    @Value("${deepseek.model}")
    private String model;

    @Value("${deepseek.timeout}")
    private int timeout;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 调用 Deepseek API 生成 Markdown 攻略
     * @param prompt 完整的 prompt
     * @return Markdown 内容
     */
    public String chat(String prompt) {
        try {
            ObjectNode requestBody = objectMapper.createObjectNode();
            requestBody.put("model", model);
            ArrayNode messages = requestBody.putArray("messages");

            ObjectNode systemMsg = objectMapper.createObjectNode();
            systemMsg.put("role", "system");
            systemMsg.put("content", "你是重庆旅游攻略专家，请用 Markdown 格式输出攻略，包含行程概览、路线说明、每日行程、美食推荐、酒店推荐、交通与住宿建议、注意事项。");
            messages.add(systemMsg);

            ObjectNode userMsg = objectMapper.createObjectNode();
            userMsg.put("role", "user");
            userMsg.put("content", prompt);
            messages.add(userMsg);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    baseUrl + "/chat/completions",
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg != null && msg.contains("401")) {
                throw new RuntimeException("大模型鉴权失败，请检查 API Key 配置", e);
            } else if (msg != null && msg.contains("402")) {
                throw new RuntimeException("大模型账户余额不足，请充值后再试", e);
            } else if (msg != null && msg.contains("429")) {
                throw new RuntimeException("大模型请求过于频繁，请稍后再试", e);
            }
            throw new RuntimeException("大模型调用失败: " + msg, e);
        }
    }
}
