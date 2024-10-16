package io.rednotice.slack.service;

import io.rednotice.common.AuthUser;
import io.rednotice.slack.entity.Slack;
import io.rednotice.slack.repository.SlackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SlackService {

    @Value("${SLACK_INCOMING_HOOK_URL}")
    private String webhookUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    private final SlackRepository slackRepository;

    public void sendNotification(String message) {

        // Slack 메시지 전송
        String payload = String.format("{\"text\":\"%s\"}", message);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(payload, headers);
        restTemplate.exchange(webhookUrl, HttpMethod.POST, entity, String.class);

        // 로그인된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((AuthUser) authentication.getPrincipal()).getId();

        // 알림 정보 DB에 저장
        Slack slack  = new Slack(userId, message, LocalDateTime.now());
        slackRepository.save(slack);
    }
}
