# PRD-01: 계정 시스템 & 튜토리얼

> **프로젝트**: 한월(韓月)  
> **작성일**: 2026-05-06  
> **우선순위**: 🔴 최우선 (게임 진입의 첫 관문)

---

## 1. 개요

사용자가 한월(韓月)에 최초 접속하여 계정을 생성하고, 튜토리얼을 통해 게임의 기본 시스템을 학습한 뒤 마을(무림맹 본산)에 도착하기까지의 전체 흐름을 정의한다.

---

## 2. 계정 시스템

### 2-1. 회원가입

| 항목 | 상세 |
|------|------|
| **기능** | 이메일 + 비밀번호 기반 회원가입 |
| **필수 입력** | 이메일, 비밀번호, 비밀번호 확인, 닉네임 |
| **닉네임 규칙** | 2~12자, 한글/영문/숫자 조합, 중복 불가 |
| **비밀번호 규칙** | 8자 이상, 영문+숫자+특수문자 조합 |
| **중복 체크** | 이메일, 닉네임 실시간 중복 검사 (비동기) |
| **약관 동의** | 서비스 이용약관, 개인정보 처리방침 동의 필수 |

### 2-2. 로그인

| 항목 | 상세 |
|------|------|
| **기능** | 이메일 + 비밀번호 로그인 |
| **세션 관리** | Spring Security 기반, 서버 세션 + Remember Me(선택) |
| **로그인 실패** | 5회 연속 실패 시 일시 잠금 (5분) |
| **자동 로그인** | Remember Me 체크 시 30일간 자동 로그인 유지 |

### 2-3. 유저 프로필

| 데이터 | 설명 |
|--------|------|
| 닉네임 | 게임 내 표시 이름 |
| 레벨 | 계정 레벨 (경험치 기반) |
| 칭호 | 업적/랭킹 기반 획득 칭호 |
| 보유 재화 | 골드, 프리미엄 재화(영석), 스태미나 |
| 전투력 | 보유 캐릭터 + 장비 기반 종합 전투력 |
| 가입일 | 계정 생성 일시 |

---

## 3. 튜토리얼 시스템

### 3-1. 전체 흐름

```
[STEP 1] 성별 선택
    → 남성(남궁세가 막내아들) / 여성(남궁세가 막내딸) 선택
    → 선택에 따라 캐릭터 외형, 일부 대사 변경
    ↓
[STEP 2] 프롤로그 컷신
    → 남궁세가의 평화로운 일상
    → 천마 출현, 마기 확산
    → 무림맹 토벌대 결성 (최고수 8인)
    → 남궁 가주 & 전대가주 출전
    → 토벌대 전멸 소식
    → 주인공 수련하기 시작("더 이상 게으를 수 없다")
    → 남궁세가까지 처들어온 적들을 막기 위해 호위무사와 함께 싸우지만 역부족
    → 호위무사의 희생으로 하인과 함께 간신히 탈출
    → 탈출하는 과정에서 절벽에서 떨어짐
    → 정신을 차리고 일어나서 기연을 만나고 기본 스킬을 얻음
    ↓
[STEP 3] 튜토리얼 전투
    → 뒤늦게 쫒아오던 천마신교의 잡졸들과 전투
    → 일반 공격, 스킬, 필살기 순서대로 학습
    → 행동 게이지, 약점 시스템 체험
    → 전투 승리 시 기본 스킬 1개 습득
    ↓
[STEP 4] 초보자 가챠
    → 무료 10연차 1회 제공 (확정 A등급 이상 캐릭터 1명 포함)
    → 가챠 결과 연출 (카드 뒤집기)
    → 획득 캐릭터를 파티에 편성하는 방법 안내
    ↓
[STEP 5] 마을 도착
    → 무림맹 본산(마을 허브) 진입
    → NPC가 주요 시설(상점, 강화소, 포탈 등) 위치 안내
    → 자유 행동 시작
```

### 3-2. 튜토리얼 전투 상세

| 단계 | 교육 내용 | 강제/선택 |
|------|-----------|:---------:|
| 1턴 | "일반 공격을 해보세요" → 일반 공격 실행, SP 회복 확인 | 강제 |
| 2턴 | "스킬을 사용해보세요" → SP 소모하여 전투 스킬 실행 | 강제 |
| 3턴 | "적의 약점을 노려보세요" → 약점 속성 공격, 강인도 감소 확인 | 강제 |
| 4턴 | "필살기가 준비되었습니다!" → 필살기 발동 체험 | 강제 |
| 5턴~ | 자유 행동 → 남은 적 처치 | 선택 |

### 3-3. 튜토리얼 상태 저장

- 튜토리얼 진행 단계는 **서버 DB에 저장**
- 각 STEP 완료 시 `tutorial_step` 필드 업데이트
- 중간에 이탈해도 재접속 시 마지막 완료 STEP 다음부터 재개
- 튜토리얼 완료 플래그: `is_tutorial_completed = true`

---

## 4. 예외 상황 처리

| 상황 | 처리 방안 |
|------|-----------|
| 회원가입 중 브라우저 닫힘 | 가입 완료 전이므로 데이터 미생성, 재가입 |
| 튜토리얼 전투 중 브라우저 닫힘 | 서버에 전투 상태 저장, 재접속 시 전투 이어서 진행 |
| 초보자 가챠 중 네트워크 끊김 | @Transactional로 가챠 결과 원자적 저장, 미완료 시 재접속 시 가챠 재실행 |
| 닉네임 중복 실시간 체크 실패 | 서버 최종 검증에서 중복이면 에러 반환 |
| 가입 후 즉시 로그인 | 가입 완료 시 자동 로그인 처리 |

---

## 5. API 엔드포인트 (예상)

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/auth/register` | 회원가입 |
| POST | `/api/auth/login` | 로그인 |
| POST | `/api/auth/logout` | 로그아웃 |
| GET | `/api/auth/check-email` | 이메일 중복 체크 |
| GET | `/api/auth/check-nickname` | 닉네임 중복 체크 |
| GET | `/api/user/profile` | 유저 프로필 조회 |
| POST | `/api/tutorial/select-gender` | 성별 선택 |
| GET | `/api/tutorial/status` | 튜토리얼 진행 상태 조회 |
| POST | `/api/tutorial/complete-step` | 튜토리얼 단계 완료 처리 |
| POST | `/api/tutorial/beginner-gacha` | 초보자 가챠 실행 |

---

## 6. 데이터베이스 테이블 (예상)

### users
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(12) UNIQUE NOT NULL,
    gender ENUM('MALE', 'FEMALE') NOT NULL,
    level INT DEFAULT 1,
    exp BIGINT DEFAULT 0,
    gold BIGINT DEFAULT 0,
    premium_currency BIGINT DEFAULT 0,  -- 영석
    stamina INT DEFAULT 120,
    combat_power BIGINT DEFAULT 0,
    title VARCHAR(100) DEFAULT NULL,
    tutorial_step INT DEFAULT 0,
    is_tutorial_completed BOOLEAN DEFAULT FALSE,
    login_fail_count INT DEFAULT 0,
    locked_until DATETIME DEFAULT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```
