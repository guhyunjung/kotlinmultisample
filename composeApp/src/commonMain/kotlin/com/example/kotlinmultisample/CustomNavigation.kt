package com.example.kotlinmultisample

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun CustomAnimatedNavigationBar(
    destinations: List<AppDestinations>,
    currentDestination: AppDestinations,
    onDestinationSelected: (AppDestinations) -> Unit
) {
    var selectedItemIndex = destinations.indexOf(currentDestination)
    // 각 아이템의 가로 길이를 저장할 리스트 (균등 분배 가정시 필요 없을 수도 있음)
    // 간단한 예시를 위해 1/N 등분한다고 가정하거나, BoxWithConstraints 사용

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        BoxWithConstraints(modifier = Modifier.weight(1f)) {
            val destinationList = destinations // to avoid smart cast issues or recomposition
            val count = destinationList.size
            if (count == 0) return@BoxWithConstraints

            val validIndex = if (selectedItemIndex >= 0) selectedItemIndex else 0
            val itemWidth = maxWidth / count

            // 움직이는 인디케이터 (The sliding pill)
            val indicatorOffset by animateDpAsState(
                targetValue = itemWidth * validIndex,
                animationSpec = tween(durationMillis = 300)
            )

            Box(
                modifier = Modifier
                    .offset(x = indicatorOffset)
                    .width(itemWidth)
                    .fillMaxHeight()
                    .padding(vertical = 12.dp, horizontal = 20.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    // .zIndex(0f)
            )

            // 실제 아이콘과 텍스트 (Items)
            Row(modifier = Modifier.fillMaxSize()) {
                destinationList.forEach { destination ->
                    val isSelected = currentDestination == destination
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { onDestinationSelected(destination) },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = destination.label,
                                tint = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                                       else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = destination.label,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                                        else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}


