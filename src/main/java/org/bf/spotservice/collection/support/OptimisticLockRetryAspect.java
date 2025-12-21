package org.bf.spotservice.collection.support;

import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 낙관적 락 예외 발생 시 지정된 횟수만큼 재시도하는
 */

@Aspect
@Component
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE) // 재시도 Aspect가 트랜잭션 Aspect보다 먼저 실행되도록 설정
@RequiredArgsConstructor
public class OptimisticLockRetryAspect {

    private final OptimisticLockRetryProperties props;

    private static final List<Class<?>> RETRYABLE_EXCEPTIONS_TYPES;

    static {
        List<Class<?>> types = new ArrayList<>();

        // 직접 참조 가능한 JPA 예외 추가
        types.add(OptimisticLockException.class);

        String[] names = {
                "org.springframework.orm.ObjectOptimisticLockingFailureException",
                "org.springframework.dao.OptimisticLockingFailureException",
                "org.hibernate.StaleObjectStateException"
        };

        for (String name : names) {
            try {
                types.add(Class.forName(name));
            } catch (ClassNotFoundException ignored) {
            }
        }
        RETRYABLE_EXCEPTIONS_TYPES = Collections.unmodifiableList(types);
    }

    @Around("@annotation(org.bf.spotservice.collection.support.RetryOnOptimisticLock)")
    public Object aroundRetry(final ProceedingJoinPoint joinPoint) throws Throwable {

        int maxAttempts = props.getMaxAttempts();
        long delay = props.getInitialDelayMs();
        double multiplier = props.getMultiplier();
        long maxDelay = props.getMaxDelayMs();

        int attempt = 0;
        Throwable lastThrowable = null;

        // 재시도 루프 -> 낙관적 락 예외 발생 시 재시도\
        /**
         * attempt는 0부터 시작하여 최대 시도 횟수까지 증가.
         * 성공 시 결과 반환. 재시도 중 성공하면 로그 출력
         */
        while (attempt < maxAttempts) {
            attempt++;
            try {
                if (attempt > 1) {
                    log.debug("재시도 {}/{}  {}", attempt, maxAttempts, joinPoint.getSignature());
                }
                Object result = joinPoint.proceed();
                if (attempt > 1) {
                    log.info("재시도 성공 {}/{}  {}", attempt, maxAttempts, joinPoint.getSignature());
                }
                return result;
            } catch (Throwable ex) {
                lastThrowable = ex;
                // 재시도 가능한 예외인지 확인
                if (isRetryable(ex)) {
                    log.warn("낙관적 락 재시도 {}/{}  {}: {}", attempt, maxAttempts, joinPoint.getSignature(), ex.getMessage());
                    if (attempt >= maxAttempts) {
                        log.error("재시도 횟수 초과 ({})  {}", maxAttempts, joinPoint.getSignature());
                        throw ex;
                    }
                    // 백오프 설정
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException ie) {
                        // 현재 스레드 인터럽트 상태 복원하고 예외 재던짐
                        Thread.currentThread().interrupt();
                        throw ie;
                    }
                    delay = Math.min((long) (delay * multiplier), maxDelay);
                    continue;
                }
                throw ex;
            }
        }

        // 루프 나왔는데도 성공 못한 경우 마지막 예외 던짐
        if (lastThrowable != null) {
            throw lastThrowable;
        }
        return null;
    }

    private boolean isRetryable(Throwable ex) {

        if (ex == null) return false;

        // Error는 재시도 대상에서 제외 -> 확인 필요
        if (ex instanceof Error) return false;

        // 예외 및 원인 체인 검사
        Throwable t = ex;
        while (t != null) {
            for (Class<?> cls : RETRYABLE_EXCEPTIONS_TYPES) {
                if (cls.isInstance(t)) return true;
            }

            t = t.getCause();
        }
        return false;
    }
}

