package uz.gita.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        val graph = navHostFragment.navController.navInflater.inflate(R.navigation.app_nav)
        navHostFragment.navController.graph = graph

    }
}