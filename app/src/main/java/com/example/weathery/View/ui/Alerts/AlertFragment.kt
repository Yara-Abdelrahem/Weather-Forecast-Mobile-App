package com.example.weathery.View.ui.Alerts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weathery.AlarmAlert.Model.AlertItem
import com.example.weathery.AlarmAlert.View.AlertAdapter
import com.example.weathery.AlarmAlert.ViewModel.AlarmViewModel
import com.example.weathery.R
import com.example.weathery.View.INavFragmaent
import com.example.weathery.databinding.FragmentAlertBinding
import kotlinx.coroutines.launch

class AlertFragment : Fragment() {

    private var _binding: FragmentAlertBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var alertViewModel: AlarmViewModel
    private lateinit var adapter: AlertAdapter
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        alertViewModel = AlarmViewModel(requireContext())
        _binding = FragmentAlertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.alertRecyclerView)
        val fab = view.findViewById<Button>(R.id.fab)

        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = AlertAdapter(mutableListOf(), alertViewModel, lifecycleScope)
        recyclerView.adapter = adapter

        loadAlerts()

        fab.setOnClickListener {
            val activity = requireActivity() as INavFragmaent
            activity.navigateTo(SelectTimeFragment(), false)
        }
    }

    private fun loadAlerts() {
        lifecycleScope.launch {
            val alertsList = alertViewModel.getAllAlerts()
            adapter.setAlerts(alertsList.toMutableList())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}