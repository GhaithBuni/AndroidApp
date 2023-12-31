package com.example.project

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.text.SimpleDateFormat
import java.util.Calendar



class graphFragment : Fragment() {






    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        lateinit var lineGraphView: GraphView
        lateinit var lineGraphView1: GraphView
        val view = inflater.inflate(R.layout.fragment_graph, container, false)

        val bottomNavigationView =
            view.findViewById<BottomNavigationView>(R.id.bottomNavigationView2)

        lineGraphView = view.findViewById(R.id.graph1)
        bottomNavigationView.menu.findItem(R.id.menu_graph).isChecked = true

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    view.findNavController().navigate(R.id.action_graphFragment_to_homePage)
                    return@setOnItemSelectedListener true
                }

                R.id.menu_graph -> {
                    view.findNavController().navigate(R.id.graphFragment)
                    return@setOnItemSelectedListener true
                }

                R.id.menu_profile -> {
                    view.findNavController().navigate(R.id.action_graphFragment_to_myProfile)
                    return@setOnItemSelectedListener true
                }

                else -> false
            }
        }
        val startDate = Calendar.getInstance().apply { set(2023, Calendar.JANUARY, 1) }
        val numberOfDays = 5 // Adjust the number of days as needed
        val customWeights =
            doubleArrayOf(98.0, 95.0, 96.0, 92.5, 93.0) // Add your custom weights here
        val seriesDataPoints = mutableListOf<DataPoint>()

        for (i in 0 until numberOfDays) {
            val currentDate = startDate.clone() as Calendar
            currentDate.add(Calendar.DAY_OF_MONTH, i * 7) // Add 7 days to the current date

            val weight = customWeights[i]

            seriesDataPoints.add(DataPoint(currentDate.time, weight))
        }

        val series: LineGraphSeries<DataPoint> = LineGraphSeries(seriesDataPoints.toTypedArray())


        lineGraphView.gridLabelRenderer.gridStyle = GridLabelRenderer.GridStyle.HORIZONTAL
        lineGraphView.gridLabelRenderer.horizontalLabelsColor = Color.WHITE
        lineGraphView.gridLabelRenderer.verticalLabelsColor = Color.WHITE



        series.isDrawBackground = true
        // Set axis titles



        series.color = Color.CYAN // Set line color
        series.thickness = 4 // Set line thickness

        // Set the x-axis labels to display dates.
        lineGraphView.gridLabelRenderer.labelFormatter =
            DateAsXAxisLabelFormatter(requireContext(), SimpleDateFormat("dd/MM"))

        lineGraphView.viewport.isScrollable = true
        lineGraphView.viewport.isScalable = false
        lineGraphView.viewport.setScalableY(false)
        lineGraphView.viewport.setScrollableY(false)

        // Set the x-axis bounds to fit the four dates.
        //  lineGraphView.viewport.setMinX(Calendar.getInstance().apply { set(2023, Calendar.JANUARY, 1) }.time.time.toDouble())
        // lineGraphView.viewport.setMaxX(Calendar.getInstance().apply { set(2023, Calendar.JANUARY, 21) }.time.time.toDouble())
        lineGraphView.addSeries(series)
        //
        //
        //
        //
        //
        //
        //

        lineGraphView1 = view.findViewById(R.id.graph2)

        val daysOfWeek = arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        val caloriesData = doubleArrayOf(1500.0, 2000.0, 2500.0, 1800.0, 3000.0, 2200.0, 2800.0)

        // Creating data points for the series
        val series1: LineGraphSeries<DataPoint> = LineGraphSeries(
            Array(daysOfWeek.size) { i ->
                DataPoint(i.toDouble(), caloriesData[i])
            }
        )
        lineGraphView1.gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(requireContext(), SimpleDateFormat("E"))


        lineGraphView1.gridLabelRenderer.gridStyle = GridLabelRenderer.GridStyle.HORIZONTAL
        lineGraphView1.viewport.setMinX(0.0)
        lineGraphView1.viewport.setMaxX((daysOfWeek.size - 1).toDouble())

        // Set the y-axis bounds based on your calorie range.
        lineGraphView1.viewport.setMinY(1500.0)
        lineGraphView1.viewport.setMaxY(3500.0)

        lineGraphView1.viewport.isScrollable = true
        lineGraphView1.viewport.isScalable = false
        lineGraphView1.viewport.setScalableY(false)
        lineGraphView1.viewport.setScrollableY(false)
        series1.isDrawBackground = true
        lineGraphView1.addSeries(series1)


        return view
    }









}