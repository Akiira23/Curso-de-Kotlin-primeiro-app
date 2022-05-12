package br.com.alura.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.alura.orgs.dao.ProdutosDao
import br.com.alura.orgs.databinding.ListaProdutosActivityBinding
import br.com.alura.orgs.ui.recyclerview.adapter.ListaProdutosAdapter

class ListaProdutosActvity : AppCompatActivity() {

    private val dao = ProdutosDao()
    private val adapter = ListaProdutosAdapter(context = this, produtos = dao.buscaTodos())
    private val binding by lazy {
        ListaProdutosActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        configuraRecyclerView()
        configuraFab()
    }

    private fun configuraRecyclerView() {
        val recyclerView = binding.activityListaRecyclerView
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        adapter.atualiza(dao.buscaTodos());
    }

    private fun configuraFab() {
        val fab = binding.activityListaFloatingActionButton
        fab.setOnClickListener {
            vaiParaFormularioProduto()
        }
    }

    private fun vaiParaFormularioProduto() {
        val intent = Intent(this, FormularioProdutoActivity::class.java)
        startActivity(intent)
    }
}