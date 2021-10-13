package uz.gita.todolist.dialogs

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.textfield.TextInputEditText
import uz.gita.todolist.R
import uz.gita.todolist.data.ContactEntity

class EditContactDialog :DialogFragment(R.layout.dialog_edit_contact) {
private var listener : ((ContactEntity) -> Unit)? = null
    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnCancel : AppCompatButton = view.findViewById(R.id.cancelEditContact)
        val btnSave : AppCompatButton = view.findViewById(R.id.saveEditContact)
        val editName : TextInputEditText = view.findViewById(R.id.editContactName)
        val editPhone : TextInputEditText = view.findViewById(R.id.editPhoneNumber)
        var pos = 0
        arguments?.let {
            val data = it.getSerializable("contact") as ContactEntity
            pos = data.id
            editName.setText(data.name)
            editPhone.setText(data.phone)
        }

        btnCancel.setOnClickListener {
            dismiss()
        }
        btnSave.setOnClickListener {
            listener?.invoke(ContactEntity(pos,editName.text.toString(), editPhone.text.toString()))
            dismiss()
        }
    }

    fun setEditListener(f :(ContactEntity) -> Unit){
        listener = f
    }
}