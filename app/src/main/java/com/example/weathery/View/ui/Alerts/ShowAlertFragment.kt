package com.example.weathery.View.ui.Alerts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.weathery.R
import com.example.weathery.View.INavFragmaent
import com.example.weathery.View.ui.Alerts.SelectTimeFragment
import com.example.weathery.View.ui.FavoriteCity.ShowFavoriteFragment

class ShowAlertFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_show_alert, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fab = view.findViewById<Button>(R.id.fab)
        fab.setOnClickListener {
            val activity = requireActivity() as INavFragmaent

            activity.navigateTo(SelectTimeFragment(),false)
        }
    }
}