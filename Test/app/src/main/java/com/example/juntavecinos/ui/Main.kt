package com.example.juntavecinos.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.juntavecinos.Model.AccountType
import com.example.juntavecinos.Model.DayTimelineEvent
import com.example.juntavecinos.R
import com.example.juntavecinos.ui.login.DirectiveLogin
import com.example.juntavecinos.ui.login.LoginHoming
import com.example.juntavecinos.ui.login.NeighborLogin
import com.example.juntavecinos.ui.neighbor.NeighborAccount
import com.example.juntavecinos.ui.neighbor.NeighborBottomBar
import com.example.juntavecinos.ui.neighbor.NeighborCalendar
import com.example.juntavecinos.ui.neighbor.NeighborDayOverview
import com.example.juntavecinos.ui.neighbor.NeighborDetailedExpenses
import com.example.juntavecinos.ui.neighbor.NeighborMoneyReport
import com.example.juntavecinos.ui.neighbor.NeighborNavigation
import com.example.juntavecinos.ui.system.JuntaTopbar
import com.example.juntavecinos.ui.theme.JuntaVecinosTheme
import java.time.LocalDate
import java.time.LocalTime

enum class Navigation(val title: Int) {
    LoadingHandler(title = R.string.loadHandle),
    LoginStarter(title = R.string.appTitle),
    NeighborLogin(title = R.string.neighborLogin),
    DirectiveLogin(title = R.string.directiveLogin),
    NeighborMoney(title = R.string.neighborMoney),
    NeighborCalendar(title = R.string.neighborCalendar),
    NeighborProfile(title = R.string.neighborProfile),
    NeighborDetailedExpenses(title = R.string.openDetailedExpenses),
    NeighborCalendarDay(title = R.string.calendarDay)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Main(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rawRoute = backStackEntry?.destination?.route ?: Navigation.LoadingHandler.name
    val baseRoute = rawRoute.substringBefore("/")
    val currentScreen = Navigation.valueOf(baseRoute)

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
            when (baseRoute) {
                Navigation.NeighborMoney.name -> NeighborBottomBar(
                    NeighborNavigation.Money,
                    { selected ->
                        if (selected == NeighborNavigation.Calendar) {
                            navController.navigate(Navigation.NeighborCalendar.name) {
                                popUpTo(baseRoute) { inclusive = true }
                                launchSingleTop = true
                            }
                        } else if (selected == NeighborNavigation.Profile) {
                            navController.navigate(Navigation.NeighborProfile.name) {
                                popUpTo(baseRoute) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    }
                )

                Navigation.NeighborCalendar.name,
                Navigation.NeighborCalendarDay.name -> NeighborBottomBar(
                    NeighborNavigation.Calendar,
                    { selected ->
                        if (selected == NeighborNavigation.Money) {
                            navController.navigate(Navigation.NeighborMoney.name) {
                                popUpTo(baseRoute) { inclusive = true }
                                launchSingleTop = true
                            }
                        } else if (selected == NeighborNavigation.Profile) {
                            navController.navigate(Navigation.NeighborProfile.name) {
                                popUpTo(baseRoute) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    }
                )

                Navigation.NeighborProfile.name -> NeighborBottomBar(
                    NeighborNavigation.Profile,
                    { selected ->
                        if (selected == NeighborNavigation.Money) {
                            navController.navigate(Navigation.NeighborMoney.name) {
                                popUpTo(baseRoute) { inclusive = true }
                                launchSingleTop = true
                            }
                        } else if (selected == NeighborNavigation.Calendar) {
                            navController.navigate(Navigation.NeighborCalendar.name) {
                                popUpTo(baseRoute) { inclusive = true }
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
                        if (accountType == AccountType.NEIGHBOR) {
                            navController.navigate(Navigation.NeighborMoney.name) {
                                popUpTo(baseRoute) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    },
                    onNotLoggedIn = {
                        navController.navigate(Navigation.LoginStarter.name) {
                            popUpTo(baseRoute) { inclusive = true }
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
                NeighborLogin({
                    navController.navigate(Navigation.NeighborMoney.name) {
                        popUpTo(navController.graph.id) { inclusive = true }
                        launchSingleTop = true
                    }
                })
            }

            composable(route = Navigation.DirectiveLogin.name) {
                DirectiveLogin({ _, _ -> })
            }

            composable(route = Navigation.NeighborDetailedExpenses.name) {
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
                NeighborCalendar(
                    onDateSelected = { date ->
                        navController.navigate("${Navigation.NeighborCalendarDay.name}/$date") {
                            launchSingleTop = true
                        }
                    }
                )
            }

            //Gemini helped here, I had no idea we could
            //pass values like this... Just like a URL
            composable(
                route = "${Navigation.NeighborCalendarDay.name}/{selectedDate}",
                arguments = listOf(
                    navArgument("selectedDate") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val dateArg = backStackEntry.arguments?.getString("selectedDate")
                val selectedDate = dateArg?.let { LocalDate.parse(it) } ?: LocalDate.now()

                NeighborDayOverview(
                    selectedDate = selectedDate,
                    events = listOf(
                        DayTimelineEvent(
                            title = "Reunión de Seguridad",
                            hoster = "directiva@vecinos.cl",
                            startTime = LocalTime.of(9, 0),
                            endTime = LocalTime.of(11, 0)
                        ),
                        DayTimelineEvent(
                            title = "Cumpleaños Comunitario",
                            hoster = "vecino12@gmail.com",
                            startTime = LocalTime.of(14, 0),
                            endTime = LocalTime.of(17, 30)
                        ),
                        DayTimelineEvent(
                            title = "Taller de Reciclaje",
                            hoster = "tesoreria@vecinos.cl",
                            startTime = LocalTime.of(19, 0),
                            endTime = LocalTime.of(21, 0)
                        )
                    )
                )
            }

            composable(route = Navigation.NeighborProfile.name) {
                NeighborAccount {
                    navController.navigate(Navigation.LoginStarter.name) {
                        popUpTo(navController.graph.id) { inclusive = true }
                        launchSingleTop = true
                    }
                }
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