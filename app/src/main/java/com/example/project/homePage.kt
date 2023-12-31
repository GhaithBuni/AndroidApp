package com.example.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER



class homePage : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)
        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottomNavigationView2)

        bottomNavigationView.setOnItemSelectedListener  { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    // No need to create a new instance, just navigate to the destination
                    view.findNavController().navigate(R.id.homePage)
                    return@setOnItemSelectedListener  true
                }
                R.id.menu_graph -> {
                    // Replace with your actual action ID
                    view.findNavController().navigate(R.id.action_homePage_to_graphFragment)
                    return@setOnItemSelectedListener  true
                }
                R.id.menu_profile -> {
                    // Replace with your actual action ID
                    view.findNavController().navigate(R.id.action_homePage_to_myProfile)
                    return@setOnItemSelectedListener  true
                }
                else -> false
            }
        }

        // No need to set the default selected fragment here

        return view
    }




}