package com.example.bsu_contest.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bsu_contest.R
import com.example.bsu_contest.models.Comment
import com.example.bsu_contest.models.MainApi
import com.example.bsu_contest.ui.theme.BlueBsu
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CommentItem(
    mainApi: MainApi,
    token: String,
    user_id: Int = 0,
    comment: Comment,
    comments: SnapshotStateList<Comment>,
){

    val deletePopUp = remember { mutableStateOf(false)}

    /* Карточка с комментарием */
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        border =

        /* Меняем оформление если комментарий принадлежит пользователю */
        if(user_id == comment.user_id)
            BorderStroke(2.dp, BlueBsu)
        else
            BorderStroke(0.dp, Color(0x00000000)),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ){

        /* Табличка для непубличных комментариев */
        if(comment.status == 2){
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(
                    color =

                    /* Меняем оформление если комментарий принадлежит пользователю */
                    if (user_id == comment.user_id)
                        BlueBsu
                    else
                        Color.Black
                ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    color = Color.White,
                    text = "Приватный (только для зарегистрированных)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color =

                    /* Меняем оформление если комментарий принадлежит пользователю */
                    if (user_id == comment.user_id)
                        Color.Black
                    else
                        BlueBsu
                )
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)) {

                /* Имя оставившего комментарий */
                Text(
                    color = Color.White,
                    text = comment.author.user_name,
                    fontSize = 16.sp
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, top = 10.dp, bottom = 10.dp)
                    ,
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    /* Комментарий */
                    Text(modifier = Modifier
                            .fillMaxWidth(),
                        color = Color.White,
                        text = comment.content,
                        fontSize = 18.sp
                    )
                }

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    /* Время создания заявки, зачеркиваем и пишем ниже новое, если редактировали */
                    if(comment.created_at == comment.updated_at){
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(.8f),
                            color = Color.White,
                            text = comment.created_at.toLocaleString()
                        )
                    }
                    else{
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(.8f),
                        ){
                            Text(
                                color = Color.Gray,
                                text = comment.created_at.toLocaleString(),
                                textDecoration = TextDecoration.LineThrough
                            )
                            Text(
                                color = Color.White,
                                text = "Обновлено " + comment.updated_at.toLocaleString()
                            )
                        }
                    }
                    if(user_id == comment.user_id){
                        Image(
                            modifier = Modifier
                                .width(25.dp)
                                .clickable {
                                    deletePopUp.value = true
                                }
                            ,
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
                                        val response = mainApi.deleteCommentById(token = token, id = comment.id)
                                        comments.remove(comment)
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