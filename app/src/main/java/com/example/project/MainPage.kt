package com.example.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainPage.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainPage : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_main_page ,container ,false)
        val startButton = view.findViewById<Button>(R.id.create_button)
        val logInBtn = view.findViewById<Button>(R.id.Login_button)



        startButton.setOnClickListener{
            view.findNavController().navigate(R.id.action_mainPage_to_choseYourGoalFragment)
        }

        logInBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_mainPage_to_homePage)
        }
        return view;
        return inflater.inflate(R.layout.fragment_main_page, container, false)
    }




}
