package com.example.kotlinmultisample.app.ui.screen.farm

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kotlinmultisample.app.presentation.farm.FarmViewModel
import com.example.kotlinmultisample.app.ui.util.BackHandler
import org.koin.compose.koinInject

/**
 * 농장(포트폴리오) 화면의 메인 엔트리 포인트입니다.
 *
 * 전체 화면 구조:
 * - 상단 컨텐츠 영역 (탭에 따라 변경):
 *   - Main: [SkySection] + [BrokerTabs] + [SummaryBarNew] + [FieldGridNew]
 *   - Warehouse: [WarehouseScreen] (Screen으로 전환)
 *   - Diary: [DiaryScreen] (Screen으로 전환)
 *   - Bag: [BagScreen]
 *   - Calculate: [CalculateScreen] (Screen으로 전환)
 * - 하단: [BottomMenuBarNew] (항상 표시)
 */
@Composable
fun FarmScreen(onMenuClick: () -> Unit = {}, showToast: (String) -> Unit = {}) {
	// Koin을 통해 싱글톤 ViewModel 주입 (앱 실행 중 튜토리얼 상태 유지 등의 역할)
	val viewModel = koinInject<FarmViewModel>()

	// StateFlow 구독 (Compose State로 변환)
	val showTutorial by viewModel.showTutorial.collectAsState()
	val brokers by viewModel.brokers.collectAsState()
	val selectedBroker by viewModel.selectedBroker.collectAsState()
	val currentSeeds by viewModel.currentSeeds.collectAsState()
	val summaryInfo by viewModel.summaryInfo.collectAsState()

	// 네비게이션 상태 관리
	var currentDestination by remember { mutableStateOf(FarmDestination.Main) }

	// 뒤로가기 핸들링: 메인 화면이 아닐 때 뒤로가기 누르면 메인으로 복귀
	BackHandler(enabled = currentDestination != FarmDestination.Main) {
		currentDestination = FarmDestination.Main
	}

	// 오버레이 및 팝업 상태 관리 변수들 (화면 전환 없이 위에 띄움)
	var selectedSeed by remember { mutableStateOf<FarmSeed?>(null) } // 선택된 종목(씨앗) -> 상세 화면 트리거
    var showAddSeed by remember { mutableStateOf(false) } // 씨앗 추가 화면 표시 여부

	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(FarmColors.getSky()) // 전체 배경색은 하늘색으로 시작
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = {}
			)
	) {
		Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {

			// 메인 컨텐츠 영역 (탭바 제외하고 나머지 공간 차지)
			Box(modifier = Modifier.weight(1f)) {
				AnimatedContent(
					targetState = currentDestination,
					transitionSpec = {
						fadeIn(tween(300)) +
							scaleIn(initialScale = 0.95f, animationSpec = tween(300)) togetherWith fadeOut(tween(300))
					},
					label = "FarmTabTransition"
				) { targetState ->
					when (targetState) {
						FarmDestination.Main -> {
							Column(modifier = Modifier.fillMaxSize()) {
								// 1. 하늘 섹션
								SkySection(onMenuClick = onMenuClick)

								// 2. 브로커 탭
								BrokerTabs(
									brokers = brokers,
									selectedBroker = selectedBroker,
									onBrokerSelected = { viewModel.selectBroker(it) },
									onAddBroker = { viewModel.addBroker(it) },
									onEditBroker = { broker, newName ->
										viewModel.updateBroker(broker, newName)
									},
									onDeleteBroker = { viewModel.deleteBroker(it) }
								)

								// 3. 농장 영역
								Column(
									modifier = Modifier
										.weight(1f)
										.fillMaxWidth()
										.background(FarmColors.getGround())
										.padding(horizontal = 12.dp)
								) {
									Spacer(modifier = Modifier.height(8.dp))
									SummaryBarNew(summaryInfo)
									Spacer(modifier = Modifier.height(8.dp))
									FieldGridNew(
                                        seeds = currentSeeds, 
                                        onSeedClick = { selectedSeed = it },
                                        onAddSeedClick = { 
                                            // 브로커가 선택되어 있어야 추가 가능
                                            if (selectedBroker != null) {
                                                showAddSeed = true 
                                            } else {
                                                showToast("증권사를 먼저 선택해주세요 😅")
                                            }
                                        }
                                    )
								}
							}
						}

						FarmDestination.Warehouse -> {
							WarehouseScreen(onDismiss = { currentDestination = FarmDestination.Main })
						}

						FarmDestination.Diary -> {
							DiaryScreen(onDismiss = { currentDestination = FarmDestination.Main })
						}

						FarmDestination.Bag -> {
							BagScreen(onDismiss = { currentDestination = FarmDestination.Main })
						}

						FarmDestination.Calculate -> {
							CalculateScreen(onDismiss = { currentDestination = FarmDestination.Main })
						}
					}
				}
			}

			// 4. 하단 메뉴바 (항상 표시, 탭 상태 연동)
			BottomMenuBarNew(
				currentDestination = currentDestination,
				onTabSelected = { newDest -> currentDestination = newDest }
			)
		}

		// --- 오버레이 정의 영역 (Z-Index 순서상 위에 그려짐) ---
		// 하단 탭 메뉴와 무관한(전역적) 오버레이들만 남김

		// 튜토리얼 오버레이
		AnimatedVisibility(
			visible = showTutorial,
			enter = fadeIn() + scaleIn(initialScale = 0.9f),
			exit = fadeOut() + scaleOut(targetScale = 0.9f)
		) {
			TutorialModal(onDismiss = { viewModel.completeTutorial() })
		}

		// 상세 정보 오버레이 (Grid 아이템 클릭 시)
		AnimatedVisibility(
			visible = selectedSeed != null,
			enter = slideInVertically(initialOffsetY = { it }),
			exit = slideOutVertically(targetOffsetY = { it })
		) {
			selectedSeed?.let { seed ->
				DetailScreen(seed = seed, onDismiss = { selectedSeed = null })
			}
		}

        // 씨앗 추가 오버레이
        if (showAddSeed) {
            AddSeedOverlay(
                onDismiss = { showAddSeed = false },
                onAdd = { newSeed ->
                    viewModel.addSeed(newSeed)
                    showAddSeed = false
                }
            )
        }
	}
}
