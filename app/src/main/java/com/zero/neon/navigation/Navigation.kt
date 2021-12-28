package com.zero.neon.navigation

sealed class Navigation(val route: String)
object Splash : Navigation(route = "splash")
object Game : Navigation(route = "game")
object GamePause : Navigation(route = "game-pause")