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

class AddArticleDialog :DialogFragment(R.layout.dialog_add_article) {
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
        val addBtn : AppCompatButton = view.findViewById(R.id.saveArticle)
        val cancelBtn : AppCompatButton = view.findViewById(R.id.cancelArticle)
        val name : TextInputEditText = view.findViewById(R.id.addArticleName)
        val desc: TextInputEditText = view.findViewById(R.id.addDesc)
        val webAddress: TextInputEditText = view.findViewById(R.id.webidInput)


        addBtn.setOnClickListener {
            listener?.invoke(ArticleEntity(0, name.text.toString(), desc.text.toString(), webAddress.text.toString()))
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