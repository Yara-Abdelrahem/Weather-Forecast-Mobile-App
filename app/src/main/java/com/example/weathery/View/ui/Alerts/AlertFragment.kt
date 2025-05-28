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
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        alertViewModel = AlarmViewModel(requireContext())
        _binding = FragmentAlertBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.alertRecyclerView)
        val fab = view.findViewById<Button>(R.id.fab)

        recyclerView.layoutManager = LinearLayoutManager(context)
        val alerts = mutableListOf<AlertItem>()
        val adapter = AlertAdapter(alerts, alertViewModel, lifecycleScope)
        recyclerView.adapter = adapter

        // Observe LiveData for real-time updates
        lifecycleScope.launch {
            var alertsList =alertViewModel.getAllAlerts()
            alerts.clear()
            alerts.addAll(alertsList)
            adapter.notifyDataSetChanged()
        }

        fab.setOnClickListener {
            val activity = requireActivity() as INavFragmaent
            activity.navigateTo(SelectTimeFragment(), false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}