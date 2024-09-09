package com.example.thenewsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.thenewsapp.R
import com.example.thenewsapp.models.Article

class NewsAdapter:RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {
    inner class ArticleViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
        lateinit var articleImage:ImageView
        lateinit var articleSource:TextView
        lateinit var articleTitle:TextView
        lateinit var articleDescriptin:TextView
        lateinit var articleDateTime:TextView

        private val differCallback =object :DiffUtil.ItemCallback<Article>(){
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return  oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return  oldItem == newItem
            }

        }
    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return  ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_news,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return  differ.currentList.size
    }

    private var onItemClickListener: ((Article) -> Unit)?=null

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {

        val  artcle =differ.currentList[position]

        articleImage =holder.itemView.findViewById(R.id.articleImage)
        articleSource =holder.itemView.findViewById(R.id.articleSource)
        articleTitle =holder.itemView.findViewById(R.id.articleTitle)
        articleDescriptin =holder.itemView.findViewById(R.id.articleDescription)
        articleDateTime =holder.itemView.findViewById(R.id.articleDateTime)

        holder.itemView.apply {
            Glide.with(this).load(artcle.urlToImage).into(articleImage)
            articleSource.text=artcle.source?.name
            articleTitle.text=artcle.title
            articleDescriptin.text= artcle.description
            articleDateTime.text=artcle.publishedAt

            setOnClickListener{
                onItemClickListener?.let {
                    it(artcle)
                }
            }
        }

    }

 fun setOnItemClickListener(listener: (Article)->Unit) {
        onItemClickListener = listener
    }
}