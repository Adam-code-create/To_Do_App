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
import uz.gita.todolist.adapter.AppointmentAdapter
import uz.gita.todolist.adapter.BankAccountAdapter
import uz.gita.todolist.adapter.BirthdayAdapter
import uz.gita.todolist.data.AppointmentEntity
import uz.gita.todolist.data.BankAccountEntity
import uz.gita.todolist.data.BirthdayEntity
import uz.gita.todolist.data.BookEntity
import uz.gita.todolist.database.TaskDatabase
import uz.gita.todolist.databinding.ScreenAppointmentBinding
import uz.gita.todolist.databinding.ScreenBankAccountBinding
import uz.gita.todolist.databinding.ScreenBirthdayBinding
import uz.gita.todolist.dialogs.*

class AppointmentScreen :Fragment(R.layout.screen_appointment) {
    private val binding by viewBinding(ScreenAppointmentBinding::bind)
    private val list by lazy { ArrayList<AppointmentEntity>() }
    private val adapter by lazy { AppointmentAdapter(list) }
    private val appointDb by lazy { TaskDatabase.getDatabase().appointmentsDao() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        binding.appointmentList.adapter = adapter
        binding.appointmentList.layoutManager = LinearLayoutManager(requireContext())
        binding.addAppointment.setOnClickListener {
            val dialog = AddAppointmentDialog()
            dialog.setListener {
                appointDb.insert(it)
                list.add(it)
                loadData()
            }
            dialog.show(childFragmentManager, "contact")
        }
        binding.appointmentMenu.setOnClickListener {
            findNavController().navigate(R.id.action_appointmentScreen_to_fragmentMain)
        }
        adapter.setItemListener { data, pos, view ->
            val popUp = PopupMenu(requireContext(), view)
            popUp.setOnMenuItemClickListener {
                if (it.itemId == R.id.shareItem){
                    val text = "${data.name}, ${data.place},  ${data.date}"
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, text)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(intent,"Contact")
                    startActivity(shareIntent)
                } else if (it.itemId == R.id.editItem){
                   val editDialog = EditAppointmentDialog()
                    val bundle = Bundle()
                    bundle.putSerializable("appointment", list[pos])
                    editDialog.arguments = bundle
                    editDialog.setListener {appointData->
                        list[pos] = appointData
                        appointDb.update(appointData)
                        adapter.notifyItemChanged(pos)
                    }
                    editDialog.show(childFragmentManager, "editContact")
                }else {
                    val alertDialog = AlertDialog.Builder(requireContext())
                        .setTitle("Do you really want to remove the appointment?")
                        .setCancelable(false)
                        .setPositiveButton("Yes"){listener, i->
                            appointDb.delete(data)
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
        list.addAll(TaskDatabase.getDatabase().appointmentsDao().getAllAppointments())
        backgroundAppointments(list)
        adapter.notifyDataSetChanged()
    }
    private fun backgroundAppointments (data : ArrayList<AppointmentEntity>) {
        view?.let { view ->
            val noText = view.findViewById<LottieAnimationView>(R.id.noAppointment)
            if (data.size == 0) noText.visibility = View.VISIBLE
            else noText.visibility = View.GONE
        }
    }
}