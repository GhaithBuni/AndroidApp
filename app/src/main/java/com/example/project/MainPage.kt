package com.example.project

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * Fragmentklass för huvudsidan i applikationen.
 *
 * Detta fragment möjliggör användarens navigering till att antingen skapa ett nytt konto
 * eller logga in med befintliga autentiseringsuppgifter.
 *
 * @constructor Skapar en ny instans av [MainPage].
 */

class MainPage : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance().reference
        val view = inflater.inflate(R.layout.fragment_main_page ,container ,false)
        val startButton = view.findViewById<Button>(R.id.create_button)
        val logInBtn = view.findViewById<Button>(R.id.Login_button)
        val emailTxt = view.findViewById<EditText>(R.id.user_email)
        val passTxt = view.findViewById<EditText>(R.id.user_password)



        startButton.setOnClickListener{
            view.findNavController().navigate(R.id.action_mainPage_to_choseYourGoalFragment)
        }



       logInBtn.setOnClickListener {
            val email = emailTxt.text.toString()
            val password = passTxt.text.toString()

           if(email.isNotEmpty() && password.isNotEmpty()) {
               auth.signInWithEmailAndPassword(email, password)
                   .addOnCompleteListener(requireActivity()) { task ->
                       if (task.isSuccessful) {
                           // Sign in success, update UI with the signed-in user's information
                           Log.d(TAG, "signInWithEmail:success")
                           view.findNavController().navigate(R.id.action_mainPage_to_homePage)
                       } else {
                           // If sign in fails, display a message to the user.
                           Log.w(TAG, "signInWithEmail:failure", task.exception)
                           Toast.makeText(
                               requireContext(),
                               "Authentication failed.",
                               Toast.LENGTH_SHORT,
                           ).show()
                       }
                   }
           }
        }
        return view


    }






}


