package com.hezheyi.main.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abxh.media.audio.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

import com.hezeyi.common.base.BaseActivity
import com.hezheyi.main.viewmodel.UserModel

/**
 * Created by dab on 2021/9/25 17:24
 */
class GuiderActivity : BaseActivity<UserModel>() {
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val items = mutableListOf(
            R.mipmap.guider_1,
            (R.mipmap.guider_2),
            (R.mipmap.guider_3),
            (R.mipmap.guider_4)
        )
        setContent {
            val state = rememberPagerState(pageCount = items.size)
            HorizontalPager(state = state) {
                PageContent(items[it])
            }

        }
    }
}

@Preview
@Composable
fun PageContent(id: Int) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(0.dp,0.dp,0.dp,10.dp)
            .background(color = Color.White)
    ) {
        Image(
            painter = painterResource(id = id),
            "",
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        IndicatorHorizontal()
    }
}
@Composable
fun IndicatorHorizontal() {
    Box(modifier = Modifier
        .height(10.dp)
        .width(30.dp)
        .background(color = Color.White)
        .clip(
            CircleShape
        ))
}