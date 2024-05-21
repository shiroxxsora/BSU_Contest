package com.example.bsu_contest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.bsu_contest.components.Header
import com.example.bsu_contest.components.LKScreen
import com.example.bsu_contest.models.MainApi
import com.example.bsu_contest.models.Team
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class LKActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* Инициализируем Retrofit для работы с http запросами (она нам все распарсит в data class'ы) */
        val retrofit = Retrofit.Builder()
            .baseUrl("https://webcomp.bsu.ru/api/finals24/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()

        val mainApi = retrofit.create(MainApi::class.java)
        val pref = getSharedPreferences("TABLE", Context.MODE_PRIVATE)

        /* Если неавторизованный пользователь попал сюда то перенаправляем на логин,
         хотя он поидее не может сюда попасть */
        if(pref.getString("Bearer-token","") != ""){
            startActivity(Intent(this, LoginActivity::class.java))
        }

        val team = Team(
            team_name = "Команда Павла",
            team_member1_name = "Иванов Павел Павлович",
            team_member2_name = "Ся Дмитрий Цзинчэнович",
            team_member3_name = "Батомункуев Тензин-Ширап Арсаланович",
            team_img = ""
        )
        setContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White)
            ) {
                Header(
                    context = LocalContext.current,
                    pref = pref
                )
                LKScreen(context = LocalContext.current, mainApi = mainApi, team = team)
            }
        }
    }
}