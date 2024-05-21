package com.example.bsu_contest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import com.example.bsu_contest.components.LoginScreen


class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* Если пользователь както попал на форму авторизации, если уже авторизован
        * то перенаправляем его в личный кабинет */
        val pref = getSharedPreferences("TABLE", Context.MODE_PRIVATE)
        if(pref.getString("Bearer-token","") != ""){
            startActivity(Intent(this, MainActivity::class.java))
        }
        else {
            setContent {
                LoginScreen(LocalContext.current, pref)
            }
        }
    }
}