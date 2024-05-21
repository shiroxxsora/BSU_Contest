package com.example.bsu_contest

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.bsu_contest.components.Header
import com.example.bsu_contest.components.UserReportItem
import com.example.bsu_contest.models.MainApi
import com.example.bsu_contest.models.Report
import com.example.bsu_contest.ui.theme.BlueBsu
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class UserReportsActivity : ComponentActivity() {
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
                val reports = remember { mutableStateListOf<Report>() }
                isLoadingData.value = true;
                LaunchedEffect(CoroutineScope(Dispatchers.IO)) {
                    reports.addAll(mainApi.getAllReportsOfUser(token = token).data
                        /* Чем больше id тем запись новее?
                        * на старте без фильтра берем */
                        .sortedByDescending { it.id })
                    isLoadingData.value = false;
                }

                /* Фильтрация */
                val filterState = remember { mutableStateOf(0) }
                suspend fun updateReportsFiltered(){
                    reports.clear()

                    isLoadingData.value = true;
                    Thread.sleep(1000)

                    reports.addAll(
                        /* Непосредственно сама фильтрация происходит тут */
                        when (filterState.value) {
                            1 -> mainApi.getAllReportsOfUser(token = token).data
                                .filter { it.report_type.id == 1 }
                                .sortedByDescending { it.id }

                            2 -> mainApi.getAllReportsOfUser(token = token).data
                                .filter { it.report_type.id == 2 }
                                .sortedByDescending { it.id }

                            else -> mainApi.getAllReportsOfUser(token = token).data
                                .sortedByDescending { it.id }
                        }
                    )
                    isLoadingData.value = false
                }

                val expandedMenu = remember { mutableStateOf(false) }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    /* Кнопка фильтра */
                    TextButton(
                        modifier = Modifier
                            .fillMaxWidth(.5f)
                            .padding(horizontal = 0.dp),
                        onClick = { expandedMenu.value = !expandedMenu.value }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = Color.White),
                            verticalAlignment = Alignment.CenterVertically,
                            //horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Image(
                                modifier = Modifier.height(25.dp),
                                painter = painterResource(id = R.drawable.ic_filter),
                                contentDescription = "Filter"
                            )
                            Text(
                                modifier = Modifier.padding(horizontal = 5.dp),
                                text = "Выбрать фильтр",
                                color = BlueBsu
                            )

                            /* Выпадающее меню */
                            DropdownMenu(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(color = Color.White),
                                expanded = expandedMenu.value,
                                onDismissRequest = { expandedMenu.value = false }) {

                                /* Фильтр все */
                                DropdownMenuItem(text = {
                                    Text(
                                        modifier = Modifier
                                            .padding(horizontal = 5.dp)
                                        /*.background(color = Color.White)*/,
                                        text = "Все",
                                        color = BlueBsu
                                    )
                                }, onClick = {
                                    CoroutineScope(Dispatchers.IO).launch() {
                                        filterState.value = 0
                                        updateReportsFiltered()
                                    }
                                    expandedMenu.value = false
                                })

                                /* Фильтр яма */
                                DropdownMenuItem(text = {
                                    Text(
                                        modifier = Modifier
                                            .padding(horizontal = 5.dp)
                                        /*.background(color = Color.White)*/,
                                        text = "Ямы",
                                        color = BlueBsu
                                    )
                                }, onClick = {
                                    CoroutineScope(Dispatchers.IO).launch() {
                                        filterState.value = 1
                                        updateReportsFiltered()
                                    }
                                    expandedMenu.value = false
                                })

                                /* Фильтр мусор */
                                DropdownMenuItem(text = {
                                    Text(
                                        modifier = Modifier
                                            .padding(horizontal = 5.dp)
                                        /*.background(color = Color.White)*/,
                                        text = "Мусорки",
                                        color = BlueBsu
                                    )
                                }, onClick = {
                                    CoroutineScope(Dispatchers.IO).launch() {
                                        filterState.value = 2
                                        updateReportsFiltered()
                                    }
                                    expandedMenu.value = false
                                })
                            }
                        }
                    }

                    /* Кнопка добавить заявку */
                    TextButton(
                        modifier = Modifier
                            .padding(horizontal = 0.dp),
                        onClick = {  }
                    ) {
                        Text(text="Добавить новую заявку", color = BlueBsu)
                    }
                }

                /* Обновляем свойпом, по хорошему надо делать через модификатор т.к. метод помечен как Deprecated, но у меня не получилось */
                val refreshing by remember { mutableStateOf(false) }
                SwipeRefresh(
                    state = SwipeRefreshState(isRefreshing = refreshing),
                    onRefresh = {
                        CoroutineScope(Dispatchers.IO).launch() {
                            updateReportsFiltered()
                        }
                    }) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ){

                        /* Добавляем репорты */
                        LazyColumn(
                            Modifier.fillMaxSize()
                        ) {
                            itemsIndexed(
                                items = reports
                            ) { index, item ->
                                UserReportItem(LocalContext.current, item)
                            }
                        }
                    }

                    if(isLoadingData.value){
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