package com.example.juntavecinos.ui.Neighbor

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.juntavecinos.ui.theme.JuntaVecinosTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.juntavecinos.R
import com.example.juntavecinos.ui.theme.Blue4

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BottomBarNeighbor(

) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf(
        stringResource(R.string.bottombarnavMoney),
        stringResource(R.string.bottombarnavCalendar),
        stringResource(R.string.bottombarnavProfile)
    )
    val icons = listOf(
        Icons.Filled.AttachMoney,
        Icons.Filled.CalendarMonth,
        Icons.Filled.Person)

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(icons[index],
                                contentDescription = item)
                       },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onBackground,
                            unselectedIconColor = MaterialTheme.colorScheme.surface
                        ),
                        label = {
                            BasicText(
                                text = item,
                                style = TextStyle(
                                    fontSize = 15.sp,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.surface
                                ),
                            )
                        },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            //TODO: Add click logic
                            when (item) {

                            }
                        }
                    )
                }
            }
        }
    ) {

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBottomBar() {
    JuntaVecinosTheme() {
        BottomBarNeighbor()
    }
}