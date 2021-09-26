package com.hezheyi.main.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.abxh.core.MmkvHelper
import com.abxh.media.audio.R
import com.alibaba.android.arouter.launcher.ARouter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hezeyi.common.base.BaseActivity
import com.hezheyi.main.viewmodel.MainModel

/**
 * Created by dab on 2021/9/25 17:24
 */
class GuiderActivity : BaseActivity<MainModel>() {
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            Surface {
                //实例系统UI控制
                val systemUiController = rememberSystemUiController()
                //设置为透明状态栏
                systemUiController.setStatusBarColor(
                    Color.Transparent, darkIcons = MaterialTheme.colors.isLight)
                Content()
            }
        }
    }

    @ExperimentalPagerApi
    @Preview()
    @Composable
    fun Content() {
        val items = mutableListOf(
            R.mipmap.guider_1,
            R.mipmap.guider_2,
            R.mipmap.guider_3,
            R.mipmap.guider_4,
        )
        val state = rememberPagerState(pageCount = items.size)
        Box(contentAlignment = Alignment.Center) {
            HorizontalPager(state = state) {
                PageContent(items[it])
            }
            val offset = 260.dp
            if (state.currentPage.inc() != items.size) {
                Indicator(size = items.size, index = state.currentPage, offset)
            }
            val width = animateDpAsState(
                targetValue = if (state.currentPage.inc() == items.size) 60.dp else 0.dp,
                animationSpec = if (state.currentPage.inc() == items.size) spring(dampingRatio = Spring.DampingRatioHighBouncy) else spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy
                )
            )
            FAB(offset, width)

        }

    }

    //按钮
    @Composable
    private fun FAB(offset: Dp, width: State<Dp>) {
        Row(
            modifier = Modifier
                .height(100.dp)
                .offset(y = offset),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            FloatingActionButton(
                onClick = {
                    MmkvHelper.getInstance().putBoolean("isGuiderActivity", true)
                    ARouter.getInstance().build("/user/login").navigation()
                    finish()
                }, backgroundColor = Color.Black, modifier = Modifier.size(width.value)
            ) {
                Icon(Icons.Default.ArrowForward, contentDescription = "", tint = Color.White)
            }
        }
    }

    //Page的内容
    @Composable
    fun PageContent(id: Int) {
        Image(
            painter = painterResource(id = id),
            "",
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
    }

    //Page下面的指示器
    @Composable
    fun Indicator(size: Int, index: Int, offset: Dp) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .offset(y = offset)
                .fillMaxWidth()
        ) {
            repeat(size) {
                IndicatorHorizontal(isSelected = it == index)
            }
        }
    }

    //Page下面的指示器
    @Composable
    fun IndicatorHorizontal(isSelected: Boolean) {
        val width = animateDpAsState(
            targetValue = if (isSelected) 30.dp else 10.dp,
            animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)
        )
        Box(
            modifier = Modifier
                .height(10.dp)
                .padding(horizontal = 5.dp)
                .width(width.value)
                .clip(CircleShape)
                .background(color = if (isSelected) Color.Black else Color.LightGray)
        )
    }
}

