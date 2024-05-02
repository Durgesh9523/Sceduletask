package com.example.schedultask


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CandidateAdapter( private val listener: OnItemClickListener,): RecyclerView.Adapter<CandidateAdapter.CandidateHolder>(){
    private var data: List<Candidate> = emptyList()

    fun setData(newData: List<Candidate>) {
        data = newData
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidateHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card, parent, false)
        return CandidateHolder(view)
    }
    override fun onBindViewHolder(holder: CandidateHolder, position: Int) {
        val item =data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class CandidateHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val title:TextView = itemView.findViewById(R.id.title)
        private val label: TextView =itemView.findViewById(R.id.label)
        init {
            itemView.setOnClickListener(this)
        }

        fun bind(item: Candidate) {
            title.text=item.candidateName
            label.text=item.candidateSpecial
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val item = data[position]
                listener.onItemClick(item)
            }
        }
    }
    interface OnItemClickListener {
        fun onItemClick(item: Candidate)
    }
}