package uz.gita.todolist.pages

import android.annotation.SuppressLint
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

class DonePage : Fragment (R.layout.page_done) {
    private val data = ArrayList<TaskEntity>()
    private val taskDao = TaskDatabase.getDatabase().getDao()
    private val adapter by lazy { MainAdapter(data,R.drawable.ic_check, ContextCompat.getColor(requireContext(), R.color.done_color), ContextCompat.getColor(requireContext(), R.color.done_bg)) }
    private var updateDonePageListener: (() -> Unit)? = null
    private var itemListener : ((TaskEntity) -> Unit)? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val rv: RecyclerView = view.findViewById(R.id.doneList)
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
                val bundle = Bundle()
                bundle.putSerializable("data", data[it])
                editDialog.arguments = bundle
                editDialog.setEditDialog { taskData ->
                    data[it] = taskData
                    taskDao.update(taskData)
                    adapter.notifyItemChanged(it)
                }
                editDialog.show(childFragmentManager, "editDialog")
            }
            bottomDialog.show(childFragmentManager, "bottomDialog")
        }

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                val swipeDrags = ItemTouchHelper.LEFT
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
                if (direction == ItemTouchHelper.LEFT) {
                    val dataTaskEntity = data[viewHolder.adapterPosition]
                    dataTaskEntity.isPos = 1
                    taskDao.update(dataTaskEntity)
                    data.removeAt(viewHolder.adapterPosition)
                    adapter.notifyItemRemoved(viewHolder.adapterPosition)
                    updateDonePageListener?.invoke()
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
    fun loadData() {
        data.clear()
        data.addAll(taskDao.getDataByPosition(2))
        backgroundNotes(data)
        adapter.notifyDataSetChanged()
    }

    fun setUpdateDonePageListener(f: () -> Unit) {
        updateDonePageListener = f

    }
    @SuppressLint("NotifyDataSetChanged")
    fun backgroundNotes(data: ArrayList<TaskEntity>) {
        view?.let {view->
            val noNotesDone = view.findViewById<LottieAnimationView>(R.id.noNotesDone)
            if (data.size == 0) {
                noNotesDone.visibility = View.VISIBLE
                adapter.notifyDataSetChanged()
            } else {
                noNotesDone.visibility = View.GONE
                adapter.notifyDataSetChanged()
            }
        }



    }
}