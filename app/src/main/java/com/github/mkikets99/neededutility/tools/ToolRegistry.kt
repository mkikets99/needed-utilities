package com.github.mkikets99.neededutility.tools

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.ChangeHistory
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.LooksOne
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.automirrored.filled.StickyNote2
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.WbTwilight
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
            id = "roman_numerals",
            title = "Roman Numerals",
            description = "Number ↔ roman",
            icon = Icons.Filled.FormatListNumbered,
            group = ToolGroup.CALCULATIONS,
            route = "roman",
        ),
        Tool(
            id = "color_converter",
            title = "Color Converter",
            description = "Hex ↔ RGB with swatch",
            icon = Icons.Filled.Palette,
            group = ToolGroup.CALCULATIONS,
            route = "color",
        ),
        Tool(
            id = "speed_distance_time",
            title = "Speed · Distance · Time",
            description = "Any two → third",
            icon = Icons.Filled.Speed,
            group = ToolGroup.CALCULATIONS,
            route = "speed",
        ),
        Tool(
            id = "fractions",
            title = "Fractions",
            description = "Fraction ↔ decimal",
            icon = Icons.Filled.Functions,
            group = ToolGroup.CALCULATIONS,
            route = "fractions",
        ),
        Tool(
            id = "geometry",
            title = "Geometry",
            description = "Areas & perimeters",
            icon = Icons.Filled.ChangeHistory,
            group = ToolGroup.CALCULATIONS,
            route = "geometry",
        ),
        Tool(
            id = "prime_checker",
            title = "Prime Checker",
            description = "Primes & factorization",
            icon = Icons.Filled.LooksOne,
            group = ToolGroup.CALCULATIONS,
            route = "primes",
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
            id = "fuel",
            title = "Fuel Calculator",
            description = "Trip cost & consumption",
            icon = Icons.Filled.LocalGasStation,
            group = ToolGroup.MONEY,
            route = "fuel",
        ),
        Tool(
            id = "savings",
            title = "Savings & Interest",
            description = "Compound growth",
            icon = Icons.Filled.Savings,
            group = ToolGroup.MONEY,
            route = "savings",
        ),
        Tool(
            id = "electricity",
            title = "Electricity Cost",
            description = "Appliance running cost",
            icon = Icons.Filled.Bolt,
            group = ToolGroup.MONEY,
            route = "electricity",
        ),
        Tool(
            id = "trip_expenses",
            title = "Trip & Expenses",
            description = "Track & split costs",
            icon = Icons.Filled.Luggage,
            group = ToolGroup.MONEY,
            route = "trip",
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
            id = "age",
            title = "Age Calculator",
            description = "Age & next birthday",
            icon = Icons.Filled.Cake,
            group = ToolGroup.HEALTH,
            route = "age",
        ),
        Tool(
            id = "bmr",
            title = "Daily Calories",
            description = "BMR & maintenance",
            icon = Icons.Filled.LocalFireDepartment,
            group = ToolGroup.HEALTH,
            route = "bmr",
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
            id = "gpa",
            title = "GPA Calculator",
            description = "US 4.0 weighted GPA",
            icon = Icons.Filled.School,
            group = ToolGroup.PRODUCTIVITY,
            route = "gpa",
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
            id = "date_diff",
            title = "Date Diff",
            description = "Days between dates",
            icon = Icons.Filled.CalendarMonth,
            group = ToolGroup.TIME,
            route = "datediff",
        ),
        Tool(
            id = "countdown",
            title = "Countdown",
            description = "Days until your events",
            icon = Icons.Filled.HourglassBottom,
            group = ToolGroup.TIME,
            route = "countdown",
        ),
        Tool(
            id = "sunrise",
            title = "Sunrise & Sunset",
            description = "Solar times anywhere",
            icon = Icons.Filled.WbTwilight,
            group = ToolGroup.TIME,
            route = "sunrise",
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
        Tool(
            id = "team_splitter",
            title = "Team Splitter",
            description = "Random balanced teams",
            icon = Icons.Filled.Groups,
            group = ToolGroup.FUN,
            route = "teams",
        ),
    )
}
