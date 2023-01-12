package com.aakash.playstoresubscriptionwithautorenewal.purchase

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.aakash.playstoresubscriptionwithautorenewal.common.Constants
import com.aakash.playstoresubscriptionwithautorenewal.common.CustomToast
import com.aakash.playstoresubscriptionwithautorenewal.common.ReusedMethod
import com.aakash.playstoresubscriptionwithautorenewal.common.SharedPreferenceManager

import com.aakash.playstoresubscriptionwithautorenewal.oAuth.OAuth2ClientImpl
import com.aakash.playstoresubscriptionwithautorenewal.oAuth.OAuthToken

class SubscriptionDetails(var context:Context) {

    private var oAuth2ClientImpl = OAuth2ClientImpl()
    fun getSubscriptionDetails(
        context: Context,
        data: Uri?,
        packageName: String,
        productId: String,
        purchaseToken: String,
    ) {
        if(data == null){
            oAuth2ClientImpl.makeAuthorizationRequest(context,OAuthToken.getClientId!!, OAuthToken.getRedirectUri!!)
        }else{
            val code = data.getQueryParameter("code")
            val error = data.getQueryParameter("error")

            if (!TextUtils.isEmpty(code)) {
                SharedPreferenceManager.putString(Constants.CODE, code!!)
                var refreshAccessToken = oAuth2ClientImpl.getRefreshAccessToken(data)
                //todo call subscription API
            }
            else if (!TextUtils.isEmpty(error)) {
                displayMessage("Some error occurred!!!")
            }


        }


    }

    fun displayMessage(message: String) {
        CustomToast.showToast(context, message)
    }


}