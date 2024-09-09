package com.example.thenewsapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.thenewsapp.models.NewsResponse
import com.example.thenewsapp.repository.NewsRepository
import com.example.thenewsapp.util.Resource
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
            response.body().let { resultResponse ->
                headlinesPage++
            if (headlinesResponse == null)
            {
                headlinesResponse = resultResponse
            }
            else
            {
                val oldArticles = headlinesResponse?.articles
                val newArticles = resultResponse.articles
                oldArticles?.addAll(newArticles)
            }
                return Resource.Success(headlinesResponse ?: resultResponse)
            }
        }
        return  Resource.Error(response.message())

    }

}