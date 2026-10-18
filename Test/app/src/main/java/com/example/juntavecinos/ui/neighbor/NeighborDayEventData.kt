package com.example.juntavecinos.ui.neighbor

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.juntavecinos.Model.DayTimelineEvent
import com.example.juntavecinos.R
import com.example.juntavecinos.ui.theme.ColorFull
import com.example.juntavecinos.ui.theme.JuntaVecinosTheme
import java.time.LocalTime


@Composable
fun NeighborDayEventData(
    topOffset : Dp,
    cardHeight : Dp,
    shape : Shape,
    event : DayTimelineEvent,
    timeSpanText : String
) {
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
    )
    {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.weight(1f)
            ) {
                BasicText(
                    text = event.title,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )

                BasicText(
                    text = "${stringResource(R.string.eventOrganizer)}: ${event.hoster}",
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Bold
                    )
                )

                BasicText(
                    text = "${stringResource(R.string.eventDescription)}: ${event.description}",
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    ),
                    overflow = TextOverflow.Clip
                )
            }
            BasicText(
                text = timeSpanText,
                style = TextStyle(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ColorFull,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 25.dp)
                    .wrapContentHeight(align = Alignment.CenterVertically),

            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewNeighborEventData() {
    JuntaVecinosTheme() {
        NeighborDayEventData(
            0.dp,
            200.dp,
            RoundedCornerShape(8.dp),
            DayTimelineEvent(
                "Titulo",
                "Host",
                "DescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripciónDescripción",
                LocalTime.now(),
                LocalTime.now()
            ),
            "10:10 am - 10:10 pm"
        )
    }
}