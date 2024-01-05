package com.example.project

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase




class CreateAccountFrgment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_create_account_frgment, container, false)

        val emailTxt = view.findViewById<EditText>(R.id.email_account)
        val passTxt = view.findViewById<EditText>(R.id.pass_account)
        val nextButton = view.findViewById<Button>(R.id.sumbit_btn)

        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        nextButton.setOnClickListener {
            val bundle = arguments
            val email = emailTxt.text.toString()
            val myGoal = bundle?.getString("val1")
            val sharedViewModel: SharedViewModel by activityViewModels()

            // Retrieve the value from the view model
            val userGoal = sharedViewModel.userGoal

            // Create a user object with data from the shared view model
            val user = User(
                sharedViewModel.userGoal, sharedViewModel.activityLevel, sharedViewModel.gender,
                sharedViewModel.age, sharedViewModel.height, sharedViewModel.weight
            )

            // Authenticate user
            auth.createUserWithEmailAndPassword(email, passTxt.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Get the UID after successful authentication
                        val uid = auth.currentUser?.uid

                        // Include UID in the user object
                        user.uid = uid

                        // Store user data in the database
                        database.child("users").child(uid ?: "").setValue(user)

                        // Navigate to home page or perform other actions
                        // view.findNavController().navigate(R.id.action_createAccountFrgment_to_homePage)
                    } else {
                        // If sign up fails, display a message to the user.
                        Toast.makeText(
                            requireContext(),
                            "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        return view
    }
}



