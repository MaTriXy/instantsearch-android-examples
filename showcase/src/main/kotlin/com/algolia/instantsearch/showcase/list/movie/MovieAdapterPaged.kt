package com.algolia.instantsearch.showcase.list.movie

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.algolia.instantsearch.showcase.R
import com.algolia.instantsearch.helper.android.inflate


class SearchArticlePaged: PagedListAdapter<SearchArticle, SearchArticleViewHolder>(SearchArticleDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchArticleViewHolder {
        return SearchArticleViewHolder(parent.inflate(R.layout.list_item_large))
    }

    override fun onBindViewHolder(holder: SearchArticleViewHolder, position: Int) {
        val item = getItem(position)

        if (item != null) holder.bind(item)
    }
}

class MovieAdapterPaged : PagedListAdapter<Movie, MovieViewHolder>(MovieDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(parent.inflate(R.layout.list_item_large))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = getItem(position)

        if (item != null) holder.bind(item)
    }
}