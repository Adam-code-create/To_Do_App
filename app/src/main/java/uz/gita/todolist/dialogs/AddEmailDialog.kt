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

class AddEmailDialog :DialogFragment(R.layout.dialog_add_email) {
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
        val saveBtn : AppCompatButton = view.findViewById(R.id.saveEmail)
        val cancelBtn : AppCompatButton = view.findViewById(R.id.cancelEmail)
        val addName : TextInputEditText = view.findViewById(R.id.addEmailName)
        val addEmail: TextInputEditText = view.findViewById(R.id.addEmailAddress)


        saveBtn.setOnClickListener {
            listener?.invoke(EmailEntity(0,addName.text.toString(), addEmail.text.toString()))
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