package com.ahmetgezici.githubrepos.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ahmetgezici.githubrepos.R
import com.ahmetgezici.githubrepos.databinding.ActivityMainBinding
import com.ahmetgezici.githubrepos.viewmodel.FavoritesViewModel

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    var manager = supportFragmentManager

    companion object{
        lateinit var favoritesViewModel: FavoritesViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favoritesViewModel = ViewModelProvider(this).get(FavoritesViewModel::class.java)

        ////////////////////////////////////////////////////////////////////////////////////////////

        // Go HomeFragment

        val homeFragment = HomeFragment()

        manager.beginTransaction()
            .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
            .replace(binding.fragmentLayout.id, homeFragment, homeFragment.tag)
            .commit()

    }
}