package com.example.kotlinmultisample.app.ui.screen.farm

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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
 */
@Composable
fun BrokerTabs(
    brokers: List<Broker>,
    selectedBroker: Broker?,
    onBrokerSelected: (Broker?) -> Unit,
    onAddBroker: (String) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }

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
                    // 선택된 항목이 없으면(null) '전체'가 활성화된 것으로 간주 (잔디색)
                    containerColor = if (selectedBroker == null) FarmColors.getGrass() else FarmColors.getGround(),
                    onClick = { onBrokerSelected(null) }
                )
            }

            // 사용자 등록 증권사 목록 (LazyRow로 효율적 렌더링)
            items(brokers) { broker ->
                PixelButton(
                    text = broker.name,
                    modifier = Modifier.widthIn(min = 60.dp),
                    // 현재 선택된 ID와 일치하면 활성화 색상 적용
                    containerColor = if (selectedBroker?.id == broker.id) FarmColors.getGrass() else FarmColors.getGround(),
                    onClick = { onBrokerSelected(broker) }
                )
            }
        }
        
        // 계좌 추가 버튼 (우측 고정)
        PixelButton(
            "+",
            modifier = Modifier.width(40.dp),
            containerColor = FarmColors.getDarkGround(),
            contentColor = FarmColors.getGold(),
            onClick = { showAddDialog = true }
        )
    }

    // 추가 버튼 클릭 시 나타나는 입력 다이얼로그
    if (showAddDialog) {
        AddBrokerDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name ->
                onAddBroker(name)
                showAddDialog = false
            }
        )
    }
}

/**
 * 증권사 추가 다이얼로그
 *
 * @param onDismiss 취소 또는 배경 클릭 시 호출
 * @param onConfirm 입력 완료 후 '추가' 버튼 클릭 시 호출
 */
@Composable
fun AddBrokerDialog(onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        PixelCard(
            containerColor = FarmColors.getNight(),
            borderColor = FarmColors.getMoon()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("새로운 밭(증권사) 추가", color = FarmColors.getGold())
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
                    Spacer(modifier = Modifier.width(8.dp))
                    PixelButton("추가", onClick = { if (text.isNotBlank()) onConfirm(text) }, containerColor = FarmColors.getGrass())
                }
            }
        }
    }
}
