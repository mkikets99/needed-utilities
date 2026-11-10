package com.github.mkikets99.neededutility.monetization

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mkikets99.neededutility.BuildConfig
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

/**
 * AdMob wiring (S-12). Keys come from ad-mob.txt via BuildConfig.
 * Ads are strictly best-effort: if anything fails to load, the app behaves
 * exactly as before (offline-first promise still holds for all tools).
 */
object AdManager {

    private var interstitialAd: InterstitialAd? = null

    fun initialize(context: Context) {
        val appContext = context.applicationContext
        MobileAds.initialize(appContext) {}
        loadInterstitial(appContext)
    }

    /** Interstitial due for this tool open? (see [AdFrequency]) */
    fun onToolOpened(): Boolean = AdFrequency.onToolOpened()

    /** Shows the interstitial if one is ready; never blocks navigation. */
    fun showInterstitialIfReady(activity: Activity) {
        if (ProState.isPro.value) return // Pro removes ads
        val ad = interstitialAd ?: return
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                interstitialAd = null
                loadInterstitial(activity.applicationContext)
            }

            override fun onAdFailedToShowFullScreenContent(error: com.google.android.gms.ads.AdError) {
                interstitialAd = null
                loadInterstitial(activity.applicationContext)
            }
        }
        ad.show(activity)
    }

    private fun loadInterstitial(context: Context) {
        if (BuildConfig.ADMOB_INTERSTITIAL_UNIT_ID.isBlank()) return
        InterstitialAd.load(
            context,
            BuildConfig.ADMOB_INTERSTITIAL_UNIT_ID,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                }
            },
        )
    }

    /**
     * Banner for the home screen (bottom). Collapses to zero height until an
     * ad actually loads — no gray placeholder boxes, nothing on failure.
     */
    @Composable
    fun HomeBanner(modifier: Modifier = Modifier) {
        if (BuildConfig.ADMOB_BANNER_UNIT_ID.isBlank()) return
        val isPro by ProState.isPro.collectAsState()
        if (isPro) return // Pro removes ads
        var loaded by remember { mutableStateOf(false) }
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(if (loaded) AdSize.BANNER.height.dp else 0.dp),
        ) {
            AndroidView(
                modifier = Modifier.fillMaxWidth(),
                factory = { context ->
                    AdView(context).apply {
                        setAdSize(AdSize.BANNER)
                        adUnitId = BuildConfig.ADMOB_BANNER_UNIT_ID
                        adListener = object : AdListener() {
                            override fun onAdLoaded() {
                                loaded = true
                            }

                            override fun onAdFailedToLoad(error: LoadAdError) {
                                loaded = false
                            }
                        }
                        loadAd(AdRequest.Builder().build())
                    }
                },
            )
        }
    }
}

/** Finds the hosting Activity from any Context (for interstitials). */
tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
