package com.example.diplom_app.professors.professor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.diplom_app.R
import com.example.diplom_app.model.professors.articles.ArticleItem
import com.example.diplom_app.model.professors.previous_theses.PreviousThesisItem

class ProfessorInfoAdapter(
    private var items: List<Any>,
    private val listener: OnItemClickListener?
) : RecyclerView.Adapter<ProfessorInfoAdapter.InfoViewHolder>() {

    interface OnItemClickListener {
        fun onArticleClicked(link: String)
    }

    inner class InfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val infoTextView: TextView = itemView.findViewById(R.id.InfoTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_professor_info, parent, false)
        return InfoViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        val item = items[position]
        when (item) {
            is ArticleItem -> {
                holder.infoTextView.text = item.title
                holder.itemView.isClickable = true
                holder.itemView.setOnClickListener {
                    listener?.onArticleClicked(item.link)
                }
            }
            is PreviousThesisItem -> {
                holder.infoTextView.text = item.title
                holder.itemView.isClickable = false
            }
            else -> {
                holder.infoTextView.text = item.toString()
                holder.itemView.isClickable = false
            }
        }
    }

    fun updateItems(newItems: List<Any>) {
        items = newItems
        notifyDataSetChanged()
    }
}