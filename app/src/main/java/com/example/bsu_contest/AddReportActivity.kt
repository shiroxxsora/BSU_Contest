package com.example.bsu_contest

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.bsu_contest.components.AddReportScreen
import com.example.bsu_contest.components.Header
import com.example.bsu_contest.models.MainApi
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class AddReportActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* Если пользователь както попал на форму авторизации, если уже авторизован
        * то перенаправляем его в личный кабинет */
        val pref = getSharedPreferences("TABLE", Context.MODE_PRIVATE)

        /* Инициализируем Retrofit для работы с http запросами (она нам все распарсит в data class'ы) */
        val retrofit = Retrofit.Builder()
            .baseUrl("https://webcomp.bsu.ru/api/finals24/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()

        val mainApi = retrofit.create(MainApi::class.java)

        var report_id:Int = 0

        try {
            report_id = intent.extras?.getInt("id", 0)!!
        }
        catch (_:Throwable){}

        setContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
            ) {
                Header(context = LocalContext.current, pref = pref)

                AddReportScreen(
                    context = LocalContext.current,
                    token = pref.getString("Bearer-Token", "")!!,
                    mainApi = mainApi,
                    report_id = report_id)
            }
        }
    }
}