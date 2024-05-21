package com.example.bsu_contest

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.bsu_contest.components.CommentItem
import com.example.bsu_contest.components.Header
import com.example.bsu_contest.models.Comment
import com.example.bsu_contest.models.MainApi
import com.example.bsu_contest.ui.theme.BlueBsu
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class UserCommentsActivity : ComponentActivity() {
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
        val token = pref.getString("Bearer-Token", "")!!

        setContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White)
            ) {

                /* Верхняя строка*/
                Header(LocalContext.current, pref)

                val isLoadingData = remember { mutableStateOf(false) }

                /* Корутина для запросов репортов из API */
                val comments = remember { mutableStateListOf<Comment>() }
                isLoadingData.value = true;
                LaunchedEffect(CoroutineScope(Dispatchers.IO)) {
                    comments.addAll(mainApi.getAllCommentsOfUser(token = token).data
                        /* Чем больше id тем запись новее?
                        * на старте без фильтра берем */
                        .sortedByDescending { it.updated_at })
                    isLoadingData.value = false;
                }

                /* Обновляем свойпом, по хорошему надо делать через модификатор т.к. метод помечен как Deprecated, но у меня не получилось */
                val refreshing by remember { mutableStateOf(false) }
                SwipeRefresh(
                    state = SwipeRefreshState(isRefreshing = refreshing),
                    onRefresh = {
                        CoroutineScope(Dispatchers.IO).launch() {
                            comments.clear()
                            comments.addAll(mainApi.getAllCommentsOfUser(token = token).data.sortedByDescending { it.updated_at })
                        }
                    }) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {

                        /* Добавляем репорты */
                        LazyColumn(
                            Modifier.fillMaxSize()
                        ) {
                            itemsIndexed(
                                items = comments
                            ) { index, item ->
                                CommentItem(
                                    context = LocalContext.current,
                                    mainApi = mainApi,
                                    token = token,
                                    user_id = 50,
                                    comment = item,
                                    comments = comments)
                            }
                        }
                    }

                    if (isLoadingData.value) {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(color = Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = BlueBsu
                            )
                        }
                    }
                }
            }
        }
    }
}