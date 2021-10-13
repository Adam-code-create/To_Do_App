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

class BirthdayAdapter (private val list : ArrayList<BirthdayEntity>): RecyclerView.Adapter<BirthdayAdapter.ContactHolder>() {
   private var itemListener : ((BirthdayEntity, Int, View)-> Unit)? = null
    inner class ContactHolder (view: View) : RecyclerView.ViewHolder(view){
        private val setting : ImageView = view.findViewById(R.id.birthdayMore)
        private val name :TextView = view.findViewById(R.id.birthdayName)
        private val date :TextView = view.findViewById(R.id.birthdayDate)
        private val age :TextView = view.findViewById(R.id.birthdayAge)
        init {
            setting.setOnClickListener {
                itemListener?.invoke(list[absoluteAdapterPosition], absoluteAdapterPosition, setting)
            }
        }

        fun bind(){
            val data = list[absoluteAdapterPosition]
            name.text = data.name
            date.text = data.date
            age.text = data.age
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder =
        ContactHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_birthday, parent, false))

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = list.size

    fun setItemListener(f: (BirthdayEntity, Int, View) -> Unit){
        itemListener = f
    }
}