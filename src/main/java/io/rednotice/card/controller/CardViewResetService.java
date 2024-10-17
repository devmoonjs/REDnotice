package io.rednotice.card.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CardViewResetService {
    private final RedisTemplate<String, Object> redisTemplate;

    @Scheduled(cron = "0 0 0 * * ?")  // 매일 자정 실행
    public void resetCardViews() {
        // 모든 카드 조회수 키 삭제
        Set<String> viewKeys = redisTemplate.keys("card:views:*");
        if (viewKeys != null) {
            redisTemplate.delete(viewKeys);
        }

        // 인기 카드 랭킹 초기화
        redisTemplate.delete("card:ranking");
    }
}
