package com.hezheyi.user.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hezeyi.common.base.BaseActivity
import com.hezheyi.user.R
import com.hezheyi.user.viewmodel.UserModel
import java.time.format.TextStyle

/**
 * Created by dab on 2021/9/26 15:10
 */
@Route(path = "/user/login")
class UserLoginActivity : BaseActivity<UserModel>() {
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

    @Preview
    @Composable
    fun Content() {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color(getColor(R.color.color_f2f3f4)))
        ) {
            Spacer(modifier = Modifier.height(160.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.loading02),
                    contentDescription = "",
                )
                Spacer(modifier = Modifier.height(30.dp))
                Account()
                Spacer(modifier = Modifier.height(10.dp))
                Password()
            }

        }

    }

    @Composable
    fun Account() {
        var text by remember { mutableStateOf("") }
        OutlinedTextField(value = text, onValueChange = {
            text = it
        }, label = {
            Text(text = "输入账号")
        })
    }
    @Composable
    fun Password() {
        var text by remember { mutableStateOf("") }
        var passwordHidden by remember{ mutableStateOf(false)}
        OutlinedTextField(value = text, onValueChange = {
            text = it
        }, label = {
            Text(text = "输入密码")
        },leadingIcon = {
            Text(text = "密码")
        },trailingIcon = {
            IconButton(
                onClick = {
                    passwordHidden = !passwordHidden
                }
            ){
                Icon(Icons.Default.Email, null)
            }
        },visualTransformation = if(passwordHidden) PasswordVisualTransformation() else VisualTransformation.None)
    }

}