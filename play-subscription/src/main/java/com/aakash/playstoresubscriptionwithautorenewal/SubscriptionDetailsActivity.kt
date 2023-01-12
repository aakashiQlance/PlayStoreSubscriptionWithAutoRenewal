package com.aakash.playstoresubscriptionwithautorenewal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.aakash.playstoresubscriptionwithautorenewal.oAuth.OAuth2ClientImpl
import com.aakash.playstoresubscriptionwithautorenewal.oAuth.OAuthToken
import com.aakash.playstoresubscriptionwithautorenewal.purchase.SubscriptionDetails

class SubscriptionDetailsActivity : AppCompatActivity() {
    private var packageNameTest = "com.aakash.playstoresubscriptionwithautorenewal"
    private var subscriptionDetails = SubscriptionDetails()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscription_details)

        val data = intent.data

        subscriptionDetails.getSubscriptionDetails(this@SubscriptionDetailsActivity, data, packageNameTest, "com.exy", "123455656653343")


    }
}