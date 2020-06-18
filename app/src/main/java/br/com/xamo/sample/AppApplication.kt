package br.com.xamo.sample

import android.app.Application
import br.com.xamo.sdk.api.XamoApiInstance
import br.com.xamo.sdk.api.XamoApiSdk

class AppApplication : Application(), XamoApiInstance {

    private val mApi by lazy {
        XamoApiSdk(this)
    }

    override fun getXamoApiSdk(): XamoApiSdk {
        return mApi
    }
}