package com.hezheyi.user.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hezeyi.common.base.BaseActivity
import com.hezeyi.common.base.BaseComposeActivity
import com.hezeyi.common.ui.ButtonSubmit
import com.hezheyi.user.R
import com.hezheyi.user.viewmodel.UserModel

/**
 * Created by dab on 2021/9/26 15:10
 */
@Route(path = "/user/login")
class UserLoginActivity : BaseComposeActivity<UserModel>() {
    @Preview
    @Composable
    override fun Content() {
        val account = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
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
                Spacer(modifier = Modifier.height(40.dp))
                Account(account.value) {
                    if (it.isEmpty() || it.matches(Regex("^[a-z0-9A-Z]+\$")) && it.length < 8) {
                        account.value = it
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Password(password.value) {
                    if (it.length < 12) {
                        password.value = it
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
            ButtonSubmit("登录") {
                Log.e("TAG", "Content: ")
            }
        }

    }

    @Composable
    fun Account(account: String, onValueChange: (String) -> Unit) {
        OutlinedTextField(
            value = account,
            onValueChange = onValueChange,
            label = {
                Text(text = "输入账号")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Default,
                autoCorrect = true,
                capitalization = KeyboardCapitalization.None,
            ),
            singleLine = true,

            )
    }

    @Composable
    fun Password(text: String, onValueChange: (String) -> Unit) {
        var passwordHidden by remember { mutableStateOf(true) }
        OutlinedTextField(
            value = text,
            onValueChange = onValueChange,
            label = {
                Text(text = "输入密码")
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        passwordHidden = !passwordHidden
                    },
                ) {
                    Icon(
                        painterResource(id = if (passwordHidden) R.drawable.user_ic_eye_show else R.drawable.user_ic_eye_hint),
                        null, modifier = Modifier.size(25.dp)
                    )
                }
            },
            singleLine = true,
            visualTransformation = if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Default,
                autoCorrect = true,
                capitalization = KeyboardCapitalization.None

            ),
        )
    }

}