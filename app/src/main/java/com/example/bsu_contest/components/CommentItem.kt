package com.example.bsu_contest.components

import android.content.Context
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.bsu_contest.models.EditingComment
import com.example.bsu_contest.models.MainApi
import com.example.bsu_contest.ui.theme.BlueBsu
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Random
import kotlin.math.abs

@Composable
fun CommentItem(
    context: Context? = null,
    mainApi: MainApi,
    token: String,
    user_id: Int = 0,
    comment: Comment,
    comments: SnapshotStateList<Comment>,
    commentTitle: String ="",
){

    val deletePopUp = remember { mutableStateOf(false)}
    val isEditNow = remember { mutableStateOf(false)}

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
        if(context != null){
            if(context?.toString()?.contains("UserCommentsActivity")!!){

                /* Корутина для API */
                /*val reportTitle = remember {mutableStateOf("")}
                CoroutineScope(Dispatchers.IO).launch() {
                    Thread.sleep( 400 + (abs(Random().nextLong()) % 2000))
                    reportTitle.value = mainApi.getReportById(comment.report_id).data.title
                }*/

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .background(color = Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        color = Color.White,
                        text = commentTitle,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
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

                    if(!isEditNow.value){
                        /* Комментарий */
                        Text(modifier = Modifier
                                .fillMaxWidth(),
                            color = Color.White,
                            text = comment.content,
                            fontSize = 18.sp
                        )
                    }else {

                        val newCommentContent = remember { mutableStateOf(comment.content) }
                        Column() {
                            /* Текстовое поле для редактирования */
                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth(.9f)
                                    .height(140.dp),
                                value = newCommentContent.value,
                                onValueChange = { newCommentContent.value = it },
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedTextColor = Color.White,
                                    focusedTextColor = Color.White,
                                    focusedBorderColor = BlueBsu,
                                    cursorColor = BlueBsu
                                ))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween

                            ) {
                                val private = remember { mutableStateOf(comment.status) }
                                val isChecked = remember { mutableStateOf(comment.status == 2) }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ){
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
                                        comments.remove(comment)

                                        /* Редактируем комментарий на сервере */
                                        CoroutineScope(Dispatchers.IO).launch() {
                                            val sendComment = EditingComment(
                                                comment_id = comment.id,
                                                content = newCommentContent.value,
                                                status = private.value,
                                            )
                                            mainApi.editComment(token = token, comment = sendComment)

                                            isEditNow.value = false
                                            /* Обновляем коммент на экране */
                                            comments.add(Comment(
                                                id = comment.id,
                                                created_at = comment.created_at,
                                                updated_at = Date(),
                                                user_id = comment.user_id,
                                                content = newCommentContent.value,
                                                status = private.value,
                                                report_id = comment.report_id,
                                                author = comment.author
                                            ))
                                        }
                                    }) {
                                    Text(text = "Обновить")
                                }
                            }
                        }
                    }
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
                                    isEditNow.value = !isEditNow.value
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