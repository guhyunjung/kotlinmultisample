package com.example.kotlinmultisample.app.ui.screen.farm

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.kotlinmultisample.getPlatform
import com.example.kotlinmultisample.shared.domain.model.Broker

/**
 * 증권사 탭 선택 영역 컴포저블
 *
 * 사용자가 등록한 증권사(포트폴리오) 목록을 가로 스크롤 가능한 탭 형태로 보여줍니다.
 * '+' 버튼을 통해 새로운 증권사를 팝업으로 추가할 수 있습니다.
 *
 * @param brokers 사용자가 등록한 증권사 리스트 (Room DB에서 로드됨)
 * @param selectedBroker 현재 선택된 증권사 (null이면 '전체' 선택 상태)
 * @param onBrokerSelected 증권사 탭 클릭 시 호출되는 콜백 (null 전달 시 '전체' 선택)
 * @param onAddBroker 새 증권사 추가 팝업에서 '추가' 버튼 클릭 시 호출되는 콜백
 * @param onDeleteBroker 증권사 삭제 콜백
 */
@Composable
fun BrokerTabs(
	brokers: List<Broker>,
	selectedBroker: Broker?,
	onBrokerSelected: (Broker?) -> Unit,
	onAddBroker: (String) -> Unit,
	onEditBroker: (Broker, String) -> Unit,
	onDeleteBroker: (Broker) -> Unit
) {
	var brokerToEdit by remember { mutableStateOf<Broker?>(null) }
	var showDialog by remember { mutableStateOf(false) }

	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 12.dp),
		horizontalArrangement = Arrangement.spacedBy(2.dp)
	) {
		LazyRow(
			modifier = Modifier.weight(1f),
			horizontalArrangement = Arrangement.spacedBy(4.dp)
		) {
			// "전체" 탭 항상 표시 (Broker가 null인 경우)
			item {
				PixelButton(
					text = "전체🌾",
					modifier = Modifier.widthIn(min = 60.dp),
					containerColor = if (selectedBroker == null) FarmColors.getGrass() else FarmColors.getGround(),
					onClick = { onBrokerSelected(null) }
				)
			}
			// 사용자 등록 증권사 목록 (LazyRow로 효율적 렌더링)
			val platformName = getPlatform().name
			val isAndroid = platformName.startsWith("Android")

			items(brokers) { broker ->
				val tabModifier = if (isAndroid) {
					Modifier
						.widthIn(min = 60.dp)
						.pointerInput(Unit) {
							detectTapGestures(
								onTap = { onBrokerSelected(broker) },
								onLongPress = {
									brokerToEdit = broker
									showDialog = true
								}
							)
						}
				} else {
					Modifier
						.widthIn(min = 60.dp)
						.pointerInput(Unit) {
							// Desktop: 우클릭 감지 -> 수정 팝업
							awaitPointerEventScope {
								while (true) {
									val event = awaitPointerEvent()
									if (event.type == PointerEventType.Press && event.buttons.isSecondaryPressed) {
										brokerToEdit = broker
										showDialog = true
										event.changes.forEach { it.consume() }
									}
								}
							}
						}
						.pointerInput(Unit) {
							// Desktop: 좌클릭 -> 선택, 롱클릭 -> 수정 팝업
							detectTapGestures(
								onTap = { onBrokerSelected(broker) },
								onLongPress = {
									brokerToEdit = broker
									showDialog = true
								}
							)
						}
				}
				PixelButton(
					text = broker.name,
					modifier = tabModifier,
					containerColor = if (selectedBroker?.id == broker.id) FarmColors.getGrass() else FarmColors.getGround()
					// onClick 파라미터 생략 (null 전달됨 -> 내부 clickable 미생성 -> modifier 제스처 정상 동작)
				)
			}
		}
		// 계좌 추가 버튼 (우측 고정)
		PixelButton(
			"+",
			modifier = Modifier.width(40.dp),
			containerColor = FarmColors.getDarkGround(),
			contentColor = FarmColors.getGold(),
			onClick = {
				brokerToEdit = null
				showDialog = true
			}
		)
	}

	if (showDialog) {
		AddOrEditBrokerDialog(
			broker = brokerToEdit,
			onDismiss = { showDialog = false },
			onConfirm = { name ->
				if (brokerToEdit == null) {
					onAddBroker(name)
				} else {
					onEditBroker(brokerToEdit!!, name)
				}
				showDialog = false
			},
			onDelete = {
				brokerToEdit?.let { onDeleteBroker(it) }
				showDialog = false
			}
		)
	}
}


/**
 * 증권사 추가/수정/삭제 다이얼로그
 * @param broker null이면 추가, 아니면 수정/삭제
 * @param onDismiss 닫기
 * @param onConfirm 저장(추가/수정)
 * @param onDelete 삭제
 */
@Composable
fun AddOrEditBrokerDialog(
	broker: Broker?,
	onDismiss: () -> Unit,
	onConfirm: (String) -> Unit,
	onDelete: () -> Unit
) {
	// broker가 변경되면 text 상태도 초기화되도록 key로 설정
	var text by remember(broker) { mutableStateOf(broker?.name ?: "") }

	Dialog(onDismissRequest = onDismiss) {
		PixelCard(
			containerColor = FarmColors.getNight(),
			borderColor = FarmColors.getMoon()
		) {
			Column(modifier = Modifier.padding(16.dp)) {
				Text(
					if (broker == null) "새로운 밭(증권사) 추가" else "증권사 정보 수정",
					color = FarmColors.getGold()
				)
				Spacer(modifier = Modifier.height(8.dp))
				OutlinedTextField(
					value = text,
					onValueChange = { text = it },
					label = { Text("증권사 이름") },
					singleLine = true
				)
				Spacer(modifier = Modifier.height(16.dp))
				Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
					PixelButton("취소", onClick = onDismiss, containerColor = FarmColors.getSoftRed())
					if (broker != null) {
						Spacer(modifier = Modifier.width(8.dp))
						PixelButton("삭제", onClick = onDelete, containerColor = FarmColors.getDarkGround())
					}
					Spacer(modifier = Modifier.width(8.dp))
					PixelButton(
						if (broker == null) "추가" else "저장",
						onClick = { if (text.isNotBlank()) onConfirm(text) },
						containerColor = FarmColors.getGrass()
					)
				}
			}
		}
	}
}
