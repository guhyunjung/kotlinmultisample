package com.example.kotlinmultisample.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.kotlinmultisample.shared.domain.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Room 로컬 DB 국가 Entity (Android)
 *
 * Country 도메인 모델은 중첩 구조가 복잡하므로,
 * Room이 직접 다루기 어려운 복잡한 객체(List, Map, 중첩 data class)는
 * Gson으로 JSON 문자열로 직렬화하여 저장합니다.
 *
 * 직렬화 전략:
 *  - 단순 타입(String, Long, Double 등) → 그대로 컬럼으로 저장
 *  - List<String>, Map, 중첩 객체 → JSON 문자열 컬럼으로 저장
 *
 * @property cca2 ISO alpha-2 국가 코드 (기본 키, 예: "CO")
 */
@Entity(tableName = "countries")
data class CountryEntity(

    /** ISO alpha-2 코드 (기본 키) */
    @PrimaryKey
    val cca2: String,

    /** ISO numeric 코드 */
    val ccn3: String?,

    /** ISO alpha-3 코드 */
    val cca3: String,

    /** IOC 코드 */
    val cioc: String?,

    /** 독립국 여부 */
    val independent: Boolean?,

    /** ISO 등록 상태 */
    val status: String,

    /** UN 회원국 여부 */
    val unMember: Boolean,

    /** 대륙 구분 (예: "Americas") */
    val region: String,

    /** 세부 지역 (예: "South America") */
    val subregion: String?,

    /** 내륙국 여부 */
    val landlocked: Boolean,

    /** 면적 (㎢) */
    val area: Double,

    /** 인구 수 */
    val population: Long,

    /** FIFA 국가 코드 */
    val fifa: String?,

    /** 유니코드 국기 이모지 (예: "🇨🇴") */
    val flag: String?,

    /** 주간 시작 요일 */
    val startOfWeek: String?,

    // ── JSON 직렬화 필드 ────────────────────────────────────────────────────

    /** 최상위 도메인 목록 JSON (List<String>) */
    val tldJson: String,

    /** 수도 목록 JSON (List<String>) */
    val capitalJson: String,

    /** 다른 표기/별칭 목록 JSON (List<String>) */
    val altSpellingsJson: String,

    /** 인접 국가 코드 목록 JSON (List<String>) */
    val bordersJson: String,

    /** 대륙 목록 JSON (List<String>) */
    val continentsJson: String,

    /** 타임존 목록 JSON (List<String>) */
    val timezonesJson: String,

    /** 위도/경도 JSON (List<Double>) */
    val latlngJson: String,

    /** 국가 이름 JSON (CountryName 객체) */
    val nameJson: String,

    /** 통화 목록 JSON (Map<String, CurrencyInfo>) */
    val currenciesJson: String,

    /** 공용어 목록 JSON (Map<String, String>) */
    val languagesJson: String,

    /** 지니 계수 JSON (Map<String, Double>) */
    val giniJson: String,

    /** 번역 목록 JSON (Map<String, TranslationEntry>) */
    val translationsJson: String,

    /** 국제 전화 코드 JSON (Idd 객체) */
    val iddJson: String?,

    /** 지도 링크 JSON (Maps 객체) */
    val mapsJson: String?,

    /** 국기 이미지 JSON (Flag 객체) */
    val flagsJson: String?,

    /** 국장 이미지 JSON (CoatOfArms 객체) */
    val coatOfArmsJson: String?,

    /** 성별 호칭 JSON (Demonyms 객체) */
    val demonymsJson: String?,

    /** 차량 정보 JSON (Car 객체) */
    val carJson: String?,

    /** 수도 위치 JSON (CapitalInfo 객체) */
    val capitalInfoJson: String?,

    /** 우편번호 형식 JSON (PostalCode 객체) */
    val postalCodeJson: String?,

    /**
     * 캐시 저장 시각 (Unix timestamp, milliseconds)
     * 캐시 만료 여부를 판단할 때 사용합니다.
     */
    val cachedAt: Long = System.currentTimeMillis()
)

// ─────────────────────────────────────────────────────────────────────────────
// 변환 함수
// ─────────────────────────────────────────────────────────────────────────────

private val gson = Gson()

/** List<String> → JSON 문자열 */
private fun List<String>.toJson(): String = gson.toJson(this)

/** JSON 문자열 → List<String> */
private fun String.toStringList(): List<String> =
    gson.fromJson(this, object : TypeToken<List<String>>() {}.type) ?: emptyList()

/** List<Double> → JSON 문자열 */
private fun List<Double>.toDoubleJson(): String = gson.toJson(this)

/** JSON 문자열 → List<Double> */
private fun String.toDoubleList(): List<Double> =
    gson.fromJson(this, object : TypeToken<List<Double>>() {}.type) ?: emptyList()

/**
 * 도메인 모델 [Country]를 Room Entity [CountryEntity]로 변환합니다.
 *
 * 복잡한 중첩 객체는 Gson으로 JSON 직렬화하여 저장합니다.
 */
fun Country.toEntity(): CountryEntity = CountryEntity(
    cca2              = cca2,
    ccn3              = ccn3,
    cca3              = cca3,
    cioc              = cioc,
    independent       = independent,
    status            = status,
    unMember          = unMember,
    region            = region,
    subregion         = subregion,
    landlocked        = landlocked,
    area              = area,
    population        = population,
    fifa              = fifa,
    flag              = flag,
    startOfWeek       = startOfWeek,
    tldJson           = tld.toJson(),
    capitalJson       = capital.toJson(),
    altSpellingsJson  = altSpellings.toJson(),
    bordersJson       = borders.toJson(),
    continentsJson    = continents.toJson(),
    timezonesJson     = timezones.toJson(),
    latlngJson        = latlng.toDoubleJson(),
    nameJson          = gson.toJson(name),
    currenciesJson    = gson.toJson(currencies),
    languagesJson     = gson.toJson(languages),
    giniJson          = gson.toJson(gini),
    translationsJson  = gson.toJson(translations),
    iddJson           = idd?.let { gson.toJson(it) },
    mapsJson          = maps?.let { gson.toJson(it) },
    flagsJson         = flags?.let { gson.toJson(it) },
    coatOfArmsJson    = coatOfArms?.let { gson.toJson(it) },
    demonymsJson      = demonyms?.let { gson.toJson(it) },
    carJson           = car?.let { gson.toJson(it) },
    capitalInfoJson   = capitalInfo?.let { gson.toJson(it) },
    postalCodeJson    = postalCode?.let { gson.toJson(it) }
)

/**
 * Room Entity [CountryEntity]를 도메인 모델 [Country]로 변환합니다.
 *
 * JSON 문자열을 Gson으로 역직렬화하여 원래 타입으로 복원합니다.
 */
fun CountryEntity.toDomain(): Country = Country(
    cca2         = cca2,
    ccn3         = ccn3,
    cca3         = cca3,
    cioc         = cioc,
    independent  = independent,
    status       = status,
    unMember     = unMember,
    region       = region,
    subregion    = subregion,
    landlocked   = landlocked,
    area         = area,
    population   = population,
    fifa         = fifa,
    flag         = flag,
    startOfWeek  = startOfWeek,
    tld          = tldJson.toStringList(),
    capital      = capitalJson.toStringList(),
    altSpellings = altSpellingsJson.toStringList(),
    borders      = bordersJson.toStringList(),
    continents   = continentsJson.toStringList(),
    timezones    = timezonesJson.toStringList(),
    latlng       = latlngJson.toDoubleList(),
    name         = gson.fromJson(nameJson, CountryName::class.java),
    currencies   = gson.fromJson(currenciesJson, object : TypeToken<Map<String, CurrencyInfo>>() {}.type) ?: emptyMap(),
    languages    = gson.fromJson(languagesJson, object : TypeToken<Map<String, String>>() {}.type) ?: emptyMap(),
    gini         = gson.fromJson(giniJson, object : TypeToken<Map<String, Double>>() {}.type) ?: emptyMap(),
    translations = gson.fromJson(translationsJson, object : TypeToken<Map<String, TranslationEntry>>() {}.type) ?: emptyMap(),
    idd          = iddJson?.let { gson.fromJson(it, Idd::class.java) },
    maps         = mapsJson?.let { gson.fromJson(it, Maps::class.java) },
    flags        = flagsJson?.let { gson.fromJson(it, Flag::class.java) },
    coatOfArms   = coatOfArmsJson?.let { gson.fromJson(it, CoatOfArms::class.java) },
    demonyms     = demonymsJson?.let { gson.fromJson(it, Demonyms::class.java) },
    car          = carJson?.let { gson.fromJson(it, Car::class.java) },
    capitalInfo  = capitalInfoJson?.let { gson.fromJson(it, CapitalInfo::class.java) },
    postalCode   = postalCodeJson?.let { gson.fromJson(it, PostalCode::class.java) }
)

