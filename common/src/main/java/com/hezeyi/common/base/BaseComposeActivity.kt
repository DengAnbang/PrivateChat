package com.hezeyi.common.base

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.abxh.jetpack.IViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * Created by dab on 2021/9/27 14:36
 */
abstract class BaseComposeActivity<VM : IViewModel> : BaseActivity<VM>() {
    override fun getContentViewRes(): Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //当设置成false，就不再使用原定的间隔框架，页面也就不再受状态栏的影响。默认true会保留状态栏的
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            //实例系统UI控制
            val systemUiController = rememberSystemUiController()
            //设置为透明状态栏
            systemUiController.setStatusBarColor(
                Color.Transparent, darkIcons = MaterialTheme.colors.isLight
            )
            Content()
        }
    }

    @Composable
    abstract fun Content()

    override fun create() {

    }
}