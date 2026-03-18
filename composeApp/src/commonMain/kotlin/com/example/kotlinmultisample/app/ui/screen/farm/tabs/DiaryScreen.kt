package com.example.kotlinmultisample.app.ui.screen.farm.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlinmultisample.app.presentation.farm.FarmViewModel
import com.example.kotlinmultisample.app.ui.screen.farm.components.PixelButton
import com.example.kotlinmultisample.app.ui.screen.farm.components.PixelCard
import com.example.kotlinmultisample.app.ui.screen.farm.model.DiaryUiModel
import com.example.kotlinmultisample.app.ui.screen.farm.model.DiaryType
import com.example.kotlinmultisample.app.ui.screen.farm.theme.FarmColors
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.koinInject

/**
 * 농부의 일기 (메모장) 오버레이
 * 투자 아이디어, 매매 원칙 등을 기록하는 공간입니다.
 * CRUD 기능을 포함합니다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryScreen(
	onDismiss: () -> Unit,
	viewModel: FarmViewModel = koinInject() // ViewModel 주입
) {
	val diaryEntries by viewModel.diaryEntries.collectAsState()

	// 편집 모드 상태 (null이면 리스트 모드, 객체가 있으면 편집/추가 모드)
	var editingEntry by remember { mutableStateOf<DiaryUiModel?>(null) }

	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(FarmColors.getNight()) // 밤하늘 배경색
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = {}
			)
			.statusBarsPadding()
			.padding(16.dp)
	) {
		if (editingEntry != null) {
			// 편집/추가 화면
			DiaryEditor(
				entry = editingEntry!!,
				onSave = {
					if (it.id == 0L) viewModel.addDiary(it) else viewModel.updateDiary(it)
					editingEntry = null
				},
				onCancel = { editingEntry = null },
				onDelete = {
					if (it.id != 0L) viewModel.deleteDiary(it.id)
					editingEntry = null
				}
			)
		} else {
			// 리스트 화면
			DiaryList(
				entries = diaryEntries,
				onDismiss = onDismiss,
				onAddClick = {
					// 새 항목 생성 (오늘 날짜 기본)
					val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
					editingEntry = DiaryUiModel(id = 0, date = today, content = "", type = DiaryType.DAILY)
				},
				onEntryClick = { editingEntry = it }
			)
		}
	}
}

@Composable
fun DiaryList(
	entries: List<DiaryUiModel>,
	onDismiss: () -> Unit,
	onAddClick: () -> Unit,
	onEntryClick: (DiaryUiModel) -> Unit
) {
	Column(
		modifier = Modifier.fillMaxSize()
	) {
		// 헤더
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.fillMaxWidth()
		) {
			PixelButton(
				"←",
				onClick = onDismiss,
				modifier = Modifier.width(40.dp),
				containerColor = Color(0xFF1A4A6E),
				contentColor = FarmColors.getMoon()
			)
			Spacer(modifier = Modifier.width(16.dp))
			Text("📔 농부의 일기", color = FarmColors.getMoon(), fontWeight = FontWeight.Bold, fontSize = 20.sp)
		}

		Spacer(modifier = Modifier.height(20.dp))

		// 일기 리스트
		LazyColumn(
			modifier = Modifier.weight(1f),
			verticalArrangement = Arrangement.spacedBy(8.dp),
			contentPadding = PaddingValues(bottom = 80.dp) // FAB 공간 확보
		) {
			items(entries.size) { index ->
				DiaryEntryItem(entry = entries[index], onClick = { onEntryClick(entries[index]) })
			}
		}
	}

	// FAB (추가 버튼)
	Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
		FloatingActionButton(
			onClick = onAddClick,
			containerColor = FarmColors.getMoon(),
			contentColor = FarmColors.getNight(),
			modifier = Modifier.padding(16.dp)
		) {
			Icon(Icons.Default.Add, contentDescription = "Add Diary")
		}
	}
}

@Composable
fun DiaryEntryItem(entry: DiaryUiModel, onClick: () -> Unit) {
	val typeColor = when (entry.type) {
		DiaryType.DAILY -> Color(0xFF4FC3F7) // Light Blue
		DiaryType.INTEREST -> Color(0xFFFFB74D) // Orange
		DiaryType.STUDY -> Color(0xFFAED581) // Light Green
	}

	PixelCard(
		modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
		containerColor = Color(0xFF0A1E2D),
		borderColor = Color(0xFF1A3A4D)
	) {
		Column(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalAlignment = Alignment.Start) {
			Row(verticalAlignment = Alignment.CenterVertically) {
				// 타입 뱃지
				Surface(
					color = typeColor.copy(alpha = 0.2f),
					shape = RoundedCornerShape(4.dp),
					modifier = Modifier.padding(end = 8.dp)
				) {
					Text(
						text = entry.type.label,
						color = typeColor,
						fontSize = 10.sp,
						fontWeight = FontWeight.Bold,
						modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
					)
				}
				Text(entry.date, fontSize = 12.sp, color = FarmColors.getMoon())
			}
			Spacer(modifier = Modifier.height(8.dp))
			Text(
				text = entry.content,
				fontSize = 14.sp,
				color = Color(0xFFC8E8FF),
				lineHeight = 20.sp,
				maxLines = 3,
				overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
			)
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryEditor(
	entry: DiaryUiModel,
	onSave: (DiaryUiModel) -> Unit,
	onCancel: () -> Unit,
	onDelete: (DiaryUiModel) -> Unit
) {
	var content by remember { mutableStateOf(entry.content) }
	var selectedType by remember { mutableStateOf(entry.type) }
	var selectedDateStr by remember { mutableStateOf(entry.date) }

	// DatePicker 상태
	var showDatePicker by remember { mutableStateOf(false) }

	if (showDatePicker) {
		val datePickerState = rememberDatePickerState()
		DatePickerDialog(
			onDismissRequest = { showDatePicker = false },
			confirmButton = {
				TextButton(onClick = {
					datePickerState.selectedDateMillis?.let { millis ->
						// Millis to YYYY-MM-DD string
						val instant = Instant.fromEpochMilliseconds(millis)
						val date = instant.toLocalDateTime(TimeZone.UTC).date
						selectedDateStr = date.toString()
					}
					showDatePicker = false
				}) {
					Text("확인")
				}
			},
			dismissButton = {
				TextButton(onClick = { showDatePicker = false }) {
					Text("취소")
				}
			}
		) {
			DatePicker(state = datePickerState)
		}
	}

	Column(
		modifier = Modifier
			.fillMaxSize()
			.verticalScroll(rememberScrollState())
	) {
		// 헤더
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.fillMaxWidth()
		) {
			IconButton(onClick = onCancel) {
				Icon(Icons.Rounded.ArrowBack, contentDescription = "Back", tint = FarmColors.getMoon())
			}
			Text(
				if (entry.id == 0L) "새 일기 쓰기" else "일기 수정하기",
				color = FarmColors.getMoon(),
				fontWeight = FontWeight.Bold,
				fontSize = 20.sp
			)
			Spacer(modifier = Modifier.weight(1f))
			if (entry.id != 0L) {
				IconButton(onClick = { onDelete(entry) }) {
					Icon(Icons.Default.Delete, contentDescription = "Delete", tint = FarmColors.getSoftRed())
				}
			}
		}

		Spacer(modifier = Modifier.height(24.dp))

		// 날짜 선택
		OutlinedTextField(
			value = selectedDateStr,
			onValueChange = {},
			label = { Text("날짜", color = Color.Gray) },
			readOnly = true,
			trailingIcon = {
				IconButton(onClick = { showDatePicker = true }) {
					Icon(Icons.Default.CalendarToday, contentDescription = "Select Date", tint = FarmColors.getMoon())
				}
			},
			modifier = Modifier
				.fillMaxWidth()
				.clickable { showDatePicker = true },
			colors = OutlinedTextFieldDefaults.colors(
				focusedTextColor = Color.White,
				unfocusedTextColor = Color.White,
				focusedBorderColor = FarmColors.getMoon(),
				unfocusedBorderColor = Color(0xFF1A3A4D)
			)
		)

		Spacer(modifier = Modifier.height(16.dp))

		// 카테고리 선택
		Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
			DiaryType.entries.forEach { type ->
				val isSelected = selectedType == type
				val bgColor = if (isSelected) FarmColors.getMoon() else Color(0xFF0A1E2D)
				val textColor = if (isSelected) Color.Black else FarmColors.getMoon()

				PixelButton(
					text = type.label,
					modifier = Modifier.weight(1f),
					containerColor = bgColor,
					contentColor = textColor,
					onClick = { selectedType = type }
				)
			}
		}

		Spacer(modifier = Modifier.height(16.dp))

		// 내용 입력
		OutlinedTextField(
			value = content,
			onValueChange = { content = it },
			label = { Text("내용", color = Color.Gray) },
			modifier = Modifier
				.fillMaxWidth()
				.height(300.dp),
			colors = OutlinedTextFieldDefaults.colors(
				focusedTextColor = Color.White,
				unfocusedTextColor = Color.White,
				focusedBorderColor = FarmColors.getMoon(),
				unfocusedBorderColor = Color(0xFF1A3A4D)
			),
			textStyle = LocalTextStyle.current.copy(lineHeight = 24.sp)
		)

		Spacer(modifier = Modifier.height(24.dp))

		// 저장 버튼
		Button(
			onClick = {
				onSave(entry.copy(date = selectedDateStr, content = content, type = selectedType))
			},
			modifier = Modifier.fillMaxWidth().height(56.dp),
			shape = RoundedCornerShape(12.dp),
			colors = ButtonDefaults.buttonColors(
				containerColor = FarmColors.getMoon(),
				contentColor = Color.Black
			)
		) {
			Text("저장하기", fontSize = 18.sp, fontWeight = FontWeight.Bold)
		}
	}
}
