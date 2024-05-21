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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.example.bsu_contest.LoginActivity
import com.example.bsu_contest.MainActivity
import com.example.bsu_contest.R
import com.example.bsu_contest.ui.theme.BlueBsu

@Composable
fun Header(context: Context, pref: SharedPreferences?=null){
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

                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = BlueBsu),
                    onClick = {
                        //startActivity(loginIntent)
                    }) {
                    Text(text = "Личный кабинет")
                }

                /* TODO: Удалить */
                val intent = Intent(context, MainActivity::class.java)
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = BlueBsu),
                    onClick = {
                        pref?.edit()?.putString("Bearer-Token","")?.apply()
                        startActivity(context, intent, null)
                    }) {
                    Text(text = "CLR")
                }
            }
        }
    }
}