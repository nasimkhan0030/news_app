package com.example.thenewsapp.repository

import Article
import com.example.thenewsapp.api.RetrofitInstance
import com.example.thenewsapp.db.ArticleDatabase
import retrofit2.http.Query

class NewsRepository(val db: ArticleDatabase){

    suspend fun getHeadlines(countryCode:String,pageNumber:Int)=
        RetrofitInstance.api.getHeadlines(countryCode,pageNumber)

    suspend fun searchNews(searchQuery: String,pageNumber: Int)=
        RetrofitInstance.api.searchForNews(searchQuery,pageNumber)

    suspend fun upsert(article: Article)=db.getArticleDao().upsert(article)

    fun getFavouriteNews()=db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article)=db.getArticleDao().deleteArticles(article)

}