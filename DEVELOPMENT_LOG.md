# Kotlin Multiplatform Sample - 개발 로그

> 마지막 업데이트: 2026-03-08  
> AI 어시스턴트(GitHub Copilot)와의 대화를 통해 진행한 개발 내용을 기록합니다.

---

## 목차

1. [프로젝트 개요](#1-프로젝트-개요)
2. [최종 프로젝트 구조](#2-최종-프로젝트-구조)
3. [주요 기술 결정 및 Q&A](#3-주요-기술-결정-및-qa)
4. [기능 구현 히스토리](#4-기능-구현-히스토리)
5. [아키텍처 설명](#5-아키텍처-설명)
6. [DI (Koin) 구성](#6-di-koin-구성)
7. [Offline-First 캐시 전략](#7-offline-first-캐시-전략)
8. [테스트 전략](#8-테스트-전략)
9. [빌드 및 실행 방법](#9-빌드-및-실행-방법)
10. [향후 개선 방향](#10-향후-개선-방향)

---

## 1. 프로젝트 개요

**Kotlin Multiplatform (KMP)** 프로젝트로, 하나의 코드베이스로 **Android**, **JVM Desktop** 두 플랫폼을 지원합니다.

| 항목 | 내용 |
|---|---|
| 언어 | Kotlin (Multiplatform) |
| UI 프레임워크 | Compose Multiplatform |
| 네트워크 | Retrofit + OkHttp |
| 로컬 DB | Room (Android + JVM Desktop) |
| DI | Koin |
| 아키텍처 | Clean Architecture (Domain / Data / Presentation) |
| 타겟 플랫폼 | Android, JVM Desktop |

### 사용 외부 API
- **REST Countries API** (`https://restcountries.com/v3.1/`) — 전 세계 국가 정보 제공

---

## 2. 최종 프로젝트 구조

```
kotlinmultisample/
├── shared/                          ← 플랫폼 공통 비즈니스 로직
│   └── src/
│       ├── commonMain/
│       │   └── kotlin/.../shared/
│       │       ├── data/
│       │       │   ├── local/datasource/
│       │       │   │   └── LocalCountryDataSource.kt       ← 로컬 캐시 인터페이스
│       │       │   ├── remote/
│       │       │   │   ├── datasource/
│       │       │   │   │   └── RemoteCountryDataSource.kt  ← 원격 데이터소스 인터페이스
│       │       │   │   └── dto/
│       │       │   │       └── CountryDto.kt               ← API 응답 DTO
│       │       │   └── repository/
│       │       │       └── CountryRepositoryImpl.kt        ← Offline-First 구현체
│       │       ├── di/
│       │       │   ├── Koin.kt                             ← Koin 초기화
│       │       │   └── NetworkModule.kt                    ← expect 네트워크 모듈
│       │       ├── domain/
│       │       │   ├── model/
│       │       │   │   └── Country.kt                      ← 도메인 모델
│       │       │   └── repository/
│       │       │       └── CountryRepository.kt            ← Repository 인터페이스
│       │       └── util/
│       │           └── Logger.kt                           ← expect/actual 로거
│       ├── androidMain/
│       │   └── kotlin/.../shared/
│       │       ├── data/remote/api/CountryApiService.kt    ← Retrofit 인터페이스 (Android)
│       │       ├── data/remote/datasource/RemoteCountryDataSourceImpl.kt
│       │       ├── di/NetworkModule.kt                     ← actual (Android)
│       │       └── util/Logger.kt                          ← actual (Android)
│       ├── jvmMain/
│       │   └── kotlin/.../shared/
│       │       ├── data/remote/api/CountryApiService.kt    ← Retrofit 인터페이스 (JVM)
│       │       ├── data/remote/datasource/RemoteCountryDataSourceImpl.kt
│       │       ├── di/NetworkModule.kt                     ← actual (JVM)
│       │       ├── di/NetworkUtils.kt                      ← OkHttp/Retrofit 빌더
│       │       └── util/Logger.kt                          ← actual (JVM)
│       └── commonTest/
│           └── CountryRepositoryTest.kt                    ← 단위 테스트 (Fake 기반)
│
├── composeApp/                      ← UI + 플랫폼별 DI / DB
│   └── src/
│       ├── commonMain/
│       │   └── kotlin/.../
│       │       ├── App.kt                                  ← 앱 진입점, 네비게이션
│       │       ├── CustomNavigation.kt                     ← 커스텀 네비게이션 바
│       │       ├── app/presentation/country/
│       │       │   ├── CountryViewModel.kt                 ← ViewModel (Offline-First)
│       │       │   └── CountryUiState.kt                   ← UI 상태
│       │       ├── app/ui/screen/
│       │       │   ├── CountryListScreen.kt                ← 국가 목록 화면
│       │       │   └── CountryDetailScreen.kt              ← 국가 상세 화면
│       │       └── di/
│       │           └── ViewModelModule.kt                  ← ViewModel Koin 모듈
│       ├── androidMain/
│       │   └── kotlin/.../
│       │       ├── MainActivity.kt
│       │       ├── MainApplication.kt                      ← Koin 초기화
│       │       ├── database/
│       │       │   ├── AppDatabase.kt                      ← Room DB (Android)
│       │       │   ├── CountryEntity.kt                    ← Room Entity + 변환 함수
│       │       │   └── CountryDao.kt                       ← Room DAO
│       │       ├── di/AndroidModule.kt                     ← Android DI 모듈
│       │       └── shared/data/local/datasource/
│       │           └── LocalCountryDataSourceImpl.kt       ← Room 구현체 (Android)
│       └── jvmMain/
│           └── kotlin/.../
│               ├── Main.kt                                 ← JVM 진입점, Koin 초기화
│               ├── database/
│               │   ├── AppDatabase.kt                      ← Room DB (JVM)
│               │   ├── CountryEntity.kt                    ← Room Entity + 변환 함수
│               │   └── CountryDao.kt                       ← Room DAO
│               ├── di/JvmModule.kt                         ← JVM DI 모듈
│               └── shared/data/local/datasource/
│                   └── LocalCountryDataSourceImpl.kt       ← Room 구현체 (JVM)
│
└── server/                          ← Spring Boot 백엔드 (샘플)
    └── src/main/kotlin/.../server/
        ├── Application.kt
        └── controller/SampleController.kt
```

---

## 3. 주요 기술 결정 및 Q&A

### Q: DI는 shared에 있는 게 맞을까? composeApp에 있는 게 맞을까?
**A:** 역할에 따라 분리합니다.
- **`shared/di/`** — 플랫폼 중립적인 공통 모듈 (`commonModule`, `networkModule` expect)
- **`composeApp/di/`** — UI/플랫폼 종속 모듈 (`ViewModelModule`, `AndroidModule`, `JvmModule`)

---

### Q: Retrofit은 Android에서도 사용하지 않나? jvmMain에 넣은 이유는?
**A:** Retrofit은 JVM 기반이므로 `androidMain`과 `jvmMain` 모두에 넣습니다.  
단, `commonMain`에는 넣을 수 없습니다 (JS 타겟이 Retrofit을 지원하지 않기 때문).  
각 플랫폼에서 동일한 인터페이스(`CountryApiService`)를 독립적으로 선언합니다.

---

### Q: `actual` / `expect` 를 사용하는 방법은?
**A:** 플랫폼마다 구현이 달라야 할 때 사용합니다.

```kotlin
// commonMain - 선언만
expect val networkModule: Module

// androidMain - Android 구현
actual val networkModule = module { ... }

// jvmMain - JVM 구현
actual val networkModule = module { ... }
```

`NetworkModule`, `Logger` 등에 적용되어 있습니다.

---

### Q: `datasource`, `datasourceImpl`, `repository`, `repositoryImpl` 이렇게 impl을 하는 이유는?
**A:** **의존성 역전 원칙(DIP)** 적용을 위해서입니다.
- 인터페이스: 도메인 계층이 소유 → 플랫폼/기술 변화에 독립
- 구현체(Impl): 데이터 계층에 위치 → 언제든 교체 가능 (Room → SQLite, Retrofit → Ktor 등)
- 테스트 시 구현체 대신 Fake/Mock 주입 가능

단, 프로젝트 규모가 작거나 교체 가능성이 없다면 impl 분리를 생략할 수도 있습니다.

---

### Q: 요청은 VO고 응답은 DTO 아닌가요?
**A:** 엄밀히는 맞지만, KMP 실무에서는 둘 다 DTO로 통칭하는 경우가 많습니다.
- **요청 VO** (Value Object) — 생성/변경 불가 불변 객체
- **응답 DTO** (Data Transfer Object) — 네트워크 전송 목적 데이터 구조
- 이 프로젝트에서는 응답 매핑 중심이므로 `CountryDto`로 통일

---

### Q: jvm과 안드로이드의 공통된 부분을 소스셋 정의 없이 공통으로 사용하는 방법은?
**A:** `actual` / `expect` + 공통 함수 추출 방식을 사용합니다.
- 공통 로직은 `commonMain`에 일반 함수/클래스로 추출
- 플랫폼별로 달라야 하는 부분만 `expect`/`actual`로 분리
- `CountryApiService` 같이 완전히 동일한 코드도 각 소스셋에 별도 선언 (중간 소스셋 추가 없이)

---

### Q: `domain` 패키지에 `repository`가 있는 게 맞을까?
**A:** **Clean Architecture 관점에서 맞습니다.**
- `domain/repository/CountryRepository.kt` → 인터페이스(계약)만 선언, 도메인이 소유
- `data/repository/CountryRepositoryImpl.kt` → 구현체, 데이터 계층 소유
- 도메인은 구현을 모르고 인터페이스에만 의존 → 테스트/교체 용이

---

### Q: Android는 원격 DB에 직접 붙는 게 불가능한가?
**A:** 기술적으로는 가능하지만 **보안상 강력히 비권장**합니다.
- DB 접속 정보(host, user, password)가 앱에 노출됨
- DB 포트를 인터넷에 직접 열어야 함 (공격 대상)
- **올바른 방법:** Android → REST API → Spring Boot 서버 → DB (서버가 중간에서 인증/인가 담당)

---

### Q: AI 관련 기능을 넣을만한 게 있나? (API 호출 외)
**A:** 논의된 항목들:
1. **온디바이스 ML** (TensorFlow Lite / ONNX) — 이미지 분류, 텍스트 분류
2. **이상치 탐지** — 통계 기반(Z-score, IQR), ML 기반(Isolation Forest)
3. **예측 모델** — 회귀/분류 (Python sklearn과 유사하나 KMP에서는 TFLite 모델 변환 필요)
4. **Gemini/GPT API 호출** — REST API 방식으로 가장 현실적

> 결론: KMP에서 순수 온디바이스 AI보다 **외부 AI API 호출**이 가장 실용적

---

## 4. 기능 구현 히스토리

### Phase 1: 기본 구조 설정
- [x] KMP 프로젝트 생성 (Android + JVM Desktop 타겟)
- [x] Koin DI 설정 (commonModule + 플랫폼별 additionalModules)
- [x] Room 로컬 DB 설정 (Android + JVM Desktop)
- [x] Retrofit + OkHttp 네트워크 설정

### Phase 2: 국가(Country) 기능 구현
- [x] REST Countries API 연동 (`https://restcountries.com/v3.1/`)
- [x] `CountryDto` — API 응답 매핑 (복잡한 중첩 구조 포함)
- [x] `Country` 도메인 모델 생성
- [x] `CountryDto.toDomain()` 변환 함수
- [x] `RemoteCountryDataSource` 인터페이스 + `RemoteCountryDataSourceImpl` (Android/JVM)
- [x] `CountryRepository` 인터페이스 + `CountryRepositoryImpl`
- [x] `CountryViewModel` — 국가 목록 로드 및 검색
- [x] `CountryListScreen` — 국가 목록 UI (검색바, 국기 이모지, 지역 뱃지)
- [x] `CountryDetailScreen` — 국가 상세 UI (전체 정보 표시)

### Phase 3: Offline-First 캐시 구현
- [x] `LocalCountryDataSource` 인터페이스 (commonMain)
- [x] `CountryEntity` — Room Entity (복잡한 객체는 Gson JSON 직렬화)
- [x] `CountryDao` — Room DAO (CRUD + count)
- [x] `LocalCountryDataSourceImpl` — Room 구현체 (Android/JVM)
- [x] `CountryRepositoryImpl` Offline-First 리팩토링
  - `getCountries()` — Cache-First 전략
  - `getCountryByCode()` — Local-First with Remote Fallback
  - `refreshCountries()` — 강제 API 갱신
- [x] `CountryViewModel.refresh()` — 강제 갱신 함수 추가
- [x] `CountryUiState.isRefreshing` — 갱신 중 상태 추가
- [x] `CountryListScreen` — 새로고침 버튼(🔄) 추가

### Phase 4: 정리
- [x] Project 임시 코드 전체 삭제
  - `Project` 도메인 모델, Repository, Interactor, DTO
  - `ProjectApiService`, `RemoteProjectDataSourceImpl`
  - `ProjectEntity`, `ProjectDao`
  - `ProjectViewModel`, `ProjectUiState`, `ProjectEvent`
  - `ProjectScreen`
  - 서버 측 Project 관련 파일 (Controller, Service, Repository, Entity, DTO)

---

## 5. 아키텍처 설명

```
┌─────────────────────────────────────────────────────────┐
│                   Presentation Layer                      │
│  CountryViewModel ──→ CountryUiState ──→ CountryListScreen│
│                                         CountryDetailScreen│
└──────────────────────────┬──────────────────────────────┘
                           │ (CountryRepository 인터페이스)
┌──────────────────────────▼──────────────────────────────┐
│                    Domain Layer                           │
│  CountryRepository (interface)  Country (model)          │
└──────────────────────────┬──────────────────────────────┘
                           │ (구현)
┌──────────────────────────▼──────────────────────────────┐
│                     Data Layer                            │
│                                                          │
│  CountryRepositoryImpl                                   │
│      ├── RemoteCountryDataSource ──→ Retrofit API        │
│      └── LocalCountryDataSource  ──→ Room DB             │
└─────────────────────────────────────────────────────────┘
```

### 소스셋 구조

```
commonMain      ← 인터페이스, 도메인 모델, Repository 구현체, ViewModel, UI
    ↑
androidMain     ← actual 구현 (NetworkModule, Logger, CountryApiService, Room)
jvmMain         ← actual 구현 (NetworkModule, Logger, CountryApiService, Room)
jsMain          ← actual 구현 (NetworkModule, Logger - stub)
```

---

## 6. DI (Koin) 구성

### 모듈 계층

```
공통 (shared/commonMain)
└── commonModule
        └── (현재 비어있음 - 향후 공통 UseCase 등록 예정)

플랫폼별 (composeApp)
├── Android
│   ├── networkModule (actual)      ← OkHttp, Retrofit, CountryApiService, RemoteCountryDataSource
│   ├── androidDatabaseModule       ← AppDatabase, CountryDao, LocalCountryDataSource
│   ├── viewModelModule             ← CountryViewModel
│   └── (익명 module)               ← CountryRepository (CountryRepositoryImpl 바인딩)
│
└── JVM Desktop
    ├── networkModule (actual)      ← OkHttp, Retrofit, CountryApiService, RemoteCountryDataSource
    ├── jvmDatabaseModule           ← AppDatabase, CountryDao, LocalCountryDataSource
    ├── jvmCountryModule            ← CountryRepository (CountryRepositoryImpl 바인딩)
    └── viewModelModule             ← CountryViewModel
```

### 주요 Koin 바인딩

| 타입 | 구현체 | 등록 위치 |
|---|---|---|
| `CountryRepository` | `CountryRepositoryImpl(remote, local)` | Android/JVM 모듈 |
| `RemoteCountryDataSource` | `RemoteCountryDataSourceImpl(apiService)` | networkModule |
| `LocalCountryDataSource` | `LocalCountryDataSourceImpl(dao)` | database 모듈 |
| `CountryApiService` | Retrofit으로 생성 | networkModule |
| `AppDatabase` | Room.databaseBuilder() | database 모듈 |
| `CountryDao` | `db.countryDao()` | database 모듈 |
| `CountryViewModel` | `CountryViewModel(repository)` | viewModelModule |

---

## 7. Offline-First 캐시 전략

### 전략 요약

```
getCountries() 호출
    │
    ▼
로컬 DB에 데이터 있음?
    ├── YES → 즉시 반환 (빠른 UI 표시)
    └── NO  → API 호출 → DB Upsert → 반환

refresh() 호출 (🔄 버튼)
    └── 무조건 API 호출 → DB Upsert → 반환
```

### CountryEntity 직렬화 전략

Room은 복잡한 중첩 객체(`Map`, `List`, data class)를 직접 저장할 수 없으므로:

| 필드 타입 | 저장 방식 |
|---|---|
| `String`, `Long`, `Double`, `Boolean` | 컬럼 직접 저장 |
| `List<String>` | Gson JSON 문자열 (`tldJson`, `capitalJson` 등) |
| `Map<String, T>` | Gson JSON 문자열 (`currenciesJson`, `languagesJson` 등) |
| 중첩 data class | Gson JSON 문자열 (`nameJson`, `flagsJson` 등) |

### 캐시 만료 (향후 구현 방향)

현재는 앱 생명주기 동안 캐시를 유지합니다.  
향후 `cachedAt` 필드를 활용하여 TTL(Time-To-Live) 기반 만료를 구현할 수 있습니다:

```kotlin
// 예시: 1시간 이상 지난 캐시는 무시
val isExpired = (System.currentTimeMillis() - cachedAt) > 3_600_000L
if (cached.isNotEmpty() && !isExpired) return cached
```

---

## 8. 테스트 전략

### commonTest — 단위 테스트 (플랫폼 독립)

`CountryRepositoryTest.kt`에서 Fake 패턴 사용:

```
FakeSuccessRemote  ← 성공 케이스 원격 데이터소스
FakeEmptyRemote    ← 빈 결과 반환
FakeErrorRemote    ← 예외 발생
FakeLocalDataSource ← 인메모리 로컬 캐시 (Room 대체)
```

### 검증하는 동작

| 테스트 케이스 | 검증 내용 |
|---|---|
| 캐시 없으면 원격 호출 후 반환 | Cache-Miss 시 API 호출 |
| 캐시 있으면 원격 호출 없이 반환 | Cache-Hit 시 API 미호출 |
| API 결과가 로컬에 저장 | 저장 동작 검증 |
| DTO → 도메인 모델 변환 | 필드 매핑 정확성 |
| refreshCountries() | 캐시 있어도 항상 원격 호출 |
| getCountryByCode() 캐시 히트 | 로컬 우선 조회 |
| getCountryByCode() 캐시 미스 | 원격 fallback |
| 네트워크 오류 예외 전파 | 에러 처리 |

### 테스트 실행

```shell
# 공통 테스트 (JVM으로 실행)
.\gradlew :shared:jvmTest

# Android 테스트
.\gradlew :shared:testDebugUnitTest
```

---

## 9. 빌드 및 실행 방법

### Android 앱 실행

```shell
# APK 빌드
.\gradlew :composeApp:assembleDebug

# 에뮬레이터/기기에 설치 및 실행
.\gradlew :composeApp:installDebug
```

### JVM Desktop 앱 실행

```shell
# IDE에서 실행 구성 'composeApp [jvmRun]' 사용
# 또는 터미널에서:
.\gradlew :composeApp:jvmRun
```

### 서버 실행

```shell
.\gradlew :server:bootRun
```

---

## 10. 향후 개선 방향

### 기능 추가
- [ ] **캐시 만료(TTL)** — `cachedAt` 필드 활용, 1시간 이상 캐시 갱신
- [ ] **즐겨찾기** — Room에 `favorites` 테이블 추가
- [ ] **국가 비교** — 두 국가 선택 후 주요 지표 비교
- [ ] **다크 모드** — MaterialTheme 시스템 테마 연동
- [ ] **다국어(i18n)** — 국가 이름 한국어 번역 표시

### 구조 개선
- [ ] **Flow 기반 캐시** — Room의 `Flow<List<CountryEntity>>`로 실시간 갱신
- [ ] **Paging** — 국가가 250개 이상이므로 Paging3 적용 고려
- [ ] **에러 타입 분리** — `sealed class Result<T>` 패턴 적용
- [ ] **공통 Repository 추상화** — BaseRepository 도입

### 플랫폼 확장
- [ ] **iOS** — iOS 타겟 추가 (현재 미지원)
- [ ] **웹(JS/WASM)** — React + Kotlin/JS 연동 (webApp 모듈 활용)

---

## 참고 자료

| 항목 | 링크 |
|---|---|
| Kotlin Multiplatform | https://kotlinlang.org/docs/multiplatform.html |
| Compose Multiplatform | https://www.jetbrains.com/compose-multiplatform/ |
| REST Countries API | https://restcountries.com/ |
| Koin | https://insert-koin.io/ |
| Room KMP | https://developer.android.com/kotlin/multiplatform/room |
| Retrofit | https://square.github.io/retrofit/ |

