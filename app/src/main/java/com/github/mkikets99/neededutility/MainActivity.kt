package com.github.mkikets99.neededutility

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.github.mkikets99.neededutility.monetization.AdManager
import com.github.mkikets99.neededutility.monetization.BillingManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        AdManager.initialize(this)
        BillingManager.start(this)
        setContent {
            NeededUtilityApp()
        }
    }
}
