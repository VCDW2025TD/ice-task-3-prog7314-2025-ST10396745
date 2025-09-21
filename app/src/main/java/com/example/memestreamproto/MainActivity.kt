package com.example.memestreamproto

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.memestreamproto.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.time.Instant

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.feedFragment,
                R.id.createMemeFragment,
                R.id.navigation_notifications,
                R.id.profilePageFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // 🔍 Desugaring runtime test
        try {
            val testInstant = Instant.parse("2024-09-21T10:15:30.00Z")
            Log.d("DesugarTest", "Epoch millis = ${testInstant.toEpochMilli()}")
        } catch (e: Exception) {
            Log.e("DesugarTest", "Parsing failed!", e)
        }
    }
}
