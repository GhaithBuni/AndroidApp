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
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_snack, container, false)

        val backBtn = view.findViewById<Button>(R.id.backBtn_Snack)

        backBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_snack_to_homePage)
        }

        val search = view.findViewById<SearchView>(R.id.searchView_Snack)
        val listView = view.findViewById<ListView>(R.id.listView_Snack)

        val names = arrayOf("Egg","meat", "chicken", "Salmon","Rice", "Oat", "Potato")


        val adapter: ArrayAdapter<String> = ArrayAdapter(requireContext(), R.layout.text_color, names)

        listView.adapter = adapter
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                search.clearFocus()
                if(names.contains(query))
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
        // Inflate the layout for this fragment
        return view
    }


}