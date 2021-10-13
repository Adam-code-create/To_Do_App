package uz.gita.todolist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.gita.todolist.R
import uz.gita.todolist.data.BirthdayEntity
import uz.gita.todolist.data.ContactEntity
import uz.gita.todolist.data.EmailEntity

class EmailAdapter (private val list : ArrayList<EmailEntity>): RecyclerView.Adapter<EmailAdapter.ContactHolder>() {
   private var itemListener : ((EmailEntity, Int, View)-> Unit)? = null
    inner class ContactHolder (view: View) : RecyclerView.ViewHolder(view){
        private val setting : ImageView = view.findViewById(R.id.emailMore)
        private val name :TextView = view.findViewById(R.id.emailName)
        private val emailId :TextView = view.findViewById(R.id.emailId)
        init {
            setting.setOnClickListener {
                itemListener?.invoke(list[absoluteAdapterPosition], absoluteAdapterPosition, setting)
            }
        }

        fun bind(){
            val data = list[absoluteAdapterPosition]
            name.text = data.name
            emailId.text = data.emailAddress
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder =
        ContactHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_email, parent, false))

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = list.size

    fun setItemListener(f: (EmailEntity, Int, View) -> Unit){
        itemListener = f
    }
}