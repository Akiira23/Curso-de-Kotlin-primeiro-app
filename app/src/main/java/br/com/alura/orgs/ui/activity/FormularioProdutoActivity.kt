package br.com.alura.orgs.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.alura.orgs.database.AppDatabase
import br.com.alura.orgs.databinding.ActivityFormularioProdutoBinding
import br.com.alura.orgs.extensions.tentaCarregarImagem
import br.com.alura.orgs.model.Produto
import br.com.alura.orgs.ui.dialog.FormularioImagemDialog
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collect
import java.math.BigDecimal

class FormularioProdutoActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityFormularioProdutoBinding.inflate(layoutInflater)
    }

    private var url: String? = null
    private var produtoId = 0L

    private val produtoDao by lazy {
        val db = AppDatabase.instancia(this)
        db.produtoDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "Cadastrar produto"

        setContentView(binding.root)
        configuraBotaoSalvar()
        binding.activityFormularioProdutoImagem.setOnClickListener {
            FormularioImagemDialog(this).mostra(url) { imagem ->
                url = imagem
                binding.activityFormularioProdutoImagem.tentaCarregarImagem(url)
            }
        }
        produtoId = intent.getLongExtra(CHAVE_PRODUTO_ID, 0L)
        tentaBuscarProduto()
    }

    private fun tentaBuscarProduto() {
        lifecycleScope.launch() {
            produtoDao.buscaPorId(produtoId).collect {
                it?.let { produtoEncontrado ->
                    title = "Alterar produto"
                    preencheCampos(produtoEncontrado)
                }
            }
        }
    }

    private fun preencheCampos(produtoCarregado: Produto) {
        url = produtoCarregado.imagem
        binding.activityFormularioProdutoImagem.tentaCarregarImagem(url)
        binding.activityFormularioProdutoNome.setText(produtoCarregado.nome)
        binding.activityFormularioProdutoDescricao.setText(produtoCarregado.descricao)
        binding.activityFormularioProdutoValor.setText(produtoCarregado.valor.toPlainString())
    }

    private fun configuraBotaoSalvar() {
        val botaoSalvar = binding.activityFormularioProdutoBotaoSalvar
        botaoSalvar.setOnClickListener {
            val produtoNovo = criaProduto()
            lifecycleScope.launch {
                produtoDao.salva(produtoNovo)
                finish()
            }
        }
    }

    private fun criaProduto(): Produto {
        val campoNome = binding.activityFormularioProdutoNome
        val nome = campoNome.text.toString()

        val campoDescricao = binding.activityFormularioProdutoDescricao
        val descricao = campoDescricao.text.toString()

        val campoValor = binding.activityFormularioProdutoValor
        val valorEmTexto = campoValor.text.toString()
        val valor = if (valorEmTexto.isBlank()) {
            BigDecimal.ZERO
        } else {
            BigDecimal(valorEmTexto)
        }

        val produtoNovo = Produto(
            id = produtoId,
            nome = nome,
            descricao = descricao,
            valor = valor,
            imagem = url
        )
        return produtoNovo
    }
}