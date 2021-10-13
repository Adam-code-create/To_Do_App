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
import uz.gita.todolist.adapter.EmailAdapter
import uz.gita.todolist.data.EmailEntity
import uz.gita.todolist.database.TaskDatabase
import uz.gita.todolist.databinding.ScreenEmailsBinding
import uz.gita.todolist.dialogs.AddEmailDialog
import uz.gita.todolist.dialogs.EditEmailDialog

class EmailScreen :Fragment(R.layout.screen_emails) {
    private val binding by viewBinding(ScreenEmailsBinding::bind)
    private val list by lazy { ArrayList<EmailEntity>() }
    private val adapter by lazy { EmailAdapter(list) }
    private val emailDb by lazy { TaskDatabase.getDatabase().emailsDao() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        binding.emailList.adapter = adapter
        binding.emailList.layoutManager = LinearLayoutManager(requireContext())
        binding.addEmail.setOnClickListener {
            val dialog = AddEmailDialog()
            dialog.setListener {
                emailDb.insert(it)
                list.add(it)
                loadData()
            }
            dialog.show(childFragmentManager, "contact")
        }
        binding.emailMenu.setOnClickListener {
            findNavController().navigate(R.id.action_emailScreen_to_fragmentMain)
        }
        adapter.setItemListener { data, pos, v ->
            val popUp = PopupMenu(requireContext(), v)
            popUp.setOnMenuItemClickListener {
                if (it.itemId == R.id.shareItem){
                    val text = "${data.name}:  ${data.emailAddress}"
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, text)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(intent,"Contact")
                    startActivity(shareIntent)
                } else if (it.itemId == R.id.editItem){
                   val editDialog = EditEmailDialog()
                    val bundle = Bundle()
                    bundle.putSerializable("email", list[pos])
                    editDialog.arguments = bundle
                    editDialog.setListener {emailData->
                        list[pos] = emailData
                        emailDb.update(emailData)
                        adapter.notifyItemChanged(pos)
                    }
                    editDialog.show(childFragmentManager, "editContact")
                }else {
                    val alertDialog = AlertDialog.Builder(requireContext())
                        .setTitle("Do you really want to remove the email?")
                        .setCancelable(false)
                        .setPositiveButton("Yes"){listener, i->
                            emailDb.delete(data)
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
        list.addAll(TaskDatabase.getDatabase().emailsDao().getAllEmails())
        backgroundEmail(list)
        adapter.notifyDataSetChanged()
    }
    private fun backgroundEmail (data : ArrayList<EmailEntity>) {
        view?.let { view ->
            val noText = view.findViewById<LottieAnimationView>(R.id.noEmail)
            if (data.size == 0) noText.visibility = View.VISIBLE
            else noText.visibility = View.GONE
        }
    }
}