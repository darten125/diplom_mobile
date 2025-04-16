package com.example.diplom_app.professors.professors_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.diplom_app.R

class DepartmentsAdapter(
    private val departments: List<String>,
    private val listener: OnDepartmentClickListener
) : RecyclerView.Adapter<DepartmentsAdapter.DepartmentViewHolder>() {

    interface OnDepartmentClickListener {
        fun onDepartmentClick(department: String)
    }

    inner class DepartmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val departmentTextView: TextView = itemView.findViewById(R.id.professorDepartmentTextView)
        val card: CardView = itemView as CardView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepartmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_department, parent, false)
        return DepartmentViewHolder(view)
    }

    override fun getItemCount(): Int = departments.size

    override fun onBindViewHolder(holder: DepartmentViewHolder, position: Int) {
        val department = departments[position]
        holder.departmentTextView.text = department
        holder.itemView.setOnClickListener { listener.onDepartmentClick(department) }
    }
}