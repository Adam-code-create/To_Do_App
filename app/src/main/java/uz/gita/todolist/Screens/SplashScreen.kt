package uz.gita.todolist.Screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import uz.gita.todolist.R

@SuppressLint("CustomSplashScreen")
class SplashScreen : Fragment (R.layout.screen_splash) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val animationView :LottieAnimationView = view.findViewById(R.id.animationView)
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            with(animationView){
                setMinAndMaxFrame(40, 60)
            }

            findNavController().navigate(
                R.id.action_fragmentSplashScreen_to_fragmentMain)
        }, 2500)
    }


}