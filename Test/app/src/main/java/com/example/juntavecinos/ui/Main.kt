package com.example.juntavecinos.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.juntavecinos.R
import com.example.juntavecinos.ui.theme.JuntaVecinosTheme


enum class Navigation(val title: Int) {
    LoadingHandler(title = R.string.loadHandle),
    LoginStarter(title = R.string.appTitle),
    NeighborLogin(title = R.string.neighborLogin),
    DirectiveLogin(title = R.string.directiveLogin)
}

@Composable
fun Main(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    //Start with any route tbh, changing this won't change anything
    val currentRoute = backStackEntry?.destination?.route ?: Navigation.LoginStarter.name
    val currentScreen = Navigation.valueOf(currentRoute)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            //Start our topbar
            JuntaTopbar(
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
            composable(route = Navigation.LoadingHandler.name) {
                LoadingHandler(
                    onLoggedIn = {},
                    onNotLoggedIn = {
                        navController.navigate(Navigation.LoginStarter.name) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(route = Navigation.LoginStarter.name) {
                LoginHoming(
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

@Preview(showBackground = true)
@Composable
fun MainPreview(){
    JuntaVecinosTheme() {
        Main()
    }
}