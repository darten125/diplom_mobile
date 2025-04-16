package com.example.diplom_app.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.diplom_app.R

class RequestsAdapter(private var requests: List<ThesisRequestModel>) :
    RecyclerView.Adapter<RequestsAdapter.RequestViewHolder>() {

    fun updateData(newData: List<ThesisRequestModel>) {
        requests = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_request, parent, false)
        return RequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        holder.bind(requests[position])
    }

    override fun getItemCount(): Int = requests.size

    class RequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val statusImageView: ImageView = itemView.findViewById(R.id.requestStatusImageView)
        private val titleTextView: TextView = itemView.findViewById(R.id.requestTitleTextView)
        private val teacherTextView: TextView = itemView.findViewById(R.id.requestTeacherTextView)

        fun bind(request: ThesisRequestModel) {
            titleTextView.text = request.thesisTitle
            teacherTextView.text = request.professorName
            statusImageView.setImageResource(request.statusIconRes)
        }
    }
}