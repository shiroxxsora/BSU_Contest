package com.example.bsu_contest.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.bsu_contest.models.MainApi
import com.example.bsu_contest.ui.theme.BlueBsu

@Composable
fun LKScreen(context: Context, mainApi: MainApi) {
    Column(
        modifier =  Modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(color = Color.DarkGray)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val backgroundReportColor = remember { mutableStateOf(BlueBsu) }
            val reportColor = remember {mutableStateOf(Color.White)}
            val backgroundCommentColor = remember { mutableStateOf(Color.White) }
            val commentColor = remember {mutableStateOf(BlueBsu)}

            Text(modifier = Modifier
                .fillMaxHeight()
                .width(200.dp)
                .clickable {
                    backgroundReportColor.value = BlueBsu
                    reportColor.value = Color.White
                    backgroundCommentColor.value = Color.White
                    commentColor.value = BlueBsu
                }
                .background(color = backgroundReportColor.value),
                text = "Мои заявки",
                color = reportColor.value
            )

            Text(modifier = Modifier
                .fillMaxHeight()
                .width(200.dp)
                .clickable {
                    backgroundReportColor.value = Color.White
                    reportColor.value = BlueBsu
                    backgroundCommentColor.value = BlueBsu
                    commentColor.value = Color.White
                }
                .background(color = backgroundCommentColor.value),
                text = "Мои комментарии",
                color = commentColor.value
            )
        }
        LazyColumn(
            modifier =  Modifier
                .fillMaxSize()
        ) {
            item{
                Text(text = "test")
            }
            item{
                Text(text = "test")
            }
            item{
                Text(text = "test")
            }
            item{
                Text(text = "test")
            }
            item{
                Text(text = "test")
            }
            item{
                Text(text = "test")
            }
            item{
                Text(text = "test")
            }
            item{
                Text(text = "test")
            }
            item{
                Text(text = "test")
            }
            item{
                Text(text = "test")
            }
            item{
                Text(text = "test")
            }
        }
    }

}