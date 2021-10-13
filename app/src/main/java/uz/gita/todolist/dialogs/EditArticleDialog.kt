package uz.gita.todolist.dialogs

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import uz.gita.todolist.R
import uz.gita.todolist.data.ArticleEntity
import uz.gita.todolist.data.BirthdayEntity

class EditArticleDialog :DialogFragment(R.layout.dialog_edit_article) {
    private var listener : ((ArticleEntity) -> Unit)? = null

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addBtn : AppCompatButton = view.findViewById(R.id.saveEditArticle)
        val cancelBtn : AppCompatButton = view.findViewById(R.id.cancelEditArticle)
        val name : TextInputEditText = view.findViewById(R.id.editArticleName)
        val desc: TextInputEditText = view.findViewById(R.id.editDesc)
        val webAddress: TextInputEditText = view.findViewById(R.id.editWebId)

        var pos = 0
        arguments?.let {
            val data = it.getSerializable("article") as ArticleEntity
            pos = data.id
            name.setText(data.name)
            name.setText(data.name)
            desc.setText(data.description)
            webAddress.setText(data.webAddress)
        }
        addBtn.setOnClickListener {
            listener?.invoke(ArticleEntity(pos, name.text.toString(), desc.text.toString(), webAddress.text.toString()))
            dismiss()
        }

        cancelBtn.setOnClickListener {
            dismiss()
        }

    }

    fun setListener(f : (ArticleEntity) -> Unit){
        listener = f
    }

}