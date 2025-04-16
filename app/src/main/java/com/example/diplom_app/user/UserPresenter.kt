package com.example.diplom_app.user

import android.content.Intent
import com.example.diplom_app.R
import com.example.diplom_app.api.RetrofitClient
import com.example.diplom_app.model.user.GetCurrentThesisRequest
import com.example.diplom_app.model.user.GetCurrentThesisResponse
import com.example.diplom_app.model.user.GetPendingRequestsListRequest
import com.example.diplom_app.model.user.GetPendingRequestsListResponse
import com.example.diplom_app.model.user.GetProcessedRequestsListRequest
import com.example.diplom_app.model.user.GetProcessedRequestsListResponse
import com.example.diplom_app.model.user.PendingRequestItem
import com.example.diplom_app.model.user.ProcessedRequestItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.atomic.AtomicInteger

// Модель для объединённого представления заявки в списке
data class ThesisRequestModel(
    val id: String,
    val professorName: String,
    val professorPosition: String,
    val professorDepartment: String,
    val thesisTitle: String,
    val description: String,
    val statusIconRes: Int
)

class UserPresenter(private val view: UserView) {

    private var pendingList: List<PendingRequestItem> = listOf()
    private var processedList: List<ProcessedRequestItem> = listOf()
    private val completedRequests = AtomicInteger(0) // Счётчик завершённых запросов

    fun loadUserData(intent: Intent) {
        view.showLoading()
        val currentThesisId = intent.getStringExtra("currentThesisId")
        val studentId = intent.getStringExtra("id") ?: ""

        if (currentThesisId != null) {
            getCurrentThesis(studentId)
        } else {
            // Инициируем оба запроса
            loadProcessedRequests(studentId)
            loadPendingRequests(studentId)
        }
    }

    private fun getCurrentThesis(studentId: String) {
        RetrofitClient.instance.getUserCurrentThesis(GetCurrentThesisRequest(studentId))
            .enqueue(object : Callback<GetCurrentThesisResponse> {
                override fun onResponse(
                    call: Call<GetCurrentThesisResponse>,
                    response: Response<GetCurrentThesisResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        view.displayCurrentThesis(response.body()!!)
                    } else {
                        view.showError("Ошибка получения данных ВКР")
                    }
                }

                override fun onFailure(call: Call<GetCurrentThesisResponse>, t: Throwable) {
                    view.showError("Ошибка соединения: ${t.localizedMessage}")
                }
            })
        view.hideLoading()
    }

    private fun loadProcessedRequests(studentId: String) {
        RetrofitClient.instance.getUserProcessedTheses(GetProcessedRequestsListRequest(studentId))
            .enqueue(object : Callback<GetProcessedRequestsListResponse> {
                override fun onResponse(
                    call: Call<GetProcessedRequestsListResponse>,
                    response: Response<GetProcessedRequestsListResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        processedList = response.body()!!.requests ?: emptyList()
                    } else {
                        view.showError("Ошибка получения обработанных заявок")
                    }
                    markRequestComplete()
                }

                override fun onFailure(call: Call<GetProcessedRequestsListResponse>, t: Throwable) {
                    view.showError("Ошибка соединения обработанных заявок: ${t.localizedMessage}")
                    markRequestComplete()
                }
            })
    }

    private fun loadPendingRequests(studentId: String) {
        RetrofitClient.instance.getUserPendingTheses(GetPendingRequestsListRequest(studentId))
            .enqueue(object : Callback<GetPendingRequestsListResponse> {
                override fun onResponse(
                    call: Call<GetPendingRequestsListResponse>,
                    response: Response<GetPendingRequestsListResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        pendingList = response.body()!!.requests ?: emptyList()
                    } else {
                        view.showError("Ошибка получения ожидающих заявок")
                    }
                    markRequestComplete()
                }

                override fun onFailure(call: Call<GetPendingRequestsListResponse>, t: Throwable) {
                    view.showError("Ошибка соединения ожидающих заявок: ${t.localizedMessage}")
                    markRequestComplete()
                }
            })
    }

    // Вызывается после завершения каждого запроса
    private fun markRequestComplete() {
        if (completedRequests.incrementAndGet() < 2) return
        combineRequests()
        completedRequests.set(0)
    }

    private fun combineRequests() {
        val combinedRequests = mutableListOf<ThesisRequestModel>()

        // Обрабатываем ожидающие заявки
        pendingList.forEach { item ->
            combinedRequests.add(
                ThesisRequestModel(
                    id = item.id,
                    professorName = item.professorName,
                    professorPosition = item.professorPosition,
                    professorDepartment = item.professorDepartment,
                    thesisTitle = item.thesisTitle,
                    description = item.description,
                    statusIconRes = R.drawable.waiting_request_icon
                )
            )
        }

        // Обрабатываем обработанные заявки
        processedList.forEach { item ->
            val icon = if (item.accepted)
                R.drawable.accepted_request_icon
            else
                R.drawable.rejected_request_icon

            combinedRequests.add(
                ThesisRequestModel(
                    id = item.id,
                    professorName = item.professorName,
                    professorPosition = item.professorPosition,
                    professorDepartment = item.professorDepartment,
                    thesisTitle = item.thesisTitle,
                    description = item.description,
                    statusIconRes = icon
                )
            )
        }
        view.hideLoading()
        if (combinedRequests.isNotEmpty() &&
            processedList.isNotEmpty() &&
            processedList.any { it.accepted }
            ) {
            view.displayRequestsAndFinalThesisButton(combinedRequests)
        } else if (combinedRequests.isNotEmpty()){
            view.displayRequests(combinedRequests)
        } else {
            view.displayEmptyRequests()
        }
    }
}