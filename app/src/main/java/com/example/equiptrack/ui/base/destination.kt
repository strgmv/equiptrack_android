package com.example.equiptrack.ui.base

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import com.example.equiptrack.R
//import androidx.compose.material.icons.outlined.AccountCircle
//import androidx.compose.material.icons.outlined.Add
//import androidx.compose.material.icons.outlined.Build
//import androidx.compose.material.icons.outlined.Home
//import androidx.compose.material.icons.outlined.Info
//import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector


sealed class Route(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector,
    val routeWithoutArgs: String = route
) {
    object AvailableEquipments :
        Route("equipments", R.string.equipments, Icons.Outlined.Add)
    object MyEquipments : Route("user_equipments", R.string.my_equip, Icons.Outlined.Home)
    object Settings : Route("settings", R.string.settings, Icons.Outlined.Settings)
    object Equipment : Route("equipment/{equipment_id}", R.string.equipment, Icons.Outlined.Info, "equipment")
    object Authorization : Route("authorization", R.string.auth, Icons.Outlined.Add)
    object UserAdministration : Route("user_administration", R.string.admin_user_menu, Icons.Outlined.AccountCircle)
    object EquipmentAdministration : Route("equip_admin", R.string.admin_equipments_menu, Icons.Outlined.Build)
    object EquipmentReserve : Route("reserve/{equipment_id}", R.string.reserve_route, Icons.Outlined.CalendarMonth)
    object RegisterUser : Route("register", R.string.register_route, Icons.Outlined.CalendarMonth)
    object CreateEquipment : Route("create_equip", R.string.create_equip, Icons.Outlined.CalendarMonth)
    object UpdateEquipment : Route("update_equip/{equipment_id}", R.string.create_equip, Icons.Outlined.CalendarMonth)
    object QrScreen : Route("qr/{equipment_id}", R.string.create_equip, Icons.Outlined.CalendarMonth)
}

val bottomBarScreens = listOf(
    Route.AvailableEquipments,
    Route.MyEquipments,
    Route.Settings,
)

val adminBottomBarScreen = listOf(
    Route.AvailableEquipments,
    Route.MyEquipments,
    Route.UserAdministration,
    Route.EquipmentAdministration,
    Route.Settings,
)
