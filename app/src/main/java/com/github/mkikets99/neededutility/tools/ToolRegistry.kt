package com.github.mkikets99.neededutility.tools

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.automirrored.filled.StickyNote2
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
    HEALTH("Health"),
    PRODUCTIVITY("Productivity"),
    TIME("Time"),
    DEVICE("Device"),
    FUN("Fun & Random"),
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
            id = "percentage",
            title = "Percentage",
            description = "P% of X, what %, change",
            icon = Icons.Filled.PieChart,
            group = ToolGroup.CALCULATIONS,
            route = "percentage",
        ),
        Tool(
            id = "base_converter",
            title = "Base Converter",
            description = "Binary, octal, decimal, hex",
            icon = Icons.Filled.Memory,
            group = ToolGroup.CALCULATIONS,
            route = "bases",
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
            id = "discount",
            title = "Discount",
            description = "Sale price and savings",
            icon = Icons.Filled.Percent,
            group = ToolGroup.MONEY,
            route = "discount",
        ),
        Tool(
            id = "loan",
            title = "Loan Calculator",
            description = "EMI, interest and total cost",
            icon = Icons.Filled.AccountBalance,
            group = ToolGroup.MONEY,
            route = "loan",
        ),
        Tool(
            id = "unit_price",
            title = "Unit Price",
            description = "Compare pack value",
            icon = Icons.Filled.ShoppingCart,
            group = ToolGroup.MONEY,
            route = "unitprice",
        ),
        Tool(
            id = "bmi",
            title = "BMI Calculator",
            description = "Body mass index & range",
            icon = Icons.Filled.Favorite,
            group = ToolGroup.HEALTH,
            route = "bmi",
        ),
        Tool(
            id = "notes",
            title = "Notes",
            description = "Quick local notes",
            icon = Icons.AutoMirrored.Filled.StickyNote2,
            group = ToolGroup.PRODUCTIVITY,
            route = "notes",
        ),
        Tool(
            id = "text_toolkit",
            title = "Text Toolkit",
            description = "Count, cases, reverse",
            icon = Icons.Filled.SortByAlpha,
            group = ToolGroup.PRODUCTIVITY,
            route = "text",
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
            id = "world_clock",
            title = "World Clock",
            description = "Times around the world",
            icon = Icons.Filled.Public,
            group = ToolGroup.TIME,
            route = "worldclock",
        ),
        Tool(
            id = "torch",
            title = "Flashlight",
            description = "Toggle the camera torch",
            icon = Icons.Filled.FlashlightOn,
            group = ToolGroup.DEVICE,
            route = "torch",
        ),
        Tool(
            id = "qr_scanner",
            title = "QR Scanner",
            description = "Scan QR codes with the camera",
            icon = Icons.Filled.QrCodeScanner,
            group = ToolGroup.DEVICE,
            route = "qr_scan",
        ),
        Tool(
            id = "qr_generator",
            title = "QR Generator",
            description = "Turn text into a QR code",
            icon = Icons.Filled.QrCode,
            group = ToolGroup.DEVICE,
            route = "qr_generate",
        ),
        Tool(
            id = "compass",
            title = "Compass",
            description = "Magnetic heading",
            icon = Icons.Filled.Explore,
            group = ToolGroup.DEVICE,
            route = "compass",
        ),
        Tool(
            id = "bubble_level",
            title = "Bubble Level",
            description = "Surface level tool",
            icon = Icons.Filled.Straighten,
            group = ToolGroup.DEVICE,
            route = "level",
        ),
        Tool(
            id = "randomizer",
            title = "Randomizer",
            description = "Dice, coin, numbers & passwords",
            icon = Icons.Filled.Casino,
            group = ToolGroup.FUN,
            route = "randomizer",
        ),
        Tool(
            id = "metronome",
            title = "Metronome",
            description = "Beat at any tempo",
            icon = Icons.Filled.GraphicEq,
            group = ToolGroup.FUN,
            route = "metronome",
        ),
        Tool(
            id = "morse_code",
            title = "Morse Code",
            description = "Encode & decode morse",
            icon = Icons.Filled.Code,
            group = ToolGroup.FUN,
            route = "morse",
        ),
    )
}
