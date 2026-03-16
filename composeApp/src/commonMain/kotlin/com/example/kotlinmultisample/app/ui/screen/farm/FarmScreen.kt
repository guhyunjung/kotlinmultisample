package com.example.kotlinmultisample.app.ui.screen.farm

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kotlinmultisample.app.presentation.farm.FarmViewModel
import org.koin.compose.koinInject

/**
 * 농장(포트폴리오) 화면의 메인 엔트리 포인트입니다.
 *
 * 전체 화면 구조:
 * 1. [SkySection]: 상단 하늘 배경 및 타이틀
 * 2. [BrokerTabs]: 증권사 선택 탭 (가로 스크롤)
 * 3. [SummaryBarNew]: 자산 요약 정보
 * 4. [FieldGridNew]: 보유 종목(작물) 리스트 그리드
 * 5. [BottomMenuBarNew]: 하단 네비게이션
 *
 * 오버레이(Overlay) 시스템:
 * 화면 위에 겹쳐지는 레이어들로, [AnimatedVisibility]를 사용하여 부드럽게 등장/퇴장합니다.
 * - 튜토리얼, 상세정보, 창고(기록), 일기장, 계산기 등
 */
@Composable
fun FarmScreen(onMenuClick: () -> Unit = {}) {
	// Koin을 통해 싱글톤 ViewModel 주입 (앱 실행 중 튜토리얼 상태 유지 등의 역할)
	val viewModel = koinInject<FarmViewModel>()
	
    // StateFlow 구독 (Compose State로 변환)
    val showTutorial by viewModel.showTutorial.collectAsState()
    val brokers by viewModel.brokers.collectAsState()
    val selectedBroker by viewModel.selectedBroker.collectAsState()

	// 오버레이 및 팝업 상태 관리 변수들 (화면 전환 없이 위에 띄움)
	var selectedSeed by remember { mutableStateOf<FarmSeed?>(null) } // 선택된 종목(씨앗) -> 상세 화면 트리거
	var showWarehouse by remember { mutableStateOf(false) }      // 창고(매매기록) 화면 표시 여부
	var showDiary by remember { mutableStateOf(false) }          // 농부일기 화면 표시 여부
	var showCalculate by remember { mutableStateOf(false) }      // 계산기 화면 표시 여부

	// 더미 데이터: 실제 앱에서는 서버 또는 DB에서 가져와야 할 주식(씨앗) 목록
	val seeds = remember {
		listOf(
			FarmSeed("한화에어로", "🌾", 10.5),
			FarmSeed("삼성전자", "🌿", 8.4),
			FarmSeed("카카오", "🍂", -3.2),
			FarmSeed("NAVER", "🌱", 5.1),
			FarmSeed("LG에너지", "🥀", -11.2)
		)
	}

	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(FarmColors.getSky()) // 전체 배경색은 하늘색으로 시작
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = {}
			) // 상위 레이어(오버레이)가 떠있을 때 뒷배경 터치 이벤트를 막기 위한 빈 핸들러
	) {
		Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
			// 1. 하늘 섹션 (구름 애니메이션, 레벨 표시, 햄버거 메뉴)
			SkySection(onMenuClick = onMenuClick)

			// 2. 브로커 탭 (증권사 선택 탭)
            // 선택된 증권사에 따라 아래 FieldGrid의 데이터가 필터링되어야 함 (현재는 더미 데이터 사용 중)
			BrokerTabs(
                brokers = brokers,
                selectedBroker = selectedBroker,
                onBrokerSelected = { viewModel.selectBroker(it) },
                onAddBroker = { viewModel.addBroker(it) }
            )

			// 3. 농장 영역 (메인 콘텐츠: 요약정보 + 작물 그리드)
			// weight(1f)를 주어 남은 세로 공간을 모두 차지하게 함
			Column(
				modifier = Modifier
					.weight(1f)
					.fillMaxWidth()
					.background(FarmColors.getGround()) // 농장 배경색(땅 색상)
					.padding(horizontal = 12.dp)
			) {
				Spacer(modifier = Modifier.height(8.dp))
				// 요약 바: 투자금, 평가금액, 손익 등을 보여주는 상단 바
				SummaryBarNew()
				Spacer(modifier = Modifier.height(8.dp))
				// 밭 그리드: 보유 중인 주식들이 작물 형태로 표시되는 영역
				FieldGridNew(seeds, onSeedClick = { selectedSeed = it })
			}

			// 4. 하단 메뉴바 (내 밭, 창고, 일기, 가방 아이콘)
			BottomMenuBarNew(
				onWarehouseClick = { showWarehouse = true },
				onDiaryClick = { showDiary = true },
				onCalculateClick = { showCalculate = true }
			)
		}

		// --- 오버레이 정의 영역 (Z-Index 순서상 위에 그려짐) ---

		// 튜토리얼 오버레이: 앱 최초 실행 시 가이드 표시 (ViewModel 상태 연동)
		AnimatedVisibility(
			visible = showTutorial, 
			enter = fadeIn() + scaleIn(initialScale = 0.9f),
			exit = fadeOut() + scaleOut(targetScale = 0.9f)
		) {
			TutorialOverlayNew(onDismiss = { viewModel.completeTutorial() }) // 완료 시 설정값 저장
		}

		// 상세 정보 오버레이: 슬라이드 업 애니메이션으로 등장하는 종목 상세 화면
		AnimatedVisibility(
			visible = selectedSeed != null,
			enter = slideInVertically(initialOffsetY = { it }),
			exit = slideOutVertically(targetOffsetY = { it })
		) {
			selectedSeed?.let { seed ->
				// 종목이 선택되었을 때만 렌더링
				DetailOverlayNew(seed = seed, onDismiss = { selectedSeed = null })
			}
		}

		// 창고 오버레이: 매매 기록 및 손익 현황 화면
		AnimatedVisibility(
			visible = showWarehouse,
			enter = slideInVertically(initialOffsetY = { it }),
			exit = slideOutVertically(targetOffsetY = { it })
		) {
			WarehouseOverlayNew(onDismiss = { showWarehouse = false })
		}

		// 일기 오버레이: 투자 노트 작성 화면
		AnimatedVisibility(
			visible = showDiary,
			enter = slideInVertically(initialOffsetY = { it }),
			exit = slideOutVertically(targetOffsetY = { it })
		) {
			DiaryOverlayNew(onDismiss = { showDiary = false })
		}

		// 계산기 오버레이
		AnimatedVisibility(
			visible = showCalculate,
			enter = slideInVertically(initialOffsetY = { it }),
			exit = slideOutVertically(targetOffsetY = { it })
		) {
			CalculateOverlayNew(onDismiss = { showCalculate = false })
		}
	}
}
