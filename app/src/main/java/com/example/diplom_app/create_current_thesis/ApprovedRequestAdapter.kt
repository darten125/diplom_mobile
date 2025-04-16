package com.example.diplom_app.create_current_thesis

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.diplom_app.R
import com.example.diplom_app.model.user.ProcessedRequestItem

class ApprovedRequestAdapter(
    private var requests: List<ProcessedRequestItem>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ApprovedRequestAdapter.RequestViewHolder>() {

    // Храним выбранную позицию; если ни одна не выбрана – -1.
    private var selectedPosition: Int = -1

    interface OnItemClickListener {
        fun onRequestSelected(request: ProcessedRequestItem)
    }

    inner class RequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val thesisTitleTextView: TextView = itemView.findViewById(R.id.thesisTitleTextView)
        val thesisDescriptionTextView: TextView = itemView.findViewById(R.id.thesisDescriptionTextView)
        val professorNameTextView: TextView = itemView.findViewById(R.id.professorNameTextView)
        val checkMark: ImageView = itemView.findViewById(R.id.checkMarkImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_approved_request, parent, false)
        return RequestViewHolder(view)
    }

    override fun getItemCount(): Int = requests.size

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val request = requests[position]
        holder.thesisTitleTextView.text = request.thesisTitle
        holder.thesisDescriptionTextView.text = request.description
        holder.professorNameTextView.text = request.professorName

        // Если позиция совпадает с выбранной – показываем галочку.
        holder.checkMark.visibility = if (position == selectedPosition) View.VISIBLE else View.GONE

        // Используем holder.adapterPosition для динамичного получения актуальной позиции
        holder.itemView.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                selectedPosition = pos
                notifyDataSetChanged()  // уведомляем для обновления галочек
                listener.onRequestSelected(requests[pos])
            }
        }
    }

    fun updateRequests(newRequests: List<ProcessedRequestItem>) {
        requests = newRequests
        selectedPosition = -1
        notifyDataSetChanged()
    }

    fun getSelectedRequest(): ProcessedRequestItem? {
        return if (selectedPosition in requests.indices) requests[selectedPosition] else null
    }
}