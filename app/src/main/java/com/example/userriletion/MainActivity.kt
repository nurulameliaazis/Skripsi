package com.example.userriletion

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()


        val buttonNavigationView = findViewById<BottomNavigationView>  (R.id.buttomNavigationView)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment, R.id.scanFragment, R.id.solusiFragment, R.id.akunFragment ->
                    buttonNavigationView.visibility= View.VISIBLE

                else ->  buttonNavigationView.visibility= View.GONE
            }
        }

        buttonNavigationView.setupWithNavController(navController)


    }
}