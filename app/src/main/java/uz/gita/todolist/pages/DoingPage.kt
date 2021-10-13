package uz.gita.todolist.pages

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import uz.gita.todolist.R
import uz.gita.todolist.adapter.MainAdapter
import uz.gita.todolist.data.TaskEntity
import uz.gita.todolist.database.TaskDatabase
import uz.gita.todolist.dialogs.BottomDialog
import uz.gita.todolist.dialogs.EditTaskDialog

class DoingPage : Fragment (R.layout.page_doing) {

    private val data = ArrayList<TaskEntity>()
    private val taskDao = TaskDatabase.getDatabase().getDao()
    private val adapter by lazy { MainAdapter(data, R.drawable.ic_doing, ContextCompat.getColor(requireContext(), R.color.doing_color), ContextCompat.getColor(requireContext(), R.color.doing_bg)) }
    private var updateToDoPageListener: (()->Unit)? = null
    private var updateDonePageListener: (()->Unit)? = null
    private var itemListener : ((TaskEntity) -> Unit)? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val rv : RecyclerView = view.findViewById(R.id.doingList)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(requireContext())
        loadData()
        adapter.setDoubleItemClickListener {
            itemListener?.invoke(data[it])
        }
        adapter.setItemClickListener {
            val bottomDialog = BottomDialog()
            bottomDialog.setDeleteListener {
                taskDao.delete(data[it])
                data.removeAt(it)
                adapter.notifyItemRemoved(it)
                backgroundNotes(data)
            }

            bottomDialog.setEditListener {
                val editDialog = EditTaskDialog()
                val bundle = Bundle ()
                bundle.putSerializable("data", data[it])
                editDialog.arguments = bundle
                editDialog.setEditDialog {taskData ->
                    data[it] = taskData
                    taskDao.update(taskData)
                    adapter.notifyItemChanged(it)
                }
                editDialog.show(childFragmentManager, "editDialog")
            }
            bottomDialog.show(childFragmentManager, "bottomDialog")
        }


        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback(){
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                val swipeDrags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            return makeMovementFlags(dragFlags, swipeDrags)

            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.RIGHT){
                    val dataTaskEntity = data[viewHolder.adapterPosition]
                    dataTaskEntity.isPos = 2
                    taskDao.update(dataTaskEntity)
                    data.removeAt(viewHolder.adapterPosition)
                    adapter.notifyItemRemoved(viewHolder.adapterPosition)
                    updateDonePageListener?.invoke()
                    backgroundNotes(data)
                } else{
                    val dataTaskEntity = data[viewHolder.adapterPosition]
                    dataTaskEntity.isPos = 0
                    taskDao.update(dataTaskEntity)
                    data.removeAt(viewHolder.adapterPosition)
                    adapter.notifyItemRemoved(viewHolder.adapterPosition)
                    updateToDoPageListener?.invoke()
                    backgroundNotes(data)
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(rv)
        backgroundNotes(data)

    }
    fun setListener (f : (TaskEntity) -> Unit){
        itemListener = f
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadData(){
        data.clear()
        data.addAll(taskDao.getDataByPosition(1))
        backgroundNotes(data)
        adapter.notifyDataSetChanged()

    }
    fun setUpdateDonePageListener (f :()->Unit){
        updateDonePageListener = f
    }
    fun setUpdateToDoPageListener (f :()->Unit){
        updateToDoPageListener = f
    }


    fun backgroundNotes(data: ArrayList<TaskEntity>) {
        view?.let {
            val noDoingText = it.findViewById<LottieAnimationView>(R.id.noNotesDoing)
            if (data.size == 0){
                noDoingText.visibility = View.VISIBLE
            }
            else {
                noDoingText.visibility = View.GONE

            }
        }


    }

}