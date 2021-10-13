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

class AddContactDialog :DialogFragment(R.layout.dialog_add_contact) {
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
        val addContactBtn : AppCompatButton = view.findViewById(R.id.saveContact)
        val cancelContactBtn : AppCompatButton = view.findViewById(R.id.cancelContact)
        val addContactName : TextInputEditText = view.findViewById(R.id.addContactName)
        val addPhoneNumber: TextInputEditText = view.findViewById(R.id.addPhoneNumber)


        addContactBtn.setOnClickListener {
            listener?.invoke(ContactEntity(0, addContactName.text.toString(), addPhoneNumber.text.toString()))
            dismiss()
        }

        cancelContactBtn.setOnClickListener {
            dismiss()
        }

    }

    fun setListener(f : (ContactEntity) -> Unit){
        listener = f
    }

}