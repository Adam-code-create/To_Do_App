package uz.gita.todolist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.gita.todolist.R
import uz.gita.todolist.data.ContactEntity

class ContactAdapter (private val list : ArrayList<ContactEntity>): RecyclerView.Adapter<ContactAdapter.ContactHolder>() {
   private var itemListener : ((ContactEntity, Int, View)-> Unit)? = null
    inner class ContactHolder (view: View) : RecyclerView.ViewHolder(view){
        private val setting : ImageView = view.findViewById(R.id.contactMore)
        private val contactName :TextView = view.findViewById(R.id.contactName)
        private val phoneNumber :TextView = view.findViewById(R.id.contactPhone)
        init {
            setting.setOnClickListener {
                itemListener?.invoke(list[absoluteAdapterPosition], absoluteAdapterPosition, setting)
            }
        }

        fun bind(){
            val data = list[absoluteAdapterPosition]
            contactName.text = data.name
            phoneNumber.text = data.phone
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder =
        ContactHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false))

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = list.size

    fun setItemListener(f: (ContactEntity, Int, View) -> Unit){
        itemListener = f
    }
}