package com.example.project

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.navigation.findNavController
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Snack.newInstance] factory method to
 * create an instance of this fragment.
 */
class Snack : Fragment() {
    private lateinit var database: DatabaseReference


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        database = FirebaseDatabase.getInstance().getReference("Food")
        val foodNames = ArrayList<String>()
        val view = inflater.inflate(R.layout.fragment_snack, container, false)

        val backBtn = view.findViewById<Button>(R.id.backBtn_Snack)

        backBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_snack_to_homePage)
        }

        val search = view.findViewById<SearchView>(R.id.searchView_Snack)
        val listView = view.findViewById<ListView>(R.id.listView_Snack)



        val adapter: ArrayAdapter<String> = ArrayAdapter(requireContext(), R.layout.text_color, foodNames)

        listView.adapter = adapter

        // Inflate the layout for this fragment

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

         search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
         override fun onQueryTextSubmit(query: String?): Boolean {
             search.clearFocus()
             if(foodNames.contains(query))
             {
                 adapter.filter.filter(query)
             }else{
                 Toast.makeText(requireContext(), "Item not Found", Toast.LENGTH_SHORT).show()
             }
             return false
         }

         override fun onQueryTextChange(newText: String?): Boolean {
             adapter.filter.filter(newText)
             return false
         }

     })
        return view
    }


}