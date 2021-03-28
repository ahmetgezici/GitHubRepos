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
import com.ahmetgezici.githubrepos.model.Repo
import com.ahmetgezici.githubrepos.utils.Tools
import com.ahmetgezici.githubrepos.view.DetailsFragment.DetailsFragment
import com.ahmetgezici.githubrepos.viewmodel.FavoritesViewModel
import com.like.LikeButton
import com.like.OnLikeListener

class SearchAdapter(

    private val repoList: List<Repo>?,
    private val favoriteUIDList: ArrayList<Int>,
    private val languageColor: Map<String, String>,
    private val manager: FragmentManager,
    private val favoritesViewModel: FavoritesViewModel

) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

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

        val repo = repoList?.get(position)

        if (repo != null) {

            holder.binding.repoName.text = repo.name
            holder.binding.description.text = repo.description
            holder.binding.language.text = repo.language
            holder.binding.starCount.text = repo.stargazers_count.toString()

            // For GitHub language color

            val colorCode = languageColor[repo.language]

            if (colorCode != null) {
                holder.binding.languageShape.setCardBackgroundColor(Color.parseColor(colorCode))
            }else{
                holder.binding.languageShape.visibility = View.INVISIBLE
            }

            // Convert Date Format

            val createdDate = Tools.convertDate(repo.created_at)
            holder.binding.createdDate.text = createdDate

            // Favorite Status

            val starStatus = favoriteUIDList.contains(repo.id)
            holder.binding.favorite.isLiked = starStatus

            holder.binding.favorite.setOnLikeListener(object : OnLikeListener {
                override fun liked(likeButton: LikeButton) {

                    val favoritesRepo = FavoritesRepo(
                        uid = repo.id,
                        userName = repo.owner.login,
                        repoName = repo.name,
                        description = repo.description,
                        language = repo.language,
                        starCount = repo.stargazers_count,
                        createdDate = createdDate
                    )

                    favoritesViewModel.addFavoriteRepoDB(favoritesRepo)

                }

                override fun unLiked(likeButton: LikeButton) {

                    favoritesViewModel.deleteFavoriteDB(repo.id)

                }
            })

            // Go DetailsFragment

            holder.binding.repo.setOnClickListener(View.OnClickListener {

                val detailsFragment = DetailsFragment(repo.owner.login, repo.name, starStatus)

                manager.beginTransaction()
                    .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                    .add(R.id.root, detailsFragment, detailsFragment.tag)
                    .commit()

            })

        }

    }

    override fun getItemCount(): Int {
        return repoList!!.size
    }


    class ViewHolder(val binding: ItemRepoBinding) : RecyclerView.ViewHolder(binding.root)

}