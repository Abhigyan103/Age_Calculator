package com.example.agecalculator

import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agecalculator.ui.theme.AgeCalculatorTheme
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.ZoneId
import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AgeCalculatorTheme {
                MyLayout()
            }
        }
    }
}

fun epochToLocalDate(epochMillis: Long): LocalDate {
    val instant = Instant.ofEpochMilli(epochMillis)
    val zoneId = ZoneId.systemDefault()
    return LocalDateTime.ofInstant(instant, zoneId).toLocalDate()
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun MyLayout() {
    val context = LocalContext.current
    var currentDate : LocalDate = LocalDate.now()
    val dateState = rememberDatePickerState()
    var epochTime = dateState.selectedDateMillis
    var selectedDate : LocalDate?
    var age:Period? = null
    if(epochTime!=null){
        selectedDate = epochToLocalDate(epochTime)
        age = Period.between(selectedDate,currentDate)
    }
    var showDialog by remember {mutableStateOf(false)}
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Image(painter = painterResource(R.drawable.android_small___2), contentDescription = null, contentScale = ContentScale.Crop)
        Text(text = "Age Calculator", fontSize = 20.sp, modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 20.dp), color = Color.White, fontWeight = FontWeight.Bold)
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            val gradientColors= listOf(Color(0xFF190032), Color(0xFFEB00FF))
            Button(onClick = {
                showDialog=true
            }, colors = ButtonDefaults.buttonColors().copy(containerColor = Color.Transparent),
                modifier = Modifier
                    .background(
                        brush = Brush.linearGradient(colors = gradientColors),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .shadow(
                        elevation = 15.dp,
                        shape = RoundedCornerShape(20.dp),
                        spotColor = Color.White
                    )
                    ) {
                Text(text = "Enter Your Birthdate", color = Color.White)
            }
            Spacer(modifier = Modifier.height(100.dp))

            if(age!=null)Text("Your Age is ${age.years} Years ${age.months} Months ${age.days} Days ")
            Spacer(modifier = Modifier.height(20.dp))
            AgeDisplay(age = age)
            if(showDialog){
                DatePickerDialog(onDismissRequest = { showDialog = false },
                    confirmButton = {
                        Button(onClick = {
                            showDialog = false
                        }) {
                            Text(text = "OK")

}
                    },
                    dismissButton = {
                        Button(onClick = {
                            showDialog = false
                        }) { Text(text = "Cancel") }
                    }
                ) {
                    DatePicker(state = dateState, showModeToggle = showDialog)
                }
            }
        }
    }
}

@Composable
fun AgeText(age:Period) {
    Text(text = "${age.years}", fontWeight = FontWeight.Black, fontSize = 50.sp, color = Color.White)
}

@Composable
fun AgeDisplay(age: Period?){
    Box(modifier = Modifier.clip(shape = RoundedCornerShape(20.dp))) {
        Row {
            Box(
                modifier = Modifier
                    .height(110.dp)
                    .width(125.dp)
                    .fillMaxSize()
                    .background(color = Color(if (age == null || age.years < 18) 0x85074905 else 0x8545FF41)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .align(alignment = Alignment.Center)
                        .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (age != null && age.years >= 18) Text(
                        text = "You are an",
                        color = Color.White
                    )
                    if (age != null && age.years >= 18) Text(
                        text = "ADULT",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp
                    )
                }
                if (age != null && age.years < 18) AgeText(age = age)
            }
            Box(
                modifier = Modifier
                    .height(110.dp)
                    .width(125.dp)
                    .fillMaxSize()
                    .background(color = Color(if (age != null && age.years < 18) 0xFFE03636 else 0x85500000)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .align(alignment = Alignment.Center)
                        .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (age != null && age.years < 18) Text(text = "You are a", color = Color.White)
                    if (age != null && age.years < 18) Text(
                        text = "MINOR",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp
                    )
                }
                if (age != null && age.years >= 18) AgeText(age = age)
            }

        }
    }
}

@Preview
@Composable
fun AgeDisplayPreview(){
    AgeDisplay(age = Period.ofYears(32))
}