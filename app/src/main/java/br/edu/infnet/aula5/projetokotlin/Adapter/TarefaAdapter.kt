package br.edu.infnet.aula5.projetokotlin.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import br.edu.infnet.aula5.projetokotlin.Domain.Tarefa
import br.edu.infnet.aula5.projetokotlin.R

class TarefaAdapter(private val context: Context, val listener: TodoClickListener?) : RecyclerView.Adapter<TarefaAdapter.TarefaViewHolder>(){

    private val todoList = ArrayList<Tarefa>()

    class TarefaViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val todo_layout = itemView.findViewById<CardView>(R.id.card_layout)
        val title = itemView.findViewById<TextView>(R.id.tv_title)
        val description = itemView.findViewById<TextView>(R.id.tv_note)
        val switchStatusText = itemView.findViewById<Switch>(R.id.status)

    }
    interface TodoClickListener {
        fun onItemClicked(todo: Tarefa)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarefaViewHolder {
        return TarefaViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    override fun onBindViewHolder(holder: TarefaViewHolder, position: Int) {
        val item = todoList[position]
        holder.title.text = item.title
        holder.title.isSelected = true
        holder.description.text = item.note
        holder.switchStatusText.text = item.status

        holder.todo_layout.setOnClickListener{
            listener?.onItemClicked(todoList[holder.adapterPosition])
        }
    }

    fun updateList(newList: List<Tarefa>){
        todoList.clear()
        todoList.addAll(newList)
        notifyDataSetChanged()
    }
}