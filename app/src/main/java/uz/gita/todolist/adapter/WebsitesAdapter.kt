package uz.gita.todolist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.gita.todolist.R
import uz.gita.todolist.data.ContactEntity
import uz.gita.todolist.data.WebsitesEntity

class WebsitesAdapter (private val list : ArrayList<WebsitesEntity>): RecyclerView.Adapter<WebsitesAdapter.ContactHolder>() {
   private var itemListener : ((WebsitesEntity, Int, View)-> Unit)? = null
    inner class ContactHolder (view: View) : RecyclerView.ViewHolder(view){
        private val setting : ImageView = view.findViewById(R.id.websiteMore)
        private val webname :TextView = view.findViewById(R.id.websiteName)
        private val webId :TextView = view.findViewById(R.id.websiteId)
        init {
            setting.setOnClickListener {
                itemListener?.invoke(list[absoluteAdapterPosition], absoluteAdapterPosition, setting)
            }
        }

        fun bind(){
            val data = list[absoluteAdapterPosition]
            webname.text = data.name
            webId.text = data.address
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder =
        ContactHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_websites, parent, false))

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = list.size

    fun setItemListener(f: (WebsitesEntity, Int, View) -> Unit){
        itemListener = f
    }
}