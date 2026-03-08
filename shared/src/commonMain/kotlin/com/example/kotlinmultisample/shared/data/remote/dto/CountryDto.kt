package com.example.kotlinmultisample.shared.data.remote.dto

import com.example.kotlinmultisample.shared.domain.model.*

/**
 * 국제 전화 코드 (IDD: International Direct Dialing)
 *
 * 예) root="+5", suffixes=["7"] → +57 (콜롬비아)
 */
data class IddDto(
    /** 전화 코드 루트 (예: "+5") */
    val root: String? = null,
    /** 루트 뒤에 붙는 접미사 목록 (예: ["7"]) */
    val suffixes: List<String>? = null
)

/**
 * 지도 링크
 */
data class MapsDto(
    /** Google Maps URL */
    val googleMaps: String? = null,
    /** OpenStreetMap URL */
    val openStreetMaps: String? = null
)

/**
 * 차량 관련 정보
 */
data class CarDto(
    /** 차량 번호판 국가 코드 (예: ["CO"]) */
    val signs: List<String>? = null,
    /** 운전 방향 (예: "right" / "left") */
    val side: String? = null
)

/**
 * 국기 이미지 정보
 */
data class FlagDto(
    /** PNG 이미지 URL */
    val png: String? = null,
    /** SVG 이미지 URL */
    val svg: String? = null,
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
    val official: String? = null,
    /** 일반 명칭 */
    val common: String? = null
)

/**
 * 국가 이름 정보
 */
data class NameDto(
    /** 영어 일반 명칭 (예: "Colombia") */
    val common: String? = null,
    /** 영어 공식 명칭 (예: "Republic of Colombia") */
    val official: String? = null,
    /**
     * 원어 이름 목록
     * key: ISO 639-3 언어 코드 (예: "spa")
     * value: 해당 언어의 공식/일반 명칭
     */
    val nativeName: Map<String, NativeNameEntryDto>? = null
)

/**
 * 통화 정보
 *
 * 예) "COP": { "symbol": "$", "name": "Colombian peso" }
 */
data class CurrencyInfoDto(
    /** 통화 기호 (예: "$") */
    val symbol: String? = null,
    /** 통화 이름 (예: "Colombian peso") */
    val name: String? = null
)

/**
 * 성별 구분 호칭 (Demonym)
 */
data class DemonymDto(
    /** 여성형 (예: "Colombian") */
    val f: String? = null,
    /** 남성형 (예: "Colombian") */
    val m: String? = null
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
    val official: String? = null,
    /** 일반 명칭 */
    val common: String? = null
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

    /**
     * ISO 3166-1 alpha-2 코드 (예: "CO")
     * 일부 국가/지역은 API 응답에서 누락될 수 있으므로 nullable 처리
     */
    val cca2: String? = null,

    /** ISO 3166-1 numeric 코드 (예: "170") */
    val ccn3: String? = null,

    /**
     * ISO 3166-1 alpha-3 코드 (예: "COL")
     * 일부 국가/지역은 API 응답에서 누락될 수 있으므로 nullable 처리
     */
    val cca3: String? = null,

    /** IOC(국제올림픽위원회) 코드 (예: "COL") */
    val cioc: String? = null,

    // ── 국가 상태 ──────────────────────────────────────────────────────────────

    /** 독립국 여부 */
    val independent: Boolean? = null,

    /**
     * ISO 3166-1 등록 상태 (예: "officially-assigned")
     * 일부 응답에서 누락될 수 있으므로 nullable 처리
     */
    val status: String? = null,

    /** UN 회원국 여부 */
    val unMember: Boolean? = null,

    // ── 전화/통신 ──────────────────────────────────────────────────────────────

    /** 국제 전화 코드 */
    val idd: IddDto? = null,

    // ── 지리 정보 ──────────────────────────────────────────────────────────────

    /** 수도 목록 (예: ["Bogotá"]) */
    val capital: List<String>? = null,

    /**
     * 다른 표기/별칭 목록
     * 일부 응답에서 누락될 수 있으므로 nullable 처리
     */
    val altSpellings: List<String>? = null,

    /**
     * 대륙 구분 (예: "Americas")
     * 일부 응답에서 누락될 수 있으므로 nullable 처리
     */
    val region: String? = null,

    /** 세부 지역 (예: "South America") */
    val subregion: String? = null,

    /** 내륙국 여부 (true = 바다 접근 불가) */
    val landlocked: Boolean? = null,

    /** 인접 국가 코드 목록 (ISO alpha-3, 예: ["BRA", "ECU"]) */
    val borders: List<String>? = null,

    /** 면적 (㎢) */
    val area: Double? = null,

    /** 지도 링크 */
    val maps: MapsDto? = null,

    /**
     * 위도/경도 [위도, 경도] (예: [4.0, -72.0])
     * 일부 응답에서 누락될 수 있으므로 nullable 처리
     */
    val latlng: List<Double>? = null,

    /**
     * 대륙 목록 (예: ["South America"])
     * 일부 응답에서 누락될 수 있으므로 nullable 처리
     */
    val continents: List<String>? = null,

    // ── 인구/사회 ──────────────────────────────────────────────────────────────

    /** 인구 수 */
    val population: Long? = null,

    /**
     * 지니 계수 (소득 불평등 지수)
     * key: 측정 연도 (예: "2019"), value: 지니 계수 (예: 51.3)
     */
    val gini: Map<String, Double>? = null,

    /** FIFA 국가 코드 (예: "COL") */
    val fifa: String? = null,

    // ── 이름/언어 ──────────────────────────────────────────────────────────────

    /**
     * 국가 이름 (공식명/일반명/원어명)
     * 일부 응답에서 누락될 수 있으므로 nullable 처리
     */
    val name: NameDto? = null,

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

    /**
     * 타임존 목록 (예: ["UTC-05:00"])
     * 일부 응답에서 누락될 수 있으므로 nullable 처리
     */
    val timezones: List<String>? = null,

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

// ─────────────────────────────────────────────────────────────────────────────
// DTO → Domain 변환 함수
// ─────────────────────────────────────────────────────────────────────────────

/**
 * [CountryDto]를 도메인 모델 [Country]로 변환합니다.
 *
 * - null 가능 컬렉션은 emptyList() / emptyMap()으로 기본값 처리합니다.
 * - 중첩 Dto도 각 변환 함수를 통해 도메인 타입으로 변환합니다.
 */
fun CountryDto.toDomain(): Country = Country(
    tld           = tld ?: emptyList(),
    // cca2가 null인 경우 cca3로 fallback, 둘 다 없으면 빈 문자열 (LazyColumn key는 UI에서 별도 처리)
    cca2          = cca2 ?: cca3 ?: "",
    ccn3          = ccn3,
    cca3          = cca3 ?: "",
    cioc          = cioc,
    independent   = independent,
    status        = status ?: "",
    unMember      = unMember ?: false,
    // IddDto 내부 필드도 nullable이므로 기본값 처리
    idd           = idd?.let { Idd(root = it.root ?: "", suffixes = it.suffixes ?: emptyList()) },
    capital       = capital ?: emptyList(),
    altSpellings  = altSpellings ?: emptyList(),
    region        = region ?: "",
    subregion     = subregion,
    landlocked    = landlocked ?: false,
    borders       = borders ?: emptyList(),
    area          = area ?: 0.0,
    // MapsDto 내부 필드도 nullable이므로 기본값 처리
    maps          = maps?.let { Maps(googleMaps = it.googleMaps ?: "", openStreetMaps = it.openStreetMaps ?: "") },
    latlng        = latlng ?: emptyList(),
    continents    = continents ?: emptyList(),
    population    = population ?: 0L,
    gini          = gini ?: emptyMap(),
    fifa          = fifa,
    // name이 null인 경우 빈 이름 객체로 대체
    name          = name?.toDomain() ?: CountryName(common = "", official = "", nativeName = emptyMap()),
    languages     = languages ?: emptyMap(),
    // TranslationEntry 생성 시 nullable 처리
    translations  = translations?.mapValues { TranslationEntry(official = it.value.official ?: "", common = it.value.common ?: "") } ?: emptyMap(),
    demonyms      = demonyms?.let {
        Demonyms(
            // DemonymDto 내부 f, m 필드도 nullable이므로 기본값 처리
            eng = it.eng?.let { d -> Demonym(f = d.f ?: "", m = d.m ?: "") },
            fra = it.fra?.let { d -> Demonym(f = d.f ?: "", m = d.m ?: "") }
        )
    },
    // CurrencyInfoDto 내부 필드도 nullable이므로 기본값 처리
    currencies    = currencies?.mapValues { CurrencyInfo(symbol = it.value.symbol ?: "", name = it.value.name ?: "") } ?: emptyMap(),
    // CarDto 내부 필드도 nullable이므로 기본값 처리
    car           = car?.let { Car(signs = it.signs ?: emptyList(), side = it.side ?: "") },
    timezones     = timezones ?: emptyList(),
    startOfWeek   = startOfWeek,
    capitalInfo   = capitalInfo?.let { CapitalInfo(it.latlng) },
    postalCode    = postalCode?.let { PostalCode(it.format, it.regex) },
    // FlagDto 내부 필드도 nullable이므로 기본값 처리
    flags         = flags?.let { Flag(png = it.png ?: "", svg = it.svg ?: "", alt = it.alt) },
    coatOfArms    = coatOfArms?.let { CoatOfArms(it.png, it.svg) },
    flag          = flag
)

/** [NameDto]를 도메인 모델 [CountryName]으로 변환합니다. */
private fun NameDto.toDomain(): CountryName = CountryName(
    common      = common ?: "",
    official    = official ?: "",
    // NativeNameEntryDto 내부 필드도 nullable이므로 기본값 처리
    nativeName  = nativeName?.mapValues { NativeNameEntry(official = it.value.official ?: "", common = it.value.common ?: "") } ?: emptyMap()
)



