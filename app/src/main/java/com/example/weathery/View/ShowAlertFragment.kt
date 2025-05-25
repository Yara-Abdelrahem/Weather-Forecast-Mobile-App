package com.example.weathery.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.weathery.R


class ShowAlertFragment : Fragment() {

    private lateinit var btn_add_alert: Button
    private lateinit var rv_alerts : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_alert, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_add_alert = view.findViewById(R.id.btn_add_new_alert)

        btn_add_alert.setOnClickListener {
            (activity as? AlertActivity)?.select_Alert()
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_alert_container, SelectTimeFragment())
                .addToBackStack(null)
                .commit()
        }

    }

}