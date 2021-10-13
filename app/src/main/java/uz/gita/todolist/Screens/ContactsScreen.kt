package uz.gita.todolist.Screens

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.airbnb.lottie.LottieAnimationView
import uz.gita.todolist.R
import uz.gita.todolist.adapter.ContactAdapter
import uz.gita.todolist.data.BirthdayEntity
import uz.gita.todolist.data.ContactEntity
import uz.gita.todolist.database.TaskDatabase
import uz.gita.todolist.databinding.ScreenContactsBinding
import uz.gita.todolist.dialogs.AddContactDialog
import uz.gita.todolist.dialogs.EditContactDialog

class ContactsScreen :Fragment(R.layout.screen_contacts) {
    private val binding by viewBinding(ScreenContactsBinding::bind)
    private val list by lazy { ArrayList<ContactEntity>() }
    private val adapter by lazy { ContactAdapter(list) }
    private val contactDB by lazy { TaskDatabase.getDatabase().getContactDao() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        binding.contactList.adapter = adapter
        binding.contactList.layoutManager = LinearLayoutManager(requireContext())
        binding.addContact.setOnClickListener {
            val dialog = AddContactDialog()
            dialog.setListener {
                contactDB.insert(it)
                list.add(it)
                loadData()
            }
            dialog.show(childFragmentManager, "contact")
        }
        binding.contactMenu.setOnClickListener {
            findNavController().navigate(R.id.action_screenContacts_to_fragmentMain)
        }
        adapter.setItemListener { data, pos, view ->
            val popUp = PopupMenu(requireContext(), view)
            popUp.setOnMenuItemClickListener {
                if (it.itemId == R.id.shareItem){
                    val text = "${data.name}:  ${data.phone}"
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, text)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(intent,"Contact")
                    startActivity(shareIntent)
                } else if (it.itemId == R.id.editItem){
                   val editDialog = EditContactDialog()
                    val bundle = Bundle()
                    bundle.putSerializable("contact", list[pos])
                    editDialog.arguments = bundle
                    editDialog.setEditListener {contactData->
                        list[pos] = contactData
                        contactDB.update(contactData)
                        adapter.notifyItemChanged(pos)
                    }
                    editDialog.show(childFragmentManager, "editContact")
                }else {
                    val alertDialog = AlertDialog.Builder(requireContext())
                        .setTitle("Do you really want to remove contact?")
                        .setCancelable(false)
                        .setPositiveButton("Yes"){listener, i->
                            contactDB.delete(data)
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
        list.addAll(TaskDatabase.getDatabase().getContactDao().getAllContacts())
        backgroundContact(list)
        adapter.notifyDataSetChanged()
    }
    private fun backgroundContact (data : ArrayList<ContactEntity>) {
        view?.let { view ->
            val noText = view.findViewById<LottieAnimationView>(R.id.noContact)
            if (data.size == 0) noText.visibility = View.VISIBLE
            else noText.visibility = View.GONE
        }
    }
}