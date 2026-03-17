package com.example.kotlinmultisample.app.ui.screen.farm

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddSeedOverlay(onDismiss: () -> Unit, onAdd: (FarmSeed) -> Unit) {
    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var emoji by remember { mutableStateOf("🌱") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onDismiss
            ),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {} // 내부 클릭 무시
                ),
            shape = RoundedCornerShape(16.dp),
            color = FarmColors.getGround()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "새로운 씨앗 심기",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = FarmColors.getGold()
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("종목명") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("수량") },
                    singleLine = true,
                     colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("매수 평균가") },
                    singleLine = true,
                     colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = {
                        val qty = quantity.toIntOrNull() ?: 0
                        val buyingPrice = price.toDoubleOrNull() ?: 0.0
                        if (name.isNotEmpty() && qty > 0) {
                            onAdd(
                                FarmSeed(
                                    brokerId = 0, // ViewModel에서 설정
                                    name = name,
                                    emoji = emoji, // 나중에 변경 가능하도록
                                    quantity = qty,
                                    buyingPrice = buyingPrice,
                                    pct = 0.0 // 초기 수익률 0
                                )
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FarmColors.getGrass())
                ) {
                    Text("심기", color = Color.White)
                }
            }
        }
    }
}

