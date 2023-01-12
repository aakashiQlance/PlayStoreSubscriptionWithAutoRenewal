package com.aakash.playstoresubscriptionwithautorenewal.purchase

import android.content.Intent
import android.net.Uri
import android.app.*
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity


class OAuth2Client {
    companion object {
        private val API_SCOPE = "https://www.googleapis.com/auth/androidpublisher"
        private val CODE = "code"
        private val TAG = "OAuth2Client"


        fun makeAuthorizationRequest(
            context: Context,
            CLIENT_ID: String,
            REDIRECT_URI: String,
        ) {
            val authorizeUrl =
                "https://accounts.google.com/o/oauth2/v2/auth?scope=$API_SCOPE" +
                        "&response_type=$CODE&access_type=offline&prompt=consent" +
                        "&redirect_uri=$REDIRECT_URI&client_id=$CLIENT_ID"

            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(authorizeUrl)
            i.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(i)
        }
    }
}