package com.example.diplom_app.create_current_thesis

import android.content.Intent
import com.example.diplom_app.api.RetrofitClient
import com.example.diplom_app.model.user.CreateCurrentThesisRequest
import com.example.diplom_app.model.user.CreateCurrentThesisResponse
import com.example.diplom_app.model.user.GetProcessedRequestsListRequest
import com.example.diplom_app.model.user.GetProcessedRequestsListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateCurrentThesisPresenter(private var view: CreateCurrentThesisView?) {

    fun loadApprovedRequests(intent: Intent) {
        //view?.showLoading()

        val studentId = intent.getStringExtra("id") ?: ""
        val request = GetProcessedRequestsListRequest(studentId)
        RetrofitClient.instance.getUserProcessedTheses(request)
            .enqueue(object : Callback<GetProcessedRequestsListResponse> {
                override fun onResponse(
                    call: Call<GetProcessedRequestsListResponse>,
                    response: Response<GetProcessedRequestsListResponse>
                ) {
                    //view?.hideLoading()
                    if (response.isSuccessful && response.body() != null) {
                        // Фильтруем только те заявки, у которых accepted == true.
                        val approved = response.body()!!.requests.filter { it.accepted }
                        view?.showApprovedRequests(approved)
                    } else {
                        view?.showError("Ошибка загрузки заявок")
                    }
                }

                override fun onFailure(call: Call<GetProcessedRequestsListResponse>, t: Throwable) {
                    //view?.hideLoading()
                    view?.showError("Ошибка соединения: ${t.localizedMessage}")
                }
            })
    }

    fun createCurrentThesis(processedRequestId: String) {
        //view?.showLoading()
        val request = CreateCurrentThesisRequest(processedRequestId)
        RetrofitClient.instance.createCurrentThesis(request)
            .enqueue(object : Callback<CreateCurrentThesisResponse> {
                override fun onResponse(
                    call: Call<CreateCurrentThesisResponse>,
                    response: Response<CreateCurrentThesisResponse>
                ) {
                    //view?.hideLoading()
                    if (response.isSuccessful && response.body() != null) {
                        view?.onThesisCreated(response.body()!!.message)
                    } else {
                        view?.showError("Ошибка утверждения ВКР")
                    }
                }

                override fun onFailure(call: Call<CreateCurrentThesisResponse>, t: Throwable) {
                    //view?.hideLoading()
                    view?.showError("Ошибка соединения: ${t.localizedMessage}")
                }
            })
    }

    fun detach() {
        view = null
    }
}