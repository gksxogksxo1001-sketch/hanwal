# ⚔️ Phase 1: 하이레벨 아키텍처 (HLD)

> **프로젝트**: 한월(韓月)  
> **작성일**: 2026-05-06  
> **상태**: 🔶 모험가 승인 대기 중

---

## 1. 시스템 아키텍처 전체 구조도

```mermaid
graph TB
    subgraph CLIENT ["🖥️ 클라이언트 (웹 브라우저)"]
        TH[Thymeleaf 초기 레이아웃]
        CV[HTML5 Canvas<br/>마을 맵/캐릭터 렌더링]
        ALP[Alpine.js<br/>클라이언트 상태 관리]
        TW[Tailwind CSS<br/>UI 스타일링]
        FETCH[Fetch API<br/>비동기 게임 액션]
        WS_C[WebSocket Client<br/>실시간 채팅]
    end

    subgraph SERVER ["🏰 서버 (Spring Boot 3.x)"]
        SC[Spring Security<br/>인증/인가]
        
        subgraph CONTROLLERS ["📡 API 계층"]
            AUTH[Auth Controller]
            GAME[Game Controller]
            BATTLE[Battle Controller]
            GACHA[Gacha Controller]
            SOCIAL[Social Controller]
            RANK[Ranking Controller]
        end

        subgraph SERVICES ["⚙️ 서비스 계층"]
            AUTH_S[인증 서비스]
            CHAR_S[캐릭터 서비스]
            BATTLE_S[전투 엔진]
            GACHA_S[가챠 서비스]
            DUNGEON_S[던전/레이드 서비스]
            EQUIP_S[장비/강화 서비스]
            SHOP_S[상점/인벤 서비스]
            RANK_S[랭킹 서비스]
            MAP_S[맵/이동 서비스]
        end

        subgraph WEBSOCKET ["💬 WebSocket 계층"]
            STOMP[STOMP Broker]
            CHAT_H[채팅 핸들러]
            ALERT_H[알림 핸들러<br/>가챠 S등급 알림 등]
        end
    end

    subgraph DATABASE ["🗄️ 데이터베이스 (MySQL)"]
        DB_USER[(users<br/>user_characters<br/>user_equipment)]
        DB_GAME[(characters<br/>equipment<br/>skills)]
        DB_BATTLE[(battle_sessions<br/>battle_logs)]
        DB_GACHA[(gacha_banners<br/>gacha_rates<br/>gacha_pity)]
        DB_SOCIAL[(guilds<br/>friends<br/>chat_logs)]
        DB_RANK[(rankings<br/>ranking_rewards)]
        DB_DUNGEON[(dungeons<br/>raid_bosses<br/>drop_tables)]
    end

    %% 클라이언트 → 서버
    TH -->|초기 페이지 로드| SC
    FETCH -->|REST API 호출| CONTROLLERS
    WS_C -->|WebSocket 연결| STOMP

    %% 서버 내부
    CONTROLLERS --> SERVICES
    STOMP --> CHAT_H
    STOMP --> ALERT_H
    SERVICES --> DATABASE
    
    %% 서비스 간 의존
    GACHA_S -.->|S등급 획득 시 알림| ALERT_H
    BATTLE_S -.->|전투 결과 → 랭킹 갱신| RANK_S
    DUNGEON_S -.->|클리어 → 보상 지급| SHOP_S
```

---

## 2. Headless API 성채 구조

### 2-1. 핵심 원칙

```
┌─────────────────────────────────────────────────────────┐
│                    Headless 아키텍처                      │
│                                                          │
│  ❌ 컨트롤러가 HTML을 반환하지 않는다                       │
│  ✅ 컨트롤러는 오직 JSON 데이터만 반환한다                  │
│  ✅ Thymeleaf는 초기 레이아웃(껍데기)만 렌더링한다           │
│  ✅ 모든 게임 로직은 Fetch API로 비동기 처리한다            │
│  ✅ 향후 Unity/Three.js 3D 클라이언트로 교체 가능하다       │
│                                                          │
└─────────────────────────────────────────────────────────┘
```

### 2-2. 요청/응답 흐름

```mermaid
sequenceDiagram
    participant B as 브라우저
    participant TH as Thymeleaf
    participant API as REST API
    participant WS as WebSocket
    participant DB as MySQL

    Note over B,DB: 1. 최초 페이지 로드
    B->>TH: GET /game (페이지 요청)
    TH->>B: HTML 레이아웃 반환 (빈 껍데기 + JS)

    Note over B,DB: 2. 게임 데이터 로드
    B->>API: GET /api/user/profile
    API->>DB: 유저 정보 조회
    DB->>API: 유저 데이터
    API->>B: JSON 응답
    B->>B: Alpine.js로 UI 바인딩

    Note over B,DB: 3. 게임 액션 (예: 가챠)
    B->>API: POST /api/gacha/pull/multi
    API->>DB: @Transactional (영석 차감 + 결과 저장)
    DB->>API: 커밋 완료
    API->>B: JSON (뽑기 결과)
    API-->>WS: S등급 획득 시 전체 채팅 알림

    Note over B,DB: 4. 실시간 채팅
    B->>WS: WebSocket CONNECT
    B->>WS: SUBSCRIBE /topic/chat/global
    WS->>B: 실시간 메시지 수신
```

---

## 3. 레이어드 아키텍처 (Layered Architecture)

```mermaid
graph TD
    subgraph PRESENTATION ["📺 프레젠테이션 계층"]
        P1["Thymeleaf (초기 레이아웃)"]
        P2["REST Controller (@RestController)"]
        P3["WebSocket Handler (@MessageMapping)"]
    end

    subgraph APPLICATION ["⚙️ 애플리케이션 계층 (서비스)"]
        A1["AuthService"]
        A2["CharacterService"]
        A3["BattleEngine"]
        A4["GachaService"]
        A5["DungeonService"]
        A6["EquipmentService"]
        A7["ShopService"]
        A8["RankingService"]
        A9["MapService"]
        A10["ChatService"]
        A11["GuildService"]
    end

    subgraph DOMAIN ["🎯 도메인 계층 (Entity/VO)"]
        D1["User, UserCharacter"]
        D2["Character, Skill, Element"]
        D3["BattleSession, BattleTurn"]
        D4["GachaBanner, GachaRate, GachaPity"]
        D5["Equipment, UserEquipment"]
        D6["Dungeon, Raid, DropTable"]
        D7["Guild, GuildMember, Friend"]
        D8["Ranking, RankingReward"]
    end

    subgraph INFRASTRUCTURE ["🗄️ 인프라 계층"]
        I1["JPA Repository"]
        I2["MySQL Database"]
        I3["WebSocket STOMP Broker"]
        I4["Spring Security"]
    end

    PRESENTATION --> APPLICATION
    APPLICATION --> DOMAIN
    APPLICATION --> INFRASTRUCTURE
    DOMAIN -.-> INFRASTRUCTURE
```

### 3-1. 각 계층의 역할

| 계층 | 역할 | 핵심 규칙 |
|------|------|-----------|
| **프레젠테이션** | HTTP 요청 수신, JSON 응답 반환 | 비즈니스 로직 금지 |
| **애플리케이션** | 비즈니스 로직, 트랜잭션 관리 | `@Service`, `@Transactional` |
| **도메인** | 엔티티, 값 객체, 비즈니스 규칙 | JPA Entity, Enum |
| **인프라** | DB 접근, 외부 시스템 연동 | `@Repository` |

---

## 4. 모듈(패키지) 구조

```
src/main/java/com/hanwol/
├── config/                      # 설정
│   ├── SecurityConfig.java      # Spring Security 설정
│   ├── WebSocketConfig.java     # WebSocket STOMP 설정
│   └── WebConfig.java           # CORS, 인터셉터 등
│
├── domain/                      # 도메인 (Entity/Enum/VO)
│   ├── user/
│   │   ├── User.java
│   │   └── UserRole.java
│   ├── character/
│   │   ├── Character.java
│   │   ├── UserCharacter.java
│   │   ├── Element.java         # FIRE, WATER, WIND, EARTH, LIGHTNING
│   │   ├── CharacterRole.java   # SWORDSMAN, TAOIST, HEALER, ASSASSIN, GUARDIAN
│   │   └── Grade.java           # C, B, A, S, U, L
│   ├── equipment/
│   │   ├── Equipment.java
│   │   └── UserEquipment.java
│   ├── battle/
│   │   ├── BattleSession.java
│   │   ├── BattleTurn.java
│   │   └── BattleStatus.java
│   ├── gacha/
│   │   ├── GachaBanner.java
│   │   ├── GachaRate.java
│   │   └── GachaPity.java
│   ├── dungeon/
│   │   ├── Dungeon.java
│   │   ├── RaidBoss.java
│   │   └── DropTable.java
│   ├── social/
│   │   ├── Guild.java
│   │   ├── GuildMember.java
│   │   └── Friend.java
│   └── ranking/
│       ├── Ranking.java
│       └── RankingReward.java
│
├── repository/                  # JPA Repository
│   ├── user/
│   ├── character/
│   ├── equipment/
│   ├── battle/
│   ├── gacha/
│   ├── dungeon/
│   ├── social/
│   └── ranking/
│
├── service/                     # 비즈니스 로직
│   ├── auth/
│   │   └── AuthService.java
│   ├── character/
│   │   └── CharacterService.java
│   ├── battle/
│   │   ├── BattleEngine.java        # 전투 핵심 엔진
│   │   ├── DamageCalculator.java    # 데미지 계산기
│   │   ├── TurnManager.java         # 행동 게이지/턴 관리
│   │   └── WeaknessProcessor.java   # 약점/격파 처리
│   ├── gacha/
│   │   ├── GachaService.java
│   │   └── GachaProbabilityEngine.java
│   ├── dungeon/
│   │   └── DungeonService.java
│   ├── equipment/
│   │   └── EquipmentService.java
│   ├── shop/
│   │   └── ShopService.java
│   ├── map/
│   │   └── MapService.java
│   ├── social/
│   │   ├── ChatService.java
│   │   └── GuildService.java
│   └── ranking/
│       └── RankingService.java
│
├── controller/                  # REST API
│   ├── api/                     # JSON 반환 (@RestController)
│   │   ├── AuthApiController.java
│   │   ├── CharacterApiController.java
│   │   ├── BattleApiController.java
│   │   ├── GachaApiController.java
│   │   ├── DungeonApiController.java
│   │   ├── EquipmentApiController.java
│   │   ├── ShopApiController.java
│   │   ├── MapApiController.java
│   │   ├── RankingApiController.java
│   │   └── SocialApiController.java
│   ├── page/                    # Thymeleaf 페이지 (@Controller)
│   │   └── PageController.java  # 초기 HTML 레이아웃만 반환
│   └── ws/                      # WebSocket 핸들러
│       ├── ChatWebSocketHandler.java
│       └── AlertWebSocketHandler.java
│
├── dto/                         # 요청/응답 DTO
│   ├── request/
│   └── response/
│
└── common/                      # 공통 유틸
    ├── exception/               # 전역 예외 처리
    │   ├── GlobalExceptionHandler.java
    │   └── GameException.java
    └── util/
        └── ValidationUtil.java
```

---

## 5. 캐릭터 위치 동기화 구조

```mermaid
sequenceDiagram
    participant C as Canvas (클라이언트)
    participant JS as JavaScript (이동 로직)
    participant API as MapApiController
    participant SVC as MapService
    participant DB as MySQL

    Note over C,DB: 캐릭터 이동 처리

    C->>JS: WASD 키 입력 감지
    JS->>JS: 클라이언트 측 즉시 캐릭터 이동<br/>(부드러운 UX)
    JS->>JS: 충돌 판정 (맵 경계, 건물)
    
    Note over JS,API: 1초 간격 Throttle
    JS->>API: POST /api/map/position<br/>{x: 150, y: 200, timestamp}
    
    API->>SVC: 위치 검증
    SVC->>SVC: 이전 위치 대비 거리/시간 계산
    
    alt 정상 이동
        SVC->>DB: 위치 업데이트 저장
        SVC->>API: 200 OK
        API->>JS: {valid: true}
    else 비정상 이동 (핵)
        SVC->>API: 400 Bad Request
        API->>JS: {valid: false, correctedPosition: {x, y}}
        JS->>C: 서버 위치로 강제 이동 (롤백)
    end

    Note over C,DB: 재접속 시
    JS->>API: GET /api/map/position
    API->>DB: 마지막 저장 위치 조회
    API->>JS: {x: 150, y: 200}
    JS->>C: 해당 위치에서 캐릭터 렌더링
```

---

## 6. 주요 통신 방식 정리

| 기능 | 통신 방식 | 프로토콜 | 비고 |
|------|-----------|----------|------|
| 페이지 초기 로드 | 동기 | HTTP GET | Thymeleaf 레이아웃 |
| 게임 데이터 조회 | 비동기 | Fetch API (REST) | JSON 응답 |
| 전투 행동 | 비동기 | Fetch API (REST) | 행동별 요청/응답 |
| 가챠 뽑기 | 비동기 | Fetch API (REST) | @Transactional |
| 캐릭터 위치 | 비동기 (Throttle) | Fetch API (REST) | 1초 간격 |
| 실시간 채팅 | 실시간 | WebSocket (STOMP) | 양방향 |
| S등급 가챠 알림 | 실시간 | WebSocket (STOMP) | 서버 → 클라이언트 |
| 시설 상호작용 | 비동기 | Fetch API (REST) | Space키 트리거 |

---

## 7. 기술 스택 최종 확정

### Backend
| 기술 | 버전 | 용도 |
|------|------|------|
| **Java** | 17+ | 언어 |
| **Spring Boot** | 3.x | 프레임워크 |
| **Spring Security** | 6.x | 인증/인가 |
| **Spring Data JPA** | 3.x | ORM |
| **Spring WebSocket** | 3.x | 실시간 채팅 (STOMP) |
| **MySQL** | 8.x | RDBMS |
| **Lombok** | 최신 | 보일러플레이트 제거 |
| **Gradle** | 8.x | 빌드 도구 |

### Frontend
| 기술 | 버전 | 용도 |
|------|------|------|
| **HTML5 Canvas** | - | 마을 맵/캐릭터 렌더링 |
| **Thymeleaf** | 3.x | 초기 페이지 레이아웃 |
| **Alpine.js** | 3.x | 클라이언트 상태 관리 |
| **Tailwind CSS** | 3.x | UI 스타일링 |
| **Lucide Icons** | 최신 | 아이콘 |
| **Chart.js** | 4.x | 스탯/전투 로그 시각화 |
| **SockJS + STOMP.js** | 최신 | WebSocket 클라이언트 |

---

## 8. 등급 시스템 최종 구조 (L등급 포함)

```mermaid
graph LR
    subgraph 가챠["🎰 가챠 획득"]
        C["C (일반)"]
        B["B (고급)"]  
        A["A (희귀)"]
        S["S (전설)"]
    end

    subgraph 파밍["⛏️ 파밍 승급"]
        U["U (초월)"]
    end

    subgraph 레전더리["👑 월간 레이드"]
        L["L (레전더리)<br/>계정당 1캐릭"]
    end

    S -->|"특수 재료 + 최대 레벨"| U
    U -->|"L등급 재료<br/>(월간 레이드 드랍)"| L
    L -->|"해제 (U로 복귀)"| U
    U -->|"다른 캐릭에<br/>L등급 재료 사용"| L

    style L fill:#00ff41,stroke:#333,color:#000,stroke-width:3px
    style U fill:#ff4444,stroke:#333,color:#fff
    style S fill:#ffd700,stroke:#333,color:#000
```

### L등급 핵심 규칙

| 규칙 | 설명 |
|------|------|
| 대상 | **태생 S등급** 캐릭터만 (U등급 상태에서 승급) |
| 계정 제한 | **계정당 1캐릭만** L등급 유지 가능 |
| 승급 재료 | 월간 레이드에서 **확률 드랍**되는 특수 재료 |
| 승급 경로 | S → U(파밍) → L(월간 레이드 재료) |
| 해제 | L → U로 강등 가능 (재료 미환급) |
| 전환 | L 해제 후 다른 U등급 캐릭터에 새 L등급 재료 사용 |
| 효과 | 모든 스탯 대폭 상승 + 전용 L등급 스킬 해금 |

---

## 9. 전투 엔진 모듈 내부 구조

```mermaid
graph TD
    subgraph BATTLE_ENGINE ["⚔️ 전투 엔진 모듈"]
        BE[BattleEngine<br/>전투 총괄]
        TM[TurnManager<br/>행동 게이지/턴 순서]
        DC[DamageCalculator<br/>데미지 계산]
        WP[WeaknessProcessor<br/>약점/격파 처리]
        SP[SkillProcessor<br/>스킬 효과 처리]
        AI[AutoBattleAI<br/>자동 전투 로직]
    end

    BE --> TM
    BE --> DC
    BE --> WP
    BE --> SP
    BE --> AI

    TM -->|"행동 가능 유닛"| BE
    DC -->|"최종 데미지"| BE
    WP -->|"Break 상태"| BE
    SP -->|"버프/디버프"| BE
    AI -->|"자동 행동 선택"| BE
```

---

## 10. 모듈 역할 및 소속 관계 요약

| 모듈 | 역할 | 주요 의존 |
|------|------|-----------|
| **인증 모듈** | 회원가입, 로그인, 세션 관리 | Spring Security |
| **캐릭터 모듈** | 캐릭터 CRUD, 레벨업, 등급 승급(S→U→L) | 장비 모듈 |
| **전투 엔진 모듈** | CTB 전투 전체 관리, 데미지 계산, 약점 처리 | 캐릭터 모듈 |
| **가챠 모듈** | 뽑기 처리, 확률 엔진, 천장 관리 | 캐릭터/장비 모듈, 알림 모듈 |
| **던전/레이드 모듈** | 입장 관리, 보상 처리, 횟수 제한 | 전투 엔진, 인벤토리 모듈 |
| **장비/강화 모듈** | 장비 CRUD, 강화, 전용무기 효과 | 캐릭터 모듈 |
| **상점/인벤토리 모듈** | 구매, 인벤토리/보관함 관리 | 재화 모듈 |
| **맵 모듈** | 위치 관리, 이동 검증, 시설 상호작용 | 인증 모듈 |
| **소셜 모듈** | 채팅, 길드(맹), 친구 | WebSocket |
| **랭킹 모듈** | 랭킹 집계, 보상 지급 | 전투/던전 모듈 |
| **알림 모듈** | 가챠 알림, 시스템 알림 | WebSocket |

---

> 📌 **다음 단계**: Phase 1 승인 후 → **Phase 2: DB 스키마 & API 상세 설계 (LLD)**로 진행
