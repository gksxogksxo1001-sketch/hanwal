# 🌙 한월 (韓月, Hanwol) - 무협 턴제 RPG 웹 게임

<div align="center">

![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Java](https://img.shields.io/badge/Java-17-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)
![TailwindCSS](https://img.shields.io/badge/Tailwind_CSS-38B2AC?style=for-the-badge&logo=tailwindcss&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Railway](https://img.shields.io/badge/Railway-0B0D0E?style=for-the-badge&logo=railway&logoColor=white)

**"강호의 거친 바람을 헤치고 나아가는 모험가를 위한 모던 무협 턴제 RPG 웹 게임"**

</div>

---

## 📜 프로젝트 소개 및 제작 계기 (Motivation)

> **"고전 턴제 RPG의 깊은 전략성과 무협의 뽕맛을 현대적인 웹 기술로 구현할 수 없을까?"**

**`한월(韓月)`** 프로젝트는 고전 무협 RPG의 매력적인 세계관과 전략적인 턴제 전투 시스템을 무거운 클라이언트 설치 없이 **웹 브라우저만으로 언제 어디서나 즐길 수 있도록** 기획 및 제작되었습니다.

1. **전략적 턴제 전투의 재미 복원**: 단순 자동 전투가 아닌 민첩성(Speed) 기반의 행동 게이지, 버프/디버프 상태이상, 연계 스킬 및 필살기 타이밍 계산 등 깊이 있는 전투 시스템을 구현했습니다.
2. **2.5D WASD 탐험의 감성**: 웹 브라우저 캔버스 위에서 플레이어가 자유롭게 WASD 키로 마을을 돌아다니며 NPC, 상점, 주점(가챠), 던전 포탈과 인터랙션할 수 있는 몰입감을 선사합니다.
3. **Headless API 아키텍처 기반의 확장성**: 백엔드는 모든 통신을 JSON API 기반으로 처리하고, 프론트엔드는 Thymeleaf 초기 렌더링 + Alpine.js 비동기 상태관리로 분리하여 추후 3D 클라이언트(Unity/Unreal)로 확장할 수 있는 견고한 아키텍처를 세웠습니다.

---

## 🌟 주요 핵심 기능 (Key Features)

### 🔑 1. 보안 인증 & 계정 관리
- **이메일 인증 시스템**: Google Apps Script API와 연동하여 가입 시 서신(인증번호) 발송.
- **개인정보 보호 & 마스킹**: 아이디 찾기 기능 시 사용자 이메일을 안전하게 마스킹(`ab***@gmail.com`) 처리하여 개인 정보 유출 방지.
- **Spring Security 세션 관리**: 안전한 로그인/회원가입 및 권한 제어.

### 📖 2. 프롤로그 & 튜토리얼
- 웹소설 감성의 프롤로그 스토리 연출 및 대화 시스템.
- 초보 모험가를 위한 WASD 조작법 안내 및 동굴 탈출 튜토리얼 전투 제공.

### 🏯 3. 마을 허브 (WASD 탐험)
- **자유로운 2D 타일 맵 이동**: Canvas API 기반 WASD 이동 및 미니맵 제공.
- **건물 & 포탈 인터랙션**: 주점(가챠 소환), 대장간(장비 관리), 상점, 문파, 던전 포탈 접근 시 동적 UI 모달 오픈.

### ⚔️ 4. 턴제 전략 전투 엔진
- **행동 게이지 (Speed 순서)**: 캐릭터의 속도 스탯에 따라 실시간 턴 순서가 계산되는 턴제 엔진.
- **다양한 스킬 & 상태이상**: 감전, 빙결, 출혈, 방어력 파쇄, 무적 보호막 등 다채로운 스킬 메커니즘.
- **필살기 & 전투 로그**: 기력(Spirit)을 소비하는 필살기 연출 및 실시간 전투 진행 로그 기록.

### 🔮 5. 가챠 (소환) & 파티 성장
- **원자적 재화 트랜잭션**: `@Transactional` 기반으로 재화 차감과 캐릭터 획득이 동시 처리되는 안전한 가챠 로직.
- **파티 편성 및 캐릭터 육성**: 최대 4인 파티 구성, 레벨업, 돌파, 경지 상승 시스템.

---

## 🛠️ 기술 스택 (Tech Stack)

### Backend
- **Framework**: Java 17, Spring Boot 3.2.4
- **Database / ORM**: MySQL 8.0, Spring Data JPA (Hibernate)
- **Security**: Spring Security, PasswordEncoder (BCrypt)
- **Real-time / Mail**: WebSocket (STOMP), RestTemplate (Google Script API)

### Frontend
- **Template Engine**: Thymeleaf
- **Styling**: Vanilla CSS, Tailwind CSS, Bento Grid Layout
- **State & Logic**: Alpine.js, Canvas API, Lucide Icons

### Infrastructure & DevOps
- **Container**: Docker (Eclipse Temurin 17 Multi-stage Build)
- **Deployment**: Railway Cloud

---

## 📁 프로젝트 구조 및 설계 문서

```text
hanwal-main/
├── doc/                        # 📚 체계적인 기획 & 아키텍처 설계 문서
│   ├── architecture/           # Phase 1~5 HLD, LLD, UI/UX, NFR, 모듈구조 설계서
│   ├── game-design/            # 게임 시스템 규칙, 스토리 개요, 밸런스 데이터 (등장인물.xlsx)
│   └── requirements/           # PRD 요구사항 명세서 9종 및 개요 문서
├── src/
│   ├── main/java/com/hanwol/
│   │   ├── config/             # Spring Security, App Config
│   │   ├── controller/         # REST API & Page Controllers (Auth, Battle, Map, Gacha 등)
│   │   ├── domain/             # JPA Entity & Repository (Character, Skill, User, Story 등)
│   │   ├── dto/                # 요청/응답 DTO 객체
│   │   └── service/            # 비즈니스 로직 (전투 엔진, 가챠, 인증, 성장 등)
│   └── main/resources/
│       ├── application.yml     # 설정 파일 (DB 및 환경변수)
│       ├── data.sql            # 마스터 데이터 (캐릭터, 스킬, 스테이지 초기화)
│       ├── static/             # static resources (css, js, images)
│       └── templates/          # Thymeleaf UI Templates (town, battle, tutorial 등)
├── Dockerfile                  # 멀티 스테이지 도커 빌드 파일
├── build.gradle                # Gradle 의존성 및 빌드 설정
└── railway.toml                # Railway 클라우드 배포 설정
```

---

## 🚀 로컬 실행 방법 (Getting Started)

### Prerequisites
- Java 17 이상
- MySQL 8.0 이상

### 1. 데이터베이스 세팅
```sql
CREATE DATABASE hanwol DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. 환경변수 설정 (`application.yml`)
기본적으로 local 프로필 설정이 되어 있으며, 이메일 인증 기능 사용 시 구글 스크립트 URL을 환경변수로 설정합니다:
```bash
export GOOGLE_SCRIPT_MAIL_URL="https://script.google.com/macros/s/YOUR_SCRIPT_ID/exec"
```

### 3. 애플리케이션 실행
```bash
./gradlew bootRun
```
브라우저에서 `http://localhost:8080` 접속!

---

## 🐳 Docker 실행 방법

```bash
# 도커 이미지 빌드
docker build -t hanwol-game .

# 도커 컨테이너 실행
docker run -d -p 8080:8080 -e SPRING_DATASOURCE_URL="jdbc:mysql://host.docker.internal:3306/hanwol" hanwol-game
```

---

## 🔒 보안 강화 및 최적화 사항

- **인증 정보 방어**: 아이디 찾기 기능 시 이메일 마스킹 처리(`findIdByNickname`)로 개인정보 노출 차단.
- **환경변수 분리**: 구글 메일 API URL 및 DB 패스워드 등 민감 정보를 코드 베이스에서 제거하고 외부 환경변수로 주입받도록 안전 조치.
- **원자적 가챠 시스템**: 가챠 소환 시 재화 소모와 획득 간의 데이터 부정합을 막기 위해 서버 차원의 `@Transactional` 원자성 검증 반영.
