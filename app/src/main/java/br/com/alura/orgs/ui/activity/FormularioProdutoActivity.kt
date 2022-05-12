package br.com.alura.orgs.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.alura.orgs.dao.ProdutosDao
import br.com.alura.orgs.databinding.ActivityFormularioProdutoBinding
import br.com.alura.orgs.model.Produto
import java.math.BigDecimal

class FormularioProdutoActivity : AppCompatActivity() {

    private val biding by lazy {
        ActivityFormularioProdutoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configuraBotaoSalvar()
        setContentView(biding.root)
    }

    private fun configuraBotaoSalvar() {
        val botaoSalvar = biding.produtoItemBotaoSalvar
        val dao = ProdutosDao()

        botaoSalvar.setOnClickListener {
            val produtoNovo = criaProduto()
            dao.adiciona(produtoNovo)
        }
    }

    private fun criaProduto(): Produto {
        val campoNome = biding.activityFormularioNome
        val nome = campoNome.text.toString()

        val campoDescricao = biding.activityFormularioDescricao
        val descricao = campoDescricao.text.toString()

        val campoValor = biding.activityFormularioValor
        val valorEmTexto = campoValor.text.toString()
        val valor = if (valorEmTexto.isBlank()) {
            BigDecimal.ZERO
        } else {
            BigDecimal(valorEmTexto)
        }

        val produtoNovo = Produto(
            nome = nome,
            descricao = descricao,
            valor = valor
        )
        return produtoNovo
    }
}