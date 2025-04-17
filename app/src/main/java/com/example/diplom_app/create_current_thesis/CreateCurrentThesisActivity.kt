package com.example.diplom_app.create_current_thesis

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diplom_app.R
import com.example.diplom_app.databinding.ActivityCreateCurrentThesisBinding
import com.example.diplom_app.model.user.ProcessedRequestItem

class CreateCurrentThesisActivity : AppCompatActivity(), CreateCurrentThesisView,
    ApprovedRequestAdapter.OnItemClickListener {

    private lateinit var binding: ActivityCreateCurrentThesisBinding
    private lateinit var presenter: CreateCurrentThesisPresenter
    private lateinit var adapter: ApprovedRequestAdapter
    private var selectedRequest: ProcessedRequestItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCurrentThesisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = CreateCurrentThesisPresenter(this)
        adapter = ApprovedRequestAdapter(emptyList(), this)
        binding.approvedRequestsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.approvedRequestsRecyclerView.adapter = adapter

        // Загружаем одобренные заявки
        presenter.loadApprovedRequests(intent)

        binding.approveThesisButton.setOnClickListener {
            selectedRequest?.let {
                presenter.createCurrentThesis(it.id)
            } ?: run {
                Toast.makeText(this, "Выберите заявку", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Реализация методов CreateCurrentThesisView:
    override fun showApprovedRequests(requests: List<ProcessedRequestItem>) {
        adapter.updateRequests(requests)
    }

    /*override fun showLoading() {
        // Если у вас есть прогрессбар в layout – используйте его, иначе можно оставить пустым
    }*/

    /*override fun hideLoading() {
        // Скрыть индикатор загрузки
    }*/

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onThesisCreated(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        // Отправляем назад в UserActivity id только что утверждённой заявки
        val resultIntent = Intent().apply {
            putExtra("currentThesisId", selectedRequest?.id)
        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    override fun onRequestSelected(request: ProcessedRequestItem) {
        selectedRequest = request
    }

    override fun onDestroy() {
        presenter.detach()
        super.onDestroy()
    }
}