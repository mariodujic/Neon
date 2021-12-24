package com.zero.neon.navigation

sealed class Navigation(val route: String)
object Game : Navigation(route = "game")
object GamePause : Navigation(route = "game-break")