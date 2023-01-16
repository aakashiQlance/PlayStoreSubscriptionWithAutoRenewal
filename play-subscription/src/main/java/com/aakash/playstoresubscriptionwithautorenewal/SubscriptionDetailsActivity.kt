package com.aakash.playstoresubscriptionwithautorenewal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aakash.playstoresubscriptionwithautorenewal.oAuth.OAuth2Client
import com.aakash.playstoresubscriptionwithautorenewal.oAuth.OAuthToken

class SubscriptionDetailsActivity : AppCompatActivity() {
    private var oAuth2Client = OAuth2Client(this@SubscriptionDetailsActivity)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscription_details)

        if (OAuthToken.getRefreshToken.isNullOrEmpty()) {
            val data = intent.data
            oAuth2Client.getSubscriptionDetails(this@SubscriptionDetailsActivity,
                data,
                "com.exy", /*productId*/
                "123455656653343"/*purchaseToken*/) { subscriptionDetails, message ->

            }
        } else {
            oAuth2Client.getSubscriptionDetails(this@SubscriptionDetailsActivity,
                "com.exy",
                "123455656653343") { subscriptionDetails, message ->

            }
        }
    }
}