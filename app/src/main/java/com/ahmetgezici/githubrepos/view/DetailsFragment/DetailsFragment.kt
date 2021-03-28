package com.ahmetgezici.githubrepos.view.DetailsFragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ahmetgezici.githubrepos.R
import com.ahmetgezici.githubrepos.databinding.FragmentDetailsBinding
import com.ahmetgezici.githubrepos.model.FavoritesRepo
import com.ahmetgezici.githubrepos.utils.Tools
import com.ahmetgezici.githubrepos.utils.datautil.Status
import com.ahmetgezici.githubrepos.view.MainActivity
import com.ahmetgezici.githubrepos.viewmodel.DetailsViewModel
import com.ahmetgezici.githubrepos.viewmodel.FavoritesViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.like.LikeButton
import com.like.OnLikeListener
import java.util.*


class DetailsFragment(val userName: String, val repoName: String, val starStatus: Boolean) :
    Fragment() {

    lateinit var binding: FragmentDetailsBinding

    lateinit var viewModel: DetailsViewModel
    lateinit var favoritesViewModel: FavoritesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)
        favoritesViewModel = MainActivity.favoritesViewModel

        ////////////////////////////////////////////////////////////////////////////////////////////

        // Loading State

        viewModel.loading.observe(viewLifecycleOwner, Observer {

            if (it) {
                binding.loadingProgress.visibility = View.VISIBLE

            } else {
                binding.loadingProgress.visibility = View.GONE
            }

        })

        //////////////////////////////////

        // Image Loading State

        viewModel.imageLoading.observe(viewLifecycleOwner, Observer {

            if (it) {
                binding.imageProgress.visibility = View.VISIBLE
                binding.avatar.visibility = View.INVISIBLE
            } else {
                binding.imageProgress.visibility = View.GONE
                binding.avatar.visibility = View.VISIBLE
            }

        })

        //////////////////////////////////

        // Get User Data && Repo Data

        viewModel.getUserAndRepo(userName, repoName).observe(viewLifecycleOwner, Observer {

            val userResource = it.userResource
            val repoResource = it.repoResource

            if (userResource?.status == Status.LOADING || repoResource?.status == Status.LOADING) {

                viewModel.loading.postValue(true)

            } else if (userResource?.status == Status.SUCCESS && repoResource?.status == Status.SUCCESS) {

                val userData = userResource.data
                val repoData = repoResource.data

                if (userData != null && repoData != null) {

                    binding.name.text = userData.name
                    binding.userName.text = userData.login
                    binding.following.text = userData.following.toString()
                    binding.followers.text = userData.followers.toString()
                    binding.location.text = userData.location
                    binding.company.text = userData.company

                    // Load Image

                    Glide.with(requireContext())
                        .asBitmap()
                        .load(userData.avatar_url)
                        .into(object : CustomTarget<Bitmap?>() {
                            override fun onResourceReady(
                                @NonNull resource: Bitmap,
                                @Nullable transition: Transition<in Bitmap?>?
                            ) {

                                binding.avatar.setImageBitmap(resource)

                                viewModel.imageLoading.postValue(false)

                            }

                            override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
                        })

                    ///////

                    binding.repoName.text = repoData.name
                    binding.description.text = repoData.description
                    binding.language.text = repoData.language
                    binding.starCount.text = repoData.stargazers_count.toString()
                    binding.issues.text = repoData.open_issues_count.toString()

                    binding.createdDate.text = Tools.convertDate(repoData.created_at)
                    binding.updatedDate.text = Tools.convertDate(repoData.updated_at)

                    val languageColor = Tools.getLanguageColor(requireActivity())
                    val colorCode = languageColor[repoData.language]

                    if (colorCode != null) {
                        binding.languageShape.setCardBackgroundColor(Color.parseColor(colorCode))
                    } else {
                        binding.languageShape.visibility = View.INVISIBLE
                    }

                    binding.goGithub.setOnClickListener(View.OnClickListener {

                        // Go Github Repo

                        if (repoData.html_url.isNotEmpty()) {

                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse(repoData.html_url)
                            startActivity(intent)

                        }

                    })

                    //////////////////////

                    // Favorite State

                    binding.favorite.isLiked = starStatus

                    // Add Favorite

                    binding.favorite.setOnLikeListener(object : OnLikeListener {
                        override fun liked(likeButton: LikeButton) {

                            Handler(Looper.getMainLooper()).postDelayed(Runnable {

                                val favoritesRepo = FavoritesRepo(
                                    uid = repoData.id,
                                    userName = userData.login,
                                    repoName = repoData.name,
                                    description = repoData.description,
                                    language = repoData.language,
                                    starCount = repoData.stargazers_count,
                                    createdDate = Tools.convertDate(repoData.created_at)
                                )

                                favoritesViewModel.addFavoriteRepoDB(favoritesRepo)

                            },600)

                        }

                        override fun unLiked(likeButton: LikeButton) {

                            // Delete Favorite

                            favoritesViewModel.deleteFavoriteDB(repoData.id)

                        }
                    })

                    viewModel.loading.postValue(false)

                }

            } else if (userResource?.status == Status.ERROR || repoResource?.status == Status.ERROR) {

                MaterialAlertDialogBuilder(
                    requireContext(), R.style.MaterialAlertDialog_Rounded
                )
                    .setTitle(getString(R.string.error_title))
                    .setMessage(getString(R.string.not_connect))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.ok)) { dialog, which ->

                        viewModel.loading.postValue(false)

                    }
                    .show()

            }

        })

        ////////////////////////////////////////////////////////////////////////////////////////////

        // Back Button

        binding.back.setOnClickListener(View.OnClickListener {

            closeFragment(this)

        })

        // Back Pressed

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    closeFragment(this@DetailsFragment)
                }
            })

        return binding.root
    }


    // Close Fragment

    fun closeFragment(fragment: Fragment) {

        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
            .remove(fragment)
            .commit()

    }

}