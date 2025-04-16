package com.example.diplom_app.professors.professor

import com.example.diplom_app.api.RetrofitClient
import com.example.diplom_app.model.professors.articles.GetAllArticlesRequest
import com.example.diplom_app.model.professors.articles.GetAllArticlesResponse
import com.example.diplom_app.model.professors.previous_theses.GetPreviousThesesRequest
import com.example.diplom_app.model.professors.previous_theses.GetPreviousThesesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfessorPresenter(private var view: ProfessorView?) {

    fun loadPreviousTheses(name: String, position: String, department: String) {
        view?.showLoading()
        val request = GetPreviousThesesRequest(name, position, department)
        RetrofitClient.instance.getPreviousTheses(request)
            .enqueue(object : Callback<GetPreviousThesesResponse> {
                override fun onResponse(
                    call: Call<GetPreviousThesesResponse>,
                    response: Response<GetPreviousThesesResponse>
                ) {
                    view?.hideLoading()
                    if (response.isSuccessful && response.body() != null) {
                        view?.showPreviousTheses(response.body()!!.theses)
                    } else {
                        view?.showError("Ошибка загрузки прошлых тем")
                    }
                }
                override fun onFailure(call: Call<GetPreviousThesesResponse>, t: Throwable) {
                    view?.hideLoading()
                    view?.showError("Ошибка соединения: ${t.localizedMessage}")
                }
            })
    }

    fun loadArticles(name: String, position: String, department: String) {
        view?.showLoading()
        val request = GetAllArticlesRequest(name, position, department)
        RetrofitClient.instance.getAllArticles(request)
            .enqueue(object : Callback<GetAllArticlesResponse> {
                override fun onResponse(
                    call: Call<GetAllArticlesResponse>,
                    response: Response<GetAllArticlesResponse>
                ) {
                    view?.hideLoading()
                    if (response.isSuccessful && response.body() != null) {
                        view?.showArticles(response.body()!!.articles)
                    } else {
                        view?.showError("Ошибка загрузки статей")
                    }
                }
                override fun onFailure(call: Call<GetAllArticlesResponse>, t: Throwable) {
                    view?.hideLoading()
                    view?.showError("Ошибка соединения: ${t.localizedMessage}")
                }
            })
    }

    fun detach() {
        view = null
    }
}