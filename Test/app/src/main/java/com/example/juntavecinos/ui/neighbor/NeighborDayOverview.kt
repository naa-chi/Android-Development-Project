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
import androidx.compose.material3.Button
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.RectangleShape
import com.example.juntavecinos.persistance.AppRepository
import com.example.juntavecinos.persistance.dataModel.DayEventData
import kotlinx.coroutines.launch
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NeighborDayOverview(
    selectedDate: LocalDate = LocalDate.now(),
    repository: AppRepository?,
    onReserveHour : () -> Unit
) {
    val hourRowHeight = 64.dp
    val startHour = 8
    val endHour = 23
    val totalHours = endHour - startHour
    var uiEvents by remember { mutableStateOf<List<DayEventData>>(emptyList()) }
    val scope = rememberCoroutineScope()
    val formattedDate = remember(selectedDate) {
        val formatter = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM", Locale.getDefault())
        selectedDate.format(formatter).replaceFirstChar { it.uppercase() }
    }



    LaunchedEffect(selectedDate) {
        scope.launch {
            // Calculate start and end of the day in milliseconds
            if (repository == null) return@launch

            val startOfDay = selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val endOfDay = selectedDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() - 1

            val dbEvents = repository.getEventsInDay(startOfDay, endOfDay)

            uiEvents = dbEvents.map { event ->
                val startTime = java.time.Instant.ofEpochMilli(event.startTime)
                    .atZone(ZoneId.systemDefault())
                    .toInstant().toEpochMilli()

                val endTime = java.time.Instant.ofEpochMilli(event.endTime)
                    .atZone(ZoneId.systemDefault())
                    .toInstant().toEpochMilli() - 1

                DayEventData(
                    title = event.title,
                    hoster = event.hoster,
                    description = event.description,
                    startTime = startTime,
                    endTime = endTime
                )
            }
        }
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
        )
        {
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
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        )
        {
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

                uiEvents.forEach { event ->
                    // Convert Long back to LocalTime ONLY for calculation/display
                    val startLocalTime = java.time.Instant.ofEpochMilli(event.startTime)
                        .atZone(ZoneId.systemDefault())
                        .toLocalTime()

                    val endLocalTime = java.time.Instant.ofEpochMilli(event.endTime)
                        .atZone(ZoneId.systemDefault())
                        .toLocalTime()

                    val startOffsetMinutes = ((startLocalTime.hour - startHour) * 60) + startLocalTime.minute
                    val durationMinutes = ((endLocalTime.hour - startLocalTime.hour) * 60) +
                            (endLocalTime.minute - startLocalTime.minute)

                    val topOffset = (hourRowHeight * startOffsetMinutes.toFloat() / 60f)
                    val cardHeight = (hourRowHeight * durationMinutes.toFloat() / 60f)

                    val shape = RoundedCornerShape(8.dp)
                    val timeSpanText = "${startLocalTime.format(DateTimeFormatter.ofPattern("HH:mm"))} - ${endLocalTime.format(DateTimeFormatter.ofPattern("HH:mm"))}"

                    NeighborDayEventData(
                        topOffset = topOffset,
                        cardHeight = cardHeight,
                        shape = shape,
                        event = event, // Pass the UI model with Longs
                        timeSpanText = timeSpanText
                    )
                }
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            onClick = onReserveHour,
            shape = RectangleShape
        ){
            BasicText(
                text = stringResource(R.string.calendarReserveHour),
                style = TextStyle(
                    fontSize = 25.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )
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
            null,
            {}
        )
    }
}