package uz.gita.todolist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.gita.todolist.R
import uz.gita.todolist.data.BankAccountEntity
import uz.gita.todolist.data.BirthdayEntity
import uz.gita.todolist.data.ContactEntity

class BankAccountAdapter (private val list : ArrayList<BankAccountEntity>): RecyclerView.Adapter<BankAccountAdapter.ContactHolder>() {
   private var itemListener : ((BankAccountEntity, Int, View)-> Unit)? = null
    inner class ContactHolder (view: View) : RecyclerView.ViewHolder(view){
        private val setting : ImageView = view.findViewById(R.id.accountMore)
        private val name :TextView = view.findViewById(R.id.cardHolderName)
        private val accountNumber :TextView = view.findViewById(R.id.cardNumber)
        private val expiration :TextView = view.findViewById(R.id.cardDate)
        init {
            setting.setOnClickListener {
                itemListener?.invoke(list[absoluteAdapterPosition], absoluteAdapterPosition, setting)
            }
        }

        fun bind(){
            val data = list[absoluteAdapterPosition]
            name.text = data.name
            accountNumber.text = data.accountNumber
            expiration.text = data.expiration
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder =
        ContactHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_bank_account, parent, false))

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = list.size

    fun setItemListener(f: (BankAccountEntity, Int, View) -> Unit){
        itemListener = f
    }
}