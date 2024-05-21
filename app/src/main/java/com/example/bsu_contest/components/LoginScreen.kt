package com.example.bsu_contest.components

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.example.bsu_contest.MainActivity
import com.example.bsu_contest.R
import com.example.bsu_contest.ui.theme.BlueBsu
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

//@Preview
@Composable
fun LoginScreen(context:Context, pref : SharedPreferences? = null){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        /* Логотип БГУ */
        Image(
            painter = painterResource(id = R.drawable.logo_intro),
            contentDescription = "BSU logo",
            //contentScale = ContentScale.Crop,
            //modifier = Modifier.fillMaxHeight(0.25f
            modifier = Modifier
                .height(200.dp)
                .width(200.dp)
        )

        /* Выводится название приложения */
        Text(
            modifier = Modifier
                .padding(20.dp)
            ,
            color = BlueBsu,
            text = stringResource(id = R.string.app_name),
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )

        val login = remember{mutableStateOf("")}

        /* Поле логина */
        TextField(
            modifier = Modifier
                .fillMaxWidth(0.75f),
            value = login.value,
            onValueChange = { login.value = it },
            singleLine = true,
            textStyle = TextStyle(fontSize = 28.sp),
            placeholder = {
                    Text(
                        color = Color.Gray,
                        text = "Логин",
                        fontSize = 28.sp)
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                unfocusedTextColor = Color(0xff888888),
                focusedContainerColor = Color.White,
                focusedTextColor = Color(0xff222222),
            )
        )

        val password = remember{mutableStateOf("")}

        /* Поле пароля */
        TextField(
            modifier = Modifier
                .fillMaxWidth(0.75f),
            value = password.value,
            onValueChange = {password.value = it},
            singleLine = true,
            textStyle = TextStyle(fontSize = 28.sp),
            placeholder = {
                Text(
                    color = Color.Gray,
                    text = "Пароль",
                    fontSize = 28.sp)
            },
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                unfocusedTextColor = Color(0xff888888),
                focusedContainerColor = Color.White,
                focusedTextColor = Color(0xff222222),
            )
        )

        /* Кнопки "забыли пароль?" и "войти" */
        Row(
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            TextButton(onClick = { /*TODO*/ }) {
                Text(text = "Забыли пароль?")
            }


            val showLoginPasswordPopUp = remember {mutableStateOf(false)}
            val showInvalidDataPopUp = remember {mutableStateOf(false)}
            val showProgressCirle = remember {mutableStateOf(false)}

            Button(
                colors = ButtonDefaults.buttonColors(containerColor = BlueBsu),
                onClick = {

                    /* В onClick нельзя использовать Composable функции поэтому сделано так */
                    if(login.value == "" || password.value == ""){
                        showLoginPasswordPopUp.value = true
                    }
                    else{
                        showProgressCirle.value = true

                        /* Корутина для получения токена */
                        CoroutineScope(Dispatchers.IO).launch() {

                            /* Искуственно замедлил (для демонстрации, чтобы колесико покрутилось подольше) */
                            Thread.sleep(3000)

                            /* По хорошему тут тоже использовать библиотеку Retrofit,
                            *  но у нас возвращается просто строка, а не пары ключ:значение, поэтому сделал так */
                            var reqParam = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(login.value, "UTF-8")
                            reqParam += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password.value, "UTF-8")
                            reqParam += "&" + URLEncoder.encode("device_name", "UTF-8") + "=" + URLEncoder.encode("cowPhone", "UTF-8")
                            val mURL = URL("https://webcomp.bsu.ru/sanctum/token")

                            with(mURL.openConnection() as HttpURLConnection) {
                                requestMethod = "POST"

                                val wr = OutputStreamWriter(getOutputStream());
                                wr.write(reqParam);
                                wr.flush();

                                println("URL : $url")
                                println("Response Code : $responseCode")

                                BufferedReader(InputStreamReader(inputStream)).use {
                                    val response = StringBuffer()

                                    var inputLine = it.readLine()
                                    while (inputLine != null) {
                                        response.append(inputLine)
                                        inputLine = it.readLine()
                                    }
                                    println("Response : $response")

                                    /* т.к. сервер не возвращает 404, а возвращает 200 и html в body */
                                    if(!response.toString().contains("<!DOCTYPE html>")){

                                        /* Сохраняем токен в хранилище приложения */
                                        val editor = pref?.edit()
                                        editor?.putString("Bearer-Token", "Bearer " + response.toString())
                                        editor?.apply()

                                        showProgressCirle.value = false

                                        /* Возвращаемся на стартовый экран (выглядит как костыль) */
                                        val intent = Intent(context, MainActivity::class.java)

                                        (context as Activity).finish()
                                        ContextCompat.startActivity(context, intent, null)
                                    }
                                    else{

                                        /* Выводим что данные неккоректны */
                                        showProgressCirle.value = false
                                        showInvalidDataPopUp.value = true
                                    }
                                }
                            }
                        }
                    }
                }) {
                Text(text="Войти")
            }

            /* Всплывающее окно, если пользователь не ввел логин или пароль */
            if(showLoginPasswordPopUp.value){
                AlertDialog(
                    modifier = Modifier.height(240.dp),
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text="Ошибка авторизации")
                        }
                    },
                    text = {
                        Text(text="Поля логин и пароль не должны быть пустыми")
                    },

                    onDismissRequest = { /*showPopUp.value = false*/ },
                    confirmButton = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){

                            Button(
                                colors = ButtonDefaults.buttonColors(containerColor = BlueBsu),
                                onClick = { showLoginPasswordPopUp.value = false
                                }) {
                                Text(text="Понятно")
                            }
                        }
                    })
            }

            /* Всплывающее окно, если пользователь ввел некорректные данные */
            if(showInvalidDataPopUp.value){
                AlertDialog(
                    modifier = Modifier.height(240.dp),
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text="Ошибка авторизации")
                        }
                    },
                    text = {
                        Text(text="Попытка авторизации провалилась, проверьте введеные данные")
                    },

                    onDismissRequest = { /*showPopUp.value = false*/ },
                    confirmButton = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){

                            Button(
                                colors = ButtonDefaults.buttonColors(containerColor = BlueBsu),
                                onClick = { showInvalidDataPopUp.value = false
                                }) {
                                Text(text="Понятно")
                            }
                        }
                    }
                )
            }

            /* Крутим колесо пока сервер возвращает токен */
            if(showProgressCirle.value){
                Dialog(onDismissRequest = { /*showProgressCirle.value = false*/ }) {
                    CircularProgressIndicator(color = BlueBsu)
                }
            }
        }

        /* Кнопочка для перехода на экран регистрации (пока не реализовано тк в API этого нет) */
        TextButton(onClick = { /*TODO*/ }) {
            Text(
                color = BlueBsu,
                textAlign = TextAlign.Center,
                text = "Еще не зарегистрированны?\nЗарегистрироваться")
        }
    }
}