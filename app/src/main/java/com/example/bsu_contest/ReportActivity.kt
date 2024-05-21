package com.example.bsu_contest

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.platform.LocalContext
import com.example.bsu_contest.components.Header
import com.example.bsu_contest.components.ReportScreen
import com.example.bsu_contest.models.MainApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ReportActivity : ComponentActivity() {
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

        /* Корутина для API */
        CoroutineScope(Dispatchers.IO).launch() {
            val report = mainApi.getReportById(intent.extras?.getInt("id", 0)!!).data
            var user_id: Int = 0

            if(pref.getString("Bearer-Token", "") != ""){

                /* ИНАЧЕ НИКАК НЕ ПОЛУЧИТЬ id ПОЛЬЗОВАТЕЛЯ КОТОРЫЙ СЕЙЧАС ЗАЛОГИНЕН */
                val user_comments = mainApi.getAllCommentsOfUser(pref.getString("Bearer-Token", "")!!).data
                if(!user_comments.isEmpty()){
                    user_id = user_comments[0].user_id
                }
            }

            runOnUiThread {
                setContent{
                    Column {
                        Header(LocalContext.current, getSharedPreferences("TABLE", MODE_PRIVATE))
                        ReportScreen(
                            context = LocalContext.current,
                            user_id = user_id,
                            report = report,
                            mainApi = mainApi,
                            pref = pref
                        )
                    }
                }
            }
        }
    }
}