package com.example.juntavecinos.ui.Neighbor

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.juntavecinos.R
import com.example.juntavecinos.ui.theme.JuntaVecinosTheme

@Composable
fun NeighborMoneyReport(
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            BasicText(
                text = stringResource(R.string.neighborLastUpdatedMoney) + " 0m",
                style = TextStyle(
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                {},
                modifier = Modifier.size(46.dp),
                shape = RectangleShape,
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = stringResource(R.string.refresh)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp).fillMaxWidth())

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
                            color = MaterialTheme.colorScheme.surface
                        ),
                    )
                    BasicText(
                        text = "Costo: {costo}",
                        style = TextStyle(
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.surface
                        ),
                    )
                    BasicText(
                        text = "Realizado por: {nombre}",
                        style = TextStyle(
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.surface
                        ),
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp).fillMaxWidth())
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMoneyReport() {
    JuntaVecinosTheme {
        NeighborMoneyReport()
    }
}