package com.example.juntavecinos.ui.neighbor

import android.os.Build
import androidx.annotation.RequiresApi

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.juntavecinos.R
import com.example.juntavecinos.ui.theme.JuntaVecinosTheme
import com.example.juntavecinos.ui.theme.customColors
import java.time.YearMonth
import java.util.Locale
import java.time.format.TextStyle as JavaTextStyle


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NeighborMoneyReport(
    onExpensesDetailed : () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        //Gemini generated val
        var currentYearMonth by remember { mutableStateOf(YearMonth.now()) }
        val fullMonthName = currentYearMonth.month.getDisplayName(
            JavaTextStyle.FULL_STANDALONE,
            Locale.getDefault()
        ).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

        val totalBudgetLabel = stringResource(R.string.totalBudget)
        val expensesLabel = stringResource(R.string.moneyLost)
        val incomeLabel = stringResource(R.string.moneyGained)

        //TODO: Connect to DB to change this
        //Bet :3

        val budget = 0
        val expenses = 10
        val income = 10

        val budgetColor = if (budget > 0) MaterialTheme.customColors.greenText else MaterialTheme.customColors.redText
        val budgetText = buildAnnotatedString {
            append("$totalBudgetLabel ")
            withStyle(style = SpanStyle(color = budgetColor)) {
                append("$$budget")
            }
        }

        val expensesColor = if (expenses < 0) MaterialTheme.customColors.greenText else MaterialTheme.customColors.redText
        val expensesText = buildAnnotatedString {
            append("$expensesLabel ")
            withStyle(style = SpanStyle(color = expensesColor)) {
                append("$$expenses")
            }
        }

        val incomeColor = if (income > 0) MaterialTheme.customColors.greenText else MaterialTheme.customColors.redText
        val incomeText = buildAnnotatedString {
            append("$incomeLabel ")
            withStyle(style = SpanStyle(color = incomeColor)) {
                append("$$income")
            }
        }

        BasicText(
            text = stringResource(R.string.expensesResume) + " "+ fullMonthName,
            style = TextStyle(
                fontSize = 30.sp,
                textAlign = TextAlign.Left,
                color = MaterialTheme.colorScheme.onBackground
            ),
        )

        Spacer(Modifier.height(20.dp).fillMaxWidth())
        BasicText(
            text = budgetText,
            style = TextStyle(
                fontSize = 26.sp,
                textAlign = TextAlign.Left,
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(10.dp).fillMaxWidth())
        BasicText(
            text = incomeText,
            style = TextStyle(
                fontSize = 26.sp,
                textAlign = TextAlign.Left,
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(10.dp).fillMaxWidth())
        BasicText(
            text = expensesText,
            style = TextStyle(
                fontSize = 26.sp,
                textAlign = TextAlign.Left,
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            BasicText(
                text = stringResource(R.string.neighborLastUpdatedMoney) + " 0m",
                style = TextStyle(
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                {
                    //TODO: UPDATE NEIGHBOR EXPENSES DB
                },
                modifier = Modifier.size(50.dp),
                shape = RectangleShape,
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = stringResource(R.string.refresh),
                    modifier = Modifier.height(40.dp).width(40.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp).fillMaxWidth())
        Button(
            onExpensesDetailed,
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth(),
            shape = RectangleShape,
            contentPadding = PaddingValues(0.dp)
        ) {
            BasicText(
                text = stringResource(R.string.openDetailedExpenses),
                style = TextStyle(
                    fontSize = 26.sp,
                    textAlign = TextAlign.Left,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewMoneyReport() {
    JuntaVecinosTheme {
        NeighborMoneyReport({})
    }
}