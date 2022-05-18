package br.com.alura.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
<<<<<<< HEAD
import android.view.*
import android.widget.PopupMenu
=======
import android.util.Log
>>>>>>> 10dbbacba3d14f74de98f9649d53f8af86cdf6c1
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import br.com.alura.orgs.database.AppDatabase
import br.com.alura.orgs.databinding.ListaProdutosActivityBinding
import br.com.alura.orgs.model.Produto
import br.com.alura.orgs.ui.recyclerview.adapter.ListaProdutosAdapter
<<<<<<< HEAD
import br.com.alura.orgs.R
=======
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.lang.Exception
>>>>>>> 10dbbacba3d14f74de98f9649d53f8af86cdf6c1

class ListaProdutosActvity : AppCompatActivity() {

    private val adapter = ListaProdutosAdapter(context = this)
    private val binding by lazy {
        ListaProdutosActivityBinding.inflate(layoutInflater)
    }
<<<<<<< HEAD
    private val produtoDao by lazy {
        AppDatabase.instancia(this).produtoDao()
=======
    private val dao by lazy {
        val db = AppDatabase.instancia(this)
        db.produtoDao()
>>>>>>> 10dbbacba3d14f74de98f9649d53f8af86cdf6c1
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_ordena_produtos, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val db = AppDatabase.instancia(this)
        val produtoDao = db.produtoDao()
        when (item.itemId) {
            R.id.menu_ordena_produtos_nome_desc ->
                adapter.atualiza(produtoDao.ordenaNomeDesc())
            R.id.menu_ordena_produtos_nome_asc ->
                adapter.atualiza(produtoDao.ordenaNomeAsc())
            R.id.menu_ordena_produtos_descricao_desc ->
                adapter.atualiza(produtoDao.ordenaDescricaoDesc())
            R.id.menu_ordena_produtos_descricao_asc ->
                adapter.atualiza(produtoDao.ordenaDescricaoAsc())
            R.id.menu_ordena_produtos_valor_desc ->
                adapter.atualiza(produtoDao.ordenaValorDesc())
            R.id.menu_ordena_produtos_valor_asc ->
                adapter.atualiza(produtoDao.ordenaValorAsc())
            R.id.menu_ordena_produtos_sem_ordem ->
                adapter.atualiza(produtoDao.buscaTodos())
        }
        return super.onOptionsItemSelected(item)
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
        adapter.quandoSeguradoNoItem = {
            val intent = Intent(
                this,
                FormularioProdutoActivity::class.java
            ).apply {
                putExtra(CHAVE_PRODUTO_ID, it.id)
            }
            val popupMenu: PopupMenu = PopupMenu(this, recyclerView)
            popupMenu.menuInflater.inflate(R.menu.menu_popup_lista_produtos, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_popup_lista_produtos_editar ->
                        startActivity(intent)
                    R.id.menu_popup_lista_produtos_remover -> {
                        produtoDao.remove(it)
                        adapter.atualiza(produtoDao.buscaTodos())
                    }
                }
                true
            }
            popupMenu.show()
        }
    }

    private fun vaiParaFormularioProduto() {
        val intent = Intent(this, FormularioProdutoActivity::class.java)
        startActivity(intent)
    }
}