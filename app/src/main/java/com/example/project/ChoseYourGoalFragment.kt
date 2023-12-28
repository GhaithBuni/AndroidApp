package com.example.project

import android.annotation.SuppressLint
import android.graphics.Color
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
 * Use the [ChoseYourGoalFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChoseYourGoalFragment : Fragment() {

    private var buttonClicked = false

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        val view = inflater.inflate(R.layout.fragment_chose_your_goal, container, false)

        val button = view.findViewById<Button>(R.id.lose_weight_btn)
        val button1 = view.findViewById<Button>(R.id.gain_weight_btn)
        val button2 = view.findViewById<Button>(R.id.maintain)
        val nextButton = view.findViewById<Button>(R.id.Next_button)

        nextButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_choseYourGoalFragment_to_activityLevel)
        }

        setButtonClickListener(button, "#000000", "#800020")  // Adjust colors as needed
        setButtonClickListener(button1, "#000000", "#800020")
        setButtonClickListener(button2, "#000000", "#800020")


        return view

    }

    private fun setButtonClickListener(button: Button, normalColor: String, selectedColor: String) {
        // Set the initial background color
        button.setBackgroundColor(Color.parseColor(normalColor))

        button.setOnClickListener {
            // Toggle button color on each click
            if (buttonClicked) {
                // Change button color to normal
                button.setBackgroundColor(Color.parseColor(normalColor)) // Set your normal color
            } else {
                // Change button color to selected color
                button.setBackgroundColor(Color.parseColor(selectedColor)) // Set your selected color
            }

            // Toggle the flag
            buttonClicked = !buttonClicked
        }
    }

}




