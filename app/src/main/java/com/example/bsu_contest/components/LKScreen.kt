package com.example.bsu_contest.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bsu_contest.R
import com.example.bsu_contest.models.MainApi
import com.example.bsu_contest.models.Team
import com.example.bsu_contest.ui.theme.BlueBsu

@Composable
fun LKScreen(context: Context, mainApi: MainApi, team: Team) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item{
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.95f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    modifier = Modifier
                        .height(220.dp)
                        .graphicsLayer {
                            shadowElevation = 6.dp.toPx()
                            shape = CircleShape
                            clip = true
                            ambientShadowColor = Color.Black
                            spotShadowColor = Color.Black
                        },
                    painter = painterResource(id = R.drawable.avatar),
                    contentDescription = "Avatar"
                )
                Text(
                    modifier = Modifier.padding(vertical = 25.dp),
                    text = team.team_name,
                    fontSize = 28.sp,
                    color = BlueBsu
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = BlueBsu)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(.97f)
                            .background(color = Color.White)
                    ) {
                        Text(
                            text = team.team_member1_name,
                            fontSize = 22.sp,
                            color = BlueBsu
                        )
                        Text(
                            text = team.team_member2_name,
                            fontSize = 22.sp,
                            color = BlueBsu
                        )
                        Text(
                            text = team.team_member3_name,
                            fontSize = 22.sp,
                            color = BlueBsu
                        )
                    }
                }
            }
        }
    }
}