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
import uz.gita.todolist.adapter.BirthdayAdapter
import uz.gita.todolist.data.BankAccountEntity
import uz.gita.todolist.data.BirthdayEntity
import uz.gita.todolist.database.TaskDatabase
import uz.gita.todolist.databinding.ScreenBirthdayBinding
import uz.gita.todolist.dialogs.AddBirthdayDialog
import uz.gita.todolist.dialogs.EditBirthdayDialog

class BirthdayScreen :Fragment(R.layout.screen_birthday) {
    private val binding by viewBinding(ScreenBirthdayBinding::bind)
    private val list by lazy { ArrayList<BirthdayEntity>() }
    private val adapter by lazy { BirthdayAdapter(list) }
    private val birthdayDB by lazy { TaskDatabase.getDatabase().getBirthdayDao() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        binding.birthdayList.adapter = adapter
        binding.birthdayList.layoutManager = LinearLayoutManager(requireContext())
        binding.addBirthday.setOnClickListener {
            val dialog = AddBirthdayDialog()
            dialog.setListener {
                birthdayDB.insert(it)
                list.add(it)
                loadData()
            }
            dialog.show(childFragmentManager, "contact")
        }
        binding.birthdayMenu.setOnClickListener {
            findNavController().navigate(R.id.action_birthdayScreen_to_fragmentMain)
        }
        adapter.setItemListener { data, pos, view ->
            val popUp = PopupMenu(requireContext(), view)
            popUp.setOnMenuItemClickListener {
                if (it.itemId == R.id.shareItem){
                    val text = "Name: ${data.name},  birthday: ${data.date}, age: ${data.age}"
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, text)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(intent,"Contact")
                    startActivity(shareIntent)
                } else if (it.itemId == R.id.editItem){
                   val editDialog = EditBirthdayDialog()
                    val bundle = Bundle()
                    bundle.putSerializable("birthday", list[pos])
                    editDialog.arguments = bundle
                    editDialog.setEditListener {contactData->
                        list[pos] = contactData
                        birthdayDB.update(contactData)
                        adapter.notifyItemChanged(pos)
                    }
                    editDialog.show(childFragmentManager, "editContact")
                }else {
                    val alertDialog = AlertDialog.Builder(requireContext())
                        .setTitle("Do you really want to remove the birthday?")
                        .setCancelable(false)
                        .setPositiveButton("Yes"){listener, i->
                            birthdayDB.delete(data)
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
        list.addAll(TaskDatabase.getDatabase().getBirthdayDao().getAllBirthdays())
        backgroundBirthday(list)
        adapter.notifyDataSetChanged()
    }
    private fun backgroundBirthday (data : ArrayList<BirthdayEntity>) {
        view?.let { view ->
            val noText = view.findViewById<LottieAnimationView>(R.id.noBirthday)
            if (data.size == 0) noText.visibility = View.VISIBLE
            else noText.visibility = View.GONE
        }
    }
}