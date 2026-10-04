package com.example.juntavecinos.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.juntavecinos.Model.AccountType
import com.example.juntavecinos.R
import com.example.juntavecinos.ui.Login.DirectiveLogin
import com.example.juntavecinos.ui.Login.LoginHoming
import com.example.juntavecinos.ui.Login.NeighborLogin
import com.example.juntavecinos.ui.Neighbor.NeighborBottomBar
import com.example.juntavecinos.ui.Neighbor.NeighborCalendar
import com.example.juntavecinos.ui.Neighbor.NeighborDetailedExpenses
import com.example.juntavecinos.ui.Neighbor.NeighborMoneyReport
import com.example.juntavecinos.ui.Neighbor.NeighborNavigation
import com.example.juntavecinos.ui.System.JuntaTopbar
import com.example.juntavecinos.ui.theme.JuntaVecinosTheme

enum class Navigation(val title: Int) {
    LoadingHandler(title = R.string.loadHandle),
    LoginStarter(title = R.string.appTitle),
    NeighborLogin(title = R.string.neighborLogin),
    DirectiveLogin(title = R.string.directiveLogin),
    NeighborMoney(title = R.string.neighborMoney),
    NeighborCalendar(title = R.string.neighborCalendar),
    NeighborProfile(title = R.string.neighborProfile),
    NeighborDetailedExpenses(title = R.string.openDetailedExpenses)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Main(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: Navigation.LoadingHandler.name
    val currentScreen = Navigation.valueOf(currentRoute)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            JuntaTopbar(
                currentScreen = currentScreen,
                loggedIn = false,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        },
        bottomBar = {
            when (currentRoute) {
                Navigation.NeighborMoney.name -> NeighborBottomBar(
                    NeighborNavigation.Money,
                    {selected ->
                        if (selected == NeighborNavigation.Calendar){
                            navController.navigate(Navigation.NeighborCalendar.name) {
                                popUpTo(currentRoute) { inclusive =true }
                                launchSingleTop = true
                            }
                        } else if (selected == NeighborNavigation.Profile){
                            navController.navigate(Navigation.NeighborProfile.name) {
                                popUpTo(currentRoute) { inclusive =true }
                                launchSingleTop = true
                            }
                        }
                    }
                )

                Navigation.NeighborCalendar.name -> NeighborBottomBar(
                    NeighborNavigation.Calendar,
                    {selected ->
                        if (selected == NeighborNavigation.Money){
                            navController.navigate(Navigation.NeighborMoney.name) {
                                popUpTo(currentRoute) { inclusive =true }
                                launchSingleTop = true
                            }
                        } else if (selected == NeighborNavigation.Profile){
                            navController.navigate(Navigation.NeighborProfile.name) {
                                popUpTo(currentRoute) { inclusive =true }
                                launchSingleTop = true
                            }
                        }
                    }
                )

                Navigation.NeighborProfile.name -> NeighborBottomBar(
                    NeighborNavigation.Profile,
                    {selected ->
                        if (selected == NeighborNavigation.Money){
                            navController.navigate(Navigation.NeighborMoney.name) {
                                popUpTo(currentRoute) { inclusive =true }
                                launchSingleTop = true
                            }
                        } else if (selected == NeighborNavigation.Calendar){
                            navController.navigate(Navigation.NeighborCalendar.name) {
                                popUpTo(currentRoute) { inclusive =true }
                                launchSingleTop = true
                            }
                        }
                    }
                )

            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Navigation.LoadingHandler.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Navigation.LoadingHandler.name) {
                LoadingHandler(
                    onLoggedIn = { accountType ->
                        if (accountType == AccountType.ADMIN) {

                        } else if (accountType == AccountType.NEIGHBOR) {
                            navController.navigate(Navigation.NeighborMoney.name) {
                                popUpTo(currentRoute) { inclusive =true }
                                launchSingleTop = true
                            }
                        }
                    },
                    onNotLoggedIn = {
                        navController.navigate(Navigation.LoginStarter.name) {
                            popUpTo(currentRoute) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(route = Navigation.LoginStarter.name) {
                LoginHoming(
                    onNavigateToNeighbor = {
                        navController.navigate(Navigation.NeighborLogin.name) {
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
                NeighborLogin({ ssn ->
                    navController.navigate(Navigation.NeighborMoney.name) {
                        //Delete all history
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                })
            }
            composable(route = Navigation.DirectiveLogin.name) {
                DirectiveLogin(
                    {isAdmin, code -> }
                )
            }

            composable(route = Navigation.NeighborDetailedExpenses.name){
                NeighborDetailedExpenses()
            }

            composable(route = Navigation.NeighborMoney.name) {
                NeighborMoneyReport({
                    navController.navigate(Navigation.NeighborDetailedExpenses.name) {
                        launchSingleTop = true
                    }
                })
            }

            composable(route = Navigation.NeighborCalendar.name) {
                NeighborCalendar({ date ->

                })
            }

            composable(route = Navigation.NeighborProfile.name) {

            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun MainPreview() {
    JuntaVecinosTheme {
        Main()
    }
}