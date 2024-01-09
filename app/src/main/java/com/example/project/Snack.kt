package com.example.project

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.project.Model.SharedViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
/**
 * Fragmentklass för sökning och visning av livsmedelsinformation.
 *
 * Detta fragment ger användaren möjlighet att söka och visa information om olika livsmedel.
 * Användaren kan även välja ett livsmedel för att se detaljerad information och lägga till
 * en mängd av det livsmedlet till sitt dagliga intag.
 *
 * @constructor Skapar en ny instans av [searchView].
 */

@Suppress("NAME_SHADOWING")
class Snack : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var myRef: DatabaseReference


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        myRef = FirebaseDatabase.getInstance().reference

        database = FirebaseDatabase.getInstance().getReference("Food")
        val foodNames = ArrayList<String>()
        val view = inflater.inflate(R.layout.fragment_snack, container, false)

        val backBtn = view.findViewById<Button>(R.id.backBtn_Snack)

        backBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_snack_to_homePage)
        }

        val search = view.findViewById<SearchView>(R.id.searchView_Snack)
        val listView = view.findViewById<ListView>(R.id.listView_Snack)


        val adapter: ArrayAdapter<String> =
            ArrayAdapter(requireContext(), R.layout.text_color, foodNames)

        listView.adapter = adapter

        // Skapa en lyssnare för att hämta livsmedelsinformation från Firebase
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val foodName = dataSnapshot.key

                if (foodName != null && dataSnapshot.hasChild("Calories")) {


                    val comment = foodName

                    foodNames.add(comment)

                    adapter.notifyDataSetChanged()
                }
            }


            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                // Implement if needed
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                // Implement if needed
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Implement if needed
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    requireContext(),
                    "Failed to load comments: ${databaseError.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        database.addChildEventListener(childEventListener)

        // Skapa en lyssnare för sökvy
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                search.clearFocus()
                if (foodNames.contains(query)) {
                    adapter.filter.filter(query)
                } else {
                    Toast.makeText(requireContext(), "Item not Found", Toast.LENGTH_SHORT).show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }

        })

        listView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String
            showPopupLayout(selectedItem, view)

        }
        return view
    }

    @SuppressLint("MissingInflatedId", "SetTextI18n", "InflateParams")
    private fun showPopupLayout(foodName: String, anchorView: View) {
        val foodRef = database.child(foodName)

        foodRef.get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                val calories = dataSnapshot.child("Calories").getValue(Double::class.java)
                val carbs = dataSnapshot.child("Carbs").getValue(Double::class.java)
                val fat = dataSnapshot.child("Fat").getValue(Double::class.java)
                val protein = dataSnapshot.child("Protein").getValue(Double::class.java)

                // Use anchorView.context to get the context
                val window = PopupWindow(anchorView.context)
                val view = layoutInflater.inflate(R.layout.fragment_popup, null)

                window.contentView = view
                window.isFocusable = true

                val protineMacro = view.findViewById<TextView>(R.id.protein_macro)
                val carbMacro = view.findViewById<TextView>(R.id.carb_macro)
                val fatMacro = view.findViewById<TextView>(R.id.fat_macro)
                val kcal = view.findViewById<TextView>(R.id.Kcal)


                protineMacro.text = protein.toString()
                fatMacro.text = fat.toString()
                carbMacro.text = carbs.toString()
                kcal.text = calories.toString() + " Kcal per 100g"

                val txt = view.findViewById<TextView>(R.id.textViewCalories)
                txt.setOnClickListener {
                    window.dismiss()
                }

                val rootView = anchorView.rootView

                window.showAtLocation(rootView, Gravity.CENTER, 0, 0)

                val valueEditText = view.findViewById<EditText>(R.id.editText)
                val addBtn = view.findViewById<Button>(R.id.add_btn)

                var new_value: Double

                addBtn.setOnClickListener {
                    val inputText = valueEditText.text.toString()

                    if (inputText.isNotBlank()) {
                        val inputValue = inputText.toDouble()

                        // Assuming "calories" is a constant value
                        if (calories != null) {
                            new_value = (calories / 100) * inputValue
                            val sharedViewModel: SharedViewModel by activityViewModels()
                            sharedViewModel.updateRemCalValue(new_value)
                            val user = auth.currentUser?.uid
                            if (user != null) {
                                myRef.child("users").child(user).child("remaining").setValue(sharedViewModel.remCalValue)
                            }
                            window.dismiss()

                            // Do something with the new_value
                        }
                    } else {
                        // Handle the case when the input is blank or not a valid number
                        // You may want to show an error message or take appropriate action
                    }
                }
            } else {
                Log.e("firebase", "Data not found for $foodName")
            }
        }.addOnFailureListener { exception ->
            Log.e("firebase", "Error getting data for $foodName", exception)
            // Handle the error if needed
        }



    }
}