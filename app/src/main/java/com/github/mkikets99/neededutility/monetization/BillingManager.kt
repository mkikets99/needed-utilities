package com.github.mkikets99.neededutility.monetization

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Play Billing glue for the one-time "Pro" purchase (S-13).
 * API verified against com.android.billingclient:billing:9.1.0.
 *
 * Product: one-time managed product with id [PRO_PRODUCT_ID] — must exist in
 * the Play Console (define it when publishing; billing silently no-ops in
 * side-loaded builds without Play services).
 */
object BillingManager {

    const val PRO_PRODUCT_ID = "pro"

    private var billingClient: BillingClient? = null

    /** Call once from MainActivity. Loads cached Pro state, then syncs. */
    fun start(context: Context) {
        val appContext = context.applicationContext
        CoroutineScope(Dispatchers.IO).launch {
            ProRepository(appContext).setPro(ProRepository(appContext).isPro.first())
        }

        val client = BillingClient.newBuilder(appContext)
            .setListener { result, purchases ->
                if (result.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                    handlePurchases(appContext, purchases)
                }
            }
            .enablePendingPurchases(
                PendingPurchasesParams.newBuilder().enableOneTimeProducts().build()
            )
            .build()

        billingClient = client
        client.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    restorePurchases(appContext)
                }
            }

            override fun onBillingServiceDisconnected() = Unit
        })
    }

    /** Re-syncs ownership (used on startup and by "Restore purchases"). */
    fun restorePurchases(context: Context) {
        val client = billingClient ?: return
        client.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        ) { _, purchases ->
            handlePurchases(context.applicationContext, purchases)
        }
    }

    /** Queries the Pro price (e.g. "$4.99"); null when unavailable. */
    fun queryPrice(onResult: (String?) -> Unit) {
        queryProductDetails { details ->
            onResult(details?.oneTimePurchaseOfferDetails?.formattedPrice)
        }
    }

    /** Launches the purchase flow for Pro. */
    fun launchPurchase(activity: Activity) {
        queryProductDetails { details ->
            if (details == null) return@queryProductDetails
            val params = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(
                    listOf(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                            .setProductDetails(details)
                            .build()
                    )
                )
                .build()
            billingClient?.launchBillingFlow(activity, params)
        }
    }

    private fun queryProductDetails(onResult: (ProductDetails?) -> Unit) {
        val client = billingClient
        if (client == null) {
            onResult(null)
            return
        }
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(PRO_PRODUCT_ID)
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
                )
            )
            .build()
        client.queryProductDetailsAsync(params) { result, detailsResult ->
            val details = if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                detailsResult.productDetailsList.firstOrNull {
                    it.productId == PRO_PRODUCT_ID
                }
            } else {
                null
            }
            onResult(details)
        }
    }

    private fun handlePurchases(context: Context, purchases: List<Purchase>) {
        val owned = purchases.any {
            it.products.contains(PRO_PRODUCT_ID) &&
                it.purchaseState == Purchase.PurchaseState.PURCHASED
        }
        if (owned) {
            CoroutineScope(Dispatchers.IO).launch {
                ProRepository(context).setPro(true)
            }
            purchases
                .filter { it.purchaseState == Purchase.PurchaseState.PURCHASED && !it.isAcknowledged }
                .forEach { purchase ->
                    val ack = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()
                    billingClient?.acknowledgePurchase(ack) { }
                }
        }
    }
}
