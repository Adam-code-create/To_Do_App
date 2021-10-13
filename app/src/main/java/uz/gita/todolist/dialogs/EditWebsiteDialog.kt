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
import uz.gita.todolist.data.WebsitesEntity

class EditWebsiteDialog :DialogFragment(R.layout.dialog_edit_websites) {
    private var listener : ((WebsitesEntity) -> Unit)? = null

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val saveBtn : AppCompatButton = view.findViewById(R.id.saveEditWebsite)
        val cancelBtn : AppCompatButton = view.findViewById(R.id.cancelEditWebsite)
        val addWebName : TextInputEditText = view.findViewById(R.id.editWebName)
        val addWebAddress: TextInputEditText = view.findViewById(R.id.editWebAddress)
        var pos = 0
        arguments?.let {
            val data = it.getSerializable("website") as WebsitesEntity
            pos = data.id
            addWebName.setText(data.name)
            addWebAddress.setText(data.address)
        }

        saveBtn.setOnClickListener {
            listener?.invoke(WebsitesEntity(pos, addWebName.text.toString(), addWebAddress.text.toString()))
            dismiss()
        }

        cancelBtn.setOnClickListener {
            dismiss()
        }

    }

    fun setListener(f : (WebsitesEntity) -> Unit){
        listener = f
    }

}