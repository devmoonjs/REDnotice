package io.rednotice.aop;

import io.rednotice.slack.service.SlackService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SlackNotificationAspect {

    private final SlackService slackService;

    @Autowired
    public SlackNotificationAspect(SlackService slackService) {
        this.slackService = slackService;
    }

    @Pointcut("@annotation(io.rednotice.common.anotation.SlackNotify)")
    public void slackNotificationPointcut() {
    }

    // 메서드가 정상적으로 실행된 후에 슬랙 알림 전송
    @AfterReturning("slackNotificationPointcut()")
    public void sendSlackNotification(JoinPoint joinPoint) {

        String methodName = joinPoint.getSignature().getName();
        String message = "메서드 '" + methodName + "'가 호출되었습니다.";
        
        slackService.sendNotification(message);
    }
}
