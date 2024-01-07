package com.example.project

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.math.round



/**
 * Fragmentklass för startsidan i applikationen.
 *
 * Detta fragment visar användarens dagliga mål, rekommenderade kaloriintag för varje måltid
 * och deras nuvarande återstående kalorimängd. Dessutom ger den användaren knappar
 * för att navigera till olika delar av applikationen, som frukost, lunch, middag och snacks.
 *
 * @constructor Skapar en ny instans av [homePage].
 */

class homePage : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    @SuppressLint("SetTextI18n", "MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)
        val bottomNavigationView =
            view.findViewById<BottomNavigationView>(R.id.bottomNavigationView2)
        val breakfastBtn = view.findViewById<Button>(R.id.breakfast_btn)
        val lunchBtn = view.findViewById<Button>(R.id.lunch_btn)
        val dinnerBtn = view.findViewById<Button>(R.id.dinner_btn)
        val snackBtn = view.findViewById<Button>(R.id.snack_btn)
        val baseGoal = view.findViewById<TextView>(R.id.baseGoal)
        val breakfast_Rec = view.findViewById<TextView>(R.id.breakfast_Rec)
        val lunch_Rec = view.findViewById<TextView>(R.id.lunch_Rec)
        val dinner_Rec = view.findViewById<TextView>(R.id.dinner_Rec)
        val snack_Rec = view.findViewById<TextView>(R.id.snack_Rec)
        val rem_Cal = view.findViewById<TextView>(R.id.calories_Prog)
        val prog = view.findViewById<ProgressBar>(R.id.progress_bar)



        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance()

        // Assuming "database" is your DatabaseReference
        val sharedViewModel: SharedViewModel by activityViewModels()
        val user = auth.currentUser?.uid

        var kcal: Int? = null
        if (user != null) {

            database.child("users").child(user).child("totalCalories").get().addOnSuccessListener { dataSnapshot ->
                // Check if the data exists
                if (dataSnapshot.exists()) {
                    // Retrieve the value as a Double
                   kcal= dataSnapshot.getValue(Int::class.java)

                    // Check if kcal is not null before using it
                    if (kcal != null) {
                        baseGoal.text = "Base Goal\n$kcal"
                        prog.max = kcal as Int
                        breakfast_Rec.text = "Recommended Calorie " + round(kcal!! * 0.25  ).toString() +" - "+round(
                            kcal!! * 0.3 ).toString() + " Kcal"
                        lunch_Rec.text = "Recommended Calorie " + round(kcal!! * 0.35  ).toString() +" - "+round(
                            kcal!! * 0.4 ).toString() + " Kcal"
                        dinner_Rec.text = "Recommended Calorie " + round(kcal!! * 0.25  ).toString() +" - "+round(
                            kcal!! * 0.3 ).toString() + " Kcal"
                        snack_Rec.text = "Recommended Calorie " + round(kcal!! * 0.05  ).toString() +" - "+round(
                            kcal!! * 0.1 ).toString() + " Kcal"
                    } else {
                        // Handle the case where kcal is null
                        Log.e("firebase", "Total calories is null")
                    }
                } else {
                    // Handle the case where the data does not exist
                    Log.e("firebase", "Total calories data does not exist")
                }
            }.addOnFailureListener { exception ->
                Log.e("firebase", "Error getting data", exception)
            }
        }

        if (user != null) {
            database.child("users").child(user).child("remaining").child("value").get().addOnSuccessListener { dataSnapShot ->
                if (dataSnapShot.exists()) {
                    val n1 = dataSnapShot.getValue(Double::class.java)

                    if (n1 != null) {
                        // Check if n1 is greater than or equal to kcal
                        if (n1 >= kcal!!) {
                            sharedViewModel.previousRemCalValue = 0.0
                            rem_Cal.text = "0.0\nKcal"
                            prog.progress = 0
                        } else {
                            sharedViewModel.previousRemCalValue = n1
                            rem_Cal.text = "$n1\nKcal"
                            prog.progress = n1.toInt()
                        }
                    }
                }
            }
        }



        breakfastBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_homePage_to_searchView2)

        }

        lunchBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_homePage_to_lunch)
        }
        dinnerBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_homePage_to_dinner2)
        }
        snackBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_homePage_to_snack)
        }

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    // No need to create a new instance, just navigate to the destination
                    view.findNavController().navigate(R.id.homePage)
                    return@setOnItemSelectedListener true
                }



                R.id.menu_profile -> {
                    // Replace with your actual action ID
                    view.findNavController().navigate(R.id.action_homePage_to_myProfile)
                    return@setOnItemSelectedListener true
                }

                else -> false
            }
        }


        // No need to set the default selected fragment here

        return view
    }


}