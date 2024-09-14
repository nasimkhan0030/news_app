package com.example.thenewsapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.thenewsapp.models.Article
import com.example.thenewsapp.models.NewsResponse
import com.example.thenewsapp.repository.NewsRepository
import com.example.thenewsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response


class NewsViewModel(app:Application,val newsRepository: NewsRepository):AndroidViewModel(app) {
    var headlines:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var headlinesPage = 1
    var headlinesResponse : NewsResponse? = null
    var searchNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse:NewsResponse?=null
    var newSearchQuery:String?=null
    var oldSearchQuery:String?=null

    private fun handleHeadlineResponse(response: Response<NewsResponse>):Resource<NewsResponse> {
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                headlinesPage++
            if (headlinesResponse == null) {
                headlinesResponse = resultResponse
            }
            else {
                val oldArticles = headlinesResponse?.articles
                val newArticles = resultResponse.articles
                oldArticles?.addAll(newArticles)
            }
                return Resource.Success(headlinesResponse?: resultResponse)
            }
        }
        return  Resource.Error(response.message())

    }
private  fun headleSearchNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{
    if (response.isSuccessful){
        response.body()?.let { resultResponse ->
            if (searchNewsResponse == null || newSearchQuery != oldSearchQuery)
            {
                searchNewsPage=1
                oldSearchQuery=newSearchQuery
                searchNewsResponse = resultResponse
            }
            else
            {
                searchNewsPage++
                val oldArticles = searchNewsResponse?.articles
                val newArticles = resultResponse.articles
                oldArticles?.addAll(newArticles)
            }
            return Resource.Success(searchNewsResponse ?: resultResponse)
        }
    }
    return  Resource.Error(response.message())

    }
    fun addToFavourites(article: Article)=viewModelScope.launch{
        newsRepository.upsert(article)
    }
    fun getFavouriteNews()=newsRepository.getFavouriteNews()

    fun deleteArticle(article: Article)=viewModelScope.launch{
        newsRepository.deleteArticle(article)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun internetConnection(context: Context): Boolean{
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
            return getNetworkCapabilities(activeNetwork)?.run {
                when{
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI)->true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)->true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)->true
                    else ->false
                }
            }?:false
        }
    }
}

