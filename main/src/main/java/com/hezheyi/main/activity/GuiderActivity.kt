package com.hezheyi.main.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.abxh.media.audio.R

import com.hezeyi.common.base.BaseActivity
import com.hezheyi.main.viewmodel.UserModel

/**
 * Created by dab on 2021/9/25 17:24
 */
class GuiderActivity : BaseActivity<UserModel>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            pageContent()
        }
    }
}

@Composable
fun pageContent() {
    Column(modifier = Modifier.padding(10.dp).height(420.dp).background(color = Color.White)) {
        Image(painter = painterResource(id = R.mipmap.guider_1), "")
    }
}