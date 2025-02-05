package com.example.equiptrack.ui.base

import androidmads.library.qrgenearator.QRGContents
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.compose.composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.equiptrack.ui.screens.AuthorizationScreen
import com.example.equiptrack.ui.screens.AvailableEquipmentsScreen
import com.example.equiptrack.ui.screens.CreateEquipmentScreen
import com.example.equiptrack.ui.screens.CreateUserScreen
import com.example.equiptrack.ui.screens.DetailedEquipmentScreen
import com.example.equiptrack.ui.screens.ManageEquipmentsScreen
import com.example.equiptrack.ui.screens.ManageUsersScreen
import com.example.equiptrack.ui.screens.MyEquipmentsScreen
import com.example.equiptrack.ui.screens.QrScreen
import com.example.equiptrack.ui.screens.ReservationScreen
import com.example.equiptrack.ui.screens.SettingsScreen
import com.example.equiptrack.viewmodels.AuthorizationVewModel

@Composable
fun EquipTrackNavHost(
    viewModel: AuthorizationVewModel = hiltViewModel()
) {
    val navController = rememberNavController()
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
    val startScreen = if (viewModel.userIdState.value != "") Route.MyEquipments
        else Route.Authorization
    val bottomMenuList = if (viewModel.userRoleState.value == "admin") adminBottomBarScreen
        else bottomBarScreens

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = bottomBarState.value,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
                content = {
                    EquipmentBottomNavigation(
//                        currentScreen = currentScreen,
                        navController = navController,
                        routeList = bottomMenuList
                    )
                }
            )
        }
    ) {
        EquipTrackNavHost(
            modifier = Modifier.padding(it),
            navController = navController,
            startScreen,
            bottomBarState
        )
    }
}

@Composable
private fun EquipTrackNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startScreen: Route,
    bottomBarState: MutableState<Boolean>
) {
    NavHost(
        navController = navController,
        startDestination = startScreen.route,
        modifier = modifier
    ) {
        composable(
            Route.MyEquipments.route,
        ) {
            bottomBarState.value = true
            MyEquipmentsScreen(navHostController = navController)
        }
        composable(route = Route.AvailableEquipments.route) {
            bottomBarState.value = true
            AvailableEquipmentsScreen(navHostController = navController)
        }
        composable(route = Route.Settings.route) {
            bottomBarState.value = true
            SettingsScreen(navController)
        }
        composable(
            route = Route.Equipment.route,
            arguments = listOf(navArgument("equipment_id") { type = NavType.StringType })
        ) { backStackEntry ->
            bottomBarState.value = true
            backStackEntry.arguments?.getString("equipment_id")?.let { id ->
                DetailedEquipmentScreen(
                    navHostController =  navController,
                    equipmentId =  id
                )
            }
        }
        composable(route = Route.Authorization.route) {
            bottomBarState.value = false
            AuthorizationScreen(navController)
        }
        composable(route = Route.UserAdministration.route) {
            bottomBarState.value = true
            ManageUsersScreen(navController)
        }
        composable(route = Route.EquipmentAdministration.route) {
            bottomBarState.value = true
            ManageEquipmentsScreen(navController)
        }
        composable(route= Route.RegisterUser.route) {
            bottomBarState.value = true
            CreateUserScreen(navController)
        }
        composable(route= Route.CreateEquipment.route) {
            bottomBarState.value = true
            CreateEquipmentScreen(navController)
        }
        composable(
            route = Route.EquipmentReserve.route,
            arguments = listOf(navArgument("equipment_id") { type = NavType.StringType })
        ) { backStackEntry ->
            bottomBarState.value = true
            backStackEntry.arguments?.getString("equipment_id")?.let { id ->
                ReservationScreen(
//                    navHostController =  navController,
                    equipmentId =  id
                )
            }
        }
        composable(
            route = Route.UpdateEquipment.route,
            arguments = listOf(navArgument("equipment_id") { type = NavType.StringType })
        ) { backStackEntry ->
            bottomBarState.value = true
            backStackEntry.arguments?.getString("equipment_id")?.let { id ->
                CreateEquipmentScreen(
                    navHostController =  navController,
                    updateId = id
                )
            }
        }
        composable(
            route = Route.QrScreen.route,
            arguments = listOf(navArgument("equipment_id") { type = NavType.StringType })
        ) { backStackEntry ->
            bottomBarState.value = true
            backStackEntry.arguments?.getString("equipment_id")?.let { id ->
                QrScreen(
                    equipmentId = id
                )
            }
        }
    }
}


@Composable
fun EquipmentBottomNavigation(
    navController: NavHostController,
    routeList: List<Route>
) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currDest = navBackStackEntry?.destination
        routeList.forEach { screen ->
            NavigationBarItem(
//                selected = screen == currentScreen,
                selected = currDest?.hierarchy?.any { it.hasRoute(screen.route::class) } == true,
//                label = {
//                    Text(text = stringResource(id = screen.resourceId))
//                },
                icon = {
                    Icon(screen.icon, contentDescription = stringResource(id = screen.resourceId))
                },
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}