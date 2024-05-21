package com.example.bsu_contest.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.AsyncImage
import com.example.bsu_contest.R
import com.example.bsu_contest.ReportActivity
import com.example.bsu_contest.UserReportsActivity
import com.example.bsu_contest.models.EditingReport
import com.example.bsu_contest.models.MainApi
import com.example.bsu_contest.models.SendingReport
import com.example.bsu_contest.ui.theme.BlueBsu
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AddReportScreen(
    context: Context,
    token:String,
    mainApi: MainApi,
    report_id: Int = 0
){

    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {selectedImageUri.value = it}
    )
    val pickerIsClicked = remember { mutableStateOf(false) }

    val title = remember { mutableStateOf("") }
    val location = remember { mutableStateOf("") }
    val content = remember { mutableStateOf("") }
    val latitude = remember { mutableStateOf("51.0") }
    val longitude = remember { mutableStateOf("107.0") }
    val reportType = remember { mutableStateOf(1) }

    val report_img = remember { mutableStateOf("1") }
    if(report_id != 0){

        CoroutineScope(Dispatchers.IO).launch() {
            val report = mainApi.getReportById(report_id).data

            title.value = report.title
            location.value = report.location
            content.value = report.content
            latitude.value = report.latitude.toString()
            longitude.value = report.longitude.toString()
            reportType.value = report.report_type.id
            report_img.value = report.img_link
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        /* Выбор картинки */
        item {
            AsyncImage(
                modifier = Modifier
                    .width(400.dp)
                    .height(400.dp),
                contentScale = ContentScale.Crop,
                model =
                if(report_id == 0){
                    selectedImageUri.value
                }
                else{
                    "https://webcomp.bsu.ru/uploads/itbur2024/" + report_img.value
                },
                contentDescription = "Image",
                error = painterResource(id = R.drawable.ic_noimage)
            )

            /* Если заявка не редактируется */
            if (report_id == 0){
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = BlueBsu),
                    onClick = {
                        if (!pickerIsClicked.value) {
                            photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

                            /* Чтобы выбор кортинки открывался только 1 */
                            pickerIsClicked.value
                        }
                    }) {
                    Text(text = "Выбрать изображение")
                }
            }
        }

        /* Ввод заголовка */
        item {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .height(60.dp),
                value = title.value,
                onValueChange = { title.value = it },
                singleLine = true,
                label = {
                    Text(
                        color = Color.Gray,
                        text = "Заголовок"
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = BlueBsu,
                    focusedBorderColor = BlueBsu,
                    unfocusedContainerColor = Color.White,
                    unfocusedTextColor = Color(0xff888888),
                    focusedContainerColor = Color.White,
                    focusedTextColor = Color(0xff222222),
                )
            )
        }

        /* Ввод локации */
        item {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .height(60.dp),
                value = location.value,
                onValueChange = { location.value = it },
                singleLine = true,
                label = {
                    Text(
                        color = Color.Gray,
                        text = "Раcположение"
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = BlueBsu,
                    focusedBorderColor = BlueBsu,
                    unfocusedContainerColor = Color.White,
                    unfocusedTextColor = Color(0xff888888),
                    focusedContainerColor = Color.White,
                    focusedTextColor = Color(0xff222222),
                )
            )
        }

        /* Ввод широты и долготы */
        item {
            Text(modifier = Modifier.padding(top=10.dp), text = "Широта и Долгота", color = BlueBsu)

            Row(
                modifier = Modifier
                    .fillMaxWidth(.7f)
                    .padding(vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                /* Поле широты */
                OutlinedTextField(
                    modifier = Modifier
                        .width(100.dp)
                        .height(50.dp),
                    value = latitude.value,
                    onValueChange = { latitude.value = it },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = BlueBsu,
                        focusedBorderColor = BlueBsu,
                        unfocusedContainerColor = Color.White,
                        unfocusedTextColor = Color(0xff888888),
                        focusedContainerColor = Color.White,
                        focusedTextColor = Color(0xff222222),
                    )
                )

                /* Поле долгота */
                OutlinedTextField(
                    modifier = Modifier
                        .width(100.dp)
                        .height(50.dp),
                    value = longitude.value,
                    onValueChange = { longitude.value = it },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = BlueBsu,
                        focusedBorderColor = BlueBsu,
                        unfocusedContainerColor = Color.White,
                        unfocusedTextColor = Color(0xff888888),
                        focusedContainerColor = Color.White,
                        focusedTextColor = Color(0xff222222),
                    )
                )
            }
        }

        /* Выбор типа репорта */
        item {

            val expandedMenu = remember { mutableStateOf(false) }
            TextButton(
                modifier = Modifier
                    .fillMaxWidth(.45f)
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
                        painter = painterResource(id = R.drawable.ic_menu),
                        contentDescription = "Report type"
                    )
                    Text(
                        modifier = Modifier.padding(horizontal = 5.dp),
                        text = "Выбрать тип заявки",
                        color = BlueBsu
                    )
                    DropdownMenu(
                        modifier = Modifier
                            .fillMaxWidth(.45f)
                            .background(color = Color.White),
                        expanded = expandedMenu.value,
                        onDismissRequest = { expandedMenu.value = false }) {

                        DropdownMenuItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color =
                                    if (reportType.value == 1) {
                                        BlueBsu
                                    } else {
                                        Color.White
                                    }
                                ),
                            text = {
                                Text(
                                    modifier = Modifier
                                        .padding(horizontal = 5.dp),
                                    text = "Яма",
                                    color =
                                    if (reportType.value == 1) {
                                        Color.White
                                    } else {
                                        BlueBsu
                                    }
                                )
                            }, onClick = {
                                reportType.value = 1
                                expandedMenu.value = false
                            })

                        DropdownMenuItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color =
                                    if (reportType.value == 2) {
                                        BlueBsu
                                    } else {
                                        Color.White
                                    }
                                ),
                            text = {
                                Text(
                                    modifier = Modifier
                                        .padding(horizontal = 5.dp),
                                    text = "Мусорка",
                                    color =
                                    if (reportType.value == 2) {
                                        Color.White
                                    } else {
                                        BlueBsu
                                    }
                                )
                            }, onClick = {
                                reportType.value = 2
                                expandedMenu.value = false
                            })
                    }
                }
            }
        }

        /* Ввод подробного описания */
        item {

            /* Поле описания */
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .height(200.dp)
                    .padding(bottom = 40.dp),
                value = content.value,
                onValueChange = { content.value = it },
                singleLine = true,
                label = {
                    Text(
                        color = Color.Gray,
                        text = "Описание"
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = BlueBsu,
                    focusedBorderColor = BlueBsu,
                    unfocusedContainerColor = Color.White,
                    unfocusedTextColor = Color(0xff888888),
                    focusedContainerColor = Color.White,
                    focusedTextColor = Color(0xff222222),
                )
            )
        }

        item{
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = BlueBsu),
                onClick = {

                    /* Отправляем заявку на сервер */
                    CoroutineScope(Dispatchers.IO).launch() {

                        /* Добавление новой заявки */
                        if(report_id == 0){
                            try {
                                val report = SendingReport(
                                    title = title.value,
                                    report_type_id = reportType.value,
                                    content = content.value,
                                    location = location.value,
                                    latitude = latitude.value.toDouble(),
                                    longitude = longitude.value.toDouble(),

                                    /*  Братся будет из selectedImageUri */
                                    img_link = "stop.png",
                                )
                                mainApi.addReport(token = token, report = report)
                            }
                            catch (e:Throwable){
                                println(e.toString())
                            }
                            val intent = Intent(context, UserReportsActivity::class.java)
                            startActivity(context, intent, null)
                        }

                        /* Редактирование существующей заявки */
                        else{
                            val report = EditingReport(
                                report_id = report_id,
                                title = title.value,
                                report_type_id = reportType.value,
                                content = content.value,
                                location = location.value,
                                latitude = latitude.value.toDouble(),
                                longitude = longitude.value.toDouble(),
                            )
                            mainApi.editReport(token = token, report = report)

                            val intent = Intent(context, ReportActivity::class.java)
                            intent.putExtra("id", report_id)
                            startActivity(context, intent, null)
                        }
                    }
                }
            ){
                Text(
                    text =
                    if(report_id == 0){
                        "Отправить заявку"
                    }
                    else{
                        "Обновить заявку"
                    }
                )
            }
        }
    }
}