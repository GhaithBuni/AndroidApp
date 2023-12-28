package com.example.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [infoFragment2.newInstance] factory method to
 * create an instance of this fragment.
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