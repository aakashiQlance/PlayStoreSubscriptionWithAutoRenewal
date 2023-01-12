package com.aakash.playstoresubscriptionwithautorenewal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aakash.playstoresubscriptionwithautorenewal.purchase.OAuth2Client

class MainActivity(var CLIENT_ID: String, var REDIRECT_URI: String) : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}