package com.ahmetgezici.githubrepos.view

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmetgezici.githubrepos.R
import com.ahmetgezici.githubrepos.adapter.SearchAdapter
import com.ahmetgezici.githubrepos.databinding.FragmentSearchBinding
import com.ahmetgezici.githubrepos.model.FavoritesRepo
import com.ahmetgezici.githubrepos.utils.Tools
import com.ahmetgezici.githubrepos.utils.datautil.Status
import com.ahmetgezici.githubrepos.viewmodel.FavoritesViewModel
import com.ahmetgezici.githubrepos.viewmodel.SearchViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding

    lateinit var viewModel: SearchViewModel
    lateinit var favoritesViewModel: FavoritesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSearchBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        favoritesViewModel = MainActivity.favoritesViewModel

        ////////////////////////////////////////////////////////////////////////////////////////////

        setupUI(binding.root)

        // Loading State

        viewModel.loading.observe(viewLifecycleOwner, Observer { loading ->

            if (loading) {
                binding.reposRecycler.visibility = View.INVISIBLE
                binding.loadingProgress.visibility = View.VISIBLE
                binding.search.progress = 1
            } else {
                binding.reposRecycler.visibility = View.VISIBLE
                binding.loadingProgress.visibility = View.GONE
                binding.search.progress = 0
            }

        })

        // Search Repo

        binding.search.setOnClickListener(View.OnClickListener {

            if (binding.userName.text?.isNotEmpty() == true) {

                // Get Repos

                viewModel.getRepos(binding.userName.text.toString()).observe(viewLifecycleOwner,
                    Observer { repoListRes ->

                        if (repoListRes.status == Status.LOADING) {

                            viewModel.loading.postValue(true)

                        } else if (repoListRes.status == Status.SUCCESS) {

                            viewModel.repoData.postValue(repoListRes.data)

                        } else if (repoListRes.status == Status.ERROR) {

                            if (repoListRes.message?.contains("HTTP 404")!!) {

                                MaterialAlertDialogBuilder(
                                    requireContext(), R.style.MaterialAlertDialog_Rounded
                                )
                                    .setMessage(getString(R.string.no_user_body))
                                    .setCancelable(false)
                                    .setPositiveButton(getString(R.string.ok)) { dialog, which ->

                                        viewModel.loading.postValue(false)

                                    }
                                    .show()

                            } else {

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

                        }

                    })

            } else {

                MaterialAlertDialogBuilder(
                    requireContext(), R.style.MaterialAlertDialog_Rounded
                )
                    .setMessage(getString(R.string.empty_edittext))
                    .setPositiveButton(getString(R.string.ok),null)
                    .show()

            }

        })

        ////////////////////////////////////////////////////////////////////////////////////////////

        var observer: Observer<List<FavoritesRepo>>? = null


        viewModel.repoData.observe(viewLifecycleOwner, Observer {

            if (observer == null) {

                observer = Observer<List<FavoritesRepo>> { favoritesList ->

                    val favoriteUIDList = ArrayList<Int>()

                    for (favorite in favoritesList!!) {

                        favoriteUIDList.add(favorite.uid)

                    }

                    // Adapter Send Data

                    val adapter = SearchAdapter(
                        viewModel.repoData.value,
                        favoriteUIDList,
                        Tools.getLanguageColor(requireActivity()),
                        requireActivity().supportFragmentManager,
                        favoritesViewModel
                    )
                    binding.reposRecycler.layoutManager =
                        LinearLayoutManager(requireContext())
                    binding.reposRecycler.adapter = adapter

                    viewModel.loading.postValue(false)

                }

            }

            // Get DB Favorite Repos

            favoritesViewModel.getFavoritesDB()?.observe(viewLifecycleOwner, observer!!)

        })

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


    ////////////////////////////////////////////////////////////////////////////////////////////////

    // Close keyboard if clicked outside of edittext

    @SuppressLint("ClickableViewAccessibility")
    fun setupUI(view: View) {
        if (view !is EditText) {

            view.setOnTouchListener(View.OnTouchListener { v, event ->
                hideSoftKeyboard(requireActivity())
                false
            })

        }

        if (view is ViewGroup) {

            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupUI(innerView)
            }

        }
    }

    private fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(
            Activity.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        val focusedView = activity.currentFocus
        if (focusedView != null) {
            inputMethodManager.hideSoftInputFromWindow(
                activity.currentFocus!!.windowToken, 0
            )
        }
    }

}