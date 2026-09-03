# PRD-03: 캐릭터 · 파티 · 장비 시스템

> **프로젝트**: 한월(韓月)  
> **작성일**: 2026-05-06  
> **우선순위**: 🔴 최우선 (게임의 핵심 자산)

---

## 1. 개요

한월(韓月)의 캐릭터, 파티 편성, 장비 시스템을 정의한다. 캐릭터는 CBAS+U 등급 체계를 따르며, 5가지 속성과 5가지 직업군으로 분류된다. 장비는 등급 변경 없이 강화만 가능하며, 캐릭터별 전용 무기가 존재한다.

---

## 2. 캐릭터 시스템

### 2-1. 등급 체계

| 등급 | 명칭 | 색상 | 가챠 확률(예시) | 비고 |
|:----:|------|:----:|:---------------:|------|
| **C** | 일반 | ⬜ Gray | 50% | 기본 등급 |
| **B** | 고급 | 🟦 Blue | 30% | |
| **A** | 희귀 | 🟪 Purple | 15% | |
| **S** | 전설 | 🟨 Gold | 5% | U등급 승급 가능 |
| **U** | 초월 | 🟥 Red | 가챠 불가 | S등급 + 특수 재료로만 승급 |
| **L** | 레전더리 | 🟨 Green | 아이디당 1개 | U등급 승급 가능 |

### 2-2. S → U 승급 조건

| 조건 | 설명 |
|------|------|
| 기본 조건 | S등급 캐릭터 보유 |
| 레벨 조건 | 해당 캐릭터 최대 레벨 달성 |
| 재료 조건 | 전용 승급 재료 수집 (특정 던전/레이드에서 파밍) |
| 골드 조건 | 일정량의 골드 소모 |
| 결과 | U등급으로 승급, 스탯 대폭 상승 + 새로운 스킬/패시브 해금 |

### 2-3. 속성 (Element)

| 속성 | 아이콘 | 오행 | 상성 (유리) | 상성 (불리) |
|:----:|:------:|:----:|:-----------:|:-----------:|
| 🔥 불(火) | 화염 | 火 | 바람 | 물 |
| 💧 물(水) | 수류 | 水 | 불 | 번개 |
| 🌿 바람(風) | 질풍 | 木 | 땅 | 불 |
| 🪨 땅(土) | 지진 | 土 | 번개 | 바람 |
| ⚡ 번개(雷) | 뇌전 | 金 | 물 | 땅 |

> **상성 관계**: 불→바람→땅→번개→물→불 (순환 구조)  
> 유리 속성으로 공격 시 데미지 1.5배, 불리 속성은 0.75배

### 2-4. 직업군 (Class/Role)

| 직업 | 역할 | 기본 스탯 경향 | 설명 |
|------|:----:|:--------------:|------|
| **검객** (劍客) | 딜러 | 공격↑ 속도↑ | 높은 화력과 빠른 행동 |
| **도사** (道士) | 서포터/딜러 | 공격↑ HP↓ | 범위기와 디버프 특화 |
| **의선** (醫仙) | 힐러 | HP↑ 방어↑ | 힐, 정화, 보호막 |
| **암기사** (暗器使) | 딜러 | 공격↑ 방어↓ | 크리티컬, 독, 출혈 특화 |
| **호위무사** (護衛武士) | 탱커 | HP↑↑ 방어↑↑ | 도발, 피해 흡수, 보호 |

### 2-5. 캐릭터 스탯

| 스탯 | 약칭 | 설명 |
|------|:----:|------|
| HP | HP | 체력. 0이 되면 전투 불능 |
| 공격력 | ATK | 일반 공격 및 스킬 데미지의 기반 수치 |
| 방어력 | DEF | 받는 피해 감소 |
| 속도 | SPD | 행동 게이지 충전 속도 결정 |
| 치명타율 | CRIT% | 치명타 발생 확률 |
| 치명타 피해 | CDMG | 치명타 시 추가 데미지 배율 |
| 효과 적중 | EHR | 디버프/상태이상 적중 확률 |
| 효과 저항 | RES | 디버프/상태이상 저항 확률 |

### 2-6. 캐릭터 스킬 구성

| 스킬 종류 | 개수 | 리소스 | 설명 |
|-----------|:----:|:------:|------|
| **일반 공격** | 1 | SP +1 회복 | 기본 공격, SP 회복용 |
| **전투 스킬** | 1~2 | SP 소모 | 강력한 공격/버프/디버프 |
| **필살기 (Ultimate)** | 1 | 에너지 100% | 턴 무시 즉시 발동, 고위력 |
| **패시브** | 1 | 자동 | 조건 충족 시 자동 발동 (U등급 시 추가 해금) |

### 2-7. 주인공 Variant 시스템 (성별 분기)

주인공(남궁세가 막내)은 성별에 따라 **스킬 이름·속성·연출만 분리**되고,  
**데미지 배율, SP 비용, 에너지 등 수치는 100% 동일**하다.

| 스킬 슬롯 | 남캐 (⚡뇌) | 여캐 (💧수/빙) | 데미지 배율 |
|-----------|-------------|----------------|:-----------:|
| 일반 공격 | 뇌전일섬 | 빙하참 | 1.0x |
| 전투 스킬 | 상승검법 | 북해빙공 | 2.5x |
| 필살기 | 만뇌귀종 | 극빙천지 | 4.0x |
| 패시브 | 뇌신의 잔영 | 빙결의 여운 | - |

> DB의 `skills` 테이블에 `name_male`, `name_female`, `element_male`, `element_female` 컬럼으로 관리.  
> 서버는 수치만 처리하고, 클라이언트에 응답 시 유저 gender에 따라 이름/속성/이펙트를 분기 전달.

---

## 3. 파티 시스템

### 3-1. 기본 규칙

| 항목 | 설명 |
|------|------|
| 최대 인원 | 4명 |
| 편성 제한 | 같은 캐릭터 중복 편성 불가 |
| 프리셋 | 최대 5개 파티 프리셋 저장 가능 |
| 필수 조건 | 최소 1명 이상 편성해야 전투 진입 가능 |

### 3-2. 파티 편성 UI 정보

파티 편성 화면에서 각 캐릭터의 다음 정보가 표시된다:

- 캐릭터 초상화 (등급별 테두리 색상)
- 이름, 레벨
- 속성 아이콘
- 직업 아이콘
- 전투력
- 장착 장비 요약

### 3-3. 파티 시너지 (향후 확장)

> 동일 속성 2인 이상 시 속성 보너스, 동일 세가/문파 출신 캐릭터 시너지 등  
> 초기 버전에서는 미구현, 향후 업데이트에서 추가 가능

---

## 4. 장비 시스템

### 4-1. 장비 등급

| 등급 | 명칭 | 색상 | 획득 경로 |
|:----:|------|:----:|-----------|
| **C** | 일반 | ⬜ Gray | 상점 구매, 낮은 난이도 던전 드랍 |
| **B** | 고급 | 🟦 Blue | 중급 던전 드랍 |
| **A** | 희귀 | 🟪 Purple | 고급 던전/레이드 드랍 |
| **S** | 전설 | 🟨 Gold | 레이드/무한탑 최고 보상, 무기 가챠 |

> ⚠️ **등급 변경(승급) 불가**: C장비는 영원히 C, S장비는 영원히 S

### 4-2. 장비 슬롯

| 슬롯 | 종류 | 주요 스탯 |
|------|------|-----------|
| **무기** | 검, 법장, 침, 암기, 대도 등 | ATK, CRIT%, CDMG |
| **갑옷** | 경갑, 도포, 가죽갑 등 | HP, DEF |
| **장신구** | 반지, 목걸이, 호부 등 | SPD, EHR, RES |
| **신발** | 경공화, 보법화 등 | SPD, 회피 |

### 4-3. 장비 강화

| 항목 | 설명 |
|------|------|
| 강화 재료 | 강화석 (던전 드랍) + 골드 |
| 강화 단계 | +1 ~ +15 (등급별 최대치 상이) |
| 강화 성공률 | +1~+5: 100% / +6~+10: 70% / +11~+15: 50% |
| 강화 실패 시 | 강화 단계 유지 (하락 없음), 재료만 소모 |
| 최대 강화 | C등급: +10, B등급: +12, A등급: +13, S등급: +15 |

### 4-4. 전용 무기

| 항목 | 설명 |
|------|------|
| 정의 | 특정 캐릭터 전용으로 설계된 S등급 무기 |
| 효과 | 해당 캐릭터 장착 시 **전용 패시브/스킬 강화** 발동 |
| 다른 캐릭터 장착 시 | 기본 스탯만 적용, 전용 효과 미발동 |
| 획득 경로 | 무기 가챠 (상시/이벤트) |

---

## 5. API 엔드포인트 (예상)

| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/api/characters` | 보유 캐릭터 목록 조회 |
| GET | `/api/characters/{id}` | 캐릭터 상세 정보 |
| POST | `/api/characters/{id}/level-up` | 캐릭터 레벨업 |
| POST | `/api/characters/{id}/upgrade` | S→U 등급 승급 |
| GET | `/api/party` | 현재 파티 편성 조회 |
| PUT | `/api/party` | 파티 편성 변경 |
| GET | `/api/party/presets` | 파티 프리셋 목록 |
| POST | `/api/party/presets` | 파티 프리셋 저장 |
| GET | `/api/equipment` | 보유 장비 목록 |
| POST | `/api/equipment/{id}/enhance` | 장비 강화 |
| POST | `/api/equipment/{id}/equip` | 장비 장착 |
| POST | `/api/equipment/{id}/unequip` | 장비 해제 |

---

## 6. 데이터베이스 테이블 (예상)

### characters (캐릭터 마스터)
```sql
CREATE TABLE characters (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    grade ENUM('C','B','A','S','U') NOT NULL,
    element ENUM('FIRE','WATER','WIND','EARTH','LIGHTNING') NOT NULL,
    role ENUM('SWORDSMAN','TAOIST','HEALER','ASSASSIN','GUARDIAN') NOT NULL,
    base_hp INT NOT NULL,
    base_atk INT NOT NULL,
    base_def INT NOT NULL,
    base_spd INT NOT NULL,
    base_crit_rate DECIMAL(5,2) DEFAULT 5.00,
    base_crit_dmg DECIMAL(5,2) DEFAULT 150.00,
    skill_normal_id BIGINT,
    skill_battle_id BIGINT,
    skill_ultimate_id BIGINT,
    skill_passive_id BIGINT,
    exclusive_weapon_id BIGINT DEFAULT NULL,
    description TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

### user_characters (유저 보유 캐릭터)
```sql
CREATE TABLE user_characters (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    character_id BIGINT NOT NULL,
    level INT DEFAULT 1,
    current_exp BIGINT DEFAULT 0,
    current_grade ENUM('C','B','A','S','U') NOT NULL,
    equipped_weapon_id BIGINT DEFAULT NULL,
    equipped_armor_id BIGINT DEFAULT NULL,
    equipped_accessory_id BIGINT DEFAULT NULL,
    equipped_shoes_id BIGINT DEFAULT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (character_id) REFERENCES characters(id)
);
```

### equipment (장비 마스터)
```sql
CREATE TABLE equipment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    slot ENUM('WEAPON','ARMOR','ACCESSORY','SHOES') NOT NULL,
    grade ENUM('C','B','A','S') NOT NULL,
    base_atk INT DEFAULT 0,
    base_hp INT DEFAULT 0,
    base_def INT DEFAULT 0,
    base_spd INT DEFAULT 0,
    base_crit_rate DECIMAL(5,2) DEFAULT 0,
    base_crit_dmg DECIMAL(5,2) DEFAULT 0,
    is_exclusive BOOLEAN DEFAULT FALSE,
    exclusive_character_id BIGINT DEFAULT NULL,
    exclusive_effect_description TEXT DEFAULT NULL,
    description TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

### user_equipment (유저 보유 장비)
```sql
CREATE TABLE user_equipment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    equipment_id BIGINT NOT NULL,
    enhance_level INT DEFAULT 0,
    is_equipped BOOLEAN DEFAULT FALSE,
    equipped_to_character_id BIGINT DEFAULT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (equipment_id) REFERENCES equipment(id)
);
```
