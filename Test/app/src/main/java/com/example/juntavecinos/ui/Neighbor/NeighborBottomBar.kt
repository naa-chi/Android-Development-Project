package com.example.juntavecinos.ui.Neighbor

import androidx.annotation.StringRes
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.juntavecinos.R
import com.example.juntavecinos.ui.theme.JuntaVecinosTheme

enum class NeighborNavigation(
    @param:StringRes val labelRes: Int,
    val icon: ImageVector
) {
    Money(R.string.bottombarnavMoney, Icons.Filled.AttachMoney),
    Calendar(R.string.bottombarnavCalendar, Icons.Filled.CalendarMonth),
    Profile(R.string.bottombarnavProfile, Icons.Filled.Person)
}

@Composable
fun NeighborBottomBar(
    currentSelection: NeighborNavigation,
    onNavigate: (NeighborNavigation) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        NeighborNavigation.entries.forEach { item ->
            val label = stringResource(item.labelRes)

            NavigationBarItem(
                selected = currentSelection == item,
                onClick = { onNavigate(item) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = label
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onBackground,
                    unselectedIconColor = MaterialTheme.colorScheme.surface
                ),
                label = {
                    BasicText(
                        text = label,
                        style = TextStyle(
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.surface
                        )
                    )
                }
            )
        }
    }
}

@Preview
@Composable
fun PreviewBottomBar() {
    JuntaVecinosTheme {
        NeighborBottomBar(
            currentSelection = NeighborNavigation.Money,
            onNavigate = {}
        )
    }
}