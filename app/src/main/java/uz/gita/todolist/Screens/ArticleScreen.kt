package uz.gita.todolist.Screens

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.airbnb.lottie.LottieAnimationView
import uz.gita.todolist.R
import uz.gita.todolist.adapter.ArticleAdapter
import uz.gita.todolist.data.AppointmentEntity
import uz.gita.todolist.data.ArticleEntity
import uz.gita.todolist.database.TaskDatabase
import uz.gita.todolist.databinding.ScreenArticlesBinding
import uz.gita.todolist.dialogs.*

class ArticleScreen :Fragment(R.layout.screen_articles) {
    private val binding by viewBinding(ScreenArticlesBinding::bind)
    private val list by lazy { ArrayList<ArticleEntity>() }
    private val adapter by lazy { ArticleAdapter(list) }
    private val bookDb by lazy { TaskDatabase.getDatabase().articleDao() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        binding.articleList.adapter = adapter
        binding.articleList.layoutManager = LinearLayoutManager(requireContext())
        binding.addArticle.setOnClickListener {
            val dialog = AddArticleDialog()
            dialog.setListener {
                bookDb.insert(it)
                list.add(it)
                loadData()
            }
            dialog.show(childFragmentManager, "article")
        }
        binding.articleMenu.setOnClickListener {
            findNavController().navigate(R.id.action_articleScreen_to_fragmentMain)
        }
        adapter.setItemListener { data, pos, v ->
            val popUp = PopupMenu(requireContext(), v)
            popUp.setOnMenuItemClickListener {
                if (it.itemId == R.id.shareItem){
                    val text = "${data.name}, ${data.description},  ${data.webAddress}"
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, text)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(intent,"Article")
                    startActivity(shareIntent)
                } else if (it.itemId == R.id.editItem){
                   val editDialog = EditArticleDialog()
                    val bundle = Bundle()
                    bundle.putSerializable("article", list[pos])
                    editDialog.arguments = bundle
                    editDialog.setListener {articleData->
                        list[pos] = articleData
                        bookDb.update(articleData)
                        adapter.notifyItemChanged(pos)
                    }
                    editDialog.show(childFragmentManager, "editArticle")
                }else {
                    val alertDialog = AlertDialog.Builder(requireContext())
                        .setTitle("Do you really want to remove the article?")
                        .setCancelable(false)
                        .setPositiveButton("Yes"){listener, i->
                            bookDb.delete(data)
                            list.remove(data)
                            adapter.notifyItemRemoved(pos)
                            loadData()
                        }
                        .setNegativeButton("No"){listener, i->}
                    alertDialog.show()
                }
                return@setOnMenuItemClickListener true
            }
            popUp.inflate(R.menu.pop_up_menu)
            popUp.show()
        }

//        adapter.setlinkListener {
//            val intent = Intent(Intent.ACTION_VIEW)
//            intent.data = Uri.parse(it)
//            startActivity(intent)
//        }
    }

    private fun loadData(){
        list.clear()
        list.addAll(TaskDatabase.getDatabase().articleDao().getAllArticles())
        backgroundArticle(list)
        adapter.notifyDataSetChanged()
    }
    private fun backgroundArticle (data : ArrayList<ArticleEntity>) {
        view?.let { view ->
            val noText = view.findViewById<LottieAnimationView>(R.id.noArticle)
            if (data.size == 0) noText.visibility = View.VISIBLE
            else noText.visibility = View.GONE
        }
    }
}