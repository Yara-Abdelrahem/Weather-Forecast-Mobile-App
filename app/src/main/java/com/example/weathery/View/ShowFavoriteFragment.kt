package com.example.weathery.View

import android.R.attr.label
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weathery.R
import com.example.weathery.View.Adapter.SimpleStringAdapter

class ShowFavoriteFragment : Fragment() {
    private lateinit var rvFavorites: RecyclerView
    private lateinit var btn_add_favorite : Button
    private val favoriteLocations = mutableListOf<String>()
    private lateinit var adapter: SimpleStringAdapter
    private var selected_fragment = SelectFavoriteLocationFragment()
    //private lateinit var fragmentManager: FragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_show_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvFavorites = view.findViewById(R.id.rvFavorites)
        btn_add_favorite = view.findViewById(R.id.btnAddToFavorites)
        rvFavorites.layoutManager = LinearLayoutManager(requireContext())
        adapter = SimpleStringAdapter(favoriteLocations)
        rvFavorites.adapter = adapter
         var fragmentManager = requireActivity().getSupportFragmentManager()
        var transaction = fragmentManager.beginTransaction()
        btn_add_favorite.setOnClickListener {
            (activity as? FavoriteActivity)?.openMapPicker()
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, SelectFavoriteLocationFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}