package com.malfaa.lembrete.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.malfaa.lembrete.databinding.ItemLembreteBinding
import com.malfaa.lembrete.room.entidade.ItemEntidade

class MainAdapter: ListAdapter<ItemEntidade, MainAdapter.ViewHolder>(ItemDiffCallBack()) {
    class ViewHolder private constructor(private val binding: ItemLembreteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemEntidade) {
            binding.remedio.text = item.remedio
            binding.data.text = item.data.toString()
            binding.horario.text = item.hora.toString()
            binding.nota.text = item.nota

            binding.executePendingBindings()
        }

        // TODO: 18/01/2022 adicionar onclick pra entrar no alterar
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemLembreteBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
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
}
