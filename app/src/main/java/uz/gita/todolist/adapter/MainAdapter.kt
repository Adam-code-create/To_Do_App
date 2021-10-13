package uz.gita.todolist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import uz.gita.todolist.R
import uz.gita.todolist.data.TaskEntity
import java.util.*


class MainAdapter(
    private val list : List<TaskEntity>,
    private val image :Int,
    private val cardColor :Int,
    private val cardBgColor : Int
)  :RecyclerView.Adapter<MainAdapter.MainHolder>() {
    private var itemClickListener : ((Int) -> Unit)? = null
    private var itemDoubleClickListener : ((Int) -> Unit)? = null
    inner class MainHolder (view : View) :RecyclerView.ViewHolder (view){
        private val textTitle : TextView = view.findViewById(R.id.textTitle)
        private val deadline : TextView = view.findViewById(R.id.deadline)
        private val alarm :ImageView = view.findViewById(R.id.reminderImage)
        private val icon : ImageView = view.findViewById(R.id.pageImage)
        private val more : ImageView = view.findViewById(R.id.more)
        private val cardBack : CardView = view.findViewById(R.id.cardColor)
        private val cardBg : ConstraintLayout = view.findViewById(R.id.card_bg)

        init {
            itemView.setOnClickListener {
                itemDoubleClickListener?.invoke(adapterPosition)
            }
            more.setOnClickListener {
                itemClickListener?.invoke(adapterPosition)
            }
        }
        fun bind(){
            val data = list[adapterPosition]
            textTitle.text = data.title
            deadline.text = data.deadline
            if (data.reminder==3)alarm.visibility = View.GONE
            else alarm.visibility = View.VISIBLE

            icon.setImageResource(image)
            cardBack.setCardBackgroundColor(cardColor)
            cardBg.setBackgroundColor(cardBgColor)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder =
        MainHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false))

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = list.size

    fun setItemClickListener (f: (Int) -> Unit) {
        itemClickListener = f
    }
    fun setDoubleItemClickListener (f: (Int) -> Unit) {
        itemDoubleClickListener = f
    }


}