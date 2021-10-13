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
import uz.gita.todolist.adapter.BankAccountAdapter
import uz.gita.todolist.adapter.BirthdayAdapter
import uz.gita.todolist.data.ArticleEntity
import uz.gita.todolist.data.BankAccountEntity
import uz.gita.todolist.data.BirthdayEntity
import uz.gita.todolist.database.TaskDatabase
import uz.gita.todolist.databinding.ScreenBankAccountBinding
import uz.gita.todolist.databinding.ScreenBirthdayBinding
import uz.gita.todolist.dialogs.AddBankAccountDialog
import uz.gita.todolist.dialogs.AddBirthdayDialog
import uz.gita.todolist.dialogs.EditBankAccountDialog
import uz.gita.todolist.dialogs.EditBirthdayDialog

class BankAccountScreen :Fragment(R.layout.screen_bank_account) {
    private val binding by viewBinding(ScreenBankAccountBinding::bind)
    private val list by lazy { ArrayList<BankAccountEntity>() }
    private val adapter by lazy { BankAccountAdapter(list) }
    private val accountDb by lazy { TaskDatabase.getDatabase().bankAccountsDao() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        binding.accountList.adapter = adapter
        binding.accountList.layoutManager = LinearLayoutManager(requireContext())
        binding.addAccount.setOnClickListener {
            val dialog = AddBankAccountDialog()
            dialog.setListener {
                accountDb.insert(it)
                list.add(it)
                loadData()
            }
            dialog.show(childFragmentManager, "contact")
        }
        binding.bankMenu.setOnClickListener {
            findNavController().navigate(R.id.action_bankAccountScreen_to_fragmentMain)
        }
        adapter.setItemListener { data, pos, view ->
            val popUp = PopupMenu(requireContext(), view)
            popUp.setOnMenuItemClickListener {
                if (it.itemId == R.id.shareItem){
                    val text = "Holder: ${data.name}, Account number: ${data.accountNumber}, Expiration: ${data.expiration}"
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, text)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(intent,"Contact")
                    startActivity(shareIntent)
                } else if (it.itemId == R.id.editItem){
                   val editDialog = EditBankAccountDialog()
                    val bundle = Bundle()
                    bundle.putSerializable("account", list[pos])
                    editDialog.arguments = bundle
                    editDialog.setListener {contactData->
                        list[pos] = contactData
                        accountDb.update(contactData)
                        adapter.notifyItemChanged(pos)
                    }
                    editDialog.show(childFragmentManager, "editContact")
                }else {
                    val alertDialog = AlertDialog.Builder(requireContext())
                        .setTitle("Do you really want to remove bank card?")
                        .setCancelable(false)
                        .setPositiveButton("Yes"){listener, i->
                            accountDb.delete(data)
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
        list.addAll(TaskDatabase.getDatabase().bankAccountsDao().getAllBankAccounts())
        backgroundAccount(list)
        adapter.notifyDataSetChanged()
    }
    private fun backgroundAccount (data : ArrayList<BankAccountEntity>) {
        view?.let { view ->
            val noText = view.findViewById<LottieAnimationView>(R.id.noAccount)
            if (data.size == 0) noText.visibility = View.VISIBLE
            else noText.visibility = View.GONE
        }
    }
}