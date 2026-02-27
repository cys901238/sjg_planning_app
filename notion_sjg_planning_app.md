# SJG 생산계획 앱 개발 (Notion 초안)

> ⚠️ 참고: OpenClaw Notion integration이 아직 "SAP 길드 매칭 플랫폼 — 초안 (Top)" 페이지에 초대되지 않아 API로 직접 복제할 수 없었습니다 (Notion 404: object_not_found). 아래 내용은 새 페이지 작성을 위해 준비한 Markdown 초안입니다. Notion에서 새 페이지를 만들고 이 내용을 붙여넣으면 바로 사용할 수 있습니다. 통합을 초대해 주시면 API로 다시 자동화할 수 있습니다.

## 프로젝트 개요
- **프로젝트명**: SJG 생산계획 앱 개발 (Android + Node.js + MariaDB)
- **레포지토리**: <https://github.com/cys901238/sjg_planning_app>
- **클라이언트 패키지**: `com.example.sjgplanning`
- **백엔드 기본 DB**: `sjg_planning_app`

## 현재 상태
1. **리포지토리 복제/리네임**
   - `sap_guild` → `sjg_planning_app` 폴더 및 GitHub repo 생성
   - node_modules, 로그, build 산출물 제거 및 .gitignore 정비
2. **Android**
   - 앱 라벨: “SJG 생산계획”
   - 레이아웃/문구: "SJG 생산계획 - 사용자 등록"
   - 새 패키지 경로 `com/example/sjgplanning`
3. **Backend**
   - 기본 DB 이름: `sjg_planning_app`
   - `.env` 예시 업데이트, 민감파일 비커밋
4. **빌드 산출물 전달**
   - APK 빌드 확인 (`./gradlew :app:assembleDebug`)
   - Windows 경로 예시: `D:\somi_work\sjg_planning_app\sjg_planning_app.apk`
5. **문서/스크립트**
   - README, run_apk.bat, local_build_help.sh, notion_app_dev_setup.md 등 SJG 명칭 반영

## 다음 단계 제안
1. **Notion Integration 초대**
   - OpenClaw integration을 새 페이지(또는 상위 페이지)에 초대 → API 복제 재시도
2. **CI 파이프라인 재점검**
   - 새 repo에 맞게 워크플로우 배지/시크릿 확인
3. **데이터베이스 분리**
   - 실제 MariaDB에서도 `sjg_planning_app` 전용 schema 운영 여부 확정
4. **기능 개발**
   - 플로우(저장/로그인) 강화, 생산계획 시나리오 입력/조회 기능 추가

---
이 파일을 Notion에 붙여 넣으면 “SJG 생산계획 앱 개발” 페이지 기본 초안으로 사용할 수 있습니다. Integration 초대가 완료되면 다시 요청해 주세요!
