package br.com.xamo.sample.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import br.com.xamo.sample.AppApplication
import br.com.xamo.sample.R
import br.com.xamo.sdk.api.endpoints.loja.XamoApiLoja
import br.com.xamo.sdk.api.model.serialization.cadastroLoja.CadastroLoja
import kotlinx.android.synthetic.main.api_layout.*
import kotlinx.coroutines.*

class ApiActivity : AppCompatActivity() {

    private val apiLoja : XamoApiLoja by lazy {
        (application as AppApplication).getXamoApiSdk().loja()
    }

    companion object {
        const val TAG = "ApiActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.api_layout)

        btnDadosLoja.setOnClickListener {
            dadosLoja()
        }

        btnCadastroLoja.setOnClickListener {
            cadastroLoja()
        }
    }

    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onDestroy() {
        super.onDestroy()
        viewModelJob.cancel()
    }

    fun dadosLoja() {
        uiScope.launch(CoroutineExceptionHandler { _, exception ->
            Log.e(TAG, exception.message, exception)
        }) {
            val result = apiLoja.getLoja().await()
            Log.d(TAG, result.toString())
        }
    }

    fun cadastroLoja() {
        uiScope.launch(CoroutineExceptionHandler { _, exception ->
            Log.e(TAG, exception.message, exception)
        }) {

            val categorias = apiLoja.getCategorias().await()
            val categoria = categorias[0]

            val cadastro = CadastroLoja()
                .endereco(
                    CadastroLoja.Endereco()
                        .cidade("Manaus")
                        .estado("Amazonas")
                        .logradouro("Rua Saturno")
                        .numero("458")
                        .bairro("Jardim das acacias")
                        .cep("32241-340")
                        .complemento(null)
                        .latitude(-23.5707855)
                        .longitude(-46.6434994)
                )
                .loja(
                    CadastroLoja.Loja()
                        .telefone("+5592981470275")
                        .cnpj(null)
                        .cpf("99170278364")
                        .rg(null)
                        .nomeProprietario("André Nascimento")
                        .email(null)
                        .nomeLoja("Mercadinho do Zé")
                        .categoria(categoria)
                        .fotoComprovanteEndereco("")
                        .fotoRgCnhProprietario("")
                )

            val result = apiLoja.cadastro(cadastro).await()
            Log.d(TAG, result.toString())
        }
    }
}