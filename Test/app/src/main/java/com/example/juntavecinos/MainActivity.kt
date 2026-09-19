package com.example.juntavecinos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.juntavecinos.ui.theme.*

enum class Navigation(@StringRes val title: Int) {
    LoginStarter(title = R.string.appTitle),
    NeighborLogin(title = R.string.neighborLogin),
    DirectiveLogin(title = R.string.directiveLogin)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JuntaVecinosTheme(dynamicColor = false) {
                Main()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JuntaAppBar(
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

@Composable
fun Main(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    //I have absolutely no idea what this does,
    val currentRoute = backStackEntry?.destination?.route ?: Navigation.LoginStarter.name
    val currentScreen = Navigation.valueOf(currentRoute)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            //Start our topbar
            JuntaAppBar(
                currentScreen = currentScreen,
                loggedIn = false,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Navigation.LoginStarter.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Navigation.LoginStarter.name) {
                LoginScreen(
                    onNavigateToNeighbor = {
                        navController.navigate(Navigation.NeighborLogin.name){
                            launchSingleTop = true
                        }
                    },
                    onNavigateToDirective = {
                        navController.navigate(Navigation.DirectiveLogin.name) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(route = Navigation.NeighborLogin.name) {
                NeighborLogin()
            }

            composable(route = Navigation.DirectiveLogin.name) {
                DirectiveLogin()
            }
        }
    }
}

@Composable
fun LoginScreen(
    onNavigateToNeighbor: () -> Unit,
    onNavigateToDirective: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.selectRole),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(50.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onNavigateToDirective,
                shape = RectangleShape,
                modifier = Modifier
                    .height(60.dp)
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.roleDirective),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Button(
                onClick = onNavigateToNeighbor,
                shape = RectangleShape,
                modifier = Modifier
                    .height(60.dp)
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.roleNeighbor),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun NeighborLogin(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            stringResource(R.string.loggingAsNeighbor),
            color = Blue4,
            style = MaterialTheme.typography.titleLarge
        )
        OutlinedTextField(
            state = rememberTextFieldState(),
            label = {
                Text(stringResource(R.string.neighborCode))
            }
        )
    }
}

@Composable
fun DirectiveLogin(
    modifier : Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            stringResource(R.string.directiveMasterCode),
            color = Blue4,
            style = MaterialTheme.typography.titleLarge
        )
        OutlinedTextField(
            state = rememberTextFieldState(),
            label = {
                Text(stringResource(R.string.directiveActivationCode))
            }
        )
        Spacer(Modifier.height(60.dp))
        Text(
            stringResource(R.string.directiveOtherwise),
            color = Blue4,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            stringResource(R.string.directiveLoginAsMemberOfDirective),
            color = Blue4,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        OutlinedTextField(
            state = rememberTextFieldState(),
            label = {
                Text(stringResource(R.string.directiveDirectiveCode))
            },
        )
    }
}