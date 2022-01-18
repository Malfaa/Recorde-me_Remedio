package com.malfaa.lembrete.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.malfaa.lembrete.R
import com.malfaa.lembrete.adapters.MainAdapter
import com.malfaa.lembrete.databinding.MainFragmentBinding
import com.malfaa.lembrete.viewmodel.MainViewModel
import com.malfaa.lembrete.viewmodelfactory.MainViewModelFactory

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding
    private lateinit var viewModelFactory: MainViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val application = requireNotNull(this.activity).application
        val datasource = com.malfaa.lembrete.room.LDatabase.recebaDatabase(application).meuDao()

        viewModelFactory = MainViewModelFactory(datasource)

        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        val adapter = MainAdapter()
        binding.recyclerview.adapter = adapter

        viewModel.listaLembretes.observe(viewLifecycleOwner,{
            adapter.submitList(it.toMutableList())
        })


        binding.adicionarLembrete.setOnClickListener {
            this.findNavController().navigate(MainFragmentDirections.actionMainFragmentToAdicionarFragment())
        }
    }

}
// TODO: 18/01/2022 https://stackoverflow.com/questions/50638093/how-to-change-actionbar-title-in-fragment-class-in-kotlin
/*<androidx.cardview.widget.CardView
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:backgroundTint="@color/azul_primario"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="@+id/textView">


            <TextView
                android:id="@+id/textView2"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_gravity="center"
                android:text="@string/lembrar"
                android:textColor="@color/white"
                android:textSize="40sp" />
        </androidx.cardview.widget.CardView>*/