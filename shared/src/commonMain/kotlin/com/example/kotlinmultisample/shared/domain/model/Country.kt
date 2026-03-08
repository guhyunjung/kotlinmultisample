package com.example.kotlinmultisample.shared.domain.model

/**
 * 국제 전화 코드 (IDD: International Direct Dialing)
 * 예) root="+5", suffixes=["7"] → +57 (콜롬비아)
 */
data class Idd(
    /** 전화 코드 루트 (예: "+5") */
    val root: String,
    /** 루트 뒤에 붙는 접미사 목록 (예: ["7"]) */
    val suffixes: List<String>
)

/** 지도 링크 */
data class Maps(
    val googleMaps: String,
    val openStreetMaps: String
)

/** 차량 관련 정보 */
data class Car(
    /** 차량 번호판 국가 코드 (예: ["CO"]) */
    val signs: List<String>,
    /** 운전 방향 (예: "right" / "left") */
    val side: String
)

/** 국기 이미지 정보 */
data class Flag(
    val png: String,
    val svg: String,
    /** 국기 설명 (접근성 alt 텍스트) */
    val alt: String? = null
)

/** 국장(國章, Coat of Arms) 이미지 정보 */
data class CoatOfArms(
    val png: String? = null,
    val svg: String? = null
)

/**
 * 원어 이름 항목 (언어 코드별)
 * 예) "spa": { "official": "República de Colombia", "common": "Colombia" }
 */
data class NativeNameEntry(
    val official: String,
    val common: String
)

/** 국가 이름 정보 */
data class CountryName(
    /** 영어 일반 명칭 (예: "Colombia") */
    val common: String,
    /** 영어 공식 명칭 (예: "Republic of Colombia") */
    val official: String,
    /**
     * 원어 이름 목록
     * key: ISO 639-3 언어 코드 (예: "spa"), value: 공식/일반 명칭
     */
    val nativeName: Map<String, NativeNameEntry>
)

/**
 * 통화 정보
 * 예) "COP": { "symbol": "$", "name": "Colombian peso" }
 */
data class CurrencyInfo(
    val symbol: String,
    val name: String
)

/** 성별 구분 호칭 (Demonym) */
data class Demonym(
    /** 여성형 (예: "Colombian") */
    val f: String,
    /** 남성형 (예: "Colombian") */
    val m: String
)

/** 언어별 국가 호칭 (영어/프랑스어) */
data class Demonyms(
    val eng: Demonym? = null,
    val fra: Demonym? = null
)

/**
 * 번역 항목 (언어별 공식/일반 명칭)
 * 예) "kor": { "official": "콜롬비아 공화국", "common": "콜롬비아" }
 */
data class TranslationEntry(
    val official: String,
    val common: String
)

/** 수도 위치 정보 */
data class CapitalInfo(
    /** 수도의 위도/경도 [위도, 경도] (예: [4.71, -74.07]) */
    val latlng: List<Double>? = null
)

/** 우편번호 형식 정보 */
data class PostalCode(
    val format: String? = null,
    val regex: String? = null
)

// ─────────────────────────────────────────────────────────────────────────────
// 최상위 국가 도메인 모델
// ─────────────────────────────────────────────────────────────────────────────

/**
 * 국가 도메인 모델
 *
 * 네트워크 응답(CountryDto)을 변환하여 사용하는 순수 도메인 객체입니다.
 * UI 및 비즈니스 로직 계층에서 이 모델을 사용합니다.
 *
 * DTO → Domain 변환: CountryDto.toDomain() 참고
 */
data class Country(
    // ── 국가 코드 ────────────────────────────────────────────────────────────
    /** 최상위 도메인 목록 (예: [".co"]) */
    val tld: List<String>,
    /** ISO 3166-1 alpha-2 코드 (예: "CO") */
    val cca2: String,
    /** ISO 3166-1 numeric 코드 (예: "170") */
    val ccn3: String?,
    /** ISO 3166-1 alpha-3 코드 (예: "COL") */
    val cca3: String,
    /** IOC(국제올림픽위원회) 코드 (예: "COL") */
    val cioc: String?,

    // ── 국가 상태 ────────────────────────────────────────────────────────────
    /** 독립국 여부 */
    val independent: Boolean?,
    /** ISO 3166-1 등록 상태 (예: "officially-assigned") */
    val status: String,
    /** UN 회원국 여부 */
    val unMember: Boolean,

    // ── 전화/통신 ────────────────────────────────────────────────────────────
    /** 국제 전화 코드 */
    val idd: Idd?,

    // ── 지리 정보 ────────────────────────────────────────────────────────────
    /** 수도 목록 (예: ["Bogotá"]) */
    val capital: List<String>,
    /** 다른 표기/별칭 목록 */
    val altSpellings: List<String>,
    /** 대륙 구분 (예: "Americas") */
    val region: String,
    /** 세부 지역 (예: "South America") */
    val subregion: String?,
    /** 내륙국 여부 (true = 바다 접근 불가) */
    val landlocked: Boolean,
    /** 인접 국가 코드 목록 (ISO alpha-3, 예: ["BRA", "ECU"]) */
    val borders: List<String>,
    /** 면적 (㎢) */
    val area: Double,
    /** 지도 링크 */
    val maps: Maps?,
    /** 위도/경도 [위도, 경도] (예: [4.0, -72.0]) */
    val latlng: List<Double>,
    /** 대륙 목록 (예: ["South America"]) */
    val continents: List<String>,

    // ── 인구/사회 ────────────────────────────────────────────────────────────
    /** 인구 수 */
    val population: Long,
    /**
     * 지니 계수 (소득 불평등 지수)
     * key: 측정 연도 (예: "2019"), value: 지니 계수 (예: 51.3)
     */
    val gini: Map<String, Double>,
    /** FIFA 국가 코드 (예: "COL") */
    val fifa: String?,

    // ── 이름/언어 ────────────────────────────────────────────────────────────
    /** 국가 이름 (공식명/일반명/원어명) */
    val name: CountryName,
    /**
     * 공용어 목록
     * key: ISO 639-3 언어 코드 (예: "spa"), value: 언어 이름 (예: "Spanish")
     */
    val languages: Map<String, String>,
    /**
     * 다국어 번역 목록
     * key: ISO 639-3 언어 코드, value: 공식/일반 명칭
     */
    val translations: Map<String, TranslationEntry>,
    /** 성별 호칭 (영어/프랑스어) */
    val demonyms: Demonyms?,

    // ── 통화/차량 ────────────────────────────────────────────────────────────
    /**
     * 통화 목록
     * key: ISO 4217 통화 코드 (예: "COP"), value: 기호 및 이름
     */
    val currencies: Map<String, CurrencyInfo>,
    /** 차량 정보 (번호판 코드, 운전 방향) */
    val car: Car?,

    // ── 시간/행정 ────────────────────────────────────────────────────────────
    /** 타임존 목록 (예: ["UTC-05:00"]) */
    val timezones: List<String>,
    /** 주간 시작 요일 (예: "monday") */
    val startOfWeek: String?,
    /** 수도 위치 정보 */
    val capitalInfo: CapitalInfo?,
    /** 우편번호 형식 */
    val postalCode: PostalCode?,

    // ── 이미지 ───────────────────────────────────────────────────────────────
    /** 국기 이미지 (PNG/SVG) 및 설명 */
    val flags: Flag?,
    /** 국장 이미지 (PNG/SVG) */
    val coatOfArms: CoatOfArms?,
    /** 유니코드 국기 이모지 (예: "🇨🇴") */
    val flag: String?
)

