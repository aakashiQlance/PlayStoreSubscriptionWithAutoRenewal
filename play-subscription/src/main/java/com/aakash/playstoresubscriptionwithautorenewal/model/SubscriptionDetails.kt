package com.aakash.playstoresubscriptionwithautorenewal.model




data class SubscriptionDetails(
    var acknowledgementState: Int = 0, // 1
    var autoRenewing: Boolean = false, // true
    var countryCode: String = "", // IN
    var developerPayload: String = "",
    var expiryTimeMillis: String = "", // 1673436083632
    var kind: String = "", // androidpublisher#subscriptionPurchase
    var orderId: String = "", // GPA.3327-6713-5562-78977
    var paymentState: Int = 0, // 1
    var priceAmountMicros: String = "", // 350000000
    var priceCurrencyCode: String = "", // INR
    var purchaseType: Int = 0, // 0
    var startTimeMillis: String = "" // 1673435679557
)