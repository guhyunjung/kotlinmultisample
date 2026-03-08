package com.example.kotlinmultisample.shared.data.local.database

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
    @PrimaryKey val cca2: String,
    val ccn3: String?,
    val cca3: String,
    val cioc: String?,
    val independent: Boolean?,
    val status: String,
    val unMember: Boolean,
    val region: String,
    val subregion: String?,
    val landlocked: Boolean,
    val area: Double,
    val population: Long,
    val fifa: String?,
    val flag: String?,
    val startOfWeek: String?,
    val tldJson: String,
    val capitalJson: String,
    val altSpellingsJson: String,
    val bordersJson: String,
    val continentsJson: String,
    val timezonesJson: String,
    val latlngJson: String,
    val nameJson: String,
    val currenciesJson: String,
    val languagesJson: String,
    val giniJson: String,
    val translationsJson: String,
    val iddJson: String?,
    val mapsJson: String?,
    val flagsJson: String?,
    val coatOfArmsJson: String?,
    val demonymsJson: String?,
    val carJson: String?,
    val capitalInfoJson: String?,
    val postalCodeJson: String?,
    /** 캐시 저장 시각 (Unix timestamp, ms) — TTL 기반 만료 판단에 사용 */
    val cachedAt: Long = System.currentTimeMillis()
)

// ─────────────────────────────────────────────────────────────────────────────
// 변환 함수
// ─────────────────────────────────────────────────────────────────────────────

private val gson = Gson()

private fun List<String>.toJson(): String = gson.toJson(this)
private fun String.toStringList(): List<String> =
    gson.fromJson(this, object : TypeToken<List<String>>() {}.type) ?: emptyList()
private fun List<Double>.toDoubleJson(): String = gson.toJson(this)
private fun String.toDoubleList(): List<Double> =
    gson.fromJson(this, object : TypeToken<List<Double>>() {}.type) ?: emptyList()

/** 도메인 모델 [Country] → Room Entity [CountryEntity] */
fun Country.toEntity(): CountryEntity = CountryEntity(
    cca2 = cca2, ccn3 = ccn3, cca3 = cca3, cioc = cioc,
    independent = independent, status = status, unMember = unMember,
    region = region, subregion = subregion, landlocked = landlocked,
    area = area, population = population, fifa = fifa, flag = flag,
    startOfWeek = startOfWeek,
    tldJson = tld.toJson(), capitalJson = capital.toJson(),
    altSpellingsJson = altSpellings.toJson(), bordersJson = borders.toJson(),
    continentsJson = continents.toJson(), timezonesJson = timezones.toJson(),
    latlngJson = latlng.toDoubleJson(),
    nameJson = gson.toJson(name), currenciesJson = gson.toJson(currencies),
    languagesJson = gson.toJson(languages), giniJson = gson.toJson(gini),
    translationsJson = gson.toJson(translations),
    iddJson = idd?.let { gson.toJson(it) },
    mapsJson = maps?.let { gson.toJson(it) },
    flagsJson = flags?.let { gson.toJson(it) },
    coatOfArmsJson = coatOfArms?.let { gson.toJson(it) },
    demonymsJson = demonyms?.let { gson.toJson(it) },
    carJson = car?.let { gson.toJson(it) },
    capitalInfoJson = capitalInfo?.let { gson.toJson(it) },
    postalCodeJson = postalCode?.let { gson.toJson(it) }
)

/** Room Entity [CountryEntity] → 도메인 모델 [Country] */
fun CountryEntity.toDomain(): Country = Country(
    cca2 = cca2, ccn3 = ccn3, cca3 = cca3, cioc = cioc,
    independent = independent, status = status, unMember = unMember,
    region = region, subregion = subregion, landlocked = landlocked,
    area = area, population = population, fifa = fifa, flag = flag,
    startOfWeek = startOfWeek,
    tld = tldJson.toStringList(), capital = capitalJson.toStringList(),
    altSpellings = altSpellingsJson.toStringList(), borders = bordersJson.toStringList(),
    continents = continentsJson.toStringList(), timezones = timezonesJson.toStringList(),
    latlng = latlngJson.toDoubleList(),
    name = gson.fromJson(nameJson, CountryName::class.java),
    currencies = gson.fromJson(currenciesJson, object : TypeToken<Map<String, CurrencyInfo>>() {}.type) ?: emptyMap(),
    languages = gson.fromJson(languagesJson, object : TypeToken<Map<String, String>>() {}.type) ?: emptyMap(),
    gini = gson.fromJson(giniJson, object : TypeToken<Map<String, Double>>() {}.type) ?: emptyMap(),
    translations = gson.fromJson(translationsJson, object : TypeToken<Map<String, TranslationEntry>>() {}.type) ?: emptyMap(),
    idd = iddJson?.let { gson.fromJson(it, Idd::class.java) },
    maps = mapsJson?.let { gson.fromJson(it, Maps::class.java) },
    flags = flagsJson?.let { gson.fromJson(it, Flag::class.java) },
    coatOfArms = coatOfArmsJson?.let { gson.fromJson(it, CoatOfArms::class.java) },
    demonyms = demonymsJson?.let { gson.fromJson(it, Demonyms::class.java) },
    car = carJson?.let { gson.fromJson(it, Car::class.java) },
    capitalInfo = capitalInfoJson?.let { gson.fromJson(it, CapitalInfo::class.java) },
    postalCode = postalCodeJson?.let { gson.fromJson(it, PostalCode::class.java) }
)

