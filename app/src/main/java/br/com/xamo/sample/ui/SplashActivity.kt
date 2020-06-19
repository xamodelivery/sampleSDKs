package br.com.xamo.sample.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import br.com.xamo.sample.AppApplication
import br.com.xamo.sample.R
import br.com.xamo.sdk.api.endpoints.loja.XamoApiLoja
import br.com.xamo.sdk.api.model.dto.loja.LojaDTO
import br.com.xamo.sdk.api.model.dto.loja.StatusLojaEnum
import br.com.xamo.sdk.login.ui.LoginNavegacao
import br.com.xamo.sdk.login.ui.LoginTipoApp
import br.com.xamo.sdk.login.utils.LoginUtils
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {

    companion object {
        const val TAG = "SPLASH"
        const val CODE = 0
    }

    private val apiLoja : XamoApiLoja by lazy {
        (application as AppApplication).getXamoApiSdk().loja()
    }

    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)
        //LoginUtils.logout()
        checarLogin()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModelJob.cancel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODE) {
            if (resultCode == RESULT_OK && data != null) {
                val loja : LojaDTO = LoginNavegacao.getResultLoginLoja(data)
                verificaTelaSeguinte(loja)
            }
        }
    }

    fun checarLogin() {
        uiScope.launch(CoroutineExceptionHandler { _, exception ->
            Log.e(TAG, exception.message, exception)
        }) {
            delay(2000)
            if (LoginUtils.usuarioLogado()) {
                val loja : LojaDTO = apiLoja.getLoja().await()
                verificaTelaSeguinte(loja)
            } else {
                LoginNavegacao.navegarTelaEscolhaAutenticacao(this@SplashActivity, LoginTipoApp.LOJA, CODE)
            }
        }
    }

    fun verificaTelaSeguinte(loja : LojaDTO) {
        when {
            loja.status!! == StatusLojaEnum.NAO_FINALIZADO -> {
                Log.i(TAG, "ir para tela de cadastro")
            }
            loja.status!! == StatusLojaEnum.APROVADO -> {
                Log.i(TAG, "ir para tela de aprovado")
            }
            loja.status!! == StatusLojaEnum.RECUSADO -> {
                Log.i(TAG, "ir para tela de recusado")
            }
            loja.status!! == StatusLojaEnum.EM_ANALISE -> {
                Log.i(TAG, "ir para tela de analise")
            }
            loja.status!! == StatusLojaEnum.ATIVO -> {
                Log.i(TAG, "ir para tela de home principal usuario logado")
            }
        }
        navelarTelApis()
    }

    fun navelarTelApis() {
        val intent = Intent(this@SplashActivity, ApiActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        ActivityCompat.finishAffinity(this@SplashActivity)
    }
}