package com.example.juntavecinos.ui.neighbor

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.example.juntavecinos.ui.theme.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.juntavecinos.Model.DayEventType
import com.example.juntavecinos.R
import com.example.juntavecinos.ui.theme.JuntaVecinosTheme
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle as JavaTextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NeighborCalendar(
    onDateSelected: (LocalDate) -> Unit = {}
) {
    //remember to avoid the UI update wiping away the data
    var currentYearMonth by remember { mutableStateOf(YearMonth.now()) }

    val calendarDays by remember(currentYearMonth) {
        derivedStateOf {
            val daysInMonth = currentYearMonth.lengthOfMonth()
            val firstDayOfMonth = currentYearMonth.atDay(1)
            val dayOfWeekOffset = firstDayOfMonth.dayOfWeek.value - 1

            val list = mutableListOf<LocalDate?>()


            //for days of the previus month
            repeat(dayOfWeekOffset) {
                list.add(null)
            }

            for (day in 1..daysInMonth) {
                list.add(currentYearMonth.atDay(day))
            }

            list
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    )
    {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            IconButton(onClick = { currentYearMonth = currentYearMonth.minusMonths(1) }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.previousMonth)
                )
            }

            //Gemini generated, a bit of a mess
            val monthTitle = "${
                currentYearMonth.month.getDisplayName(JavaTextStyle.FULL, Locale.getDefault())
                    .replaceFirstChar { it.uppercase() }
            } ${currentYearMonth.year}"

            //ok back to human
            BasicText(
                text = monthTitle,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )

            IconButton(onClick = { currentYearMonth = currentYearMonth.plusMonths(1) }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = stringResource(R.string.nextMonth)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        )
        {
            DayOfWeek.entries.forEach { dayOfWeek ->
                BasicText(
                    text = dayOfWeek.getDisplayName(JavaTextStyle.SHORT, Locale.getDefault()),
                    modifier = Modifier.weight(1f),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        )
        {
            items(calendarDays) { date ->
                if (date == null) {
                    Box(modifier = Modifier.aspectRatio(1f))
                } else {
                    val dayType : DayEventType = DayEventType.random()

                    val buttonColor = when(dayType) {
                        DayEventType.Free -> Color.Transparent
                        DayEventType.ReservedAvailable -> ColorAvailable
                        DayEventType.ReservedFull -> ColorFull
                    }

                    val textColor = when(dayType) {
                        DayEventType.Free -> MaterialTheme.colorScheme.onSurface
                        DayEventType.ReservedAvailable -> Color.White
                        DayEventType.ReservedFull -> Color.White
                    }
                    val shape = RoundedCornerShape(8.dp)
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(shape)
                            .background(buttonColor)
                            .clickable {
                                onDateSelected(date)
                           }
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.onBackground,
                                shape = shape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        BasicText(
                            text = date.dayOfMonth.toString(),
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontWeight = if (dayType == DayEventType.Free) FontWeight.Normal else FontWeight.Bold,
                                color = textColor,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(30.dp).fillMaxWidth())
        Column(
            modifier = Modifier.padding(16.dp)
        )
        {
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .fillMaxWidth(),

            )
            {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Box(
                        Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .width(26.dp)
                            .height(26.dp)
                            .border(1.dp, MaterialTheme.colorScheme.onBackground)
                    )
                    Spacer(Modifier.width(12.dp))
                    BasicText(
                        text = stringResource(R.string.dayFree),
                        style = TextStyle(
                            fontSize = 18.sp,
                            textAlign = TextAlign.Left,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
            }

            Spacer(Modifier.height(12.dp).fillMaxWidth())

            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .fillMaxWidth(),

                )
            {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Box(
                        Modifier
                            .background(ColorAvailable)
                            .width(26.dp)
                            .height(26.dp)
                            .border(1.dp, Color.Black)
                    )
                    Spacer(Modifier.width(12.dp))
                    BasicText(
                        text = stringResource(R.string.dayAvailable),
                        style = TextStyle(
                            fontSize = 18.sp,
                            textAlign = TextAlign.Left,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
            }

            Spacer(Modifier.height(12.dp).fillMaxWidth())

            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .fillMaxWidth(),

                )
            {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Box(
                        Modifier
                            .background(ColorFull)
                            .width(26.dp)
                            .height(26.dp)
                            .border(1.dp, Color.Black)
                    )
                    Spacer(Modifier.width(12.dp))
                    BasicText(
                        text = stringResource(R.string.dayBusy),
                        style = TextStyle(
                            fontSize = 18.sp,
                            textAlign = TextAlign.Left,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
            }
        }
    }


}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewNeighborCalendar() {
    JuntaVecinosTheme {
        NeighborCalendar(
            {}
        )
    }
}