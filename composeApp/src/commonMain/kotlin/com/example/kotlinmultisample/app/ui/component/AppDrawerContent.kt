package com.example.kotlinmultisample.app.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 앱 공통 드로어(Drawer) 컨텐츠 구성 요소
 *
 * 사용자가 사이드바(햄버거 메뉴)를 열었을 때 나타나는 UI입니다.
 * 앱의 주요 섹션으로 이동할 수 있는 네비게이션 메뉴와 헤더, 버전 정보를 포함합니다.
 *
 * @param currentRoute 현재 활성화된 화면의 라벨 (선택된 항목 하이라이트 처리에 사용)
 * @param onDestinationClick 메뉴 항목이 클릭되었을 때 호출되는 콜백 (선택된 라벨 전달)
 */
@Composable
fun AppDrawerContent(
    currentRoute: String,
    onDestinationClick: (String) -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier.widthIn(max = 280.dp) // 드로어의 최대 너비 제한
    ) {
        // ── 드로어 상단 헤더 섹션 ──────────────────────────────────────────
        DrawerHeader()

        // 메뉴 항목과의 구분을 위한 간격 및 구분선
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(8.dp))

        // ── 주요 네비게이션 메뉴 항목 ────────────────────────────────────────
        // 각 항목은 DrawerMenuItem 컴포저블을 사용하여 일관된 디자인 유지
        DrawerMenuItem(
            icon = Icons.Default.Home,
            label = "Home",
            isSelected = currentRoute == "Home",
            onClick = { onDestinationClick("Home") }
        )
        DrawerMenuItem(
            icon = Icons.Default.Place,
            label = "Countries",
            isSelected = currentRoute == "Countries",
            onClick = { onDestinationClick("Countries") }
        )
        DrawerMenuItem(
            icon = Icons.Default.Favorite,
            label = "Simple",
            isSelected = currentRoute == "Simple",
            onClick = { onDestinationClick("Simple") }
        )
        DrawerMenuItem(
            icon = Icons.Default.Settings,
            label = "Settings",
            isSelected = currentRoute == "Settings",
            onClick = { onDestinationClick("Settings") }
        )
        DrawerMenuItem(
            icon = Icons.Default.AccountBox,
            label = "Profile",
            isSelected = currentRoute == "Profile",
            onClick = { onDestinationClick("Profile") }
        )

        // 하단 버전 정보를 바닥에 붙이기 위해 남은 공간을 모두 차지하도록 함
        Spacer(modifier = Modifier.weight(1f))

        // ── 드로어 하단 정보 섹션 ──────────────────────────────────────────
        HorizontalDivider()
        Text(
            text = "Kotlin Multiplatform v1.0",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(16.dp)
        )
    }
}

/**
 * 드로어 상단에 표시되는 헤더 컴포저블
 *
 * 앱 로고 역할을 하는 아이콘과 앱 이름, 간단한 설명을 표시합니다.
 */
@Composable
private fun DrawerHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // 앱 로고 아이콘 배경 (KM 텍스트 포함)
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(56.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "KM",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        // 앱 메인 타이틀
        Text(
            text = "KotlinMultiSample",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        // 앱 서브 타이틀 또는 설명
        Text(
            text = "Compose Multiplatform",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * 드로어 내의 개별 메뉴 항목을 구성하는 컴포저블
 *
 * @param icon 메뉴 옆에 표시될 아이콘
 * @param label 메뉴 이름
 * @param isSelected 현재 이 항목이 선택된 상태인지 여부
 * @param onClick 항목 클릭 시 실행될 액션
 */
@Composable
private fun DrawerMenuItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = label // 접근성을 위한 설명
            )
        },
        label = { Text(label) },
        selected = isSelected,
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
    )
}

