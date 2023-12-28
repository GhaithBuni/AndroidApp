package com.example.project

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
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

    private lateinit var loseWeightBtn: Button
    private lateinit var gainWeightBtn: Button
    private lateinit var maintainBtn: Button
    private lateinit var nextButton: Button

    private var buttonClicked = false
    private var anyButtonClicked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chose_your_goal, container, false)

        loseWeightBtn = view.findViewById(R.id.lose_weight_btn)
        gainWeightBtn = view.findViewById(R.id.gain_weight_btn)
        maintainBtn = view.findViewById(R.id.maintain)
        nextButton = view.findViewById(R.id.Next_button)

        nextButton.setOnClickListener {
            if (anyButtonClicked) {
                view.findNavController().navigate(R.id.action_choseYourGoalFragment_to_activityLevel)
            } else {
                Toast.makeText(requireContext(), "Please make a choice", Toast.LENGTH_SHORT).show()
            }
        }

        setButtonClickListener(loseWeightBtn)
        setButtonClickListener(gainWeightBtn)
        setButtonClickListener(maintainBtn)

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
        loseWeightBtn.setBackgroundColor(Color.parseColor("#000000"))
        gainWeightBtn.setBackgroundColor(Color.parseColor("#000000"))
        maintainBtn.setBackgroundColor(Color.parseColor("#000000"))
    }
}





