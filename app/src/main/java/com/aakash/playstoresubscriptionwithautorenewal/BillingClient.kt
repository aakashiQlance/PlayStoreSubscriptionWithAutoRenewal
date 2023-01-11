package com.aakash.playstoresubscriptionwithautorenewal

import android.app.Activity
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.aakash.playstoresubscriptionwithautorenewal.common.CustomToast
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.ArrayList


class BillingClient(
    var activityContext: Activity,
    var subscriptionList: List<String>,
    var function: (String) -> Unit,
) : PurchasesUpdatedListener {

    private var TAG = "com.aakash.playstoresubscriptionautorenewal"
    private var billingClient: BillingClient = BillingClient.newBuilder(activityContext)
        .enablePendingPurchases()
        .setListener(this)
        .build()

    fun startBillingClient() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    printLog("Billing Client Started")

                }
            }

            override fun onBillingServiceDisconnected() {
                printLog("Billing Client Failed")
            }
        })
    }

    public fun stopBillingClient() {
        billingClient.endConnection()
        printLog("Billing Client Connection Ended")
    }

    public fun checkBillingClientState(): Boolean = billingClient.isReady

    public fun buySubscription(subscriptionId: String) {
        if (checkBillingClientState()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                purchaseFlow1(subscriptionId)
            } else {
                purchaseFlow2(subscriptionId)
            }
        } else {
            displayMessage(activityContext.getString(R.string.please_start_billing_client))
        }
    }

    private fun purchaseFlow1(subscriptionId: String) {

        val productList: ArrayList<QueryProductDetailsParams.Product> = ArrayList()
        for (product in subscriptionList) {
            productList.add(
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(product)
                    .setProductType(BillingClient.ProductType.SUBS)
                    .build()
            )
        }

        val params = QueryProductDetailsParams.newBuilder().setProductList(productList).build()
        billingClient.queryProductDetailsAsync(params) { billingResult, productDetailList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED) {
                printLog("Please Update Playstore")
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && productDetailList.isNotEmpty()) {

                for (productDetails in productDetailList) {
                    if (subscriptionId.equals(productDetails.productId, true)) {
                        val productDetailsParamsList = listOf(
                            BillingFlowParams.ProductDetailsParams.newBuilder()
                                .setProductDetails(productDetails)
                                .setOfferToken(productDetails.subscriptionOfferDetails!![0].offerToken)
                                .build()
                        )

                        val billingFlowParams = BillingFlowParams.newBuilder()
                            .setProductDetailsParamsList(productDetailsParamsList)
                            .build()


                        billingClient.launchBillingFlow(activityContext, billingFlowParams)
                    }
                }
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(activityContext, "Coming Soon", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }
    }

    private fun purchaseFlow2(subscriptionId: String) {

        val params =
            SkuDetailsParams.newBuilder().setSkusList(subscriptionList)
                .setType(BillingClient.SkuType.SUBS)
                .build()

        billingClient.querySkuDetailsAsync(params) { billingResult, skuDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList!!.isNotEmpty()) {
                for (skuDetails in skuDetailsList) {

                    if (subscriptionId == skuDetails.sku) {

                        val flowParams = BillingFlowParams.newBuilder()
                            .setSkuDetails(skuDetails)
                            .build()

                        billingClient.launchBillingFlow(activityContext, flowParams)

                    }
                }
            }

        }
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?,
    ) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            handlePurchase(purchases)
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            printLog(billingResult.debugMessage.toString())
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            printLog(billingResult.debugMessage.toString())
        } else {
            printLog(billingResult.debugMessage.toString())
        }
    }

    private fun handlePurchase(purchases: MutableList<Purchase>) {
        for (purchase in purchases) {
            if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {

                if (!purchase.isAcknowledged && purchase.isAutoRenewing) {
                    acknowledgePurchase(purchase.purchaseToken, purchase.originalJson)
                }
            }
        }
    }

    private fun acknowledgePurchase(purchaseToken: String, originalJson: String) {
        val params = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchaseToken)
            .build()

        billingClient.acknowledgePurchase(params) { billingResult ->
            val responseCode = billingResult.responseCode
            val debugMessage = billingResult.debugMessage
            printLog(debugMessage)
            printLog(responseCode.toString())
            function(originalJson)
        }

    }

    private fun displayMessage(message: String) {
        CustomToast.showToast(activityContext, message)
    }

    private fun printLog(message: String) {
        Log.i(TAG, message)
    }
}