# sjg_planning_app

SJG 생산계획 앱 (Android + Node.js + MariaDB) 프로젝트입니다. 기존 sap_guild 앱을 기반으로 복제한 뒤, SJG 명칭과 워크플로우에 맞춰 리네임했습니다.

## 구성
- `app/`: Android (Kotlin) 클라이언트 — 기본 패키지 `com.example.sjgplanning`
- `backend/`: Express + mysql2 백엔드 (기본 DB: `sjg_planning_app`)
- `.github/workflows/`: GitHub Actions 워크플로우 (Android 빌드/스모크 테스트)
- `run_apk.bat`, `local_build_help.sh`: 로컬/Windows 빌드 & 설치 스크립트

## Android 빌드
1. `local.properties`에 SDK 경로 지정
2. 디버그 APK 빌드
   ```bash
   ./gradlew assembleDebug
   ```
3. 산출물: `app/build/outputs/apk/debug/app-debug.apk`

### Windows에서 APK 설치
```
D:\Android_dev\Sdk\platform-tools\adb install -r D:\somi_work\sjg_planning_app\sjg_planning_app.apk
D:\Android_dev\Sdk\platform-tools\adb reverse tcp:3000 tcp:3000
```

## 백엔드 (개발)
- 위치: `backend/`
- 환경 변수: `.env` (미포함). 예시는 `.env.example` 참고
- 실행 예시
  ```bash
  cd backend
  npm install
  node app.js
  ```

## 참고
- Notion: "SJG 생산계획 앱 개발" 페이지에서 환경/진행 상황 정리 예정
- 민감 정보(.env, 로그, node_modules)는 Git에 포함되지 않습니다.
