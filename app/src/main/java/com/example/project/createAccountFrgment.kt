package com.example.project

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [createAccountFrgment.newInstance] factory method to
 * create an instance of this fragment.
 */
private lateinit var auth: FirebaseAuth

class createAccountFrgment : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_create_account_frgment, container, false)

        auth = FirebaseAuth.getInstance()

        val emailTxt = view.findViewById<EditText>(R.id.email_account)
        val passTxt = view.findViewById<EditText>(R.id.pass_account)
        val nextButton = view.findViewById<Button>(R.id.account_next)

        nextButton.setOnClickListener {
            val email = emailTxt.text.toString()
            val password = passTxt.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Sign up success, navigate to home page or perform other actions
                            view.findNavController().navigate(R.id.action_createAccountFrgment_to_homePage)
                        } else {
                            // If sign up fails, display a message to the user.
                            Toast.makeText(
                                requireContext(),
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Email and password cannot be empty.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }

        return view
    }
}