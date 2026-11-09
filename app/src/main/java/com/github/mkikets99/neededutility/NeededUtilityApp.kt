package com.github.mkikets99.neededutility

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.mkikets99.neededutility.data.SettingsRepository
import com.github.mkikets99.neededutility.data.ThemeMode
import com.github.mkikets99.neededutility.data.ThemeSettings
import com.github.mkikets99.neededutility.data.ToolPreferences
import com.github.mkikets99.neededutility.tools.bases.BaseConverterScreen
import com.github.mkikets99.neededutility.tools.bmi.BmiScreen
import com.github.mkikets99.neededutility.tools.calculator.CalculatorScreen
import com.github.mkikets99.neededutility.tools.compass.CompassScreen
import com.github.mkikets99.neededutility.tools.converter.UnitConverterScreen
import com.github.mkikets99.neededutility.tools.discount.DiscountScreen
import com.github.mkikets99.neededutility.tools.level.LevelScreen
import com.github.mkikets99.neededutility.tools.loan.LoanScreen
import com.github.mkikets99.neededutility.tools.metronome.MetronomeScreen
import com.github.mkikets99.neededutility.tools.morse.MorseScreen
import com.github.mkikets99.neededutility.tools.notes.NotesScreen
import com.github.mkikets99.neededutility.tools.percentage.PercentageScreen
import com.github.mkikets99.neededutility.tools.qr.QrGeneratorScreen
import com.github.mkikets99.neededutility.tools.qr.QrScannerScreen
import com.github.mkikets99.neededutility.tools.randomizer.RandomizerScreen
import com.github.mkikets99.neededutility.tools.stopwatch.StopwatchScreen
import com.github.mkikets99.neededutility.tools.text.TextToolkitScreen
import com.github.mkikets99.neededutility.tools.torch.TorchScreen
import com.github.mkikets99.neededutility.tools.tip.TipCalculatorScreen
import com.github.mkikets99.neededutility.tools.unitprice.UnitPriceScreen
import com.github.mkikets99.neededutility.tools.worldclock.WorldClockScreen
import com.github.mkikets99.neededutility.ui.home.HomeScreen
import com.github.mkikets99.neededutility.ui.settings.SettingsScreen
import com.github.mkikets99.neededutility.ui.theme.NeededUtilityTheme

object Routes {
    const val HOME = "home"
    const val CONVERTER = "converter"
    const val CALCULATOR = "calculator"
    const val PERCENTAGE = "percentage"
    const val BASES = "bases"
    const val TIP = "tip"
    const val DISCOUNT = "discount"
    const val LOAN = "loan"
    const val UNITPRICE = "unitprice"
    const val BMI = "bmi"
    const val NOTES = "notes"
    const val TEXT = "text"
    const val STOPWATCH = "stopwatch"
    const val WORLDCLOCK = "worldclock"
    const val TORCH = "torch"
    const val QR_SCAN = "qr_scan"
    const val QR_GENERATE = "qr_generate"
    const val COMPASS = "compass"
    const val LEVEL = "level"
    const val RANDOMIZER = "randomizer"
    const val METRONOME = "metronome"
    const val MORSE = "morse"
    const val SETTINGS = "settings"
}

@Composable
fun NeededUtilityApp() {
    val context = LocalContext.current
    val settingsRepository = remember { SettingsRepository(context.applicationContext) }
    val toolPreferences = remember { ToolPreferences(context.applicationContext) }

    val settings by settingsRepository.settings
        .collectAsStateWithLifecycle(initialValue = ThemeSettings())

    val darkTheme = when (settings.mode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    NeededUtilityTheme(darkTheme = darkTheme, dynamicColor = settings.dynamicColor) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Routes.HOME) {
            composable(Routes.HOME) {
                HomeScreen(
                    onToolClick = { tool -> navController.navigate(tool.route) },
                    onSettingsClick = { navController.navigate(Routes.SETTINGS) }
                )
            }
            composable(Routes.CONVERTER) {
                UnitConverterScreen(
                    onBack = { navController.popBackStack() },
                    preferences = toolPreferences
                )
            }
            composable(Routes.CALCULATOR) {
                CalculatorScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.PERCENTAGE) {
                PercentageScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.BASES) {
                BaseConverterScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.TIP) {
                TipCalculatorScreen(
                    onBack = { navController.popBackStack() },
                    preferences = toolPreferences
                )
            }
            composable(Routes.DISCOUNT) {
                DiscountScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.LOAN) {
                LoanScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.UNITPRICE) {
                UnitPriceScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.BMI) {
                BmiScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.NOTES) {
                NotesScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.TEXT) {
                TextToolkitScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.STOPWATCH) {
                StopwatchScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.WORLDCLOCK) {
                WorldClockScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.TORCH) {
                TorchScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.QR_SCAN) {
                QrScannerScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.QR_GENERATE) {
                QrGeneratorScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.COMPASS) {
                CompassScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.LEVEL) {
                LevelScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.RANDOMIZER) {
                RandomizerScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.METRONOME) {
                MetronomeScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.MORSE) {
                MorseScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.SETTINGS) {
                SettingsScreen(
                    onBack = { navController.popBackStack() },
                    repository = settingsRepository
                )
            }
        }
    }
}
