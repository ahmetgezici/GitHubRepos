package com.ahmetgezici.githubrepos.view

import android.app.Activity
import android.app.Dialog
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.ahmetgezici.githubrepos.R
import com.ahmetgezici.githubrepos.databinding.DialogSettingBinding
import com.ahmetgezici.githubrepos.databinding.FragmentHomeBinding
import com.ahmetgezici.githubrepos.utils.Tools
import com.mahfa.dnswitch.DayNightSwitchListener
import java.util.*


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        ////////////////////////////////////////////////////////////////////////////////////////////

        // For Setting State Change

        if (Locale.getDefault().language.equals("tr")) {
            setLocale(requireActivity(), "tr")
        } else {
            setLocale(requireActivity(), "en")
        }

        // Go SearchFragment

        binding.searchRepo.setOnClickListener(View.OnClickListener {

            val searchFragment = SearchFragment()

            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                .replace(R.id.fragmentLayout, searchFragment, searchFragment.tag)
                .commit()

        })

        // Go FavoritesFragment

        binding.favoriteRepos.setOnClickListener(View.OnClickListener {

            val favoritesFragment = FavoritesFragment()

            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                .replace(R.id.fragmentLayout, favoritesFragment, favoritesFragment.tag)
                .commit()

        })

        ////////////////////////////////////////////////////////////////////////////////////////////

        // Open Settings

        binding.setting.setOnClickListener(View.OnClickListener {

            // Create Setting Dialog

            val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                Resources.getSystem().displayMetrics.widthPixels - (Tools.dpToPx(25F) +
                        Tools.dpToPx(25F)),
                Tools.dpToPx(300F)
            )

            val dialog = Dialog(requireContext())
            val dialogBinding: DialogSettingBinding =
                DialogSettingBinding.bind(inflater.inflate(R.layout.dialog_setting, null))

            dialog.setContentView(dialogBinding.root, params)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            ///////////////////////////////////

            // Theme Setting

            when (requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {

                Configuration.UI_MODE_NIGHT_YES -> {
                    dialogBinding.dayNightSwitch.setIsNight(true)
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    dialogBinding.dayNightSwitch.setIsNight(false)
                }
                Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                    dialogBinding.dayNightSwitch.setIsNight(false)
                }
            }

            dialogBinding.dayNightSwitch.setListener(DayNightSwitchListener {

                Handler(Looper.getMainLooper()).postDelayed(Runnable {
                    if (it) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                }, 600)

            })

            ///////////////////////////////////

            // Language Setting

            if (Locale.getDefault().language == "tr") {
                dialogBinding.languageSwitch.setChecked(false)
            } else {
                dialogBinding.languageSwitch.setChecked(true)
            }

            dialogBinding.languageSwitch.setOnCheckedChangeListener { checked ->

                Handler(Looper.getMainLooper()).postDelayed(Runnable {

                    if (checked) {

                        setLocale(requireActivity(), "en");
                        refleshUI();
                        dialog.dismiss();

                    } else {

                        setLocale(requireActivity(), "tr");
                        refleshUI();
                        dialog.dismiss();
                    }

                }, 600)

            }

            ///////////////////////////////////

            dialog.show();

        })

        ////////////////////////////////////////////////////////////////////////////////////////////

        // View Animations

        binding.searchRepo.animate().alphaBy(1F).setDuration(400)
            .setInterpolator(AccelerateDecelerateInterpolator()).startDelay = 200

        binding.favoriteRepos.animate().alphaBy(1F).setDuration(400)
            .setInterpolator(AccelerateDecelerateInterpolator()).startDelay = 400

        binding.setting.animate().alphaBy(1F).rotationBy(360F).setDuration(800)
            .setInterpolator(AccelerateDecelerateInterpolator()).startDelay = 450


        return binding.root
    }


    // For Language Setting

    fun refleshUI() {
        val homeFragment = HomeFragment()
        parentFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
            .replace(R.id.fragmentLayout, homeFragment, homeFragment.tag)
            .commit()
    }


    // Set Language

    private fun setLocale(activity: Activity, languageCode: String) {

        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources = activity.resources
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

    }


}