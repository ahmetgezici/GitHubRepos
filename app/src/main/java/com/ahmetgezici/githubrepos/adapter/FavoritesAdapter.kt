package com.ahmetgezici.githubrepos.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.ahmetgezici.githubrepos.R
import com.ahmetgezici.githubrepos.databinding.ItemRepoBinding
import com.ahmetgezici.githubrepos.model.FavoritesRepo
import com.ahmetgezici.githubrepos.view.DetailsFragment.DetailsFragment
import com.ahmetgezici.githubrepos.viewmodel.FavoritesViewModel
import com.like.LikeButton
import com.like.OnLikeListener


class FavoritesAdapter(

    private val favoritesList: List<FavoritesRepo>?,
    private val languageColor: Map<String, String>,
    private val manager: FragmentManager,
    private val favoritesViewModel: FavoritesViewModel

) : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRepoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val favoritesRepo = favoritesList?.get(holder.adapterPosition)

        if (favoritesRepo != null) {

            holder.binding.repoName.text = favoritesRepo.repoName
            holder.binding.description.text = favoritesRepo.description
            holder.binding.language.text = favoritesRepo.language
            holder.binding.starCount.text = favoritesRepo.starCount.toString()

            // For GitHub language color

            val colorCode = languageColor[favoritesRepo.language]

            if (colorCode != null) {
                holder.binding.languageShape.setCardBackgroundColor(Color.parseColor(colorCode))
            }else{
                holder.binding.languageShape.visibility = View.INVISIBLE
            }

            // Go DetailsFragment

            holder.binding.repo.setOnClickListener(View.OnClickListener {

                val detailsFragment =
                    DetailsFragment(favoritesRepo.userName, favoritesRepo.repoName, true)

                manager.beginTransaction()
                    .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                    .add(R.id.root, detailsFragment, detailsFragment.tag)
                    .commit()

            })

        }

        // Favorite Status

        holder.binding.favorite.isLiked = true

        holder.binding.favorite.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton) {
            }

            override fun unLiked(likeButton: LikeButton) {
                favoritesViewModel.deleteFavoriteDB(favoritesRepo?.uid)
            }
        })

    }

    override fun getItemCount(): Int {
        return favoritesList!!.size
    }


    class ViewHolder(val binding: ItemRepoBinding) : RecyclerView.ViewHolder(binding.root)

}