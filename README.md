# 🖥️ Java Socket 기반 교실 예약 & 파일 동기화 시스템

> **외부 IP 연결 → 로그인/회원가입 → 규칙 동의 → 사용자/관리자 예약 관리 → 알림/통계**까지 한 번에 제공하는  
> **Java Swing + Socket 기반** 프로젝트입니다.  
> 파일 변경은 서버를 통해 **실시간 동기화**되며, 동시 접속은 **3명 제한 + 대기열(FIFO)** 로 관리됩니다.  

## 🌐 관련 프로젝트
- 💻 **Client (Roomify)** : 현재 저장소  
- 🖥️ **Server** : [myname-jin/SERVER](https://github.com/myname-jin/SERVER)

---

## 📑 발표 자료
➡️ [**발표PPT 다운로드**](https://raw.githubusercontent.com/myname-jin/roomify/develop/2%EB%B6%84%EB%B0%98%203%EC%A1%B0-%EB%B0%9C%ED%91%9C.pptx)

---

## ✨ 주요 기능 (Overview)

1) **인증 & 연결**
- 서버 IP/PORT 입력 후 연결
- 회원가입 및 로그인
- 사용자 유형(관리자/교수,학생)별 진입점 제공

2) **규칙 동의 & 관리**
- 로그인 직후 약관/규칙 동의 화면
- 관리자가 규칙 항목 등록·수정 가능

3) **예약 시스템 (사용자 + 관리자)**
- 사용자: 교실/시간 선택, 예약 생성/조회/취소, 알림
- 관리자: 예약 전체 현황 관리(검색/수정/삭제), <BR>예약 승인 및 거절기능,
          강의실 정보 수정기능,  예약 시각화, 공지사항 수정,
          일부 계정 예약 제한기능, 예약 취소관리(취소사유등)
- 달력/다이얼로그 기반 UI 제공

4) **파일 동기화 & 세션 관리**
- 파일 변경 감지 → 서버 반영 → 다른 클라이언트에 실시간 전파
- 동시 접속 3명 제한, 초과 시 대기열(FIFO) 관리
- 종료 시 세션 정리 후 대기자 자동 입장

<p align="center">
  <img src="src/main/resources/서버-클라이언트구조.jpg" width="600" alt="아키텍처 다이어그램">
</p>

---

## 📸 실행 화면

### 👤 사용자 기능 (User)

<table>
<tr><td align="center"><img src="src/main/resources/사용자예약.jpg" width="550" alt="사용자예약.jpg" height="400"/><br>사용자예약</td>
<td align="center"><img src="src/main/resources/사용자예약조회jpg.jpg" width="550" alt="사용자예약조회jpg" height="400"/><br>사용자 메인 화면</td></tr>
</table>

---
 > 👨‍💻 강의실 및 실습실 예약을 할 수 있고 신청 이후 관리자의 승인을 기다릴땐 대기, 승인완료되면 승인이라고 표시합니다.
---

### 🛠 관리자 기능 (Admin)

<table>
<tr><td align="center"><img src="src/main/resources/관리자메인.jpg" width="550" alt="관리자 메인" height="400"/><br>관리자메인</td>
<td align="center"><img src="src/main/resources/관리자예약취소관리.jpg" width="550" alt="관리자 예약취소 관리" height="400"/><br>관리자 예약취소 관리</td></tr>
<tr><td align="center"><img src="src/main/resources/관리자강의실예약제한.jpg" width="550" alt="관리자 강의실 예약제한" height="300"/><br>관리자 강의실 예약제한</td>
<td align="center"><img src="src/main/resources/관리자예약시각화.jpg" width="550" alt="관리자 예약 시각화" height="500"/><br>관리자 예약 시각화</td></tr>
</table>

---

 > 👨‍💻 예약이 있을경우 예약을 승인,취소 할 수 있고 예약자가 직접 취소한 경우 취소사유를 볼 수 있습니다. 또한 공휴일이나 갑작스럽게 예약을 막아야 하는경우 특정 날,시간대로 예약을 막을 수 있습니다. 마지막으로 예약 요일, 실습실 호수별로 시각화 기능이 있습니다.



---

### 📂 프로젝트 관리 시각화

<p align="center">
  <img src="src/main/resources/머지시각화.jpg" width="650" alt="머지 시각화">
</p>

- ** Sourcetree를 이용하여 팀원들의 push,pull,merge등을 시각화 하였습니다
---

### 🎬 대기열 (GIF)
<p align="center">
  <img src="src/main/resources/대기열.gif" width="650" alt="대기열">
</p>

- ** 4번째 사용자 접속 후 대기열과 기존 접속중인 사용자 로그아웃시 자동로그인 GIF입니다.


---
## 🧱 모듈 구조 (주요 클래스 맵)

### 1) 인증 & 연결
- `Main` (프로그램 시작)
- `ConnectView` (서버 IP/포트 입력 화면)
- `LoginController`, `LoginModel`, `LoginService`, `LoginView`
- `SignupController`, `SignupModel`, `SignupView`

### 2) 규칙(약관) 동의/관리 (MVC)
- `RuleAgreementController`, `RuleAgreementModel`, `RuleAgreementView`
- `RuleManagementController`, `RuleManagementModel`, `RuleManagementView`

### 3) 예약 시스템 (MVC)
- 공용/UI
  - `UserMainController`, `UserMainModel`, `UserMainView` (사용자 대시보드)
  - `ReservationController`, `ReservationModel` (핵심 로직)
  - `ReservationGUIController`, `ReservationView`, `ConsoleView`
  - `MainView`, `DetailView` (예약 목록/상세)
  - 달력 & 선택 다이얼로그:  
    `CalendarController`, `CalendarView`, `DialogController`,  
    `RoomTypeDialogView`, `RoomNumberDialogView`, `TimeSlotDialogView`, `BlockTypeDialogView`
- 사용자 기능
  - `UserReservationListController`, `UserReservationModel`, `UserReservationListView`
  - `UserReservationCancelController`, `UserReservationCancelModel`, `UserReservationCancelView`
  - `UserNoticeController`, `UserNoticeModel`, `UserNoticeView` (알림)
  - `UserStatsController`, `UserStatsModel`, `UserStatsView` (통계)
  - `CheckinDialog` (체크인/확인 다이얼로그)
- 관리자 기능
  - `ReservationMgmtController`, `ReservationMgmtModel`, `ReservationMgmtDataModel`, `ReservationMgmtView`
  - 교실 관리: `ClassroomController`, `ClassroomModel`, `ClassroomView`, `RoomModel`
  - (옵션) 엑셀 로드: `ExcelLoader` ※ `.xlsx` 사용 시 Apache POI 필요 가능

### 4) 동기화 & 세션
- `FileSyncClient`, `FileWatcher`, `SocketManager`, `LogoutUtil`
- 알림 UI 버튼: `NotificationButton`
- 알림 MVC: `NotificationController`, `NotificationModel`, `NotificationView`

---

## 🛠 기술 스택

- **Language/UI**: Java 8+ / **Swing** (일부 `.form` → NetBeans GUI Builder 기반)
- **Network**: **TCP Socket** (클라이언트–서버), ACK 기반 동기화
- **Pattern**: **MVC 분리** (Controller ↔ Model ↔ View)
  
[![Java](https://img.shields.io/badge/Java-8%2B-orange?logo=java)]()
[![Swing](https://img.shields.io/badge/UI-Java%20Swing-blue?logo=java)]()
[![Sockets](https://img.shields.io/badge/Network-TCP%20Sockets-lightgrey?logo=socket.io)]()
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](#-license)

```text
핵심 설계 포인트
- TCP 소켓 통신
- 세션 제한(동시 3명) + FIFO 대기열 + 자동 입장
- 파일 동기화(서버 반영 + ACK 전파)
