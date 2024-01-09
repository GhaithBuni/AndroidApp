package com.example.project

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.project.databinding.ActivityMainBinding
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnSuccessListener

/**
 * MainActivity har i uppgift att fråga om appen får använda användarens position data
 * Sen så ska den spara positions datan och använda den för att kalkylera hur långt användaren har sprungit, medelhastigheten och hur länge användaren sprang
 * Till sist så ska MainActivity skicka positions datan till mapActivity
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var running = false
    var offset: Long = 0
    var totalRunLength: Double = 0.0
    var medelSpeed: Double = 0.0
    var firstLocation: Int = 0

    val OFFSET_KEY = "offset"
    val RUNNING_KEY = "running"
    val BASE_KEY = "base"
    val latitude = "latitude"
    val longitude = "longitude"
    val traveledDistance = "totalRunLength"
    val topSpeed = "maxSpeedKmH"

    lateinit var requestLocationButton: Button
    lateinit var Pause: Button
    lateinit var Finish: Button
    lateinit var runDistance: TextView
    lateinit var averageSpeed: TextView
    lateinit var comment: TextView

    lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    val mainHandler = Handler(Looper.getMainLooper())

    var locationPointsLat = ArrayList<Double>()
    var locationPointsLong = ArrayList<Double>()
    var arrayLat: ArrayList<String> = ArrayList()
    var arrayLong: ArrayList<String> = ArrayList()
    private var stopRunning = false

    /**
     * Ger alla parametrar de värden eller referenser de ska ha så att allt fungerar
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        runDistance = findViewById<TextView>(R.id.runDistance)
        averageSpeed = findViewById<TextView>(R.id.averageSpeed)
        comment = findViewById<TextView>(R.id.comment)
        requestLocationButton = findViewById<Button>(R.id.button_get_location)
        Pause = findViewById<Button>(R.id.Pause)
        Finish = findViewById<Button>(R.id.Finish)

        /**
         * Om det finns några värden som har sparats i appen så hämtas dom.
         */
        if (savedInstanceState != null) {
            offset = savedInstanceState.getLong(OFFSET_KEY)
            running = savedInstanceState.getBoolean(RUNNING_KEY)
            arrayLat = savedInstanceState.getStringArrayList(latitude)!!
            arrayLong = savedInstanceState.getStringArrayList(longitude)!!
            totalRunLength = savedInstanceState.getDouble(traveledDistance)
            runDistance.setText(totalRunLength.toString() + " KM")
            medelSpeed = savedInstanceState.getDouble(topSpeed)
            averageSpeed.setText(medelSpeed.toString() + " M/H")
            if (running) {
                binding.stopwatch.base = savedInstanceState.getLong(BASE_KEY)
                binding.stopwatch.start()
                mainHandler.post(moveRunnable)

            } else setBaseTime()
        }

        /**
         * När man trycker på [requestLocationButton] så körs funktionen [requestPermission]
         */
        requestLocationButton.setOnClickListener(View.OnClickListener { view: View? -> requestPermission() })

        /**
         * När man trycker på [Pause] knappen så pausas allt (klockan och hämtningen av positions data)
         */
        binding.Pause.setOnClickListener {
            if (running) {
                saveOffset()
                binding.stopwatch.stop()
                running = false
                stopRunning = true
            }
        }

        /**
         * När man trycker på [Finish] knappen så pausas allt och sen så görs [locationPointsLong] och [locationPointsLat] om till string array
         * så de kan skickas över till mapActivity och användas där.
         * Om man inte har ens börjat springa när man trycker på [Finish] så säger appen till att man måste springa för att kunna sluta springa
         */
        binding.Finish.setOnClickListener {
            if(locationPointsLong.size > 1 && locationPointsLat.size > 1) {
                if (running) {
                    saveOffset()
                    binding.stopwatch.stop()
                    running = false
                    stopRunning = true
                }

                var intent = Intent (this, mapActivity::class.java)

                for (lat in locationPointsLat) {
                    arrayLat.add(lat.toString())
                }

                for (long in locationPointsLong) {
                    arrayLong.add(long.toString())
                }

                intent.putExtra("latitude", arrayLat)
                intent.putExtra("longitude", arrayLong)

                startActivity(intent)
            }
            else {
                comment.setText("You can't finish a running session before it even began")
            }
        }

        requestPermissionLauncher = registerForActivityResult<String, Boolean>(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                requestPermission()
            } else {

            }
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }


    /**
     * Använder [getLocation] för att få informationen om vart användaren är någonstans
     * Sen så läggs datan in i en array av LatLng(innehåller latitude och longitude data) som heter [location]
     * Efter det så används [location] som in inparameter för [searchArea]
     * Allt det här görs varje sekund med hjälp av [Runnable]
     */
    private val moveRunnable = object : Runnable {
        override fun run() {
                if (!stopRunning) {
                    getLocation()
                    var coordinate = 0
                    totalRunLength = 0.0

                    while(coordinate < locationPointsLong.size - 1 && coordinate < locationPointsLat.size - 1) {
                        var location = ArrayList<LatLng>()

                        location.add(LatLng(locationPointsLat[coordinate], locationPointsLong[coordinate]))
                        location.add(LatLng(locationPointsLat[coordinate + 1], locationPointsLong[coordinate + 1]))
                        searchArea(location)
                        coordinate++
                    }
                    mainHandler.postDelayed(this, 1000)
                }
        }
    }

    /**
     * Frågar om appen får använda användarens positions information
     * Om appen får göra det så startas klockan och [Runnable]
     * Om appen inte får göra det så berättar den att appen kan inte fungera utan användarens positions data.
     */
    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (!running) {
                setBaseTime()
                binding.stopwatch.start()
                running = true
                stopRunning = false
            }
            mainHandler.post(moveRunnable)
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            comment.setText("If you do not give permission so that this app can use your location data then it won't work")
            // Request Permission

        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    /**
     * Tar reda på användarens position är och sätter in den i [locationPointsLat] och [locationPointsLong]
     */
    private fun getLocation() {
        fusedLocationProviderClient.getLastLocation()
            .addOnSuccessListener(this, object : OnSuccessListener<Location?> {
                override fun onSuccess(location: Location?) {
                    if (location != null) {
                        if(firstLocation != 0) {
                            locationPointsLat.add(location.latitude)
                            locationPointsLong.add(location.longitude)
                        }

                    }

                }
            })
        val currentLocationRequest: CurrentLocationRequest = CurrentLocationRequest.Builder()
            .setMaxUpdateAgeMillis(1000)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()
        fusedLocationProviderClient.getCurrentLocation(currentLocationRequest, null)
            .addOnSuccessListener(this, object : OnSuccessListener<Location?> {
                override fun onSuccess(location: Location?) {
                    if (location != null) {
                        if(firstLocation == 0) {
                            locationPointsLat.add(location.latitude)
                            locationPointsLong.add(location.longitude)
                            firstLocation++
                        }

                    }
                }
            })
    }

    /**
     * Pausar klockan
     */
    override fun onPause() {
        super.onPause()
        if (running) {
            saveOffset()
            binding.stopwatch.stop()
        }
    }

    /**
     * Startar klockan om den har pausats
     */
    override fun onResume() {
        super.onResume()
        if (running) {
            setBaseTime()
            binding.stopwatch.start()
            offset = 0
        }
    }

    /**
     * Sparar allt data så den kan användas igen
     * Används för när man vrider mobilen 90 grader så finns all information kvar
     */
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        for (lat in locationPointsLat) {
            arrayLat.add(lat.toString())
        }

        for (long in locationPointsLong) {
            arrayLong.add(long.toString())
        }

        savedInstanceState.putLong(OFFSET_KEY, offset)
        savedInstanceState.putBoolean(RUNNING_KEY, running)
        savedInstanceState.putLong(BASE_KEY, binding.stopwatch.base)
        savedInstanceState.putStringArrayList(latitude, arrayLat)
        savedInstanceState.putStringArrayList(longitude, arrayLong)
        savedInstanceState.getDouble(topSpeed, medelSpeed)
        savedInstanceState.getDouble(traveledDistance, totalRunLength)
        super.onSaveInstanceState(savedInstanceState)
    }

    /**
     * Bestämmer vilken tid som klockan ska börja på
     */
    private fun setBaseTime() {
        binding.stopwatch.base = SystemClock.elapsedRealtime() - offset
    }

    /**
     * Sparar tiden som klockan har
     */
    private fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - binding.stopwatch.base
    }


    /**
     * Tar en array av LatLng som inparameter för att kalkylera avståndet mellan de två punkterna
     * Sen så kalkylerar och vissar medelhastigheten och hur långt användaren har sprungit.
     */
    private fun searchArea(location: ArrayList<LatLng>) {
        var currentLocation: LatLng = location[0]
        var nextLocation: LatLng = location[1]
        val results = FloatArray(1)
        Location.distanceBetween(
            currentLocation.latitude,
            currentLocation.longitude,
            nextLocation.latitude,
            nextLocation.longitude,
            results
        )
        totalRunLength += results[0]

        medelSpeed = totalRunLength / locationPointsLong.size

        val rD = String.format("%.1f", totalRunLength / 1000)
        val fR = String.format("%.1f", medelSpeed)

        runDistance.setText("Du har sprungit $rD KM")
        averageSpeed.setText("Medelhastighet: $fR M/S")
    }
}