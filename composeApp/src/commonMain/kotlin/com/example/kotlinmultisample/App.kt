package com.example.kotlinmultisample

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.kotlinmultisample.app.presentation.country.CountryViewModel
import com.example.kotlinmultisample.app.ui.screen.CountryDetailScreen
import com.example.kotlinmultisample.app.ui.screen.CountryListScreen
import com.example.kotlinmultisample.app.ui.screen.SplashScreen
import com.example.kotlinmultisample.shared.domain.model.Country
import com.example.kotlinmultisample.shared.domain.repository.CountryRepository
import kotlinmultisample.composeapp.generated.resources.Res
import kotlinmultisample.composeapp.generated.resources.compose_multiplatform
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

enum class AppDestinations(
    val label: String,
    val icon: ImageVector
) {
    HOME("Home", Icons.Default.Home),
    COUNTRIES("Countries", Icons.Default.Place),
    FAVORITES("Favorites", Icons.Default.Favorite),
    PROFILE("Profile", Icons.Default.AccountBox)
}

@Composable
fun App() {
    MaterialTheme {
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

@Composable
fun MainContent() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
    // 국가 상세 화면 상태 (null이면 목록, 값이 있으면 상세)
    var selectedCountry by remember { mutableStateOf<Country?>(null) }

    // 국가 상세 화면이 열려 있으면 상세 화면 표시
    if (selectedCountry != null && currentDestination == AppDestinations.COUNTRIES) {
         CountryDetailScreen(
            country = selectedCountry!!,
            onBack = { selectedCountry = null }
        )
        return
    }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach { destination ->
                val isSelected = currentDestination == destination
                item(
                    icon = {
                        // 아이콘 선택 시 크기 변화 애니메이션 (Scale 효과)
                        // 슬라이딩보다 이쪽이 NavigationBar UX에 더 자연스럽습니다.
                        AnimatedContent(
                            targetState = isSelected,
                            transitionSpec = {
                                if (targetState) {
                                    // 선택될 때: 커지면서 등장
                                    (scaleIn() + fadeIn()) togetherWith (fadeOut())
                                } else {
                                    // 선택 해제될 때: 작아지면서 퇴장
                                    (scaleIn() + fadeIn()) togetherWith (scaleOut() + fadeOut())
                                }
                            }
                        ) { selected ->
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = destination.label,
                                // 선택된 아이콘은 색상을 다르게 하거나 채워진 아이콘 사용 가능
                                tint = if (selected) MaterialTheme.colorScheme.primary
                                       else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    label = { Text(destination.label) },
                    selected = isSelected,
                    onClick = { currentDestination = destination }
                )
            }
        }
    ) {
        AnimatedContent(
            targetState = currentDestination,
            transitionSpec = {
                val direction = if (targetState.ordinal > initialState.ordinal) {
                    // 오른쪽(다음) 탭으로 갈 때: 오른쪽에서 들어오고 왼쪽으로 나감
                    slideInHorizontally { width -> width } + fadeIn() togetherWith
                            slideOutHorizontally { width -> -width } + fadeOut()
                } else {
                    // 왼쪽(이전) 탭으로 갈 때: 왼쪽에서 들어오고 오른쪽으로 나감
                    slideInHorizontally { width -> -width } + fadeIn() togetherWith
                            slideOutHorizontally { width -> width } + fadeOut()
                }
                direction.using(SizeTransform(clip = false))
            },
            label = "NavigationTransition"
        ) { destination ->
            when (destination) {
                AppDestinations.COUNTRIES -> {
                    val viewModel = koinViewModel<CountryViewModel>()
                    CountryListScreen(
                        viewModel = viewModel,
                        onCountryClick = { country ->
                            selectedCountry = country
                        }
                    )
                }
                else -> ScaffoldContent(destination)
            }
        }
    }
}

@Composable
fun ScaffoldContent(destination: AppDestinations) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        var showContent by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // 미사용 파라미터 경고 해결: destination.label 표시
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
