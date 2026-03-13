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
import com.example.kotlinmultisample.app.ui.screen.*
import com.example.kotlinmultisample.shared.domain.model.Country
import com.example.kotlinmultisample.shared.domain.repository.CountryRepository
import com.example.kotlinmultisample.simple.FruitScreen
import com.example.kotlinmultisample.simple.FruitViewModel
import com.example.kotlinmultisample.shared.network.ConnectivityObserver
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
	SETTINGS("Settings", Icons.Default.Settings),
	PROFILE("Profile", Icons.Default.AccountBox)
}

@Composable
fun App() {
	// 테마 설정을 위한 ViewModel 주입
	val settingsViewModel = koinInject<SettingsViewModel>()
	val themeMode by settingsViewModel.themeMode.collectAsState()
	val isSystemDark = isSystemInDarkTheme()

	val isDarkTheme = when (themeMode) {
		ThemeMode.SYSTEM -> isSystemDark
		ThemeMode.LIGHT -> false
		ThemeMode.DARK -> true
	}

	MaterialTheme(
		colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
	) {
		// 앱 실행 준비 상태 (스플래시 화면 표시 여부)
		var isAppReady by rememberSaveable { mutableStateOf(false) }

		// 데이터 프리로딩을 위한 Repository 주입
		val countryRepository = koinInject<CountryRepository>()

		// 초기 데이터 로딩 시뮬레이션
		LaunchedEffect(Unit) {

			// 실제 데이터 로딩: 캐시된 데이터가 있는지 확인하거나 API 호출을 미리 시도
			try {
				// 첫 화면에 필요한 데이터를 미리 로드하여 캐시에 저장해둡니다.
				// 이 호출은 네트워크/DB 상황에 따라 시간이 걸릴 수 있습니다.
				countryRepository.getCountries()
			} catch (_: Exception) {
				// 에러 무시 (메인 화면에서 다시 처리)
			} finally {
				// 최소 노출 시간 보장 (너무 빨리 지나가면 깜빡임처럼 보임)
				delay(1500)
				isAppReady = true
			}
		}

		// 스플래시 화면과 메인 화면 전환 애니메이션
		AnimatedContent(
			targetState = isAppReady,
			transitionSpec = {
				// 스플래시가 사라질 때: 페이드 아웃 + 위로 살짝 이동
				fadeIn(animationSpec = tween(500)) togetherWith
					fadeOut(animationSpec = tween(500)) + slideOutVertically { -it / 5 }
			},
			label = "SplashScreenTransition"
		) { ready ->
			if (!ready) {
				SplashScreen()
			} else {
				MainContent()
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent() {
    // Koin DI를 통해 현재 플랫폼에 맞는 ConnectivityObserver 구현체를 주입받습니다.
    val connectivityObserver = koinInject<ConnectivityObserver>()
    
    // Connectivity Flow를 관찰하여 UI 상태(State)로 변환합니다. 초기값은 'Available'로 설정합니다.
    val status by connectivityObserver.observe().collectAsState(initial = ConnectivityObserver.Status.Available)
    
    // Snackbar를 표시하기 위한 HostState 생성
    val snackbarHostState = remember { SnackbarHostState() }

    // 네트워크 상태(status)가 변경될 때마다 실행되는 로직
    LaunchedEffect(status) {
        // 연결이 불안정하거나 끊긴 경우 사용자에게 알림
        if (status == ConnectivityObserver.Status.Unavailable || status == ConnectivityObserver.Status.Lost) {
            snackbarHostState.showSnackbar(
                message = "인터넷 연결을 확인해주세요.",
                duration = SnackbarDuration.Indefinite, // 사용자가 확인하거나 연결될 때까지 유지
                withDismissAction = true // 닫기 버튼 표시
            )
        }
    }

	var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
	var selectedCountry by remember { mutableStateOf<Country?>(null) }

	// 드로어 상태 관리
	val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
	val scope = rememberCoroutineScope()

	val openDrawer: () -> Unit = { scope.launch { drawerState.open() } }

	// 국가 상세 화면이 열려 있으면 상세 화면만 표시
	if (selectedCountry != null && currentDestination == AppDestinations.COUNTRIES) {
		CountryDetailScreen(
			country = selectedCountry!!,
			onBack = { selectedCountry = null }
		)
		return
	}

	ModalNavigationDrawer(
		drawerState = drawerState,
		drawerContent = {
			AppDrawerContent(
				currentRoute = currentDestination.label,
				onDestinationClick = { label ->
					val destination = AppDestinations.entries.firstOrNull { it.label == label }
					if (destination != null) currentDestination = destination
					scope.launch { drawerState.close() }
				}
			)
		}
	) {
		Scaffold(
			// 상단 상태 표시줄(Status Bar) 영역은 각 화면(HomeScreen 등)의 TopAppBar에서 처리하도록
			// 외부 Scaffold에서는 상단 Inset을 제외합니다. (중복 패딩 방지)
			contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom),
            snackbarHost = { SnackbarHost(snackbarHostState) },
			bottomBar = {
				AppBottomNavigationBar(
					currentDestination = currentDestination,
					onDestinationClick = { currentDestination = it }
				)
			}
		) { innerPadding ->
			Box(modifier = Modifier.padding(innerPadding)) {
				AnimatedContent(
					targetState = currentDestination,
					transitionSpec = {
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
		AppDestinations.entries.forEach { destination ->
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
				navigationIcon = {
					IconButton(onClick = onMenuClick) {
						Icon(Icons.Default.Menu, contentDescription = "메뉴 열기")
					}
				},
				colors = TopAppBarDefaults.topAppBarColors(
					containerColor = MaterialTheme.colorScheme.primaryContainer,
					titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
					navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
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
