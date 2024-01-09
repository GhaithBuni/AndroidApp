package com.example.project

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.project.Model.SharedViewModel


class ChoseYourGoalFragment : Fragment() {

    private lateinit var loseWeightBtn: Button
    private lateinit var gainWeightBtn: Button
    private lateinit var maintainBtn: Button
    private lateinit var nextButton: Button

    private lateinit var buttonClicked: String
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



                view.findNavController()
                    .navigate(R.id.action_choseYourGoalFragment_to_activityLevel)
            } else {
                Toast.makeText(requireContext(), "Please make a choice", Toast.LENGTH_SHORT).show()
            }
        }

        setButtonClickListener(loseWeightBtn)
        setButtonClickListener(gainWeightBtn)
        setButtonClickListener(maintainBtn)


        return view
    }
    /**
     * Ställer in en klicklyssnare för den angivna knappen för att hantera händelser vid knappklick.
     *
     * @param button Knappen för vilken klicklyssnaren ska ställas in.
     */
    private fun setButtonClickListener(button: Button) {
        button.setOnClickListener {
            resetButtonState()
            // Change button color to selected color
            button.setBackgroundColor(Color.parseColor("#800020")) // Set your selected color
            anyButtonClicked = true
            buttonClicked = button.text.toString()


            val sharedViewModel: SharedViewModel by activityViewModels()

            // Set the value in the view model
            sharedViewModel.userGoal = buttonClicked


        }
    }
    /**
     * Återställer tillståndet för alla knappar för aktivitetsnivå till deras standardtillstånd.
     */
    private fun resetButtonState() {
        loseWeightBtn.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.default_outline_button)
        gainWeightBtn.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.default_outline_button)
        maintainBtn.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.default_outline_button)
    }
}





