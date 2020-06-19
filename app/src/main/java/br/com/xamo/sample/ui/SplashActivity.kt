package br.com.xamo.sample.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import br.com.xamo.sample.R
import br.com.xamo.sdk.login.ui.LoginNavegacao
import br.com.xamo.sdk.login.ui.LoginTipoApp
import br.com.xamo.sdk.login.utils.LoginUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)
        checarLogin()
    }

    companion object {
        const val CODE = 0
    }

    fun checarLogin() {
        GlobalScope.launch(Dispatchers.Main) {
            //LoginUtils.logout()
            delay(2000)
            if (LoginUtils.usuarioLogado()) {
                navelarTelaUsuarioLogado()
            } else {
                LoginNavegacao.navegarTelaEscolhaAutenticacao(this@SplashActivity, LoginTipoApp.LOJA, CODE)
            }
        }
    }

    fun navelarTelaUsuarioLogado() {
        val intent = Intent(this@SplashActivity, ApiActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        ActivityCompat.finishAffinity(this@SplashActivity)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODE) {
            if (resultCode == RESULT_OK && data != null) {
                //val model : LojaDTO = LoginNavegacao.getResultLoginLoja(data)
                navelarTelaUsuarioLogado()
            }
        }
    }
}