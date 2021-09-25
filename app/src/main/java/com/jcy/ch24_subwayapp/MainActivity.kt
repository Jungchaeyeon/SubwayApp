package com.jcy.ch24_subwayapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.jcy.ch24_subwayapp.databinding.ActivityMainBinding
import com.jcy.ch24_subwayapp.extension.toGone
import com.jcy.ch24_subwayapp.extension.toVisible
import com.jcy.ch24_subwayapp.presentation.stationarrivals.StationArrivalsFragmentArgs

class MainActivity : AppCompatActivity() {
    val binding :ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val navigationController by lazy {
        (supportFragmentManager.findFragmentById(R.id.mainNavigationHostContainer) as NavHostFragment).navController
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
        bindViews()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navigationController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun initViews() {
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navigationController)
    }

    private fun bindViews() {
        navigationController.addOnDestinationChangedListener { _, destination, argument ->
            if (destination.id == R.id.station_arrivals_dest) {
                title = StationArrivalsFragmentArgs.fromBundle(argument!!).station.name
                binding.toolbar.toVisible()
                binding.toolbar.toVisible()

            } else {
                binding.toolbar.toGone()
            }
        }
    }
}