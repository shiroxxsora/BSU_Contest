package com.example.bsu_contest

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.bsu_contest.components.ReportItem
import com.example.bsu_contest.models.MainApi
import com.example.bsu_contest.models.Report
import com.example.bsu_contest.ui.theme.BlueBsu
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.yandex.mapkit.MapKitFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.system.exitProcess

class MainActivity : ComponentActivity() {
    /* Локальное хранилище */
    private var pref:SharedPreferences? = null

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* Инициализируем API ключ к Yandex MapKit */
        MapKitFactory.setApiKey("dc5823d8-04fc-4a2c-af2c-ff0461ab4249")

        /* Инициализируем локальное хранилище */
        pref = getSharedPreferences("TABLE", Context.MODE_PRIVATE)

        /* Инициализируем Retrofit для работы с http запросами (она нам все распарсит в data class'ы) */
        val retrofit = Retrofit.Builder()
            .baseUrl("https://webcomp.bsu.ru/api/finals24/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()

        val mainApi = retrofit.create(MainApi::class.java)

        setContent {


            /*
            *
            * Здесь почти все скопировано из MainActivity
            *
            * */



            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White)
            ) {

                /* Верхняя строка*/
                Header(LocalContext.current, pref)

                val isLoadingData = remember {mutableStateOf(false) }

                /* Корутина для запросов репортов из API */
                val reports = remember { mutableStateListOf<Report>() }
                isLoadingData.value = true;
                LaunchedEffect(CoroutineScope(Dispatchers.IO)) {
                    reports.addAll(mainApi.getAllReports().data
                        /* Чем больше id тем запись новее?
                        * на старте без фильтра берем */
                        .sortedByDescending { it.id })
                    //println(mainApi.getCommentsById(1))
                    isLoadingData.value = false;
                }

                /* Фильтрация */
                val filterState = remember { mutableStateOf(0) }
                suspend fun updateReportsFiltred(){
                    reports.clear()

                    isLoadingData.value = true;
                    Thread.sleep(1000)

                    reports.addAll(
                        /* Непосредственно сама фильтрация происходит тут */
                        when (filterState.value) {
                            1 -> mainApi.getAllReports().data
                                .filter { it.report_type.id == 1 }
                                .sortedByDescending { it.id }

                            2 -> mainApi.getAllReports().data
                                .filter { it.report_type.id == 2 }
                                .sortedByDescending { it.id }

                            else -> mainApi.getAllReports().data
                                .sortedByDescending { it.id }
                        }
                    )
                    isLoadingData.value = false
                }

                val expandedMenu = remember { mutableStateOf(false) }
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth(.6f)
                        .padding(horizontal = 0.dp)
                        .clickable {
                            //expandedMenu.value = !expandedMenu.value
                            /*CoroutineScope(Dispatchers.IO).launch() {
                                filterState.value++
                                filterState.value = filterState.value % 3

                                updateReportsFiltred()
                                //println(mainApi.getCommentsById(1))
                            }*/
                        },
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
                        DropdownMenu(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = Color.White),
                            expanded = expandedMenu.value,
                            onDismissRequest = { expandedMenu.value = false }) {
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
                                    updateReportsFiltred()
                                }
                                expandedMenu.value = false
                            })
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
                                    updateReportsFiltred()
                                }
                                expandedMenu.value = false
                            })
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
                                    updateReportsFiltred()
                                }
                                expandedMenu.value = false
                            })
                        }
                    }
                }

                /* Обновляем свойпом, по хорошему надо делать через модификатор т.к. метод помечен как Deprecated, но у меня не получилось */
                val refreshing by remember {mutableStateOf(false)}
                SwipeRefresh(
                    state = SwipeRefreshState(isRefreshing = refreshing),
                    onRefresh = {
                        CoroutineScope(Dispatchers.IO).launch() {
                            updateReportsFiltred()
                            //println(mainApi.getCommentsById(1))
                        }
                    }) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                        //.pullRefresh(test)
                    ){

                        /* Добавляем репорты */
                        LazyColumn(
                            Modifier.fillMaxSize()
                        ) {
                            itemsIndexed(
                                items = reports
                            ) { index, item ->
                                ReportItem(LocalContext.current, item)
                            }
                        }
                    }

                    if(isLoadingData.value == true){
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

    override fun onBackPressed() {
        this.finish()
        exitProcess(1)
        super.onBackPressed()

    }
}
