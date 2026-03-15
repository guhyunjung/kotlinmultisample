package com.example.kotlinmultisample

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.kotlinmultisample.app.presentation.country.CountryViewModel
import com.example.kotlinmultisample.app.presentation.settings.SettingsViewModel
import com.example.kotlinmultisample.app.presentation.settings.ThemeMode
import com.example.kotlinmultisample.app.ui.component.AppDrawerContent
import com.example.kotlinmultisample.app.ui.screen.HomeScreen
import com.example.kotlinmultisample.app.ui.screen.SettingsScreen
import com.example.kotlinmultisample.app.ui.screen.SplashScreen
import com.example.kotlinmultisample.app.ui.screen.country.CountryDetailScreen
import com.example.kotlinmultisample.app.ui.screen.country.CountryListScreen
import com.example.kotlinmultisample.app.ui.screen.farm.FarmScreen
import com.example.kotlinmultisample.shared.domain.model.Country
import com.example.kotlinmultisample.shared.domain.repository.CountryRepository
import com.example.kotlinmultisample.shared.network.ConnectivityObserver
import com.example.kotlinmultisample.simple.FruitScreen
import com.example.kotlinmultisample.simple.FruitViewModel
import kotlinmultisample.composeapp.generated.resources.Res
import kotlinmultisample.composeapp.generated.resources.compose_multiplatform
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

enum class AppDestinations(
	val label: String,
	val icon: ImageVector
) {
	HOME("Home", Icons.Default.Home),
	COUNTRIES("Countries", Icons.Default.Place),
	FAVORITES("Simple", Icons.Default.Favorite),
	FARM("Farm", Icons.Default.Face),
	SETTINGS("Settings", Icons.Default.Settings),
	PROFILE("Profile", Icons.Default.AccountBox)
}

/**
 * 앱의 메인 엔트리 포인트 컴포저블
 *
 * 이 함수는 앱의 전체적인 테마 설정, 초기 데이터 로딩(스플래시 화면),
 * 그리고 메인 화면으로의 전환을 관리합니다.
 */
@Composable
fun App() {
	// Koin을 통해 설정 관련 ViewModel 주입 (다크모드 설정 등)
	val settingsViewModel = koinInject<SettingsViewModel>()
	val themeMode by settingsViewModel.themeMode.collectAsState()
	val isSystemDark = isSystemInDarkTheme()

	// 현재 테마 모드에 따른 다크모드 여부 결정
	val isDarkTheme = when (themeMode) {
		ThemeMode.SYSTEM -> isSystemDark
		ThemeMode.LIGHT -> false
		ThemeMode.DARK -> true
	}

	// Material3 테마 적용
	MaterialTheme(
		colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
	) {
		// 앱 실행 준비 상태 (false: 스플래시 표시, true: 메인 컨텐츠 표시)
		var isAppReady by rememberSaveable { mutableStateOf(false) }

		// 프리로딩을 위한 Repository 주입
		val countryRepository = koinInject<CountryRepository>()

		// 앱 초기화 로직: 데이터 프리로딩 및 지연 시간 부여
		LaunchedEffect(Unit) {
			try {
				// 초기 화면에 필요한 국가 데이터를 미리 로드하여 캐시
				countryRepository.getCountries()
			} catch (_: Exception) {
				// 로딩 중 발생한 에러는 여기서 잡고 메인 화면에서 재처리 가능하도록 함
			} finally {
				// 스플래시 화면이 너무 순식간에 지나가지 않도록 최소 노출 시간(1.5초) 보장
				delay(1500)
				isAppReady = true
			}
		}

		// 스플래시 화면에서 메인 화면으로 전환될 때의 애니메이션 처리
		AnimatedContent(
			targetState = isAppReady,
			transitionSpec = {
				// 나타날 때 페이드 인, 사라질 때 페이드 아웃 + 위로 살짝 이동
				fadeIn(animationSpec = tween(500)) togetherWith
					fadeOut(animationSpec = tween(500)) + slideOutVertically { -it / 5 }
			},
			label = "SplashScreenTransition"
		) { ready ->
			if (!ready) {
				SplashScreen() // 로딩 중 표시될 화면
			} else {
				MainContent() // 로딩 완료 후 표시될 메인 구조
			}
		}
	}
}

/**
 * 앱의 실질적인 메인 구조를 담당하는 컴포저블
 *
 * 네비게이션 드로어, 상단 바, 하단 바, 그리고 각 화면 간의 전환 애니메이션을 포함합니다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent() {
    // 네트워크 상태 감시자 주입 (플랫폼별 구현체 사용)
    val connectivityObserver = koinInject<ConnectivityObserver>()
    
    // 네트워크 연결 상태를 관찰하여 UI 상태로 변환
    val status by connectivityObserver.observe().collectAsState(initial = ConnectivityObserver.Status.Available)
    
    // 하단 스낵바 표시를 위한 상태 관리
    val snackbarHostState = remember { SnackbarHostState() }

    // 네트워크 상태 변경 시 사용자에게 알림 메시지 표시
    LaunchedEffect(status) {
        if (status == ConnectivityObserver.Status.Unavailable || status == ConnectivityObserver.Status.Lost) {
            snackbarHostState.showSnackbar(
                message = "인터넷 연결을 확인해주세요.",
                duration = SnackbarDuration.Indefinite,
                withDismissAction = true
            )
        }
    }

	// 현재 선택된 네비게이션 목적지 상태
	var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
	// 국가 목록에서 선택된 상세 국가 정보 (null이면 목록 표시)
	var selectedCountry by remember { mutableStateOf<Country?>(null) }

	// 네비게이션 드로어(사이드 메뉴) 상태 및 제어 스코프
	val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
	val scope = rememberCoroutineScope()

	// 드로어 열기 함수 (코루틴 내에서 실행 필요)
	val openDrawer: () -> Unit = { scope.launch { drawerState.open() } }

	// [비즈니스 로직] 국가 상세 화면이 활성화된 경우 (목록보다 우선순위 높음)
	if (selectedCountry != null && currentDestination == AppDestinations.COUNTRIES) {
		CountryDetailScreen(
			country = selectedCountry!!,
			onBack = { selectedCountry = null }
		)
		return
	}

	// 메인 레이아웃: 드로어 내부에 스캐폴드 배치
	ModalNavigationDrawer(
		drawerState = drawerState,
		drawerContent = {
			// 사이드 메뉴 컨텐츠
			AppDrawerContent(
				currentRoute = currentDestination.label,
				onDestinationClick = { label ->
					// 클릭된 메뉴의 라벨에 맞는 목적지 탐색 및 변경
					val destination = AppDestinations.entries.firstOrNull { it.label == label }
					if (destination != null) currentDestination = destination
					scope.launch { drawerState.close() }
				}
			)
		}
	) {
		Scaffold(
			// 시스템 바 인셋 처리 (중복 패딩 방지)
			contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom),
            snackbarHost = { SnackbarHost(snackbarHostState) },
			bottomBar = {
				// 하단 네비게이션 탭
				AppBottomNavigationBar(
					currentDestination = currentDestination,
					onDestinationClick = { 
						currentDestination = it
						// 탭 변경 시 선택된 국가 상세 정보는 초기화
						if (it != AppDestinations.COUNTRIES) selectedCountry = null
					}
				)
			}
		) { innerPadding ->
			// 메인 화면 영역 (화면 전환 애니메이션 적용)
			Box(modifier = Modifier.padding(innerPadding)) {
				AnimatedContent(
					targetState = currentDestination,
					transitionSpec = {
						// 인덱스 비교를 통해 왼쪽/오른쪽 슬라이드 방향 결정
						val direction = if (targetState.ordinal > initialState.ordinal) {
							slideInHorizontally { width -> width } + fadeIn() togetherWith
								slideOutHorizontally { width -> -width } + fadeOut()
						} else {
							slideInHorizontally { width -> -width } + fadeIn() togetherWith
								slideOutHorizontally { width -> width } + fadeOut()
						}
						direction.using(SizeTransform(clip = false))
					},
					label = "NavigationTransition"
				) { destination ->
					// 목적지에 따른 화면 렌더링
					when (destination) {
						AppDestinations.HOME -> {
							HomeScreen(
								onMenuClick = openDrawer,
								onNavigateToCountries = { currentDestination = AppDestinations.COUNTRIES },
								onNavigateToSimple = { currentDestination = AppDestinations.FAVORITES }
							)
						}

						AppDestinations.COUNTRIES -> {
							val viewModel = koinViewModel<CountryViewModel>()
							CountryListScreen(
								viewModel = viewModel,
								onMenuClick = openDrawer,
								onCountryClick = { country -> selectedCountry = country }
							)
						}

						AppDestinations.FAVORITES -> {
							val viewModel = koinViewModel<FruitViewModel>()
							FruitScreen(
								viewModel = viewModel,
								onMenuClick = openDrawer
							)
						}

						AppDestinations.FARM -> {
							FarmScreen(onMenuClick = openDrawer)
						}

						AppDestinations.SETTINGS -> {
							SettingsScreen(onMenuClick = openDrawer)
						}

						else -> ScaffoldContent(destination, onMenuClick = openDrawer)
					}
				}
			}
		}
	} // ModalNavigationDrawer 끝
}

// ── 하단 네비게이션 바 ─────────────────────────────────────────────────────────

@Composable
private fun AppBottomNavigationBar(
	currentDestination: AppDestinations,
	onDestinationClick: (AppDestinations) -> Unit
) {
	NavigationBar {
		// Settings는 하단 탭에서 제외하고 햄버거 메뉴(Drawer)에서만 접근 가능하도록 필터링
		AppDestinations.entries.filter { it != AppDestinations.SETTINGS }.forEach { destination ->
			val isSelected = currentDestination == destination
			NavigationBarItem(
				icon = {
					AnimatedContent(
						targetState = isSelected,
						transitionSpec = {
							if (targetState) {
								(scaleIn() + fadeIn()) togetherWith fadeOut()
							} else {
								(scaleIn() + fadeIn()) togetherWith (scaleOut() + fadeOut())
							}
						},
						label = "NavIconAnim_${destination.label}"
					) { selected ->
						Icon(
							imageVector = destination.icon,
							contentDescription = destination.label,
							tint = if (selected) MaterialTheme.colorScheme.primary
							else MaterialTheme.colorScheme.onSurfaceVariant
						)
					}
				},
				label = { Text(destination.label) },
				selected = isSelected,
				onClick = { onDestinationClick(destination) }
			)
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldContent(destination: AppDestinations, onMenuClick: () -> Unit) {
	Scaffold(
		modifier = Modifier.fillMaxSize(),
		topBar = {
			TopAppBar(
				title = { Text(destination.label) },
				// navigationIcon에서 제거
				actions = {
					// 햄버거 메뉴를 actions (우측)으로 이동
					IconButton(onClick = onMenuClick) {
						Icon(Icons.Default.Menu, contentDescription = "메뉴 열기")
					}
				},
				colors = TopAppBarDefaults.topAppBarColors(
					containerColor = MaterialTheme.colorScheme.primaryContainer,
					titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
					actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer // actions 색상 지정
				)
			)
		}
	) { innerPadding ->
		var showContent by remember { mutableStateOf(false) }
		Column(
			modifier = Modifier
				.background(MaterialTheme.colorScheme.primaryContainer)
				.safeContentPadding()
				.fillMaxSize()
				.padding(innerPadding),
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			Text(
				text = "${destination.label} Screen",
				style = MaterialTheme.typography.titleLarge,
				modifier = Modifier.padding(16.dp)
			)

			Button(onClick = { showContent = !showContent }) {
				Text("Click me!")
			}
			AnimatedVisibility(showContent) {
				val greeting = remember { Greeting().greet() }
				Column(
					modifier = Modifier.fillMaxWidth(),
					horizontalAlignment = Alignment.CenterHorizontally,
				) {
					Image(painterResource(Res.drawable.compose_multiplatform), null)
					Text("Compose: $greeting")
				}
			}
		}
	}
}
