package uz.gita.todolist.dialogs

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import uz.gita.todolist.R
import uz.gita.todolist.data.ContactEntity
import uz.gita.todolist.data.EmailEntity

class EditEmailDialog :DialogFragment(R.layout.dialog_edit_email) {
    private var listener : ((EmailEntity) -> Unit)? = null

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val saveBtn : AppCompatButton = view.findViewById(R.id.saveEditEmail)
        val cancelBtn : AppCompatButton = view.findViewById(R.id.cancelEditEmail)
        val editName : TextInputEditText = view.findViewById(R.id.editEmailName)
        val editEmail: TextInputEditText = view.findViewById(R.id.editEmailAddress)
        var pos = 0
        arguments?.let {
            val data = it.getSerializable("email") as EmailEntity
            pos = data.id
            editName.setText(data.name)
            editEmail.setText(data.emailAddress)
        }

        saveBtn.setOnClickListener {
            listener?.invoke(EmailEntity(pos,editName.text.toString(), editEmail.text.toString()))
            dismiss()
        }

        cancelBtn.setOnClickListener {
            dismiss()
        }

    }

    fun setListener(f : (EmailEntity) -> Unit){
        listener = f
    }

}