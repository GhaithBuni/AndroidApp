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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.math.round

/**
 * Fragmentklass för att skapa ett användarkonto.
 *
 * Detta fragment låter användaren ange e-post och lösenord för att skapa ett konto.
 * Den beräknar också totala kaloribehovet baserat på användarens angivna information och mål.
 *
 * @constructor Skapar en ny instans av [CreateAccountFrgment].
 */
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
        val sharedViewModel1: SharedViewModel by activityViewModels()
        val weightCal = sharedViewModel1.weight
        val heightCal = sharedViewModel1.height
        val ageCal = sharedViewModel1.age

        var bmr = 0.0
        var amr = 0.0
        var totalCalories = 0.0

        if (sharedViewModel1.gender == "Female") {

            if (weightCal != null && heightCal != null && ageCal != null) {
                bmr = 655.1 + (9.563 * weightCal.toDouble()) + (1.850 * heightCal.toDouble() ) - (4.676 * ageCal.toInt())
            }

        }
        if (sharedViewModel1.gender == "Male") {

            if (weightCal != null && heightCal != null && ageCal != null) {
                bmr = 66.47 + (13.75 * weightCal.toDouble()) + (5.003 * heightCal.toDouble() ) - (6.775 * ageCal.toInt())
            }

        }

        if (sharedViewModel1.activityLevel == "Light: exercise 1-3 times/week"){
            amr = bmr * 1.375
        }
        if (sharedViewModel1.activityLevel == "Moderate_ exercise 4-5 times/week"){
            amr = bmr * 1.55
        }
        if (sharedViewModel1.activityLevel == "Active: daily exercise or intense exercise 3-4 times/week"){
            amr = bmr * 1.725
        }
        if (sharedViewModel1.activityLevel == "Very Active: intense exercise 6-7 times/week"){
            amr = bmr * 1.9

        }
        if (sharedViewModel1.userGoal == "Lose Weight"){
            totalCalories = amr - 500.0
        }




        val sharedViewModel: SharedViewModel by activityViewModels()
        // Set the value in the view model
        sharedViewModel.totalCalories = totalCalories

        nextButton.setOnClickListener {

            val email = emailTxt.text.toString()

            val sharedViewModel2: SharedViewModel by activityViewModels()

            // Retrieve the value from the view model


            // Create a user object with data from the shared view model
            val user = User(
                sharedViewModel2.userGoal, sharedViewModel2.activityLevel, sharedViewModel2.gender,
                sharedViewModel2.age, sharedViewModel2.height, sharedViewModel2.weight,
            )

            // Authenticate user
            auth.createUserWithEmailAndPassword(email, passTxt.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Get the UID after successful authentication
                        val uid = auth.currentUser?.uid

                        // Include UID in the user object
                        user.uid = uid
                        user.totalCalories = round( totalCalories / 100.0) * 100.0

                        // Store user data in the database
                        database.child("users").child(uid ?: "").setValue(user)
                        view.findNavController().navigate(R.id.action_createAccountFrgment_to_homePage)

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



