package com.example.diplom_app.professors.professors_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.diplom_app.R
import com.example.diplom_app.model.professors.ProfessorItem

class ProfessorsAdapter(
    private val professors: List<ProfessorItem>,
    private val listener: OnProfessorClickListener
) : RecyclerView.Adapter<ProfessorsAdapter.ProfessorViewHolder>() {

    interface OnProfessorClickListener {
        fun onProfessorClick(professor: ProfessorItem)
    }

    inner class ProfessorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.professorNameTextView)
        val positionTextView: TextView = itemView.findViewById(R.id.professorPositionTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfessorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_professor, parent, false)
        return ProfessorViewHolder(view)
    }

    override fun getItemCount(): Int = professors.size

    override fun onBindViewHolder(holder: ProfessorViewHolder, position: Int) {
        val professor = professors[position]
        holder.nameTextView.text = professor.name
        holder.positionTextView.text = professor.position
        holder.itemView.setOnClickListener { listener.onProfessorClick(professor) }
    }
}