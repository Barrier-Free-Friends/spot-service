# Spot Service

Barrier Free Friends MSA의 컬렉션 서비스입니다. 장소(Spot) 정보와 사용자의 즐겨찾기/모음집(Collection)을 관리합니다.

전체 서비스 구성은 [조직 README](https://github.com/Barrier-Free-Friends)를 참고하세요.

## 기술 스택
- Java 21, Spring Boot 3.5.7 (JPA, QueryDSL)
- PostgreSQL, JWT(jjwt), AWS
- Kafka, Resilience4j
- Eureka Client, Config Client

## 주요 구조
- `collection/` — application(command/query), domain, infrastructure/persistence, presentation (CollectionController)
- `spot/` — SpotService, domain/Spot, infrastructure/persistence
- 낙관적 락 재시도 AOP (`OptimisticLockRetryAspect`)로 동시성 충돌 대응

## 실행

```bash
./gradlew bootRun
```

기본 포트: `3500`

필요 환경변수: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`

## Docker

```bash
docker build -t spot-service .
docker run -p 3500:3500 spot-service
```
