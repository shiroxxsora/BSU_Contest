package com.example.bsu_contest.components


import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.AsyncImage
import com.example.bsu_contest.R
import com.example.bsu_contest.ReportActivity
import com.example.bsu_contest.models.Report
import com.example.bsu_contest.ui.theme.BlueBsu


//@Preview
@Composable
fun UserReportItem(
    /* Context для перехода на другой экран */
    context:Context,
    report: Report
){



    /*
    *
    *
    *       НЕ ИСПОЛЬЗУЕТСЯ
    *
    * */
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
                .fillMaxSize()
                .background(color = BlueBsu)
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
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
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {

                        /* Название заявки */
                        Text(
                            color = Color.White,
                            text = report.title,
                            fontSize = 16.sp
                        )
                    }
                }

                /* Время создания заявки */
                Text(
                    color = Color.White,
                    text = report.report_type.created_at.toLocaleString()
                )
            }
        }
    }
}