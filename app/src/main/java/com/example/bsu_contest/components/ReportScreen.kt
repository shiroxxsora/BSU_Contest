package com.example.bsu_contest.components

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.AsyncImage
import com.example.bsu_contest.R
import com.example.bsu_contest.ReportMapActivity
import com.example.bsu_contest.models.Comment
import com.example.bsu_contest.models.MainApi
import com.example.bsu_contest.models.Report
import com.example.bsu_contest.models.SendingComment
import com.example.bsu_contest.ui.theme.BlueBsu
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ReportScreen(
    context: Context,
    user_id: Int,
    report: Report,
    mainApi: MainApi,
    pref: SharedPreferences
){
    val isLoadingData = remember {mutableStateOf(false)}
    val comments = remember { mutableStateListOf<Comment>() }

    var token = pref.getString("Bearer-Token", "")!!
    LaunchedEffect(CoroutineScope(Dispatchers.IO)) {
        isLoadingData. value = true
        //Thread.sleep(4000)

        /* Если не авторизован*/
        if(token == ""){
            comments.clear()
            comments.addAll(mainApi.getPublicCommentsByReportId(report.id).data.sortedBy { it.created_at })
        }
        /* Если авторизован*/
        else{
            comments.clear()
            comments.addAll(mainApi.getAllCommentsByReportId(token, report.id).data.sortedBy { it.created_at })
        }

        isLoadingData.value = false
    }

    val listState = rememberLazyListState()

    LazyColumn(
        Modifier.fillMaxSize(),
        state =  listState
    ) {

        /* Информация о заявке */
        item {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White)
                //.verticalScroll(rememberScrollState()),
                ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                /* Картинка из заявки с использованием
                *  библиотки io.coil-kt:coil-compose:2.6.0 для AsyncImage */
                AsyncImage(
                    model = "https://webcomp.bsu.ru/uploads/itbur2024/" + report.img_link,
                    contentDescription = "Report image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(5.dp)
                        .background(color = Color(0x22000000)),
                    contentScale = ContentScale.Crop,

                    /* Подставляет изображение, если возвращается какая-нибудь ошибка по ссылке в model */
                    error = painterResource(id = R.drawable.ic_noimage)
                )


                /* Заголовок */
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    text = report.title,
                    fontSize = 28.sp,
                    color = BlueBsu
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                ) {

                    /* Кто добавил? */
                    Text(
                        text = "Добавил пользователь: " + report.author.user_name,
                        fontSize = 14.sp,
                        color = BlueBsu)

                    /* Когда добавил? */
                    Text(
                        text = "Создано: " + report.created_at.toLocaleString(),
                        fontSize = 14.sp,
                        color = BlueBsu
                    )

                    /* Яма/Мусор */
                    Text(
                        text = "Тип заявки: " + report.report_type.title,
                        fontSize = 14.sp,
                        color = BlueBsu
                    )

                    /* Где? */
                    Text(
                        text = "Местоположение: " + report.location,
                        fontSize = 14.sp,
                        color = BlueBsu
                    )

                    /* Что еще написал? */
                    Text(
                        modifier = Modifier
                            .padding(vertical = 20.dp),
                        text = "Дополнительно:\n" + report.content,
                        fontSize = 14.sp,
                        color = BlueBsu)
                }


                /* Было вынесено для отладки
                *  Просто картинка с картой, если кликнуть переходим на экран с функционирующей картой
                * */
                val url = "https://static-maps.yandex.ru/v1?lang=ru_RU&ll=${report.longitude},${report.latitude}&size=400,400&z=17&pt=${report.longitude},${report.latitude},pm2orgl&apikey=86aef359-e444-4e5b-a9e0-4178cce4d897"

                AsyncImage(
                    modifier = Modifier
                        .width(400.dp)
                        .height(400.dp)
                        .clickable {
                            val intent = Intent(context, ReportMapActivity::class.java)
                            intent.putExtra("latitude", report.latitude)
                            intent.putExtra("longitude", report.longitude)
                            startActivity(context, intent, null)
                        },
                    model = url,
                    contentDescription = "Map image",
                    error = painterResource(id = R.drawable.ic_noimage)
                )
            }
        }

        /* Если залогинен то покажем блок "добавить комментарий" */
        if (token != ""){
            item {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    /* Удалено в эстетических целях*/
                    /*Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp, vertical = 10.dp),
                        text = "Оставить комментарий",
                        fontSize = 18.sp,
                    color = BlueBsu)*/

                    val commentContent = remember { mutableStateOf("") }

                    /* Текстовое поле для комментария */
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(.9f)
                            .height(140.dp),
                        value = commentContent.value,
                        onValueChange = { commentContent.value = it },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BlueBsu,
                            cursorColor = BlueBsu
                        ))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween

                    ){
                        val private = remember { mutableStateOf(1) }
                        val isChecked = remember { mutableStateOf(false) }

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            /* Чек бокс чтобы проверить приватный коммент или нет */
                            Checkbox(
                                colors = CheckboxDefaults.colors(
                                    checkedColor = BlueBsu,
                                    uncheckedColor = BlueBsu,
                                    checkmarkColor = Color.White
                                ),
                                checked = isChecked.value,
                                onCheckedChange = {
                                    isChecked.value = !isChecked.value
                                    if (private.value == 1) {
                                        private.value = 2
                                    } else {
                                        private.value = 1
                                    }
                                    println(private.value)
                                },
                            )
                            Text(
                                color = BlueBsu,
                                text = "Приватный",
                                fontSize = 18.sp
                            )
                        }
                        Button(
                            colors = ButtonDefaults.buttonColors(containerColor = BlueBsu),
                            onClick = {
                                isLoadingData.value = true

                                /* Отправляем комментарий на сервер */
                                CoroutineScope(Dispatchers.IO).launch() {
                                    val comment = SendingComment(
                                        content = commentContent.value,
                                        status = private.value,
                                        report_id = report.id
                                    )

                                    val result = mainApi.addComment(token = token, comment = comment)
                                    comments.add(result.data)

                                    /* Сбрасываем все что написано в формочке комментария */
                                    commentContent.value = ""
                                    isChecked.value = false

                                    isLoadingData.value = false
                                }
                            }) {
                            Text(text = "Оставить комментарий")
                        }
                    }
                }
            }
        }

        /* Показываем надпись комментарии если они есть */
        if(!comments.isEmpty()){
            item{
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp, start = 15.dp, end = 15.dp, bottom = 5.dp),
                    text = "Комментарии",
                    fontSize = 18.sp,
                    color = BlueBsu)
            }
        }

        /* Загруженные из API коментарии */
        itemsIndexed(
            items = comments
        ) { index, item ->
            CommentItem(
                context = LocalContext.current,
                mainApi = mainApi,
                token = token,
                user_id = user_id,
                comment = item,
                comments = comments
            )
        }
    }


    /* Не работает почему-то, колесико покрутить хотелось  */
    /*if(isLoadingData.value){
        Dialog(onDismissRequest = { *//*TODO*//* }){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = BlueBsu
                )
            }
        }
    }*/
}