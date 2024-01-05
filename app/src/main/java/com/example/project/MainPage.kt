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
import com.google.firebase.database.database

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainPage.newInstance] factory method to
 * create an instance of this fragment.
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
        //    view.findNavController().navigate(R.id.action_mainPage_to_homePage)
            writeNewUser("user1","ghaith")
        }

       /* logInBtn.setOnClickListener {
            val email = emailTxt.text.toString()
            val password = passTxt.text.toString()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
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
        }*/
        return view;





    }



    private fun writeNewUser(userId: String, name: String) {
        val user = User(name)

        database.child("users").child(userId).setValue(user)


    }


}


