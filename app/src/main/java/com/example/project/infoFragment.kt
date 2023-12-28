package com.example.project

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [infoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class infoFragment : Fragment() {

    private var buttonClicked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_info,container,false)

        val maleBtn = view.findViewById<Button>(R.id.male_btn)
        val femaleBtn = view.findViewById<Button>(R.id.female_btn)
        val nextButton = view.findViewById<Button>(R.id.info_next)

        setButtonClickListener(maleBtn)
        setButtonClickListener(femaleBtn)


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