package com.example.kotlinmultisample

import androidx.compose.animation.AnimatedVisibility
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
				AppDestinations.entries.forEach {
					item(
						icon = { Icon(it.icon, contentDescription = it.label) },
						label = { Text(it.label) },
						selected = currentDestination == it,
						onClick = { currentDestination = it }
					)
				}
			}
		) {
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