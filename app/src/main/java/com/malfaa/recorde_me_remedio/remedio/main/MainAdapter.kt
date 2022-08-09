package com.malfaa.recorde_me_remedio.remedio.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.malfaa.recorde_me_remedio.databinding.ItemRemedioBinding
import com.malfaa.recorde_me_remedio.local.Remedio

class MainAdapter(private val clickListener: RemedioListener, private val longClickListener: LongRemedioListener) : ListAdapter<Remedio, MainAdapter.ViewHolder>(ItemDiffCallBack()){

    //onCreate & onBind
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener, longClickListener)
    }

    class ViewHolder private constructor(val binding: ItemRemedioBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Remedio, clickListener: RemedioListener, longClickListener: LongRemedioListener) {
//            binding.remedio.text = item.remedio
//            binding.data.text = conversorPosEmData(item.data, item.verificaDataCustom)
//            binding.horario.text = conversorPosEmHoras(item.hora, item.verificaHoraCustom)
//            if (item.nota.isEmpty()){
//                binding.expand.visibility = View.GONE
//            }else{
//                binding.nota.text = item.nota
//            }
//            binding.inicioValor.text = item.horaInicial
//            binding.terminaValor.text = item.dataConjunto

            binding.item = item
            binding.clickListener = clickListener
            binding.longClickListener = longClickListener


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

    class LongRemedioListener(val longClickListener: (Remedio) -> Unit){
        fun onLongClick(remedio: Remedio) = longClickListener(remedio)
    }
}