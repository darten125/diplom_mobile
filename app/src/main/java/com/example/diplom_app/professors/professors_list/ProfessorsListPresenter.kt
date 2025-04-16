package com.example.diplom_app.professors.professors_list

import com.example.diplom_app.api.RetrofitClient
import com.example.diplom_app.model.professors.GetAllProfessorsResponse
import com.example.diplom_app.model.professors.ProfessorItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfessorsListPresenter(private var view: ProfessorsListView?) {

    private var allProfessors: List<ProfessorItem> = listOf()

    fun loadProfessors() {
        view?.showLoading()
        RetrofitClient.instance.getAllProfessors().enqueue(object : Callback<GetAllProfessorsResponse> {
            override fun onResponse(call: Call<GetAllProfessorsResponse>, response: Response<GetAllProfessorsResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    allProfessors = response.body()!!.professors
                    val departments = allProfessors.map { it.department }.distinct().sorted()
                    view?.hideLoading()
                    view?.showDepartments(departments)
                } else {
                    view?.showError("Ошибка получения списка преподавателей")
                }
            }
            override fun onFailure(call: Call<GetAllProfessorsResponse>, t: Throwable) {
                view?.showError("Ошибка соединения: ${t.localizedMessage}")
            }
        })
    }

    fun onDepartmentSelected(department: String) {
        val filtered = allProfessors.filter { it.department == department }
        view?.showProfessors(filtered)
    }

    fun detach() {
        view = null
    }
}