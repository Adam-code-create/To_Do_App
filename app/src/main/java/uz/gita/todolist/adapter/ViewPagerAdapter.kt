package uz.gita.todolist.adapter

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter (
    activity: FragmentActivity,
    private val todoPage :Fragment,
    private val doingPage :Fragment,
    private val donePage :Fragment
        ) : FragmentStateAdapter(activity){
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0-> {
                todoPage
            }
            1-> {
                doingPage
            }
            else -> {
                donePage
            }
        }
    }
}