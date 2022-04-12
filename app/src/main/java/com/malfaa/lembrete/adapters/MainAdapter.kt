package com.malfaa.lembrete.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.malfaa.lembrete.conversorPosEmData
import com.malfaa.lembrete.conversorPosEmHoras
import com.malfaa.lembrete.databinding.ItemLembreteBinding
import com.malfaa.lembrete.fragment.MainFragment.Companion.expandValue
import com.malfaa.lembrete.fragment.MainFragment.Companion.lembreteDestino
import com.malfaa.lembrete.room.entidade.ItemEntidade
import com.malfaa.lembrete.viewmodel.MainViewModel.Companion.alterar
import com.malfaa.lembrete.viewmodel.MainViewModel.Companion.deletar

class MainAdapter: ListAdapter<ItemEntidade, MainAdapter.ViewHolder>(ItemDiffCallBack()) {

    class ViewHolder private constructor(val binding: ItemLembreteBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemEntidade) {
            binding.remedio.text = item.remedio
            binding.data.text = conversorPosEmData(item.data, item.verificaDataCustom)
            binding.horario.text = conversorPosEmHoras(item.hora, item.verificaHoraCustom)
            binding.nota.text = item.nota
            binding.inicioValor.text = item.horaInicial
            binding.terminaValor.text = item.dataConjunto

            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemLembreteBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class ItemDiffCallBack: DiffUtil.ItemCallback<ItemEntidade>(){
        override fun areItemsTheSame(oldItem: ItemEntidade, newItem: ItemEntidade): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: ItemEntidade, newItem: ItemEntidade): Boolean {
            return oldItem === newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

        //Alterar Lembrete
        holder.binding.lembrete.setOnClickListener {
            lembreteDestino.value = item
            alterar.value = true
        }

        //Apagar Lembrete
        holder.binding.lembrete.setOnLongClickListener{
            lembreteDestino.value = item
            deletar.value = true
            true
        }

        //Expandir Nota
        holder.binding.expand?.setOnClickListener {
            expandValue.value = true
        }
    }
}
