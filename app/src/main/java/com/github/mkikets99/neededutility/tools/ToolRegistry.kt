package com.github.mkikets99.neededutility.tools

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Timer
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Grouping of tools on the home screen. New tools can be added to existing
 * groups or to a new group by extending this enum and [ToolRegistry].
 */
enum class ToolGroup(val title: String) {
    CALCULATIONS("Calculations"),
    MONEY("Money"),
    TIME("Time"),
    DEVICE("Device"),
}

/**
 * Metadata describing one tool of the toolbox. [route] must match a route
 * registered in the NavHost (see NeededUtilityApp).
 */
data class Tool(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val group: ToolGroup,
    val route: String,
)

/**
 * Central registry of all tools. The home screen renders this list grouped by
 * [ToolGroup]; adding a new tool is a single new entry here plus a screen and
 * a NavHost route.
 */
object ToolRegistry {

    val tools: List<Tool> = listOf(
        Tool(
            id = "unit_converter",
            title = "Unit Converter",
            description = "Length, mass, temperature and volume",
            icon = Icons.Filled.SwapHoriz,
            group = ToolGroup.CALCULATIONS,
            route = "converter",
        ),
        Tool(
            id = "calculator",
            title = "Calculator",
            description = "Everyday arithmetic",
            icon = Icons.Filled.Calculate,
            group = ToolGroup.CALCULATIONS,
            route = "calculator",
        ),
        Tool(
            id = "tip_splitter",
            title = "Tip & Split",
            description = "Tip and bill splitting",
            icon = Icons.Filled.Payments,
            group = ToolGroup.MONEY,
            route = "tip",
        ),
        Tool(
            id = "stopwatch_timer",
            title = "Stopwatch & Timer",
            description = "Count up or count down",
            icon = Icons.Filled.Timer,
            group = ToolGroup.TIME,
            route = "stopwatch",
        ),
        Tool(
            id = "torch",
            title = "Flashlight",
            description = "Toggle the camera torch",
            icon = Icons.Filled.FlashlightOn,
            group = ToolGroup.DEVICE,
            route = "torch",
        ),
    )
}
