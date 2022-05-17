package br.com.alura.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import br.com.alura.orgs.database.AppDatabase
import br.com.alura.orgs.databinding.ListaProdutosActivityBinding
import br.com.alura.orgs.model.Produto
import br.com.alura.orgs.ui.recyclerview.adapter.ListaProdutosAdapter
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class ListaProdutosActvity : AppCompatActivity() {

    private val adapter = ListaProdutosAdapter(context = this)
    private val binding by lazy {
        ListaProdutosActivityBinding.inflate(layoutInflater)
    }
    private val dao by lazy {
        val db = AppDatabase.instancia(this)
        db.produtoDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        configuraRecyclerView()
        configuraFab()

        val handler = coroutineExceptionHandler()

        lifecycleScope.launch(handler) {
            dao.buscaTodos().collect { produtos ->
                adapter.atualiza(produtos)
            }
        }
    }

    private fun coroutineExceptionHandler(): CoroutineExceptionHandler {
        val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.i("ListaProdutosActivity", "onResume: throwable $throwable")
            Toast.makeText(
                this,
                "Ocorreu um problema",
                Toast.LENGTH_SHORT
            ).show()
        }
        return handler
    }

    private fun configuraFab() {
        val fab = binding.activityListaFloatingActionButton
        fab.setOnClickListener {
            vaiParaFormularioProduto()
        }
    }

    private fun configuraRecyclerView() {
        val recyclerView = binding.activityListaRecyclerView
        recyclerView.adapter = adapter
        adapter.quandoClicaNoItem = {
            val intent = Intent(
                this,
                DetalhesProdutosActivity::class.java
            ).apply {
                putExtra(CHAVE_PRODUTO_ID, it.id)
            }
            startActivity(intent)
        }
    }

    private fun vaiParaFormularioProduto() {
        val intent = Intent(this, FormularioProdutoActivity::class.java)
        startActivity(intent)
    }
}