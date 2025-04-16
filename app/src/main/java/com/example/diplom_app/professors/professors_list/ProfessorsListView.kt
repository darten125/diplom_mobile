package com.example.diplom_app.professors.professors_list

import com.example.diplom_app.model.professors.ProfessorItem

interface ProfessorsListView {
    fun showDepartments(departments: List<String>)
    fun showProfessors(professors: List<ProfessorItem>)
    fun showError(message: String)
    fun showLoading()
    fun hideLoading()
}