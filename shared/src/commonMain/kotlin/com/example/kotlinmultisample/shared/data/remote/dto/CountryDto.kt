package com.example.kotlinmultisample.shared.data.remote.dto

/**
 * 국제 전화 코드 (IDD: International Direct Dialing)
 *
 * 예) root="+5", suffixes=["7"] → +57 (콜롬비아)
 */
data class IddDto(
    /** 전화 코드 루트 (예: "+5") */
    val root: String,
    /** 루트 뒤에 붙는 접미사 목록 (예: ["7"]) */
    val suffixes: List<String>
)

/**
 * 지도 링크
 */
data class MapsDto(
    /** Google Maps URL */
    val googleMaps: String,
    /** OpenStreetMap URL */
    val openStreetMaps: String
)

/**
 * 차량 관련 정보
 */
data class CarDto(
    /** 차량 번호판 국가 코드 (예: ["CO"]) */
    val signs: List<String>,
    /** 운전 방향 (예: "right" / "left") */
    val side: String
)

/**
 * 국기 이미지 정보
 */
data class FlagDto(
    /** PNG 이미지 URL */
    val png: String,
    /** SVG 이미지 URL */
    val svg: String,
    /** 국기 설명 (접근성 alt 텍스트) */
    val alt: String? = null
)

/**
 * 국장(國章, Coat of Arms) 이미지 정보
 */
data class CoatOfArmsDto(
    /** PNG 이미지 URL */
    val png: String? = null,
    /** SVG 이미지 URL */
    val svg: String? = null
)

/**
 * 원어 이름 항목 (언어 코드별)
 *
 * 예) "spa": { "official": "República de Colombia", "common": "Colombia" }
 */
data class NativeNameEntryDto(
    /** 공식 명칭 */
    val official: String,
    /** 일반 명칭 */
    val common: String
)

/**
 * 국가 이름 정보
 */
data class NameDto(
    /** 영어 일반 명칭 (예: "Colombia") */
    val common: String,
    /** 영어 공식 명칭 (예: "Republic of Colombia") */
    val official: String,
    /**
     * 원어 이름 목록
     * key: ISO 639-3 언어 코드 (예: "spa")
     * value: 해당 언어의 공식/일반 명칭
     */
    val nativeName: Map<String, NativeNameEntryDto>
)

/**
 * 통화 정보
 *
 * 예) "COP": { "symbol": "$", "name": "Colombian peso" }
 */
data class CurrencyInfoDto(
    /** 통화 기호 (예: "$") */
    val symbol: String,
    /** 통화 이름 (예: "Colombian peso") */
    val name: String
)

/**
 * 성별 구분 호칭 (Demonym)
 */
data class DemonymDto(
    /** 여성형 (예: "Colombian") */
    val f: String,
    /** 남성형 (예: "Colombian") */
    val m: String
)

/**
 * 언어별 국가 호칭 (영어/프랑스어)
 */
data class DemonymsDto(
    /** 영어 호칭 */
    val eng: DemonymDto? = null,
    /** 프랑스어 호칭 */
    val fra: DemonymDto? = null
)

/**
 * 번역 항목 (언어별 공식/일반 명칭)
 *
 * 예) "kor": { "official": "콜롬비아 공화국", "common": "콜롬비아" }
 */
data class TranslationEntryDto(
    /** 공식 명칭 */
    val official: String,
    /** 일반 명칭 */
    val common: String
)

/**
 * 수도 위치 정보
 */
data class CapitalInfoDto(
    /** 수도의 위도/경도 [위도, 경도] (예: [4.71, -74.07]) */
    val latlng: List<Double>? = null
)

/**
 * 우편번호 형식 정보
 */
data class PostalCodeDto(
    /** 우편번호 형식 문자열 (없으면 null) */
    val format: String? = null,
    /** 우편번호 검증 정규식 (없으면 null) */
    val regex: String? = null
)

// ──────────────────────────────────────────────────────────────────────────────
// 최상위 국가 DTO
// ──────────────────────────────────────────────────────────────────────────────

/**
 * 국가 정보 응답 DTO
 *
 * REST Countries API (https://restcountries.com) 응답 형식과 호환됩니다.
 *
 * 사용 예:
 * ```kotlin
 * @GET("v3.1/alpha/{code}")
 * suspend fun getCountry(@Path("code") code: String): List<CountryDto>
 * ```
 */
data class CountryDto(
    // ── 국가 코드 ──────────────────────────────────────────────────────────────

    /** 최상위 도메인 목록 (예: [".co"]) */
    val tld: List<String>? = null,

    /** ISO 3166-1 alpha-2 코드 (예: "CO") */
    val cca2: String,

    /** ISO 3166-1 numeric 코드 (예: "170") */
    val ccn3: String? = null,

    /** ISO 3166-1 alpha-3 코드 (예: "COL") */
    val cca3: String,

    /** IOC(국제올림픽위원회) 코드 (예: "COL") */
    val cioc: String? = null,

    // ── 국가 상태 ──────────────────────────────────────────────────────────────

    /** 독립국 여부 */
    val independent: Boolean? = null,

    /** ISO 3166-1 등록 상태 (예: "officially-assigned") */
    val status: String,

    /** UN 회원국 여부 */
    val unMember: Boolean,

    // ── 전화/통신 ──────────────────────────────────────────────────────────────

    /** 국제 전화 코드 */
    val idd: IddDto? = null,

    // ── 지리 정보 ──────────────────────────────────────────────────────────────

    /** 수도 목록 (예: ["Bogotá"]) */
    val capital: List<String>? = null,

    /** 다른 표기/별칭 목록 */
    val altSpellings: List<String>,

    /** 대륙 구분 (예: "Americas") */
    val region: String,

    /** 세부 지역 (예: "South America") */
    val subregion: String? = null,

    /** 내륙국 여부 (true = 바다 접근 불가) */
    val landlocked: Boolean,

    /** 인접 국가 코드 목록 (ISO alpha-3, 예: ["BRA", "ECU"]) */
    val borders: List<String>? = null,

    /** 면적 (㎢) */
    val area: Double,

    /** 지도 링크 */
    val maps: MapsDto? = null,

    /** 위도/경도 [위도, 경도] (예: [4.0, -72.0]) */
    val latlng: List<Double>,

    /** 대륙 목록 (예: ["South America"]) */
    val continents: List<String>,

    // ── 인구/사회 ──────────────────────────────────────────────────────────────

    /** 인구 수 */
    val population: Long,

    /**
     * 지니 계수 (소득 불평등 지수)
     * key: 측정 연도 (예: "2019"), value: 지니 계수 (예: 51.3)
     */
    val gini: Map<String, Double>? = null,

    /** FIFA 국가 코드 (예: "COL") */
    val fifa: String? = null,

    // ── 이름/언어 ──────────────────────────────────────────────────────────────

    /** 국가 이름 (공식명/일반명/원어명) */
    val name: NameDto,

    /**
     * 공용어 목록
     * key: ISO 639-3 언어 코드 (예: "spa"), value: 언어 이름 (예: "Spanish")
     */
    val languages: Map<String, String>? = null,

    /**
     * 다국어 번역 목록
     * key: ISO 639-3 언어 코드, value: 공식/일반 명칭
     */
    val translations: Map<String, TranslationEntryDto>? = null,

    /** 성별 호칭 (영어/프랑스어) */
    val demonyms: DemonymsDto? = null,

    // ── 통화/차량 ──────────────────────────────────────────────────────────────

    /**
     * 통화 목록
     * key: ISO 4217 통화 코드 (예: "COP"), value: 기호 및 이름
     */
    val currencies: Map<String, CurrencyInfoDto>? = null,

    /** 차량 정보 (번호판 코드, 운전 방향) */
    val car: CarDto? = null,

    // ── 시간/행정 ──────────────────────────────────────────────────────────────

    /** 타임존 목록 (예: ["UTC-05:00"]) */
    val timezones: List<String>,

    /** 주간 시작 요일 (예: "monday") */
    val startOfWeek: String? = null,

    /** 수도 위치 정보 */
    val capitalInfo: CapitalInfoDto? = null,

    /** 우편번호 형식 */
    val postalCode: PostalCodeDto? = null,

    // ── 이미지 ────────────────────────────────────────────────────────────────

    /** 국기 이미지 (PNG/SVG) 및 설명 */
    val flags: FlagDto? = null,

    /** 국장 이미지 (PNG/SVG) */
    val coatOfArms: CoatOfArmsDto? = null,

    /** 유니코드 국기 이모지 (예: "🇨🇴") */
    val flag: String? = null
)



