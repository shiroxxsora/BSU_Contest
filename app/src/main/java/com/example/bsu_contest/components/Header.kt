package com.example.bsu_contest.components

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.example.bsu_contest.LKActivity
import com.example.bsu_contest.LoginActivity
import com.example.bsu_contest.MainActivity
import com.example.bsu_contest.R
import com.example.bsu_contest.UserCommentsActivity
import com.example.bsu_contest.UserReportsActivity
import com.example.bsu_contest.ui.theme.BlueBsu

@Composable
fun Header(
    context: Context,
    pref: SharedPreferences?=null
    ){
    val loginIntent = Intent(context, LoginActivity::class.java)

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ){
                Image(
                    modifier = Modifier.height(30.dp),
                    painter = painterResource(id = R.drawable.logo_intro),
                    contentDescription = "Logo")
                /* Название приложения */
                Text(
                    modifier = Modifier
                        .padding(horizontal = 5.dp),
                    text = stringResource(id = R.string.app_name),
                    color = BlueBsu
                )
            }

            /* Кнопка для перехода на экран авторизации (с проверкой авторизован ли пользователь) */
            //println(perf?.getString("Bearer-Token", ""))
            if(pref?.getString("Bearer-Token", "") == ""){
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = BlueBsu),
                    onClick = {
                        startActivity(context, loginIntent, null)
                    }) {
                    Text(text = "Войдите в аккаунт")
                }
            }
            else{
                val expandedMenu = remember { mutableStateOf(false) }
                print(context.toString())
                Button(
                    modifier = Modifier
                        .fillMaxWidth(.6f)
                        .padding(horizontal = 0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BlueBsu),
                    onClick = { expandedMenu.value = !expandedMenu.value }
                ) {
                    Text(text = "Меню")

                    /* Выпадающее меню */
                    DropdownMenu(
                        modifier = Modifier
                            .fillMaxWidth(.6f)
                            .background(color = Color.White),
                        expanded = expandedMenu.value,
                        onDismissRequest = { expandedMenu.value = false }) {

                        /* Кнопка главная */
                        DropdownMenuItem(
                            modifier = Modifier
                                .background(
                                    color =
                                    if(context.toString().contains("MainActivity")) {
                                        BlueBsu
                                    }
                                    else{
                                        Color.White
                                    }
                                ),
                            text = {
                                Text(
                                    modifier = Modifier
                                        .padding(horizontal = 5.dp)
                                    /*.background(color = Color.White)*/,
                                    text = "Главная",
                                    color =
                                        if(context.toString().contains("MainActivity")) {
                                            Color.White
                                        }
                                        else{
                                            BlueBsu
                                        }
                                    )
                                },
                            onClick = {
                                expandedMenu.value = false
                                val intent = Intent(context, MainActivity::class.java)
                                startActivity(context, intent, null)
                            }
                        )

                        /* Кнопка профиля */
                        DropdownMenuItem(
                            modifier = Modifier
                                .background(
                                    color =
                                    if(context.toString().contains("LKActivity")) {
                                        BlueBsu
                                    }
                                    else{
                                        Color.White
                                    }
                                ),
                            text = {
                                Text(
                                    modifier = Modifier
                                        .padding(horizontal = 5.dp)
                                    /*.background(color = Color.White)*/,
                                    text = "Профиль",
                                    color =
                                    if(context.toString().contains("LKActivity")) {
                                        Color.White
                                    }
                                    else{
                                        BlueBsu
                                    }
                                )
                            },
                            onClick = {
                                expandedMenu.value = false
                                val intent = Intent(context, LKActivity::class.java)
                                startActivity(context, intent, null)
                            }
                        )

                        /* Кнопка мои заявки */
                        DropdownMenuItem(
                            modifier = Modifier
                                .background(
                                    color =
                                        if(context.toString().contains("UserReportsActivity")) {
                                            BlueBsu
                                        }
                                        else{
                                            Color.White
                                        }
                                ),
                            text = {
                                Text(
                                    modifier = Modifier
                                        .padding(horizontal = 5.dp)
                                    /*.background(color = Color.White)*/,
                                    text = "Мои заявки",
                                    color =
                                        if(context.toString().contains("UserReportsActivity")) {
                                            Color.White
                                        }
                                        else{
                                            BlueBsu
                                        }
                                    )
                               },
                            onClick = {
                                expandedMenu.value = false
                                val intent = Intent(context, UserReportsActivity::class.java)
                                startActivity(context, intent, null)
                            }
                        )

                        /* Кнопка мои комментарии*/
                        DropdownMenuItem(
                            modifier = Modifier
                                .background(
                                    color =
                                    if(context.toString().contains("UserCommentsActivity")) {
                                        BlueBsu
                                    }
                                    else{
                                        Color.White
                                    }
                                ),
                            text = {
                                Text(
                                    modifier = Modifier
                                        .padding(horizontal = 5.dp)
                                    /*.background(color = Color.White)*/,
                                    text = "Мои комментарии",
                                    color =
                                        if(context.toString().contains("UserCommentsActivity")) {
                                            Color.White
                                        }
                                        else{
                                            BlueBsu
                                        }
                                    )
                                },
                            onClick = {
                                expandedMenu.value = false
                                val intent = Intent(context, UserCommentsActivity::class.java)
                                startActivity(context, intent, null)
                            }
                        )

                        /* Кнопка выйти */
                        DropdownMenuItem(
                            text = {
                                Text(
                                    modifier = Modifier
                                        .padding(horizontal = 5.dp)
                                        .background(color = Color.White)
                                    /*.background(color = Color.White)*/,
                                    text = "Выйти из аккаунта",
                                    color =BlueBsu
                                )
                            },
                            onClick = {
                                expandedMenu.value = false
                                pref?.edit()?.putString("Bearer-Token", "")?.apply()
                                val intent = Intent(context, MainActivity::class.java)
                                startActivity(context, intent, null)
                            }
                        )
                    }
                }

            }
        }
    }
}