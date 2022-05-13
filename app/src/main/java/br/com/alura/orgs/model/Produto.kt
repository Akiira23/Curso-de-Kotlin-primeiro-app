package br.com.alura.orgs.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import java.math.BigDecimal

@Parcelize
data class Produto(
    val nome: String,
    val descricao: String,
    val valor: BigDecimal,
    val imagem: String? = null
) : Parcelable
