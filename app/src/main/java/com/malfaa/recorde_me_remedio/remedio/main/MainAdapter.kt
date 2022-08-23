package com.malfaa.recorde_me_remedio.remedio.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import com.malfaa.recorde_me_remedio.R
import com.malfaa.recorde_me_remedio.databinding.ItemRemedioBinding
import com.malfaa.recorde_me_remedio.local.Remedio
import com.malfaa.recorde_me_remedio.remedio.main.MainViewModel.Companion.deletar
import com.malfaa.recorde_me_remedio.remedio.main.MainViewModel.Companion.remedioItem

class MainAdapter(private val clickListener: RemedioListener) : ListAdapter<Remedio, MainAdapter.ViewHolder>(ItemDiffCallBack()){

    //onCreate & onBind
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)

        holder.binding.lembrete.setOnLongClickListener {
            remedioItem.value = item
            deletar.value = true
            true
        }
        //Expandir Nota
        holder.binding.expand.setOnClickListener {
            if(holder.binding.notaBox.visibility == View.GONE){
                androidx.transition.TransitionManager.beginDelayedTransition(holder.binding.lembrete, AutoTransition())
                holder.binding.notaBox.visibility = View.VISIBLE
                holder.binding.expand.setBackgroundResource(R.drawable.ic_expand_less)
            }else{
                androidx.transition.TransitionManager.beginDelayedTransition(holder.binding.lembrete, AutoTransition())
                holder.binding.notaBox.visibility = View.GONE
                holder.binding.expand.setBackgroundResource(R.drawable.ic_expand_more)
            }
        }
    }

    class ViewHolder private constructor(val binding: ItemRemedioBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Remedio, clickListener: RemedioListener) {
            binding.item = item
            binding.clickListener = clickListener

            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemRemedioBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class ItemDiffCallBack: DiffUtil.ItemCallback<Remedio>(){
        override fun areItemsTheSame(oldItem: Remedio, newItem: Remedio): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Remedio, newItem: Remedio): Boolean {
            return oldItem === newItem
        }
    }

    class RemedioListener(val clickListener: (Remedio) -> Unit) {
        fun onClick(remedio: Remedio) = clickListener(remedio)
    }
}