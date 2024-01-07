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
import android.widget.PopupMenu
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.childEvents
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Comment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class searchView : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var myRef: DatabaseReference
    private lateinit var listView: ListView
    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val foodNames = ArrayList<String>()
        database = FirebaseDatabase.getInstance().getReference("Food")
        myRef = FirebaseDatabase.getInstance().reference
        val view = inflater.inflate(R.layout.fragment_search_view, container, false)
        val rac = view.findViewById<TextView>(R.id.calories_Prog)
        auth = FirebaseAuth.getInstance()

        val backBtn = view.findViewById<Button>(R.id.backBtn)

        backBtn.setOnClickListener {
            view.findNavController().popBackStack()
        }


        val search = view.findViewById<SearchView>(R.id.searchView)
        listView = view.findViewById<ListView>(R.id.listView)

        val adapter: ArrayAdapter<String> =
            ArrayAdapter(requireContext(), R.layout.text_color, foodNames)

        listView.adapter = adapter

        // Uncomment and adapt the following code if you want to implement search functionality


        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                // Get the key of the item, e.g., "Appel", "Banana", etc.
                val foodName = dataSnapshot.key

                // Check if the key is not null and matches the expected format
                if (foodName != null && dataSnapshot.hasChild("Calories")) {
                    // Get the specific properties of the item
                    val calories = dataSnapshot.child("Calories").value?.toString() ?: "N/A"
                    val carbs = dataSnapshot.child("Carbs").value?.toString() ?: "N/A"
                    val fat = dataSnapshot.child("Fat").value?.toString() ?: "N/A"
                    val protein = dataSnapshot.child("Protein").value?.toString() ?: "N/A"

                    // Create a string representation of the item
                    val comment = foodName

                    // Add the string representation to the list
                    foodNames.add(comment)

                    // Update the adapter
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


    @SuppressLint("MissingInflatedId", "SetTextI18n")
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

                // Get the root view to calculate the center
                val rootView = anchorView.rootView

                // Show the PopupWindow at the center
                window.showAtLocation(rootView, Gravity.CENTER, 0, 0)

                // Access the EditText and Button from the existing layout (view)
                val valueEditText = view.findViewById<EditText>(R.id.editText)
                val addBtn = view.findViewById<Button>(R.id.add_btn)

                var new_value: Double = 0.0

                addBtn.setOnClickListener {
                    // Get the text from the EditText
                    val inputText = valueEditText.text.toString()

                    if (inputText.isNotBlank()) {
                        try {
                            // Convert the input to Double
                            val inputValue = inputText.toDouble()

                            // Assuming "calories" is a constant value
                            if (calories != null) {
                                // Calculate the new value based on user input
                                val new_value = (calories / 100) * inputValue

                                // Update the SharedViewModel with the new value
                                val sharedViewModel: SharedViewModel by activityViewModels()
                                sharedViewModel.newValue = new_value
                                sharedViewModel.updateRemCalValue(new_value)
                                val user = auth.currentUser?.uid
                                if (user != null) {
                                    myRef.child("users").child(user).child("remaining").setValue(sharedViewModel.remCalValue)
                                }

                                // Dismiss the PopupWindow
                                window.dismiss()
                            }
                        } catch (e: NumberFormatException) {
                            // Handle the case when the input is not a valid number
                            // Show an error message or take appropriate action
                            Toast.makeText(requireContext(), "Please enter a valid number", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Handle the case when the input is blank
                        // Show an error message or take appropriate action
                        Toast.makeText(requireContext(), "Please enter a number", Toast.LENGTH_SHORT).show()
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


