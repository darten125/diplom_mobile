package com.example.diplom_app.professors.professor

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diplom_app.databinding.ActivityProfessorBinding
import com.example.diplom_app.model.professors.articles.ArticleItem
import com.example.diplom_app.model.professors.previous_theses.PreviousThesisItem

class ProfessorActivity : AppCompatActivity(), ProfessorView,
    ProfessorInfoAdapter.OnItemClickListener {

    private lateinit var binding: ActivityProfessorBinding
    private lateinit var presenter: ProfessorPresenter
    private lateinit var adapter: ProfessorInfoAdapter

    private var professorId: String? = null
    private var professorName: String = ""
    private var professorPosition: String = ""
    private var professorDepartment: String = ""

    private var currentMode: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfessorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = ProfessorPresenter(this)

        professorId = intent.getStringExtra("professorId")
        professorName = intent.getStringExtra("professorName") ?: ""
        professorPosition = intent.getStringExtra("professorPosition") ?: ""
        professorDepartment = intent.getStringExtra("professorDepartment") ?: ""

        binding.professorNameTextView.text = professorName
        binding.departmentTextView.text = professorDepartment
        binding.positionTextView.text = professorPosition

        adapter = ProfessorInfoAdapter(emptyList(), this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.visibility = View.GONE

        binding.themesButton.setOnClickListener {
            currentMode = "themes"
            binding.recyclerView.visibility = View.GONE
            presenter.loadPreviousTheses(professorName, professorPosition, professorDepartment)
        }

        binding.articlesButton.setOnClickListener {
            currentMode = "articles"
            binding.recyclerView.visibility = View.GONE
            presenter.loadArticles(professorName, professorPosition, professorDepartment)
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.registerButton.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra("professorId", professorId)
                putExtra("professorName", professorName)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    override fun showPreviousTheses(theses: List<PreviousThesisItem>) {
        adapter.updateItems(theses)
        binding.recyclerView.visibility = View.VISIBLE
    }

    override fun showArticles(articles: List<ArticleItem>) {
        adapter.updateItems(articles)
        binding.recyclerView.visibility = View.VISIBLE
    }

    override fun showError(message: String) {
        binding.progressBar.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onArticleClicked(link: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(intent)
    }

    override fun onDestroy() {
        presenter.detach()
        super.onDestroy()
    }
}