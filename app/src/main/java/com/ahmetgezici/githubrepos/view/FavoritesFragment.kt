package com.ahmetgezici.githubrepos.view

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmetgezici.githubrepos.R
import com.ahmetgezici.githubrepos.adapter.FavoritesAdapter
import com.ahmetgezici.githubrepos.databinding.FragmentFavoritesBinding
import com.ahmetgezici.githubrepos.utils.Tools
import com.ahmetgezici.githubrepos.viewmodel.FavoritesViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class FavoritesFragment : Fragment() {

    lateinit var binding: FragmentFavoritesBinding

    lateinit var viewModel: FavoritesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        viewModel = MainActivity.favoritesViewModel

        ////////////////////////////////////////////////////////////////////////////////////////////

        // Get DB Favorite Repos

        viewModel.getFavoritesDB()?.observe(viewLifecycleOwner, Observer {

            if (it.isNotEmpty()) {

                binding.reposRecycler.visibility = View.VISIBLE
                binding.noData.visibility = View.GONE

                val adapter = FavoritesAdapter(
                    it,
                    Tools.getLanguageColor(requireActivity()),
                    requireActivity().supportFragmentManager,
                    viewModel
                )
                binding.reposRecycler.layoutManager = LinearLayoutManager(requireContext())
                binding.reposRecycler.adapter = adapter

            } else {

                binding.reposRecycler.visibility = View.GONE
                binding.noData.visibility = View.VISIBLE

            }

            //////////////////////////////////

            binding.bottomAppBar.setOnMenuItemClickListener { item ->

                if (item.itemId == R.id.allDelete) {

                    // Delete All Favorite

                    if (it.isNotEmpty()) {

                        MaterialAlertDialogBuilder(
                            requireContext(),
                            R.style.MaterialAlertDialog_Rounded
                        )
                            .setTitle(getString(R.string.delete_all_title))
                            .setMessage(getString(R.string.delete_all_body))
                            .setPositiveButton(
                                getString(R.string.yes)
                            ) { dialog, which ->
                                viewModel.deleteAllFavoritesDB()
                            }
                            .setNegativeButton(getString(R.string.no), null)
                            .show()
                    }
                }

                false
            }

        })

        //////////////////////////////////

        // Go SearchFragment

        binding.addFavorite.setOnClickListener(View.OnClickListener {

            val searchFragment = SearchFragment()

            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                .replace(R.id.fragmentLayout, searchFragment, searchFragment.tag)
                .commit()

        })

        ////////////////////////////////////////////////////////////////////////////////////////////

        // BottomAppBar Animation

        val valueAnimator = ValueAnimator.ofFloat(Tools.dpToPx(40f).toFloat(), 0f)
        val mDuration = 1300
        valueAnimator.duration = mDuration.toLong()
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator.addUpdateListener { animation ->
            binding.bottomAppBar.cradleVerticalOffset = animation.animatedValue as Float
        }
        valueAnimator.start()

        // FAB Animation

        binding.addFavorite.animate().rotationBy(360F).setDuration(500)
            .setInterpolator(AccelerateDecelerateInterpolator()).startDelay = 450

        ////////////////////////////////////////////////////////////////////////////////////////////

        // Back Button

        binding.back.setOnClickListener(View.OnClickListener {

            closeFragment()

        })

        // Back Pressed

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    closeFragment()
                }
            })

        return binding.root
    }

    // Close Fragment

    fun closeFragment() {

        val homeFragment = HomeFragment()

        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
            .add(R.id.fragmentLayout, homeFragment, homeFragment.tag)
            .commit()

    }

}