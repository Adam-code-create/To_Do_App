package uz.gita.todolist.Screens

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.airbnb.lottie.LottieAnimationView
import uz.gita.todolist.R
import uz.gita.todolist.adapter.BookAdapter
import uz.gita.todolist.data.BookEntity
import uz.gita.todolist.data.BankAccountEntity
import uz.gita.todolist.data.BirthdayEntity
import uz.gita.todolist.data.TaskEntity
import uz.gita.todolist.database.TaskDatabase
import uz.gita.todolist.databinding.ScreenBankAccountBinding
import uz.gita.todolist.databinding.ScreenBirthdayBinding
import uz.gita.todolist.databinding.ScreenBooksBinding
import uz.gita.todolist.dialogs.*

class BookScreen :Fragment(R.layout.screen_books) {
    private val binding by viewBinding(ScreenBooksBinding::bind)
    private val list by lazy { ArrayList<BookEntity>() }
    private val adapter by lazy { BookAdapter(list) }
    private val bookDb by lazy { TaskDatabase.getDatabase().booksDao() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        binding.bookList.adapter = adapter
        binding.bookList.layoutManager = LinearLayoutManager(requireContext())
        binding.addBook.setOnClickListener {
            val dialog = AddBookDialog()
            dialog.setListener {
                bookDb.insert(it)
                list.add(it)
                loadData()
            }
            dialog.show(childFragmentManager, "contact")
        }
        binding.bookMenu.setOnClickListener {
            findNavController().navigate(R.id.action_bookScreen_to_fragmentMain)
        }
        adapter.setItemListener { data, pos, v ->
            val popUp = PopupMenu(requireContext(), v)
            popUp.setOnMenuItemClickListener {
                if (it.itemId == R.id.shareItem){
                    val text = "${data.name}, ${data.startDate},  ${data.endDate}"
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, text)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(intent,"Contact")
                    startActivity(shareIntent)
                } else if (it.itemId == R.id.editItem){
                   val editDialog = EditBookDialog()
                    val bundle = Bundle()
                    bundle.putSerializable("book", list[pos])
                    editDialog.arguments = bundle
                    editDialog.setListener {appointData->
                        list[pos] = appointData
                        bookDb.update(appointData)
                        adapter.notifyItemChanged(pos)
                    }
                    editDialog.show(childFragmentManager, "editContact")
                }else {
                    val alertDialog = AlertDialog.Builder(requireContext())
                        .setTitle("Do you really want to remove the book")
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
    }

    private fun loadData(){
        list.clear()
        list.addAll(TaskDatabase.getDatabase().booksDao().getAllBooks())
        backgroundBooks(list)
        adapter.notifyDataSetChanged()
    }
    private fun backgroundBooks (data : ArrayList<BookEntity>) {
        view?.let { view ->
            val noText = view.findViewById<LottieAnimationView>(R.id.noBook)
            if (data.size == 0) noText.visibility = View.VISIBLE
            else noText.visibility = View.GONE

        }
    }
}