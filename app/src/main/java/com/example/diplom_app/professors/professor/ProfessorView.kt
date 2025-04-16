package com.example.diplom_app.professors.professor

import com.example.diplom_app.model.professors.articles.ArticleItem
import com.example.diplom_app.model.professors.previous_theses.PreviousThesisItem

interface ProfessorView {
    fun showPreviousTheses(theses: List<PreviousThesisItem>)
    fun showArticles(articles: List<ArticleItem>)
    fun showError(message: String)
    fun showLoading()
    fun hideLoading()
}