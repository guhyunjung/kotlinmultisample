package com.example.kotlinmultisample.app.presentation.farm

import com.example.kotlinmultisample.app.ui.screen.farm.model.FarmSeed
import com.example.kotlinmultisample.app.ui.screen.farm.model.DiaryType as UiDiaryType
import com.example.kotlinmultisample.shared.domain.model.DiaryType as DomainDiaryType
import com.example.kotlinmultisample.app.ui.screen.farm.model.DiaryUiModel as UiDiaryModel
import com.example.kotlinmultisample.shared.domain.model.Diary as DomainDiary
import com.example.kotlinmultisample.shared.domain.model.FarmSeed as DomainFarmSeed

/**
 * Domain 모델과 UI 모델 간의 변환을 담당하는 매퍼 함수들
 */

fun DomainFarmSeed.toUiModel(): FarmSeed {
    return FarmSeed(
        id = id,
        brokerId = brokerId,
        name = name,
        emoji = emoji,
        pct = pct,
        buyingPrice = buyingPrice,
        quantity = quantity
    )
}

fun FarmSeed.toDomainModel(brokerId: Long): DomainFarmSeed {
    return DomainFarmSeed(
        id = id,
        brokerId = brokerId,
        name = name,
        emoji = emoji,
        pct = pct,
        buyingPrice = buyingPrice,
        quantity = quantity
    )
}

fun DomainDiary.toUiModel(): UiDiaryModel {
    return UiDiaryModel(
        id = id,
        date = date,
        content = content,
        type = type.toUiModel()
    )
}

fun UiDiaryModel.toDomainModel(): DomainDiary {
    return DomainDiary(
        id = id,
        date = date,
        content = content,
        type = type.toDomainModel()
    )
}

fun DomainDiaryType.toUiModel(): UiDiaryType {
    return when (this) {
        DomainDiaryType.DAILY -> UiDiaryType.DAILY
        DomainDiaryType.INTEREST -> UiDiaryType.INTEREST
        DomainDiaryType.STUDY -> UiDiaryType.STUDY
    }
}

fun UiDiaryType.toDomainModel(): DomainDiaryType {
    return when (this) {
        UiDiaryType.DAILY -> DomainDiaryType.DAILY
        UiDiaryType.INTEREST -> DomainDiaryType.INTEREST
        UiDiaryType.STUDY -> DomainDiaryType.STUDY
    }
}
