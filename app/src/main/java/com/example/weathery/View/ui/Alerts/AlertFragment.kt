package com.example.weathery.View.ui.Alerts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
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
    private val binding get() = _binding!!

    private lateinit var alertViewModel: AlarmViewModel
    private lateinit var adapter: AlertAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        alertViewModel = AlarmViewModel(requireContext())
        _binding = FragmentAlertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.alertRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter = AlertAdapter(mutableListOf(), alertViewModel, viewLifecycleOwner.lifecycleScope)
        binding.alertRecyclerView.adapter = adapter

        alertViewModel.alerts.observe(viewLifecycleOwner) { alertsList ->
            adapter.setAlerts(alertsList.toMutableList())
        }

        binding.fab.setOnClickListener {
            val activity = requireActivity() as INavFragmaent
            activity.navigateTo(SelectTimeFragment(), false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
