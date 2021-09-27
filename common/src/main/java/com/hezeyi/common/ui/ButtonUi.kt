package com.hezeyi.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Created by dab on 2021/9/27 11:35
 */
@Preview()
@Composable
fun ButtonSubmit(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
//                        Color.Red,
//                        Color.Green,
                        Color(0xFFFFA445),
                        Color(0xFFFF8200),
                    ),
                    startX = 0.3f
                )
            )
            .height(45.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }


}