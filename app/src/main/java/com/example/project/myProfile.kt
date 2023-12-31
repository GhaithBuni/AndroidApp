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
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [myProfile.newInstance] factory method to
 * create an instance of this fragment.
 */
class myProfile : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_profile, container, false)
        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottomNavigationView2)
        bottomNavigationView.menu.findItem(R.id.menu_profile).isChecked = true

        bottomNavigationView.setOnItemSelectedListener  { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    // No need to create a new instance, just navigate to the destination
                    view.findNavController().navigate(R.id.action_myProfile_to_homePage)
                    return@setOnItemSelectedListener  true
                }
                R.id.menu_graph -> {
                    // Replace with your actual action ID
                    view.findNavController().navigate(R.id.action_myProfile_to_graphFragment)
                    return@setOnItemSelectedListener  true
                }
                R.id.menu_profile -> {
                    // Replace with your actual action ID
                    view.findNavController().navigate(R.id.myProfile)
                    return@setOnItemSelectedListener  true
                }
                else -> false
            }
        }

        // No need to set the default selected fragment here

        return view
    }

    }


