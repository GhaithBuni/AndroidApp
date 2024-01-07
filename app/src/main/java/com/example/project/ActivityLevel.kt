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



class ActivityLevel : Fragment() {

    private lateinit var lightBtn: Button
    private lateinit var moderateBtn: Button
    private lateinit var activeBtn: Button
    private lateinit var veryActiveBtn: Button
    private lateinit var nextButton: Button
    private lateinit var buttonClicked: String
    private var anyButtonClicked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_activity_level, container, false)


        lightBtn = view.findViewById(R.id.Light_btn)
        moderateBtn = view.findViewById(R.id.moderate_btn)
        activeBtn = view.findViewById(R.id.active_btn)
        veryActiveBtn = view.findViewById(R.id.veryActive_btn)
        nextButton = view.findViewById(R.id.activity_next)

        nextButton.setOnClickListener {
            if (anyButtonClicked) {
                view.findNavController().navigate(R.id.action_activityLevel_to_infoFragment)
            } else {
                Toast.makeText(requireContext(), "Please make a choice", Toast.LENGTH_SHORT).show()
            }
        }

        setButtonClickListener(lightBtn)
        setButtonClickListener(moderateBtn)
        setButtonClickListener(activeBtn)
        setButtonClickListener(veryActiveBtn)

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
            sharedViewModel.activityLevel = buttonClicked


        }
    }

    /**
     * Återställer tillståndet för alla knappar för aktivitetsnivå till deras standardtillstånd.
     */
    private fun resetButtonState() {
        lightBtn.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.default_outline_button)
        moderateBtn.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.default_outline_button)
        activeBtn.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.default_outline_button)
        veryActiveBtn.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.default_outline_button)
    }


}
