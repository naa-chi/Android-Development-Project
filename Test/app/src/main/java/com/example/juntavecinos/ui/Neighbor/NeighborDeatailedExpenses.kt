package com.example.juntavecinos.ui.Neighbor

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.juntavecinos.R
import com.example.juntavecinos.ui.theme.JuntaVecinosTheme
import java.time.YearMonth
import java.time.format.TextStyle as JavaTextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NeighborDetailedExpenses() {
    Column(Modifier.padding(12.dp)){
        //Gemini generated val
        var currentYearMonth by remember { mutableStateOf(YearMonth.now()) }
        val fullMonthName = currentYearMonth.month.getDisplayName(
            JavaTextStyle.FULL_STANDALONE,
            Locale.getDefault()
        ).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }


        BasicText(
            text = stringResource(R.string.neighborExpensesDetailed) + " "+ fullMonthName,
            style = TextStyle(
                fontSize = 30.sp,
                textAlign = TextAlign.Left,
                color = MaterialTheme.colorScheme.onBackground
            ),
        )
        Spacer(Modifier.height(12.dp).fillMaxWidth())


        Column(
            Modifier
                .verticalScroll(rememberScrollState())
        ) {
            repeat(25) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(MaterialTheme.colorScheme.onBackground)
                        .padding(12.dp)
                ){
                    Column() {
                        BasicText(
                            text = "Nombre del gasto: {nombre}",
                            style = TextStyle(
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.background
                            ),
                        )
                        BasicText(
                            text = "Costo: {costo}",
                            style = TextStyle(
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.background
                            ),
                        )
                        BasicText(
                            text = "Realizado por: {nombre}",
                            style = TextStyle(
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.background
                            ),
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp).fillMaxWidth())
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewDetailedExpenses(){
    JuntaVecinosTheme() {
        NeighborDetailedExpenses()
    }
}