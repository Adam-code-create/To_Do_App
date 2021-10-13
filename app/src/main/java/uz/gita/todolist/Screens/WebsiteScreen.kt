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
import uz.gita.todolist.adapter.WebsitesAdapter
import uz.gita.todolist.data.EmailEntity
import uz.gita.todolist.data.WebsitesEntity
import uz.gita.todolist.database.TaskDatabase
import uz.gita.todolist.databinding.ScreenContactsBinding
import uz.gita.todolist.databinding.ScreenWebsitesBinding
import uz.gita.todolist.dialogs.AddContactDialog
import uz.gita.todolist.dialogs.AddWebsiteDialog
import uz.gita.todolist.dialogs.EditContactDialog
import uz.gita.todolist.dialogs.EditWebsiteDialog

class WebsiteScreen :Fragment(R.layout.screen_websites) {
    private val binding by viewBinding(ScreenWebsitesBinding::bind)
    private val list by lazy { ArrayList<WebsitesEntity>() }
    private val adapter by lazy { WebsitesAdapter(list) }
    private val websitesDB by lazy { TaskDatabase.getDatabase().websitesDao() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        binding.webList.adapter = adapter
        binding.webList.layoutManager = LinearLayoutManager(requireContext())
        binding.addWebsite.setOnClickListener {
            val dialog = AddWebsiteDialog()
            dialog.setListener {
                websitesDB.insert(it)
                list.add(it)
                loadData()
            }
            dialog.show(childFragmentManager, "contact")
        }
        binding.websiteMenu.setOnClickListener {
            findNavController().navigate(R.id.action_websiteScreen_to_fragmentMain)
        }
        adapter.setItemListener { data, pos, view ->
            val popUp = PopupMenu(requireContext(), view)
            popUp.setOnMenuItemClickListener {
                if (it.itemId == R.id.shareItem){
                    val text = "${data.name}:  ${data.address}"
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, text)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(intent,"Contact")
                    startActivity(shareIntent)
                } else if (it.itemId == R.id.editItem){
                   val editDialog = EditWebsiteDialog()
                    val bundle = Bundle()
                    bundle.putSerializable("website", list[pos])
                    editDialog.arguments = bundle
                    editDialog.setListener {webData->
                        list[pos] = webData
                        websitesDB.update(webData)
                        adapter.notifyItemChanged(pos)
                    }
                    editDialog.show(childFragmentManager, "editContact")
                }else {
                    val alertDialog = AlertDialog.Builder(requireContext())
                        .setTitle("Do you really want to remove the website?")
                        .setCancelable(false)
                        .setPositiveButton("Yes"){listener, i->
                            websitesDB.delete(data)
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
        list.addAll(TaskDatabase.getDatabase().websitesDao().getAllWebsites())
        backgroundWebsite(list)
        adapter.notifyDataSetChanged()
    }
    private fun backgroundWebsite (data : ArrayList<WebsitesEntity>) {
        view?.let { view ->
            val noText = view.findViewById<LottieAnimationView>(R.id.noWebsite)
            if (data.size == 0) noText.visibility = View.VISIBLE
            else noText.visibility = View.GONE
        }
    }
}