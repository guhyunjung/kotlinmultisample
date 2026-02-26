package com.example.kotlinmultisample

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import kotlinmultisample.composeapp.generated.resources.Res
import kotlinmultisample.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource

@Composable
@Preview
fun App() {
	MaterialTheme {
		var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

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
									tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
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
				ScaffoldContent(destination)
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

enum class AppDestinations(
	val label: String,
	val icon: ImageVector
) {
	HOME(
		label = "Home",
		icon = Icons.Default.Home
	),
	FAVORITES(
		label = "Favorites",
		icon = Icons.Default.Favorite
	),
	PROFILE(
		label = "Profile",
		icon = Icons.Default.AccountBox
	)
}