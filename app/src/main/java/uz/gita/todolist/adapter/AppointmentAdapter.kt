package uz.gita.todolist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.gita.todolist.R
import uz.gita.todolist.data.AppointmentEntity
import uz.gita.todolist.data.BankAccountEntity

class AppointmentAdapter (private val list : ArrayList<AppointmentEntity>): RecyclerView.Adapter<AppointmentAdapter.ContactHolder>() {
   private var itemListener : ((AppointmentEntity, Int, View)-> Unit)? = null
    inner class ContactHolder (view: View) : RecyclerView.ViewHolder(view){
        private val setting : ImageView = view.findViewById(R.id.appointmentMore)
        private val name :TextView = view.findViewById(R.id.appointmentPerson)
        private val place :TextView = view.findViewById(R.id.appointmentPlace)
        private val date :TextView = view.findViewById(R.id.appointmentdate)
        init {
            setting.setOnClickListener {
                itemListener?.invoke(list[absoluteAdapterPosition], absoluteAdapterPosition, setting)
            }
        }

        fun bind(){
            val data = list[absoluteAdapterPosition]
            name.text = data.name
            place.text = data.place
            date.text = data.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder =
        ContactHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_appointments, parent, false))

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = list.size

    fun setItemListener(f: (AppointmentEntity, Int, View) -> Unit){
        itemListener = f
    }
}