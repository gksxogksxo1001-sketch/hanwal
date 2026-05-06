# PRD-07: 상점 · 인벤토리 · 재화 시스템

> **프로젝트**: 한월(韓月)  
> **작성일**: 2026-05-06  
> **우선순위**: 🔴 필수

---

## 1. 개요

한월(韓月)의 경제 시스템을 정의한다. 재화(골드, 영석, 스태미나 등), 상점 운영, 인벤토리와 보관함 관리를 포함한다.

---

## 2. 재화 시스템

### 2-1. 재화 종류

| 재화 | 아이콘 | 용도 | 획득 경로 |
|------|:------:|------|-----------|
| **골드** | 💰 | 상점 구매, 장비 강화, 등급 승급 비용 | 전투 보상, 요일 던전(일), 아이템 판매 |
| **영석** (프리미엄) | 💎 | 가챠 뽑기, 스태미나 충전 | 스토리 최초 클리어, 무한탑 마일스톤, 업적, (과금) |
| **스태미나** | ⚡ | 던전/레이드 입장 | 시간 자동 회복, 영석 충전 |
| **무혼** (武魂) | 🔮 | 가챠 중복 캐릭터 변환 화폐 → 상점에서 특수 아이템 교환 | 가챠 중복 캐릭터 |
| **강화석** | 🪨 | 장비 강화 재료 | 요일 던전(토), 전투 드랍 |
| **승급 재료** (속성별) | 🔥💧🌿🪨⚡ | 캐릭터 레벨업/S→U 승급 | 요일 던전(월~금), 레이드 |

### 2-2. 스태미나 시스템

| 항목 | 설명 |
|------|------|
| 최대치 | 120 (레벨에 따라 증가 가능) |
| 자동 회복 | 5분당 1 스태미나 회복 |
| 영석 충전 | 영석 60개 → 스태미나 60 즉시 충전 (1일 3회 제한) |
| 레벨업 충전 | 레벨업 시 스태미나 전량 회복 |
| 초과 보관 | 이벤트/보상으로 받은 스태미나는 최대치 초과 보관 가능 (자동 회복 멈춤) |

---

## 3. 상점 시스템

### 3-1. 골드 상점

| 탭 | 판매 품목 | 등급 제한 | 가격대 |
|----|-----------|:--------:|--------|
| 소모품 | 포션, 부활석, 버프 아이템 | - | 100~5,000 골드 |
| 장비 | 무기, 갑옷, 장신구, 신발 | C~B 등급만 | 1,000~20,000 골드 |
| 재료 | 강화석(소), 기본 경험치 재료 | - | 500~3,000 골드 |

> ⚠️ **A등급 이상 장비는 상점에서 판매하지 않음** — 파밍으로만 획득

### 3-2. 무혼 상점 (특수 상점)

| 품목 | 필요 무혼 | 월간 제한 |
|------|:---------:|:---------:|
| A등급 캐릭터 선택권 | 300 | 1회 |
| S등급 캐릭터 선택권 | 1,500 | 1회 |
| 영석 100개 | 50 | 5회 |
| U등급 승급 재료 | 200 | 2회 |

### 3-3. 상점 운영 관리

- 상점 품목/가격은 **DB 테이블(shop_items)**에서 관리
- 운영 중 품목 추가/삭제/가격 변경 가능
- 월간 제한 품목은 매월 1일 00:00 초기화

---

## 4. 인벤토리 시스템

### 4-1. 인벤토리 구조

| 탭 | 내용 | 비고 |
|----|------|------|
| 장비 | 보유 무기, 갑옷, 장신구, 신발 | 등급별 필터, 장착 여부 필터 |
| 소모품 | 포션, 부활석, 버프 아이템 | 스택 가능 (최대 999) |
| 재료 | 강화석, 승급 재료, 경험치 재료 | 스택 가능 |
| 기타 | 이벤트 아이템, 퀘스트 아이템 | |

### 4-2. 인벤토리 용량

| 항목 | 설명 |
|------|------|
| 장비 슬롯 | 기본 100칸 (영석으로 확장 가능, 최대 300칸) |
| 소모품/재료 | 종류별 스택이므로 칸 제한 없음 |
| 용량 초과 시 | 새 장비 획득 불가 → 우편함으로 전달 (7일 보관) |

---

## 5. 보관함 시스템

### 5-1. 기본 기능

| 항목 | 설명 |
|------|------|
| 용도 | 당장 사용하지 않는 아이템을 별도 보관 |
| 용량 | 기본 50칸 (확장 가능, 최대 150칸) |
| 이동 | 인벤토리 ↔ 보관함 자유 이동 |
| 접근 | 마을 보관함 시설에서만 접근 가능 |

### 5-2. UI 레이아웃

```
┌──────────────────────────────────────┐
│            보관함 UI                  │
│                                      │
│  ┌─── 인벤토리 ───┐ ┌─── 보관함 ───┐ │
│  │ [아이템1] →    │ │    [아이템A] │ │
│  │ [아이템2]      │ │ ← [아이템B] │ │
│  │ [아이템3]      │ │    [아이템C] │ │
│  │ ...           │ │    ...      │ │
│  └───────────────┘ └──────────────┘ │
│                                      │
│           [닫기]                     │
└──────────────────────────────────────┘

→: 인벤토리 → 보관함 이동
←: 보관함 → 인벤토리 이동
```

---

## 6. API 엔드포인트 (예상)

| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/api/shop/items` | 상점 품목 목록 |
| POST | `/api/shop/buy` | 상점 아이템 구매 |
| GET | `/api/shop/muhon` | 무혼 상점 품목 (남은 구매 횟수 포함) |
| POST | `/api/shop/muhon/buy` | 무혼 상점 구매 |
| GET | `/api/inventory` | 인벤토리 전체 조회 |
| GET | `/api/inventory?tab=EQUIPMENT` | 탭별 필터 조회 |
| POST | `/api/inventory/sell` | 아이템 판매 |
| GET | `/api/storage` | 보관함 조회 |
| POST | `/api/storage/deposit` | 인벤토리 → 보관함 이동 |
| POST | `/api/storage/withdraw` | 보관함 → 인벤토리 이동 |
| GET | `/api/currency` | 보유 재화 조회 |
| POST | `/api/stamina/recharge` | 영석으로 스태미나 충전 |

---

## 7. 데이터베이스 테이블 (예상)

### shop_items (상점 품목)
```sql
CREATE TABLE shop_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    shop_type ENUM('GOLD','MUHON') NOT NULL,
    item_type ENUM('EQUIPMENT','CONSUMABLE','MATERIAL','TICKET') NOT NULL,
    item_reference_id BIGINT,  -- 실제 아이템 테이블 참조
    price INT NOT NULL,
    currency_type ENUM('GOLD','MUHON') NOT NULL,
    grade ENUM('C','B','A','S') DEFAULT NULL,
    monthly_limit INT DEFAULT NULL,  -- NULL이면 무제한
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

### user_inventory (유저 인벤토리)
```sql
CREATE TABLE user_inventory (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    item_type ENUM('EQUIPMENT','CONSUMABLE','MATERIAL','ETC') NOT NULL,
    item_id BIGINT NOT NULL,
    quantity INT DEFAULT 1,
    storage_type ENUM('INVENTORY','STORAGE') DEFAULT 'INVENTORY',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### user_purchase_history (구매 이력)
```sql
CREATE TABLE user_purchase_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    shop_item_id BIGINT NOT NULL,
    quantity INT DEFAULT 1,
    total_price INT NOT NULL,
    purchased_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```
