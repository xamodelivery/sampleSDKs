package br.com.xamo.sample.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import br.com.xamo.sample.AppApplication
import br.com.xamo.sample.R
import br.com.xamo.sdk.api.endpoints.loja.XamoApiLoja
import kotlinx.android.synthetic.main.teste_layout.*
import kotlinx.coroutines.*

class ApiActivity : AppCompatActivity() {

    private val apiLoja : XamoApiLoja by lazy {
        (application as AppApplication).getXamoApiSdk().loja()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.teste_layout)

        button.setOnClickListener {
            apiGetLoginLoja()
        }
    }

    private val ceh = CoroutineExceptionHandler { _, throwable ->
        Log.e("XAMOAPI","CEH Handled Crash [$throwable]")
    }

    fun apiGetLoginLoja() {
        GlobalScope.launch(Dispatchers.Main + ceh) {
            val result = apiLoja.getLoja().await()
            Log.d("XAMOAPI", result.id ?: "")
        }
    }
}