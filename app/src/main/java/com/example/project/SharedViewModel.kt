package com.example.project

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SharedViewModel : ViewModel() {

    private val _remCalValue = MutableLiveData<Double>()

    // Expose the LiveData as read-only
    val remCalValue: LiveData<Double>
        get() = _remCalValue

    // Variable to store the previous value
     var previousRemCalValue: Double = 0.0

    // Firebase
    private val database = FirebaseDatabase.getInstance().reference


    private var user: String? = null

    // Function to update the value
    fun updateRemCalValue(newValue: Double) {
        // Save the previous value
       // previousRemCalValue = _remCalValue.value ?: 0.0

        // Calculate the new value by adding the previous value and the new one
        val updatedValue = previousRemCalValue + newValue
        //println(updatedValue)
        println(previousRemCalValue)
        // Update the LiveData
        _remCalValue.value = updatedValue

        // Update the value in Firebase if a user is logged in
        user?.let { updateUserRemainingCalories(updatedValue) }
    }

    // Function to fetch remaining calories from Firebase
    fun fetchRemainingCaloriesFromFirebase(userId: String) {
        user = userId

        database.child("users").child(userId).child("remaining").child("value")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val remainingCalories = snapshot.getValue(Double::class.java)
                    if (remainingCalories != null) {
                        updateRemCalValue(remainingCalories)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
    }

    // Function to update remaining calories in Firebase
    private fun updateUserRemainingCalories(newValue: Double) {
        user?.let {
            database.child("users").child(it).child("remaining").setValue(newValue)
        }
    }

    // Your other variables
    var userGoal: String? = null
    var activityLevel: String? = null
    var gender: String? = null
    var age: String? = null
    var height: String? = null
    var weight: String? = null
    var totalCalories: Double? = null
    var newValue: Double? = null
}
