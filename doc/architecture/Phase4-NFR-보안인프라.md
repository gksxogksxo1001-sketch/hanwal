# 🛡️ Phase 4: 방어 체계 & 인프라 설계 (NFR)

> **프로젝트**: 한월(韓月)  
> **작성일**: 2026-05-06  
> **상태**: 🔶 모험가 승인 대기 중

---

## 1. 이동 변조(핵) 방지

### 1-1. 서버 측 좌표 검증

```
[클라이언트]                               [서버]
    │                                        │
    │ POST /api/map/position                 │
    │ {x: 420, y: 310, timestamp: T}         │
    │ ──────────────────────────────────▶    │
    │                                        ├─ 검증 1: 이전 위치와의 거리
    │                                        │   distance = √((x2-x1)² + (y2-y1)²)
    │                                        │   maxAllowed = SPD * (T2-T1) * 1.2
    │                                        │   distance > maxAllowed → 핵 의심
    │                                        │
    │                                        ├─ 검증 2: 맵 경계 내 좌표인지
    │                                        │   0 ≤ x ≤ MAP_WIDTH
    │                                        │   0 ≤ y ≤ MAP_HEIGHT
    │                                        │
    │                                        ├─ 검증 3: 건물/벽 충돌 영역 진입
    │                                        │   isCollision(x, y) → 핵 의심
    │                                        │
    │  ◀──── {valid: false, correctedPos} ───┤ 비정상 시: 마지막 유효 위치로 롤백
    │  ◀──── {valid: true} ──────────────────┤ 정상 시: 위치 업데이트
```

### 1-2. 이동 핵 탐지 기준

| 검증 항목 | 기준 | 조치 |
|-----------|------|------|
| 순간이동 | 1초 내 200px 이상 이동 | 롤백 + 경고 로그 |
| 맵 이탈 | 맵 경계 밖 좌표 | 롤백 |
| 벽 관통 | 충돌 영역 좌표 진입 | 롤백 |
| 누적 위반 | 5분 내 10회 이상 비정상 | 접속 차단 30분 |

---

## 2. 전투 행동 검증

### 2-1. 서버 권위(Server Authority) 원칙

| 항목 | 클라이언트 역할 | 서버 역할 |
|------|:-------------:|:---------:|
| 데미지 계산 | ❌ 표시만 | ✅ **서버가 계산** |
| HP 차감 | ❌ 연출만 | ✅ **서버가 관리** |
| 턴 순서 결정 | ❌ 표시만 | ✅ **서버가 결정** |
| 스킬 사용 가능 여부 | ❌ 표시만 | ✅ **서버가 검증** |
| 전투 결과 (승/패) | ❌ 표시만 | ✅ **서버가 판정** |

### 2-2. 전투 행동 검증 체크리스트

```
POST /api/battle/{id}/action 수신 시:

1. ✅ battleSession이 해당 유저 소유인지
2. ✅ battleSession 상태가 IN_PROGRESS인지
3. ✅ 요청한 characterId가 현재 턴 순서의 유닛인지
4. ✅ 선택한 스킬이 해당 캐릭터가 보유한 스킬인지
5. ✅ SP가 충분한지 (SP 소모 스킬의 경우)
6. ✅ 에너지가 충분한지 (필살기의 경우)
7. ✅ 타겟이 유효한지 (살아있는 적/아군)
8. ✅ 연타 방지: 같은 요청의 중복 제출 차단 (idempotency)
```

---

## 3. 가챠 트랜잭션 보호

### 3-1. 동시성 이슈 방어

```java
@Service
public class GachaService {

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public GachaResultDto pull(Long userId, Long bannerId, String idempotencyKey) {
        // 1. 멱등성 키로 중복 요청 확인
        if (gachaHistoryRepo.existsByIdempotencyKey(idempotencyKey)) {
            return getCachedResult(idempotencyKey);
        }
        
        // 2. 재화 차감 (SELECT FOR UPDATE으로 락)
        User user = userRepo.findByIdForUpdate(userId);
        if (user.getPremiumCurrency() < COST) {
            throw new InsufficientCurrencyException();
        }
        user.deductCurrency(COST);
        
        // 3. 확률 계산 & 결과 결정
        GachaResult result = probabilityEngine.roll(bannerId, userId);
        
        // 4. 결과 저장 (캐릭터/무기 지급)
        deliverRewards(userId, result);
        
        // 5. 천장 카운터 갱신
        updatePityCounter(userId, bannerId, result);
        
        // 6. 이력 저장
        saveHistory(userId, bannerId, result, idempotencyKey);
        
        return result;
        // → 어느 단계에서든 실패 시 전체 롤백 (@Transactional)
    }
}
```

### 3-2. 가챠 연타 방지

| 방어 레이어 | 방법 |
|------------|------|
| 프론트엔드 | 뽑기 버튼 클릭 후 3초간 비활성화 |
| API 게이트웨이 | Rate Limiting: 유저당 가챠 API 5초에 1회 |
| 서비스 | 멱등성 키(UUID)로 동일 요청 중복 처리 방지 |
| DB | `SELECT FOR UPDATE`로 재화 락 |

---

## 4. 인증 & 세션 보안

| 항목 | 방법 |
|------|------|
| 비밀번호 저장 | BCrypt 해싱 |
| 세션 관리 | Spring Session (HttpSession) |
| 로그인 실패 | 5회 실패 시 30분 계정 잠금 |
| CSRF | Spring Security CSRF 토큰 (API는 stateless라 주의) |
| XSS | 채팅 메시지 입력 시 HTML 태그 이스케이프 |
| SQL 인젝션 | JPA Parameterized Query 사용 (기본 방어) |

---

## 5. 성능 최적화 전략

### 5-1. DB 쿼리 최적화

| 시나리오 | 전략 |
|----------|------|
| 랭킹 조회 | 1시간 단위 캐싱 (Spring `@Cacheable`) |
| 캐릭터 목록 | Lazy Loading + 필요 시 Fetch Join |
| 전투 중 상태 | BattleSession을 메모리에 캐싱 (턴마다 DB 미접근) |
| 채팅 메시지 | DB 미저장 (메모리 버퍼 100건만 유지) |
| 맵 시설 데이터 | 서버 시작 시 로딩 후 메모리 캐싱 (변경 드묾) |

### 5-2. Throttle & Debounce

| 이벤트 | 전략 | 간격 |
|--------|------|:----:|
| 캐릭터 이동 | Throttle | 1초 |
| 채팅 전송 | Rate Limit | 1초 |
| 가챠 뽑기 | Debounce + Lock | 5초 |
| 랭킹 갱신 | Server-side Batch | 1시간 |

---

## 6. 에러 처리 & 예외 상황

### 6-1. 전역 예외 처리

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    // 재화 부족
    @ExceptionHandler(InsufficientCurrencyException.class)
    public ResponseEntity<ErrorResponse> handleCurrency(InsufficientCurrencyException e) {
        return ResponseEntity.status(400).body(
            new ErrorResponse("INSUFFICIENT_CURRENCY", "영석이 부족합니다.", e.getRequired(), e.getCurrent())
        );
    }
    
    // 전투 불가 상태
    @ExceptionHandler(InvalidBattleStateException.class)
    // ...
    
    // 중복 요청
    @ExceptionHandler(DuplicateRequestException.class)
    // ...
}
```

### 6-2. 브라우저 이탈 대응

| 상황 | 서버 대응 |
|------|-----------|
| 전투 중 새로고침 | BattleSession DB에 있으므로 복귀 가능 |
| 전투 중 탭 닫기 | 30분 후 자동 패배 처리 (스태미나 미환불) |
| 가챠 중 끊김 | 트랜잭션 커밋 완료 → 결과 보관, 미커밋 → 롤백 |
| WebSocket 끊김 | 재접속 시 채널 자동 재구독 |
| 세션 만료 | 자동 로그아웃, 로그인 페이지로 리다이렉트 |

---

## 7. 브라우저 호환성

| 브라우저 | 지원 | 비고 |
|----------|:----:|------|
| Chrome 90+ | ✅ | 기본 타겟 |
| Edge 90+ | ✅ | Chromium base |
| Firefox 90+ | ✅ | |
| Safari 15+ | ⚠️ | WebSocket 제한적 테스트 |
| IE | ❌ | 미지원 |

---

> 📌 **다음 단계**: Phase 5: 모듈 구조 & 빌드 지침
