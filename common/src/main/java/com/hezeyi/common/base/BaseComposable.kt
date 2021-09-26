package com.hezeyi.common.base

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

/**
 * Created by dab on 2021/9/26 15:40
 */
@Preview
@Composable
fun BaseTitle(title: String) {
    Row {
        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
        Text(text = title)
    }
}