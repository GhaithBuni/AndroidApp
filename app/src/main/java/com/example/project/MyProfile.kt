package com.example.project

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

/**
* Fragmentklass för användarens profil.
*
* Detta fragment visar användarens e-postadress, vikt, mål, längd och aktivitetsnivå.
* Användaren kan också uppdatera sin vikt i profilen.
*
* @constructor Skapar en ny instans av [MyProfile].
*/
class MyProfile : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth





    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_my_profile, container, false)
        val logout = view.findViewById<ImageButton>(R.id.logout_button)
        val getUserEmail = view.findViewById<TextView>(R.id.user_email)
        val getUserWeight = view.findViewById<EditText>(R.id.user_weight)
        val getUserGoal = view.findViewById<TextView>(R.id.user_goal)
        val getUserHeight = view.findViewById<TextView>(R.id.user_height)
        val getUserActiv = view.findViewById<TextView>(R.id.user_activ)
        val getUserUpdate = view.findViewById<Button>(R.id.update_button)

        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser?.uid
        val currentUser = auth.currentUser?.email
        if (user != null) {
            getUserEmail.text = currentUser
            database.child("users").child(user).child("weight").get().addOnSuccessListener {
                val userWeight = it.value
                getUserWeight.setText(userWeight.toString())


            }
            database.child("users").child(user).child("userGoal").get().addOnSuccessListener {
                val userGoal = it.value
                getUserGoal.text = userGoal.toString()

            }
            database.child("users").child(user).child("height").get().addOnSuccessListener {
                val userHeight = it.value
                getUserHeight.text = userHeight.toString() + " " + "cm"

            }
            database.child("users").child(user).child("activityLevel").get().addOnSuccessListener {
                val userActiv = it.value
                getUserActiv.text = userActiv.toString()

            }
            // Uppdatera användarens vikt i databasen

            getUserUpdate.setOnClickListener {

                val newWeight = getUserWeight.text.toString()
                database.child("users").child(user).child("weight").setValue(newWeight).toString()

                Toast.makeText(requireContext(), "Update successfully", Toast.LENGTH_SHORT).show()
            }


        }
        // Sätt en lyssnare för logga ut-knappen
        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(), MainActivity::class.java))
        }

        // Hitta navigationsvy och sätt en lyssnare för dess menyobjekt

        val bottomNavigationView =
            view.findViewById<BottomNavigationView>(R.id.bottomNavigationView2)
        bottomNavigationView.menu.findItem(R.id.menu_profile).isChecked = true

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    // No need to create a new instance, just navigate to the destination
                    view.findNavController().navigate(R.id.action_myProfile_to_homePage)
                    return@setOnItemSelectedListener true
                }

                R.id.menu_profile -> {
                    // Replace with your actual action ID
                    view.findNavController().navigate(R.id.myProfile)
                    return@setOnItemSelectedListener true
                }

                else -> false
            }
        }

        // No need to set the default selected fragment here

        return view


    }
}


