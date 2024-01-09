package com.example.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.project.Model.SharedViewModel


/**
 * Fragmentklass för att ange ytterligare information som längd och vikt.
 *
 * Detta fragment låter användaren ange ytterligare information som längd och vikt och
 * navigera till nästa skärm för att skapa ett konto.
 *
 * @constructor Skapar en ny instans av [infoFragment2].
 */
class infoFragment2 : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        val view = inflater.inflate(R.layout.fragment_info2, container,false)
        val heightTxt = view.findViewById<EditText>(R.id.height_txt)
        val weightTxt = view.findViewById<EditText>(R.id.weight_txt)
        val nextButton = view.findViewById<Button>(R.id.info2_next)

        nextButton.setOnClickListener {
           val  height = heightTxt.text.toString().trim()
            val weight = weightTxt.text.toString().trim()
            if(height.isNotEmpty()) {
                if(weight.isNotEmpty()) {
                    val sharedViewModel: SharedViewModel by activityViewModels()
                    sharedViewModel.height = height
                    sharedViewModel.weight = weight

                    view.findNavController()
                        .navigate(R.id.action_infoFragment2_to_createAccountFrgment)
                }else{
                    Toast.makeText(requireContext(), "Please enter your Weight", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Please enter your Height", Toast.LENGTH_SHORT).show()
            }
        }


        return view
    }


}