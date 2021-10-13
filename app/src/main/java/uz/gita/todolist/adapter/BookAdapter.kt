package uz.gita.todolist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.gita.todolist.R
import uz.gita.todolist.data.BookEntity
import uz.gita.todolist.data.BankAccountEntity

class BookAdapter (private val list : ArrayList<BookEntity>): RecyclerView.Adapter<BookAdapter.ContactHolder>() {
   private var itemListener : ((BookEntity, Int, View)-> Unit)? = null
    inner class ContactHolder (view: View) : RecyclerView.ViewHolder(view){
        private val setting : ImageView = view.findViewById(R.id.bookMore)
        private val name :TextView = view.findViewById(R.id.bookName)
        private val startDate :TextView = view.findViewById(R.id.startDate)
        private val finishDate :TextView = view.findViewById(R.id.finishDate)
        init {
            setting.setOnClickListener {
                itemListener?.invoke(list[absoluteAdapterPosition], absoluteAdapterPosition, setting)
            }
        }

        fun bind(){
            val data = list[absoluteAdapterPosition]
            name.text = data.name
            startDate.text = data.startDate
            finishDate.text = data.endDate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder =
        ContactHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_books, parent, false))

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = list.size

    fun setItemListener(f: (BookEntity, Int, View) -> Unit){
        itemListener = f
    }
}