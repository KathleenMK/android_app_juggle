package org.wit.juggle.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
//import com.google.android.gms.auth.api.signin.JuggleSignIn
import org.wit.juggle.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        Toast.makeText(context, "home frag line 40", Toast.LENGTH_LONG).show()
        Toast.makeText(context, homeViewModel.googleSignInAccount.toString(), Toast.LENGTH_LONG).show()
        Log.w(TAG, "line 44 : ${homeViewModel.googleSignInAccount}")
        Log.w(TAG, "line 44 : ${homeViewModel.googleSignInClient}")
        Log.w(TAG, "line 44 : ${homeViewModel.googleSignInClient.value}")
        //revokeAccess()
        
        //val intent = Intent(this, GrabberLogin::class.java)
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        //startActivity(intent)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "HomeFragment"

    }

    private fun revokeAccess() {
        homeViewModel.googleSignInClient.value!!.revokeAccess()
        //startActivity(Intent(this, JuggleSignIn::class.java))
            //.addOnCompleteListener(this) {
                //updateUI(null)
            //}
        //val intent = Intent(this, JuggleSignIn::class.java)
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        //startActivity(intent)

    }


}