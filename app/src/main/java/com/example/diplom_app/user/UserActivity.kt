package com.example.diplom_app.user

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diplom_app.databinding.ActivityUserBinding
import com.example.diplom_app.model.user.GetCurrentThesisResponse
import android.view.View
import com.example.diplom_app.create_current_thesis.CreateCurrentThesisActivity
import com.example.diplom_app.new_request.NewRequestDialogFragment


class UserActivity : AppCompatActivity(), UserView {

    companion object {
        private const val RC_CREATE_THESIS = 2001
    }

    private lateinit var binding: ActivityUserBinding
    private lateinit var presenter: UserPresenter
    private lateinit var requestsAdapter: RequestsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.userNameTextView.text = intent.getStringExtra("name")
        if (intent.getStringExtra("role") == "bac")
            binding.userRoleTextView.text = "Бакалавриат"
        else
            binding.userRoleTextView.text = "Магистратура"
        binding.userGroupTextView.text = intent.getStringExtra("userGroup")

        presenter = UserPresenter(this)

        binding.addButton.setOnClickListener {
            val dialog = NewRequestDialogFragment().apply {
                arguments = Bundle().apply { putString("studentId", intent.getStringExtra("id")) }
            }
            dialog.show(supportFragmentManager, "NewRequestDialogFragment")
        }

        binding.createFinalThesisButton.setOnClickListener {
            val intent = Intent(this, CreateCurrentThesisActivity::class.java).apply {
                putExtra("id", intent.getStringExtra("id"))
            }
            startActivityForResult(intent, RC_CREATE_THESIS)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        requestsAdapter = RequestsAdapter(emptyList())
        binding.recyclerView.adapter = requestsAdapter

        binding.swipeRefresher.setOnRefreshListener {
            presenter.loadUserData(intent)
        }

        presenter.loadUserData(intent)
    }

    // 1) Отображение текущей ВКР
    override fun displayCurrentThesis(request: GetCurrentThesisResponse) {
        runOnUiThread {
            // Показываем контейнер с текущей ВКР
            binding.currentThesisContainer.visibility = View.VISIBLE
            // Скрываем контейнер с RecyclerView
            binding.unfinalRequestsContainer.visibility = View.GONE
            // Скрываем контейнер с пустыми запросами
            binding.emptyRequestsContainer.visibility = View.GONE
            // Прячем кнопку добавления
            binding.addButton.visibility = View.GONE
            binding.createFinalThesisButton.visibility = View.GONE

            // Заполняем поля
            binding.thesisTopicTextView.text = request.thesis.title
            binding.thesisDescriptionTextView.text = request.thesis.description
            binding.thesisTeacherTextView.text = request.thesis.professorName
        }
    }

    // 2) Отображение списка заявок через RecyclerView
    override fun displayRequests(requests: List<ThesisRequestModel>) {
        runOnUiThread {
            binding.currentThesisContainer.visibility = View.GONE
            binding.unfinalRequestsContainer.visibility = View.VISIBLE
            binding.emptyRequestsContainer.visibility = View.GONE
            binding.addButton.visibility = View.VISIBLE
            binding.createFinalThesisButton.visibility = View.GONE

            requestsAdapter.updateData(requests)
        }
    }

    override fun displayRequestsAndFinalThesisButton(requests: List<ThesisRequestModel>) {
        runOnUiThread {
            binding.currentThesisContainer.visibility = View.GONE
            binding.unfinalRequestsContainer.visibility = View.VISIBLE
            binding.emptyRequestsContainer.visibility = View.GONE
            binding.addButton.visibility = View.VISIBLE
            binding.createFinalThesisButton.visibility = View.VISIBLE

            requestsAdapter.updateData(requests)
        }
    }

    // 3) Отображение контейнера, если запросов нет вообще
    override fun displayEmptyRequests() {
        runOnUiThread {
            binding.currentThesisContainer.visibility = View.GONE
            binding.unfinalRequestsContainer.visibility = View.GONE
            binding.emptyRequestsContainer.visibility = View.VISIBLE
            binding.addButton.visibility = View.VISIBLE
            binding.createFinalThesisButton.visibility = View.GONE
        }
    }

    // Вывод сообщения об ошибке
    override fun showError(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }

    override fun showLoading() {
        binding.swipeRefresher.isRefreshing = true
    }

    override fun hideLoading() {
        binding.swipeRefresher.isRefreshing = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_CREATE_THESIS && resultCode == Activity.RESULT_OK) {
            val newCurrentId = data?.getStringExtra("currentThesisId")
            if (!newCurrentId.isNullOrEmpty()) {
                intent.putExtra("currentThesisId", newCurrentId)
                presenter.loadUserData(intent)
            }
        }
    }

}
