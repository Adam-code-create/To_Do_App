package uz.gita.todolist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.gita.todolist.R
import uz.gita.todolist.data.ArticleEntity


class ArticleAdapter (private val list : ArrayList<ArticleEntity>): RecyclerView.Adapter<ArticleAdapter.ContactHolder>() {
   private var itemListener : ((ArticleEntity, Int, View)-> Unit)? = null
    private var linkListener :((String) -> Unit)? = null
    inner class ContactHolder (view: View) : RecyclerView.ViewHolder(view){
        private val setting : ImageView = view.findViewById(R.id.articleMore)
        private val name :TextView = view.findViewById(R.id.articleName)
        private val desc :TextView = view.findViewById(R.id.articleDes)
        private val webId :TextView = view.findViewById(R.id.articleWebId)
        init {
            setting.setOnClickListener {
                itemListener?.invoke(list[absoluteAdapterPosition], absoluteAdapterPosition, setting)
            }

            webId.setOnClickListener {
                linkListener?.invoke(list[absoluteAdapterPosition].webAddress)
            }
        }

        fun bind(){
            val data = list[absoluteAdapterPosition]
            name.text = data.name
            desc.text = data.description
            webId.text = data.webAddress
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder =
        ContactHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_articles, parent, false))

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = list.size

    fun setItemListener(f: (ArticleEntity, Int, View) -> Unit){
        itemListener = f
    }

    fun setlinkListener (f : (String) ->Unit){
        linkListener = f
    }
}