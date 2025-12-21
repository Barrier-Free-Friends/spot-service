package org.bf.spotservice.collection.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// retry 어노테이션 구성. 낙관적 락 예외 발생 시 재시도할 메서드에 추가
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RetryOnOptimisticLock {
}

