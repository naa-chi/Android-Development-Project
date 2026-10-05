package com.example.juntavecinos.ui.neighbor

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.juntavecinos.R
import com.example.juntavecinos.ui.theme.JuntaVecinosTheme
import com.example.juntavecinos.ui.theme.customColors
import java.time.YearMonth
import java.time.format.TextStyle as JavaTextStyle
import java.util.Locale


//NOTE: Originalmente hice un código manualmente para hacer
//las formas de los tickets, pero se veian horribles...
//asique le pedi a gemini que mejorara la forma, por lo que
//el código está mejorado con gemini.
class TicketShape(
    private val cornerRadius: Float,
    private val notchRadius: Float,
    private val notchPositionXRatio: Float = 0.80f
) : Shape
{
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            reset()
            val notchCenter = size.width * notchPositionXRatio

            moveTo(0f, cornerRadius)
            arcTo(Rect(0f, 0f, 2 * cornerRadius, 2 * cornerRadius), 180f, 90f, false)

            lineTo(notchCenter - notchRadius, 0f)
            arcTo(
                Rect(notchCenter - notchRadius, -notchRadius, notchCenter + notchRadius, notchRadius),
                180f,
                -180f,
                false
            )

            lineTo(size.width - cornerRadius, 0f)
            arcTo(Rect(size.width - 2 * cornerRadius, 0f, size.width, 2 * cornerRadius), 270f, 90f, false)

            lineTo(size.width, size.height - cornerRadius)
            arcTo(
                Rect(size.width - 2 * cornerRadius, size.height - 2 * cornerRadius, size.width, size.height),
                0f,
                90f,
                false
            )

            lineTo(notchCenter + notchRadius, size.height)
            arcTo(
                Rect(notchCenter - notchRadius, size.height - notchRadius, notchCenter + notchRadius, size.height + notchRadius),
                0f,
                -180f,
                false
            )

            lineTo(cornerRadius, size.height)
            arcTo(Rect(0f, size.height - 2 * cornerRadius, 2 * cornerRadius, size.height), 90f, 90f, false)

            close()
        }
        return Outline.Generic(path)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NeighborDetailedExpenses() {
    Column(Modifier.padding(12.dp)) {
        var currentYearMonth by remember { mutableStateOf(YearMonth.now()) }
        val fullMonthName = currentYearMonth.month.getDisplayName(
            JavaTextStyle.FULL_STANDALONE,
            Locale.getDefault()
        ).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

        BasicText(
            text = stringResource(R.string.neighborExpensesDetailed) + " " + fullMonthName,
            style = TextStyle(
                fontSize = 30.sp,
                textAlign = TextAlign.Left,
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(Modifier.height(12.dp).fillMaxWidth())

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            repeat(25) { index ->
                ExpenseTicketItem(index = index)
            }
        }
    }
}

@Composable
fun ExpenseTicketItem(
    index: Int,
    modifier: Modifier = Modifier
) {
    val ticketShape = remember { TicketShape(cornerRadius = 24f, notchRadius = 20f, notchPositionXRatio = 0.80f) }
    val dividerColor = Color.Black

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(ticketShape)
            .background(MaterialTheme.customColors.gray1)
    ) {
        Column(
            modifier = Modifier
                .weight(0.80f)
                .fillMaxHeight()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                BasicText(
                    text = stringResource(R.string.expenseName) + " [nombre]",
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Left,
                        color = MaterialTheme.customColors.gray3
                    )
                )

                BasicText(
                    text = stringResource(R.string.expenseAuthor) + " [nombre]",
                    style = TextStyle(
                        fontSize = 20.sp,
                        textAlign = TextAlign.Left,
                        color = MaterialTheme.customColors.gray3
                    )
                )
            }

            BasicText(
                text = stringResource(R.string.expenseCost) + " [valor]",
                style = TextStyle(
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Left,
                    color = Color.Black
                )
            )
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .drawBehind {
                    drawLine(
                        color = dividerColor,
                        start = Offset(0f, 16f),
                        end = Offset(0f, size.height - 16f),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f),
                        strokeWidth = 2.dp.toPx()
                    )
                }
        )

        Column(
            modifier = Modifier
                .weight(0.20f)
                .fillMaxHeight()
                .padding(vertical = 16.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val barWeights = remember(index) {
                    listOf(2f, 1f, 3f, 1f, 4f, 2f, 1f, 3f, 2f, 1f)
                }
                barWeights.forEach { weight ->
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(weight.dp)
                            .background(Color.Black)
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewDetailedExpenses() {
    JuntaVecinosTheme {
        NeighborDetailedExpenses()
    }
}