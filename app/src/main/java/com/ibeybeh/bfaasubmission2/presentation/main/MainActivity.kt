package com.ibeybeh.bfaasubmission2.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.ibeybeh.bfaasubmission2.R
import com.ibeybeh.bfaasubmission2.R.layout
import kotlinx.android.synthetic.main.activity_main.navMainView
import kotlinx.android.synthetic.main.app_bar.toolbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        setSupportActionBar(toolbar)

        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.navigation_search,
            R.id.navigation_search
        ).build()

        val navController = Navigation.findNavController(this, R.id.navHostFragment)

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(navMainView, navController)
    }
}