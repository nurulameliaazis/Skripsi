package com.example.userriletion

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()


        val buttonNavigationView = findViewById<BottomNavigationView>  (R.id.bottomNavigationView)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment, R.id.scanFragment, R.id.solusiFragment, R.id.historyFragment ->
                    buttonNavigationView.visibility= View.VISIBLE

                else ->  buttonNavigationView.visibility= View.GONE
            }
        }

        buttonNavigationView.setupWithNavController(navController)


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    Toast.makeText(
                        this,
                        "GPS is berhasil",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Activity.RESULT_CANCELED -> Toast.makeText(
                    this,
                    "GPS is r is required",
                            Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}
