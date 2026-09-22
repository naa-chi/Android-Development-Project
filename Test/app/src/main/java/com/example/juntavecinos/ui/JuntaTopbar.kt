package com.example.juntavecinos.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.juntavecinos.R
import com.example.juntavecinos.ui.theme.JuntaVecinosTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JuntaTopbar (
    currentScreen: Navigation,
    loggedIn: Boolean,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(currentScreen.title),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
            ),
            navigationIcon = {
                if (canNavigateBack && !loggedIn) {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            painter = painterResource(R.drawable.ic_back_arrow),
                            contentDescription = stringResource(R.string.backButton)
                        )
                    }
                } else if (loggedIn) {
                    IconButton(onClick = { /* Open menu */ }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_menu),
                            contentDescription = stringResource(R.string.menuButton)
                        )
                    }
                }
            },
            actions = {
                if (loggedIn){
                    IconButton(onClick = { /* Account action */ }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_account),
                            contentDescription = stringResource(R.string.accountButton)
                        )
                    }
                }
            }
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(15.dp)
                .background(MaterialTheme.colorScheme.secondary)
        )
    }
}
@Preview(showBackground = true)
@Composable
fun JuntaTopbarPreview() {
    JuntaVecinosTheme() {
        JuntaTopbar(Navigation.LoginStarter, false, false, {})
    }
}