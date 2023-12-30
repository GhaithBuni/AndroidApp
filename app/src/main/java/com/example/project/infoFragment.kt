package com.example.project

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
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

    private lateinit var maleBtn: Button
    private lateinit var femaleBtn: Button
    private lateinit var nextButton: Button
    private lateinit var ageText: EditText

    private var buttonClicked = false
    private var anyButtonClicked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_info, container, false)

        maleBtn = view.findViewById(R.id.male_btn)
        femaleBtn = view.findViewById(R.id.female_btn)
        nextButton = view.findViewById(R.id.info_next)
        ageText = view.findViewById(R.id.age_txt)

        setButtonClickListener(maleBtn)
        setButtonClickListener(femaleBtn)

        nextButton.setOnClickListener {
            if (anyButtonClicked) {
                val age = ageText.text.toString().trim()
                if (age.isNotEmpty()) {
                    view.findNavController().navigate(R.id.action_infoFragment_to_infoFragment2)
                } else {
                    Toast.makeText(requireContext(), "Please enter your age", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Please make a choice", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun setButtonClickListener(button: Button) {
        button.setOnClickListener {
            resetButtonState()
            // Change button color to selected color
            button.setBackgroundColor(Color.parseColor("#800020")) // Set your selected color
            anyButtonClicked = true
        }
    }

    private fun resetButtonState() {
        maleBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.default_outline_button)
        femaleBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.default_outline_button)
    }
}