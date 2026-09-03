# PRD-05: 가챠 시스템

> **프로젝트**: 한월(韓月)  
> **작성일**: 2026-05-06  
> **우선순위**: 🔴 최우선 (핵심 수익 & 성장 모델)

---

## 1. 개요

한월(韓月)의 가챠 시스템은 **캐릭터 가챠**와 **무기 가챠**로 분리 운영된다. 상시 가챠(기본 풀)와 이벤트 가챠(기간 한정 픽업)가 존재하며, 천장(Pity) 시스템으로 최소한의 보장을 제공한다. 모든 가챠 트랜잭션은 `@Transactional`로 원자적으로 처리되어 데이터 정합성을 보장한다.

---

## 2. 가챠 종류

### 2-1. 캐릭터 가챠

| 종류 | 설명 | 기간 |
|------|------|------|
| **상시 가챠** | 기본 캐릭터 풀에서 뽑기 | 상시 |
| **이벤트 가챠** | 특정 S등급 캐릭터 픽업 확률 상승 | 기간 한정 (2~3주) |
| **초보자 가챠** | 튜토리얼 후 무료 10연차 (1회 한정) | 신규 유저 한정 |

### 2-2. 무기 가챠

| 종류 | 설명 | 기간 |
|------|------|------|
| **상시 무기 가챠** | 기본 무기 풀에서 뽑기 | 상시 |
| **이벤트 무기 가챠** | 특정 전용무기 픽업 확률 상승 | 기간 한정 |

---

## 3. 확률 테이블

### 3-1. 캐릭터 가챠 확률

| 등급 | 상시 가챠 | 이벤트 가챠 (픽업) | 비고 |
|:----:|:---------:|:------------------:|------|
| **S** | 0.5% | 0.5% (픽업 캐릭 50%) | 천장으로도 획득 가능 |
| **A** | 13.5% | 13.5% | |
| **B** | 40% | 40% | |
| **C** | 46% | 46% | |

> ⚠️ **U등급은 가챠로 획득 불가** — S등급 파밍 승급으로만 획득

### 3-2. 무기 가챠 확률

| 등급 | 상시 가챠 | 이벤트 가챠 (픽업) |
|:----:|:---------:|:------------------:|
| **S** | 0.5% | 0.5% (픽업 무기 50%) |
| **A** | 15.5% | 15.5% |
| **B** | 38% | 38% |
| **C** | 46% | 46% |

### 3-3. 확률 관리

- 모든 확률은 **DB 테이블(gacha_rates)**에서 관리
- 운영 중 확률 실시간 조정 가능 (서버 재시작 불필요)
- 확률 변경 시 변경 이력 로그 저장

---

## 4. 천장 (Pity) 시스템

### 4-1. 캐릭터 가챠 천장

| 천장 종류 | 조건 | 효과 |
|-----------|------|------|
| **소프트 천장** | 70회 이후 | S등급 확률 매 회차 +5% 씩 증가 |
| **하드 천장** | 90회 | S등급 **100% 확정** 획득 |
| **픽업 보장** | 이벤트 가챠에서 S등급 획득 시 50%로 픽업 캐릭터 / 2번 연속 비픽업 시 다음 S등급 100% 픽업 확정 |

### 4-2. 무기 가챠 천장

| 천장 종류 | 조건 | 효과 |
|-----------|------|------|
| **하드 천장** | 80회 | S등급 무기 **100% 확정** |
| **픽업 보장** | S등급 무기 획득 시 50%로 픽업 무기 / 비픽업 시 다음 100% 픽업 확정 |

### 4-3. 천장 카운터 관리

- 천장 카운터는 **배너(가챠 풀)별로 독립** 관리
- 이벤트 가챠 종료 시 천장 카운터 **다음 이벤트로 이월**
- 상시 가챠 천장 카운터는 항상 유지

---

## 5. 뽑기 방식

### 5-1. 단일 뽑기 (1회)

| 항목 | 설명 |
|------|------|
| 비용 | 영석 160개 |
| 결과 | 캐릭터/무기 1개 획득 |
| 연출 | 카드 1장 뒤집기 애니메이션 |

### 5-2. 10연차 뽑기

| 항목 | 설명 |
|------|------|
| 비용 | 영석 1,600개 (할인 없음) |
| 결과 | 캐릭터/무기 10개 획득 |
| 보장 | 10연차 중 **최소 A등급 이상 1개 보장** |
| 연출 | 카드 10장 순차 뒤집기 → 등급별 이펙트 차등 |

---

## 6. 가챠 연출

### 6-1. CSS 3D 카드 뒤집기

```
[뽑기 버튼 클릭]
    → 화면 전환 (배경 어두워짐)
    → 카드 등장 (뒷면 상태)
    → 탭/클릭 시 카드 뒤집기 (CSS 3D Transform)
    → 등급별 차등 연출:
        C: 기본 뒤집기 (이펙트 없음)
        B: 파란 테두리 글로우
        A: 보라 광선 + 파티클
        S: 황금 폭발 + 전체 화면 이펙트 + 캐릭터 일러스트 등장
```

### 6-2. 10연차 연출

```
[10장 카드 나열]
    → 순차적으로 하나씩 뒤집기
    → S등급 출현 시: 사전에 특별 컷인 연출 삽입
    → 마지막 카드 뒤집기 후 결과 요약 화면
    → "다시 뽑기" / "확인" 버튼
```

---

## 7. 중복 캐릭터/무기 처리

### 7-1. 캐릭터 중복 획득 시

| 등급 | 처리 |
|:----:|------|
| C~A | 전용 교환 화폐 "무혼(武魂)"으로 변환 |
| S | 해당 캐릭터의 "각성 재료"로 변환 → 스킬 레벨업에 사용 |

### 7-2. 무기 중복 획득 시

| 등급 | 처리 |
|:----:|------|
| C~A | 강화 재료로 자동 변환 |
| S | 해당 무기 "재련" 재료로 변환 → 무기 특수 옵션 강화 |

---

## 8. 트랜잭션 처리 (핵심)

### 8-1. 가챠 실행 프로세스

```
@Transactional
가챠 실행:
    1. 유저 영석 잔액 확인 (부족 시 에러)
    2. 영석 차감
    3. 확률 테이블 기반 결과 계산
    4. 천장 카운터 업데이트
    5. 획득 캐릭터/무기 인벤토리에 추가 (중복 시 변환)
    6. 가챠 이력 로그 저장
    → 어느 단계에서든 실패 시 전체 롤백
```

### 8-2. 연타 방지

| 방어 수단 | 설명 |
|-----------|------|
| 서버 쿨다운 | 같은 유저의 가챠 요청 간 최소 2초 간격 강제 |
| 중복 요청 방지 | 동일 요청 ID로 중복 실행 차단 (멱등성 키) |
| 클라이언트 | 뽑기 버튼 클릭 후 결과 도착까지 비활성화 |

---

## 9. API 엔드포인트 (예상)

| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/api/gacha/banners` | 현재 활성 배너 목록 조회 |
| GET | `/api/gacha/banners/{id}/rates` | 배너별 확률표 조회 |
| POST | `/api/gacha/pull/single` | 단일 뽑기 실행 |
| POST | `/api/gacha/pull/multi` | 10연차 뽑기 실행 |
| GET | `/api/gacha/pity/{bannerId}` | 천장 카운터 조회 |
| GET | `/api/gacha/history` | 가챠 이력 조회 |

### 뽑기 요청 JSON
```json
{
    "bannerId": 1001,
    "pullType": "MULTI",
    "idempotencyKey": "uuid-xxxx-xxxx"
}
```

### 뽑기 결과 JSON
```json
{
    "results": [
        { "type": "CHARACTER", "id": 15, "name": "남궁설화", "grade": "A", "isNew": true },
        { "type": "CHARACTER", "id": 3, "name": "당소소", "grade": "B", "isNew": false, "converted": "무혼 10개" },
        { "type": "CHARACTER", "id": 28, "name": "제갈명", "grade": "S", "isNew": true, "isPickup": true }
    ],
    "pityCount": 45,
    "remainingCurrency": 3200
}
```

---

## 10. 데이터베이스 테이블 (예상)

### gacha_banners (배너 관리)
```sql
CREATE TABLE gacha_banners (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    type ENUM('CHARACTER','WEAPON') NOT NULL,
    sub_type ENUM('PERMANENT','EVENT','BEGINNER') NOT NULL,
    pickup_item_id BIGINT DEFAULT NULL,
    start_date DATETIME,
    end_date DATETIME,
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

### gacha_rates (확률 테이블)
```sql
CREATE TABLE gacha_rates (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    banner_id BIGINT NOT NULL,
    grade ENUM('C','B','A','S') NOT NULL,
    rate DECIMAL(5,2) NOT NULL,  -- 퍼센트 (예: 1.50)
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (banner_id) REFERENCES gacha_banners(id)
);
```

### gacha_pity (천장 카운터)
```sql
CREATE TABLE gacha_pity (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    banner_id BIGINT NOT NULL,
    pull_count INT DEFAULT 0,
    last_s_grade_pull INT DEFAULT 0,
    guaranteed_pickup BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (banner_id) REFERENCES gacha_banners(id),
    UNIQUE KEY (user_id, banner_id)
);
```

### gacha_history (가챠 이력)
```sql
CREATE TABLE gacha_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    banner_id BIGINT NOT NULL,
    pull_type ENUM('SINGLE','MULTI') NOT NULL,
    result_type ENUM('CHARACTER','WEAPON') NOT NULL,
    result_item_id BIGINT NOT NULL,
    result_grade ENUM('C','B','A','S') NOT NULL,
    is_pickup BOOLEAN DEFAULT FALSE,
    pity_count_at_pull INT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```
