package com.example.juntavecinos.ui.neighbor

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.juntavecinos.Model.DayTimelineEvent
import com.example.juntavecinos.R
import com.example.juntavecinos.ui.theme.ColorFull
import com.example.juntavecinos.ui.theme.JuntaVecinosTheme
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NeighborDayOverview(
    selectedDate: LocalDate = LocalDate.now(),
    events: List<DayTimelineEvent> = emptyList()
) {
    val hourRowHeight = 64.dp
    val startHour = 8
    val endHour = 23
    val totalHours = endHour - startHour

    val formattedDate = remember(selectedDate) {
        val formatter = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM", Locale.getDefault())
        selectedDate.format(formatter).replaceFirstChar { it.uppercase() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.CalendarMonth,
                contentDescription = stringResource(R.string.date),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(45.dp)
            )
            BasicText(
                text = formattedDate,
                style = TextStyle(
                    fontSize = 25.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }

        //Gemini assisted from here and below
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(hourRowHeight * totalHours)
            )
            {
                Column(modifier = Modifier.fillMaxSize()) {
                    for (hour in startHour until endHour) {
                        //show 1 hour
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(hourRowHeight),
                            verticalAlignment = Alignment.Top
                        ) {
                            //gemini helped with this, I couldn't figure out
                            //a nice way to show the text
                            BasicText(
                                text = String.format(Locale.getDefault(), "%02d:00", hour),
                                style = TextStyle(
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colorScheme.outline,
                                    textAlign = TextAlign.Start
                                ),
                                modifier = Modifier.width(48.dp)
                            )
                            //the looong line
                            //who would've thought there's a specific
                            //composable function for that?
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                                thickness = 1.dp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }

                events.forEach { event ->
                    //my god gemini carried HARD here I couldn't figure
                    //this out for the life of me
                    val startOffsetMinutes = ((event.startTime.hour - startHour) * 60) + event.startTime.minute
                    val durationMinutes = ((event.endTime.hour - event.startTime.hour) * 60) +
                            (event.endTime.minute - event.startTime.minute)

                    val topOffset = (hourRowHeight * startOffsetMinutes.toFloat() / 60f)
                    val cardHeight = (hourRowHeight * durationMinutes.toFloat() / 60f)

                    val shape = RoundedCornerShape(8.dp)
                    val timeSpanText = "${event.startTime.format(DateTimeFormatter.ofPattern("HH:mm"))} - ${event.endTime.format(DateTimeFormatter.ofPattern("HH:mm"))}"

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 54.dp, end = 4.dp)
                            .offset(y = topOffset)
                            .height(cardHeight)
                            .clip(shape)
                            .background(ColorFull.copy(alpha = 0.15f))
                            .border(1.5.dp, ColorFull, shape)
                            .padding(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                BasicText(
                                    text = event.title,
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                )
                                BasicText(
                                    text = "Organiza: ${event.hoster}",
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                                    )
                                )
                            }
                            BasicText(
                                text = timeSpanText,
                                style = TextStyle(
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = ColorFull
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewDayOverview() {
    JuntaVecinosTheme {
        NeighborDayOverview(
            selectedDate = LocalDate.now(),
            events = listOf(
                DayTimelineEvent(
                    title = "Reunión de Seguridad",
                    hoster = "directiva@vecinos.cl",
                    startTime = LocalTime.of(9, 0),
                    endTime = LocalTime.of(11, 0)
                ),
                DayTimelineEvent(
                    title = "Cumpleaños Comunitario",
                    hoster = "vecino12@gmail.com",
                    startTime = LocalTime.of(14, 0),
                    endTime = LocalTime.of(17, 30)
                ),
                DayTimelineEvent(
                    title = "Taller de Reciclaje",
                    hoster = "tesoreria@vecinos.cl",
                    startTime = LocalTime.of(19, 0),
                    endTime = LocalTime.of(21, 0)
                )
            )
        )
    }
}