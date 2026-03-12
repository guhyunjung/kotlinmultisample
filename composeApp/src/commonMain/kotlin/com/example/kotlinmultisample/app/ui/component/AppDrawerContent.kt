package com.example.kotlinmultisample.app.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 앱 공통 드로어(Drawer) 컨텐츠
 *
 * 햄버거 버튼 클릭 시 나타나는 사이드 메뉴입니다.
 * [ModalNavigationDrawer]의 drawerContent 슬롯에 사용합니다.
 *
 * @param currentRoute 현재 선택된 탭 라벨 (하이라이트 처리에 사용)
 * @param onDestinationClick 메뉴 항목 클릭 시 콜백 (라벨 전달)
 */
@Composable
fun AppDrawerContent(
    currentRoute: String,
    onDestinationClick: (String) -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier.widthIn(max = 280.dp)
    ) {
        // ── 드로어 헤더 ──────────────────────────────────────────────────────
        DrawerHeader()

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(8.dp))

        // ── 네비게이션 항목 ──────────────────────────────────────────────────
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

        Spacer(modifier = Modifier.weight(1f))

        // ── 드로어 하단 버전 정보 ──────────────────────────────────────────
        HorizontalDivider()
        Text(
            text = "Kotlin Multiplatform v1.0",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(16.dp)
        )
    }
}

// ── 드로어 헤더 ───────────────────────────────────────────────────────────────

@Composable
private fun DrawerHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
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
        Text(
            text = "KotlinMultiSample",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Compose Multiplatform",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// ── 드로어 메뉴 항목 ──────────────────────────────────────────────────────────

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
                contentDescription = label
            )
        },
        label = { Text(label) },
        selected = isSelected,
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
    )
}

