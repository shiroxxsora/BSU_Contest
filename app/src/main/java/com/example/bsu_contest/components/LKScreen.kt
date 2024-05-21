package com.example.bsu_contest.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        modifier =  Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item{
            Column(
                modifier =  Modifier
                    .fillMaxWidth(0.95f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
            Row(modifier =  Modifier
                .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .height(220.dp),
                    painter = painterResource(id = R.drawable.avatar),
                    contentDescription = "Avatar"
                )

                Text(
                    text = team.team_name,
                    fontSize = 28.sp,
                    color = BlueBsu
                )
            }
                Spacer(
                    Modifier.height(30.dp)
                )
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