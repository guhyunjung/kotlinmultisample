package com.example.kotlinmultisample.app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 홈 화면 컴포저블
 *
 * 앱의 메인 진입점으로, 간단한 환영 메시지와 다른 주요 섹션으로의 바로가기 카드를 제공합니다.
 *
 * @param onMenuClick 햄버거 버튼 클릭 시 사이드 네비게이션 드로어를 여는 콜백
 * @param onNavigateToCountries '국가 목록' 섹션으로 이동하는 콜백
 * @param onNavigateToSimple 'Simple MVVM' 섹션으로 이동하는 콜백
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onMenuClick: () -> Unit,
    onNavigateToCountries: () -> Unit,
    onNavigateToSimple: () -> Unit
) {
    Scaffold(
        topBar = {
            // 상단 앱 바 설정
            TopAppBar(
                title = { Text("홈") },
                navigationIcon = {
                    // 메뉴 열기 버튼 (햄버거 아이콘)
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "메뉴 열기"
                        )
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
        // 메인 컨텐츠 영역: 스크롤 가능한 컬럼 레이아웃
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()) // 컨텐츠가 많아질 경우를 대비한 스크롤 설정
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. 환영 배너 섹션
            WelcomeBanner()

            // 2. 바로가기 메뉴 섹션 타이틀
            Text(
                text = "바로가기",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            // 3. 국가 목록 바로가기 카드
            HomeNavigationCard(
                title = "국가 목록",
                description = "세계 각국의 정보와 국기를 확인하고 검색해보세요.",
                icon = Icons.Default.Place,
                onClick = onNavigateToCountries
            )

            // 4. Simple MVVM 예제 바로가기 카드
            HomeNavigationCard(
                title = "Simple MVVM",
                description = "간단한 MVVM 패턴 예제(과일 목록)를 확인해보세요.",
                icon = Icons.Default.Favorite,
                onClick = onNavigateToSimple
            )
        }
    }
}

/**
 * 사용자 환영 메시지를 표시하는 배너 카드
 */
@Composable
private fun WelcomeBanner() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "안녕하세요 👋",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                text = "Kotlin Multiplatform 샘플 앱입니다.\n하단 탭 또는 좌측 메뉴에서 각 섹션으로 이동할 수 있습니다.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

/**
 * 다른 화면으로 이동하기 위한 공통 카드 레이아웃
 *
 * @param title 카드 제목
 * @param description 카드에 대한 보조 설명
 * @param icon 왼쪽에 표시될 아이콘
 * @param onClick 카드 클릭 시 동작
 */
@Composable
private fun HomeNavigationCard(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick, // 카드 전체 클릭 가능
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 아이콘 배경 및 아이콘
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null, // 장식용 아이콘이므로 설명 생략
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            // 텍스트 정보 섹션
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

