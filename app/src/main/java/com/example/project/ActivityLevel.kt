package com.example.project

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ActivityLevel.newInstance] factory method to
 * create an instance of this fragment.
 */
class ActivityLevel : Fragment() {

    private var buttonClicked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_activity_level, container, false)

        val lightBtn = view.findViewById<Button>(R.id.Light_btn)
        val modrateBtn = view.findViewById<Button>(R.id.moderate_btn)
        val activeBtn = view.findViewById<Button>(R.id.active_btn)
        val veryActiveBtn = view.findViewById<Button>(R.id.veryActive_btn)
        val nextButton = view.findViewById<Button>(R.id.activity_next)

        nextButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_activityLevel_to_infoFragment)
        }

        setButtonClickListener(lightBtn)
        setButtonClickListener(modrateBtn)
        setButtonClickListener(activeBtn)
        setButtonClickListener(veryActiveBtn)



        return view
    }

    private fun setButtonClickListener(button: Button) {
        button.setOnClickListener {
            // Toggle button color on each click
            if (buttonClicked) {
                // Change button color to normal
                button.setBackgroundColor(Color.parseColor("#000000")) // Set your normal color
            } else {
                // Change button color to selected color
                button.setBackgroundColor(Color.parseColor("#800020")) // Set your selected color
            }

            // Toggle the flag
            buttonClicked = !buttonClicked
        }
    }


}