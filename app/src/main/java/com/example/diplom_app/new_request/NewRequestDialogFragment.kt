package com.example.diplom_app.new_request

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import com.example.diplom_app.api.RetrofitClient
import com.example.diplom_app.databinding.DialogNewRequestBinding
import com.example.diplom_app.model.new_request.CreateNewPendingRequestRequest
import com.example.diplom_app.model.new_request.CreateNewPendingRequestResponse
import com.example.diplom_app.professors.professors_list.ProfessorsListActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewRequestDialogFragment : DialogFragment() {

    private lateinit var binding: DialogNewRequestBinding

    private var selectedProfessorId: String? = null

    private var studentId: String? = null

    private val professorResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { data ->
                selectedProfessorId = data.getStringExtra("professorId")
                binding.chooseProfessor.setText(data.getStringExtra("professorName"))
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        studentId = arguments?.getString("studentId") ?: ""

        binding = DialogNewRequestBinding.inflate(layoutInflater)

        binding.chooseProfessor.setOnClickListener {
            val intent = Intent(requireContext(), ProfessorsListActivity::class.java)
            professorResultLauncher.launch(intent)
        }

        binding.btnSubmit.setOnClickListener {
            val thesisTitle = binding.thesisTitleEditText.text.toString().trim()
            val thesisDescription = binding.thesisDescriptionEditText.text.toString().trim()

            if (thesisTitle.isEmpty()) {
                binding.thesisTitleEditText.error = "Заполните тему ВКР"
                return@setOnClickListener
            }
            if (thesisDescription.isEmpty()) {
                binding.thesisDescriptionEditText.error = "Заполните описание ВКР"
                return@setOnClickListener
            }
            if (selectedProfessorId.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Выберите преподавателя", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (studentId.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Не найден идентификатор студента", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = CreateNewPendingRequestRequest(
                studentId = studentId!!,
                professorId = selectedProfessorId!!,
                thesisTitle = thesisTitle,
                description = thesisDescription
            )

            RetrofitClient.instance.createPendingRequest(request)
                .enqueue(object : Callback<CreateNewPendingRequestResponse> {
                    override fun onResponse(
                        call: Call<CreateNewPendingRequestResponse>,
                        response: Response<CreateNewPendingRequestResponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(requireContext(), "Запрос успешно создан", Toast.LENGTH_SHORT).show()
                            dismiss()
                        } else {
                            Toast.makeText(requireContext(), "Ошибка создания запроса", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(
                        call: Call<CreateNewPendingRequestResponse>,
                        t: Throwable
                    ) {
                        Toast.makeText(requireContext(), "Ошибка соединения: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
            .setTitle("Новый запрос")
            .setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
            }
        return builder.create()
    }
}