package br.com.alura.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.alura.orgs.database.AppDatabase
import br.com.alura.orgs.databinding.ListaProdutosActivityBinding
import br.com.alura.orgs.ui.recyclerview.adapter.ListaProdutosAdapter
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import java.lang.Exception

class ListaProdutosActvity : AppCompatActivity() {

    private val adapter = ListaProdutosAdapter(context = this)
    private val binding by lazy {
        ListaProdutosActivityBinding.inflate(layoutInflater)
    }
    private val context by lazy {
        this
    }
    private val job = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        configuraRecyclerView()
        configuraFab()
    }

    override fun onResume() {
        super.onResume()
        val db = AppDatabase.instancia(this)
        val produtoDao = db.produtoDao()
        val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.i("ListaProdutosActivity", "onResume: throwable $throwable")
            Toast.makeText(
                this,
                "Ocorreu um problema",
                Toast.LENGTH_SHORT
            ).show()
        }
        val scope = MainScope()

        scope.launch(handler) {
            val produtos = withContext(IO) {
                produtoDao.buscaTodos()
            }
            adapter.atualiza(produtos)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
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