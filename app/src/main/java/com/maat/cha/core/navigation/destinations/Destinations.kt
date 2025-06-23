package com.maat.cha.core.navigation.destinations

/**
 * Encapsulates destination's route information.
 *
 * @param route the unique route to the destination. To be used when navigating.
 * @param params the list of mandatory parameter names to add to the route to get the full route.
 * @property fullRoute the full route to the destination, including possible parameters. To be used when providing destinations inside NavGraph.
 */
sealed class Destination(val route: String, vararg params: String) {
    val fullRoute: String = if (params.isEmpty()) route else {
        val builder = StringBuilder(route)
        params.forEach { builder.append("/{$it}") }
        builder.toString()
    }

    protected fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }

    object Settings : Destination("settings")
    object Main : Destination("main")
    object Splash : Destination("splash")
    object Info : Destination("info/{type}", "type") {
        fun createRoute(type: InfoType) = withArgs(type.name)
    }
    object ReferenceInfo : Destination("reference_info/{type}/{source}", "type", "source") {
        fun createRoute(type: InfoType, source: ReferenceInfoSource = ReferenceInfoSource.ONBOARDING) = withArgs(type.name, source.name)
    }
    object Game : Destination("game")
}

enum class InfoType {
    HOW_TO_PLAY,
    TERMS_OF_USE,
    PRIVACY,
    PRIVACY_POLICY,
    TERMS_OF_USE_POLICY
}

enum class ReferenceInfoSource {
    ONBOARDING,
    MAIN,
    GAME,
    SETTINGS
}