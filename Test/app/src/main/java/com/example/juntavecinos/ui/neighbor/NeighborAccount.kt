package com.example.juntavecinos.ui.neighbor

import androidx.compose.foundation.layout.Arrangement
import com.example.juntavecinos.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.juntavecinos.ui.theme.JuntaVecinosTheme

@Composable
fun NeighborAccount(
    onLogOff : () -> Unit
){
    var showLogOffDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = stringResource(R.string.accountName),
                modifier = Modifier
                    .height(70.dp)
                    .width(70.dp)
            )
            BasicText(
                text = "[Nombre de usuario]",
                style = TextStyle(
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Left
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Email,
                contentDescription = stringResource(R.string.accountEmail),
                modifier = Modifier
                    .height(70.dp)
                    .width(70.dp)
            )
            BasicText(
                text = "[Email]",
                style = TextStyle(
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Left
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Groups,
                contentDescription = stringResource(R.string.accountCommunity),
                modifier = Modifier
                    .height(70.dp)
                    .width(70.dp)
            )
            BasicText(
                text = "[Nombre de junta]",
                style = TextStyle(
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Left
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = { showLogOffDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RectangleShape
        ){
            BasicText(
                text = stringResource(R.string.logoff),
                style = TextStyle(
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center
                )
            )
        }
    }


    if (showLogOffDialog) {
        AlertDialog(
            onDismissRequest = { showLogOffDialog = false },
            containerColor = MaterialTheme.colorScheme.primary,
            title = {
                BasicText(
                    text = stringResource(R.string.logoff),
                    style = TextStyle(
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            text = {
                BasicText(
                    text = stringResource(R.string.reallyLogoff),
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogOffDialog = false
                        onLogOff()
                    }
                ) {
                    BasicText(
                        text = stringResource(R.string.confirm),
                        style = TextStyle(color = MaterialTheme.colorScheme.onPrimary)
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogOffDialog = false }) {
                    BasicText(
                        text = stringResource(R.string.cancel),
                        style = TextStyle(color = MaterialTheme.colorScheme.onPrimary)
                    )
                }
            },


        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNeighborAccount(){
    JuntaVecinosTheme() {
        NeighborAccount {  }
    }
}