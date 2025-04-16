package com.example.diplom_app.professors.professors_list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diplom_app.databinding.ActivityProfessorsListBinding
import com.example.diplom_app.model.professors.ProfessorItem
import com.example.diplom_app.professors.professor.ProfessorActivity

class ProfessorsListActivity : AppCompatActivity(), ProfessorsListView,
    DepartmentsAdapter.OnDepartmentClickListener, ProfessorsAdapter.OnProfessorClickListener {

    private lateinit var binding: ActivityProfessorsListBinding
    private lateinit var presenter: ProfessorsListPresenter
    private lateinit var departmentsAdapter: DepartmentsAdapter
    private lateinit var professorsAdapter: ProfessorsAdapter
    private var departments: List<String> = listOf()

    companion object {
        const val REQUEST_PROFESSOR = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfessorsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = ProfessorsListPresenter(this)

        binding.recyclerDepartments.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        binding.recyclerProfessors.layoutManager = LinearLayoutManager(this)
        binding.recyclerProfessors.visibility = View.GONE

        binding.backButton.setOnClickListener { finish() }

        presenter.loadProfessors()
    }

    override fun showDepartments(departments: List<String>) {
        this.departments = departments
        departmentsAdapter = DepartmentsAdapter(departments, this)
        binding.recyclerDepartments.adapter = departmentsAdapter
    }

    override fun showProfessors(professors: List<ProfessorItem>) {
        professorsAdapter = ProfessorsAdapter(professors, this)
        binding.recyclerProfessors.adapter = professorsAdapter
        binding.recyclerProfessors.visibility = View.VISIBLE
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onDepartmentClick(department: String) {
        presenter.onDepartmentSelected(department)
    }

    override fun onProfessorClick(professor: ProfessorItem) {
        val intent = Intent(this, ProfessorActivity::class.java).apply {
            putExtra("professorId", professor.id)
            putExtra("professorName", professor.name)
            putExtra("professorPosition", professor.position)
            putExtra("professorDepartment", professor.department)
        }
        startActivityForResult(intent, REQUEST_PROFESSOR)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_PROFESSOR && resultCode == Activity.RESULT_OK && data != null) {
            setResult(Activity.RESULT_OK, data)
            finish()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroy() {
        presenter.detach()
        super.onDestroy()
    }
}