package com.example.project

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*


/**
 * ViewModel-klass som delas mellan olika fragment för att hantera och uppdatera gemensam data.
 *
 * Denna klass innehåller LiveData för återstående kalorivärden, samt funktioner för att uppdatera
 * dessa värden och hämta/uppdatera dem från och till Firebase-databasen.
 *
 * @constructor Skapar en ny instans av [SharedViewModel].
 */
class SharedViewModel : ViewModel() {

    private val _remCalValue = MutableLiveData<Double>()

    val remCalValue: LiveData<Double>
        get() = _remCalValue

     var previousRemCalValue: Double = 0.0

    // Firebase
    private val database = FirebaseDatabase.getInstance().reference


    private var user: String? = null

    /**
     * Funktion för att uppdatera det återstående kalorivärdet.
     *
     * @param newValue Det nya värdet som ska läggas till det befintliga återstående kalorivärdet.
     */
    fun updateRemCalValue(newValue: Double) {


        val updatedValue = previousRemCalValue + newValue
        println(previousRemCalValue)
        _remCalValue.value = updatedValue

        user?.let { updateUserRemainingCalories(updatedValue) }
    }

    /**
     * Funktion för att hämta det återstående kalorivärdet från Firebase.
     *
     * @param userId Användarens ID för att identifiera rätt användare i databasen.
     */
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

    /**
     * Funktion för att uppdatera det återstående kalorivärdet i Firebase.
     *
     * @param newValue Det nya värdet som ska uppdateras i databasen.
     */
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
