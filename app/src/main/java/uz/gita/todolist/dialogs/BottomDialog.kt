package uz.gita.todolist.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uz.gita.todolist.R

class BottomDialog : BottomSheetDialogFragment() {
    private var editListener : (() -> Unit)? = null
    private var deleteListener : (() -> Unit)? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_bottom, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val edit : LinearLayout = view.findViewById(R.id.edit)
        val delete : LinearLayout = view.findViewById(R.id.delete)

        edit.setOnClickListener {
            editListener?.invoke()
            dismiss()
        }
        delete.setOnClickListener {
            deleteListener?.invoke()
            dismiss()
        }

    }

    fun setEditListener ( f : () -> Unit){
        editListener = f
    }
    fun setDeleteListener ( f : () -> Unit){
        deleteListener = f
    }


}