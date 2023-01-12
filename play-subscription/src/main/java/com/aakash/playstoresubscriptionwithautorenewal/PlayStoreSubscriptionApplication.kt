package com.aakash.playstoresubscriptionwithautorenewal

import android.app.Application

public class PlayStoreSubscriptionApplication: Application() {


    companion object {
        var instance: PlayStoreSubscriptionApplication? = null

    } init {
        instance = this

    }

    public override fun onCreate() {
        super.onCreate()
    }
}