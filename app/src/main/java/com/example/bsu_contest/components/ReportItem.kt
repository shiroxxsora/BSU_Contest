package com.example.bsu_contest.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.AsyncImage
import com.example.bsu_contest.AddReportActivity
import com.example.bsu_contest.R
import com.example.bsu_contest.ReportActivity
import com.example.bsu_contest.models.MainApi
import com.example.bsu_contest.models.Report
import com.example.bsu_contest.ui.theme.BlueBsu
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


//@Preview
@Composable
fun ReportItem(
    context:Context,
    mainApi: MainApi? = null,
    token: String = "",
    report: Report,
    reports: SnapshotStateList<Report>? = null
){

    /* Карточка с заявкой */
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable {

                /* Если кликнули, то задаем данные которые будут переданы на другой экран */
                val intent = Intent(context, ReportActivity::class.java)

                /* Информация о репорте */
                intent.putExtra("id", report.id)
                startActivity(context, intent, null)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color =
                        if (context.toString().contains("UserReportsActivity")
                        ) {
                            Color.Black
                        } else {
                            BlueBsu
                        }
                    )
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                        ,
                        verticalAlignment = Alignment.CenterVertically,
                        //horizontalArrangement = Arrangement.SpaceAround
                    ) {

                        /* Картинка из заявки с использованием
                        *  библиотки io.coil-kt:coil-compose:2.6.0 для AsyncImage */
                        AsyncImage(
                            model = "https://webcomp.bsu.ru/uploads/itbur2024/" + report.img_link,
                            contentDescription = "Report image",
                            modifier = Modifier
                                .fillMaxWidth(.33f)
                                .height(100.dp)
                                .padding(end = 10.dp),
                            contentScale = ContentScale.Crop,

                            /* Подставляет изображение, если возвращается какая-нибудь ошибка по ссылке в model */
                            error = painterResource(id = R.drawable.ic_noimage)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            /* Название заявки */
                            Text(
                                color = Color.White,
                                text = report.title,
                                fontSize = 16.sp
                            )
                        }
                    }

                    val deletePopUp = remember { mutableStateOf(false) }

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        /* Время создания заявки, зачеркиваем и пишем ниже новое, если редактировали */
                        if(report.created_at == report.updated_at){
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(.8f),
                                color = Color.White,
                                text = report.created_at.toLocaleString()
                            )
                        }
                        else{
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(.8f),
                            ){
                                Text(
                                    color = Color.Gray,
                                    text = report.created_at.toLocaleString(),
                                    textDecoration = TextDecoration.LineThrough
                                )
                                Text(
                                    color = Color.White,
                                    text = "Обновлено " + report.updated_at.toLocaleString()
                                )
                            }
                        }

                        if(context.toString().contains("UserReportsActivity")){
                            Image(
                                modifier = Modifier
                                    .width(25.dp)
                                    .clickable {
                                        val intent = Intent(context, AddReportActivity::class.java)
                                        intent.putExtra("id", report.id)
                                        startActivity(context, intent, null)
                                    },
                                painter = painterResource(id = R.drawable.ic_edit), contentDescription = "Edit icon")

                            Image(
                                modifier = Modifier
                                    .width(25.dp)
                                    .clickable {
                                        deletePopUp.value = true
                                    },
                                painter = painterResource(id = R.drawable.ic_delete), contentDescription = "Delete icon")
                        }
                    }

                    if(deletePopUp.value){
                        AlertDialog(
                            modifier = Modifier.height(240.dp),
                            title = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text="Подтвердите удаление")
                                }
                            },
                            text = {
                                Text(text="Вы уверены что хотите удалить комментарий?")
                            },

                            onDismissRequest = { /*showPopUp.value = false*/ },
                            confirmButton = {
                                Button(
                                    modifier = Modifier.align(Alignment.Start),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                    onClick = {
                                        deletePopUp.value = false
                                        CoroutineScope(Dispatchers.IO).launch() {
                                            val response = mainApi?.deleteReportById(token = token, id = report.id)
                                            reports?.remove(report)
                                        }
                                    }) {
                                    Text(text="Да, продолжить")
                                }
                            },
                            dismissButton = {
                                Button(
                                    modifier = Modifier.align(Alignment.End),
                                    colors = ButtonDefaults.buttonColors(containerColor = BlueBsu),
                                    onClick = { deletePopUp.value = false
                                    }) {
                                    Text(text="Отмена")
                                }
                            }
                        )
                    }
                }
            }
    }
}