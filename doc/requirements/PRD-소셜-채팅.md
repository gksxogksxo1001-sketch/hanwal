# PRD-08: 소셜 & 채팅 시스템

> **프로젝트**: 한월(韓月)  
> **작성일**: 2026-05-06  
> **우선순위**: 🔴 필수 (게임의 핵심 차별점)

---

## 1. 개요

한월(韓月)의 핵심 차별점인 **소셜 & 채팅 시스템**을 정의한다. 전체 채팅(로비)과 파티/길드(맹) 채팅이 명확히 분리되며, WebSocket 기반 실시간 통신으로 구현한다.

---

## 2. 채팅 시스템

### 2-1. 채팅 채널 종류

| 채널 | 아이콘 | 설명 | 접근 조건 |
|------|:------:|------|-----------|
| **전체 채팅** | 🌐 | 접속 중인 모든 유저가 참여하는 로비 채팅 | 마을 접속 시 자동 연결 |
| **맹 채팅** (길드) | ⚔️ | 소속 맹(길드) 원들만 참여하는 채팅 | 맹 가입 시 |
| **파티 채팅** | 👥 | 현재 편성된 파티원과의 채팅 | 파티 구성 시 (향후 멀티 확장 대비) |
| **귓속말** | 💬 | 1:1 개인 메시지 | 닉네임 지정 |

### 2-2. 채팅 UI 레이아웃

```
┌──────────────────────────────────┐
│ 채팅 패널                        │
│ ┌────┬────┬────┬────┐          │
│ │전체│ 맹 │파티│귓말│ ← 탭 전환  │
│ └────┴────┴────┴────┘          │
│ ┌──────────────────────────────┐│
│ │ [남궁검] 다들 오늘 요일던전 뭐함?│  │
│ │ [당소소] 뇌전 재료 파밍 ㄱ     ││
│ │ [모용비] 레이드 같이 할 사람?   ││
│ │ [시스템] 나한테 귓속말 왔어      ││
│ │ ...                          ││
│ └──────────────────────────────┘│
│ ┌──────────────────┐ ┌──────┐ │
│ │ 메시지 입력...     │ │전송  │ │
│ └──────────────────┘ └──────┘ │
└──────────────────────────────────┘
```

### 2-3. 채팅 기능 상세

| 기능 | 설명 |
|------|------|
| 메시지 전송 | 텍스트 메시지 전송 (최대 200자) |
| 닉네임 클릭 | 유저 프로필 보기 / 귓속말 / 친구 추가 팝업 |
| 시스템 메시지 | 입장/퇴장, 공지사항, S등급 가챠 알림 등 |
| 도배 방지 | 같은 유저가 1초 이내 연속 전송 시 차단 |
| 욕설 필터 | 서버에서 금칙어 목록 기반 필터링 |
| 채팅 이력 | 최근 100건까지 스크롤 가능 (이전 이력은 미제공) |

### 2-4. 가챠 알림 (선택)

- S등급 캐릭터/무기 획득 시 전체 채팅에 자동 알림
- 예: `🎉 [남궁검]님이 [S] 제갈명을(를) 획득했습니다!`
- 알림 수신 ON/OFF 설정 가능
- 소셜 분위기 활성화 + 가챠 동기 부여 효과


---

## 3. 맹 (길드) 시스템

### 3-1. 맹 기본 정보

| 항목 | 설명 |
|------|------|
| 맹 이름 | 2~16자, 중복 불가 |
| 맹 소개 | 최대 200자 소개글 |
| 최대 인원 | 30명 (향후 확장 가능) |
| 맹 레벨 | 맹원 활동에 따라 경험치 축적, 레벨업 시 혜택 |
| 맹 해산 | 맹주만 가능, 맹원 0명일 때 또는 전원 동의 시 |

### 3-2. 맹 직책

| 직책 | 인원 | 권한 |
|------|:----:|------|
| **맹주** (盟主) | 1명 | 모든 권한 (직책 변경, 가입 승인, 해산 등) |
| **부맹주** | 최대 2명 | 가입 승인/거절, 맹원 추방, 공지 작성 |
| **장로** | 최대 5명 | 가입 승인/거절 |
| **맹원** | 나머지 | 채팅, 맹 컨텐츠 참여 |

### 3-3. 맹 기능

| 기능 | 설명 | 우선순위 |
|------|------|:--------:|
| 맹 생성 | 골드 10,000 소모, 이름/소개 설정 | 🔴 필수 |
| 맹 가입 | 가입 신청 → 맹주/부맹주 승인 | 🔴 필수 |
| 맹 탈퇴 | 즉시 탈퇴, 24시간 재가입 쿨다운 | 🔴 필수 |
| 맹 채팅 | 실시간 맹원 전용 채팅 | 🔴 필수 |
| 맹 공지 | 맹주/부맹주가 작성하는 공지사항 | 🟡 권장 |
| 맹 기여도 | 맹원 활동(던전/레이드)에 따른 기여도 집계 | 🟡 권장 |

---

## 4. 친구 시스템

### 4-1. 기본 기능

| 기능 | 설명 |
|------|------|
| 친구 추가 | 닉네임 검색 또는 채팅에서 클릭 → 친구 신청 |
| 친구 수락/거절 | 알림을 통해 수락 또는 거절 |
| 친구 목록 | 온라인/오프라인 상태 표시 |
| 친구 삭제 | 즉시 삭제, 상대에게 알림 없음 |
| 최대 친구 수 | 50명 |

### 4-2. 친구 혜택 (향후 확장)

- 친구에게 일일 격려(하트) 전송 → 스태미나 소량 획득
- 친구 파티 구경 (프로필 조회)
- 친구와 함께 던전 도전 (멀티플레이 확장 시)

---

## 5. WebSocket 통신 구조

### 5-1. 연결 관리

```
[클라이언트]                           [서버 (Spring Boot + WebSocket)]
    │                                      │
    │  1. 로그인 후 마을 접속                │
    │  ──── WebSocket CONNECT ──────▶     │
    │                                      │
    │  2. 채널 구독                         │
    │  ──── SUBSCRIBE /topic/chat/global ─▶│
    │  ──── SUBSCRIBE /topic/chat/guild/1 ▶│
    │                                      │
    │  3. 메시지 전송                       │
    │  ──── SEND /app/chat/send ──────▶   │
    │                                      │
    │  4. 메시지 수신 (브로드캐스트)          │
    │  ◀──── MESSAGE /topic/chat/global ── │
    │                                      │
    │  5. 로그아웃/이탈 시                   │
    │  ──── WebSocket DISCONNECT ──────▶  │
```

### 5-2. 메시지 JSON 스키마

#### 전송 (Client → Server)
```json
{
    "channelType": "GLOBAL",
    "channelId": null,
    "content": "다들 레이드 ㄱ?",
    "messageType": "CHAT"
}
```

#### 수신 (Server → Client)
```json
{
    "id": "msg-uuid-xxxx",
    "channelType": "GLOBAL",
    "sender": {
        "userId": 42,
        "nickname": "남궁검",
        "level": 35,
        "title": "무한탑 정복자"
    },
    "content": "다들 레이드 ㄱ?",
    "messageType": "CHAT",
    "timestamp": "2026-05-06T10:30:00"
}
```

---

## 6. API 엔드포인트 (예상)

### 채팅

| Method | Endpoint | 설명 |
|--------|----------|------|
| WS | `/ws/chat` | WebSocket 연결 엔드포인트 |
| WS | `/topic/chat/global` | 전체 채팅 구독 |
| WS | `/topic/chat/guild/{guildId}` | 맹 채팅 구독 |
| WS | `/topic/chat/whisper/{userId}` | 귓속말 구독 |
| WS | `/app/chat/send` | 메시지 전송 |

### 맹 (길드)

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/guild/create` | 맹 생성 |
| GET | `/api/guild/{id}` | 맹 정보 조회 |
| POST | `/api/guild/{id}/join` | 맹 가입 신청 |
| POST | `/api/guild/{id}/approve` | 가입 승인 |
| POST | `/api/guild/{id}/reject` | 가입 거절 |
| POST | `/api/guild/{id}/leave` | 맹 탈퇴 |
| POST | `/api/guild/{id}/kick` | 맹원 추방 |
| GET | `/api/guild/{id}/members` | 맹원 목록 |

### 친구

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/friends/request` | 친구 신청 |
| POST | `/api/friends/accept` | 친구 수락 |
| POST | `/api/friends/reject` | 친구 거절 |
| DELETE | `/api/friends/{id}` | 친구 삭제 |
| GET | `/api/friends` | 친구 목록 (온라인 상태 포함) |

---

## 7. 데이터베이스 테이블 (예상)

### guilds (맹)
```sql
CREATE TABLE guilds (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(16) UNIQUE NOT NULL,
    description VARCHAR(200),
    leader_id BIGINT NOT NULL,
    level INT DEFAULT 1,
    exp BIGINT DEFAULT 0,
    max_members INT DEFAULT 30,
    member_count INT DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (leader_id) REFERENCES users(id)
);
```

### guild_members (맹원)
```sql
CREATE TABLE guild_members (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    guild_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    role ENUM('LEADER','VICE_LEADER','ELDER','MEMBER') DEFAULT 'MEMBER',
    contribution BIGINT DEFAULT 0,
    joined_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (guild_id) REFERENCES guilds(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    UNIQUE KEY (user_id)  -- 1인 1맹
);
```

### friends (친구)
```sql
CREATE TABLE friends (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    friend_id BIGINT NOT NULL,
    status ENUM('PENDING','ACCEPTED') DEFAULT 'PENDING',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (friend_id) REFERENCES users(id),
    UNIQUE KEY (user_id, friend_id)
);
```
