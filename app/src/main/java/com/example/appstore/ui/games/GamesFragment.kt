package com.example.appstore.ui.games

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appstore.model.AppData
import com.example.appstore.databinding.FragmentGamesBinding
import com.example.appstore.model.IClickListener
import com.example.appstore.utils.FirebaseUtils
import com.example.appstore.utils.IFirebaseListener

class GamesFragment : Fragment() {
    private lateinit var gameAdapterSquare : GameAdapterSquare
    private lateinit var gameAdapterBigger: GameAdapterBigger
    private lateinit var recyclerViewUpper : RecyclerView
    private lateinit var recyclerViewMiddle: RecyclerView
    private lateinit var recyclerViewBottom: RecyclerView
    private var _binding: FragmentGamesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(GamesViewModel::class.java)

        _binding = FragmentGamesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewUpper = binding.upperScroller
        recyclerViewMiddle = binding.middleScroller
        recyclerViewBottom = binding.bottomScroller

        gameAdapterSquare = GameAdapterSquare(requireContext(), ArrayList())
        gameAdapterBigger = GameAdapterBigger(requireContext(), ArrayList())

        recyclerViewMiddle.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewMiddle.adapter = gameAdapterSquare

        recyclerViewBottom.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewBottom.adapter = gameAdapterSquare

        recyclerViewUpper.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewUpper.adapter = gameAdapterBigger
        gameAdapterBigger.setOnClickListener(object : IClickListener {
            override fun clicked(appData: AppData) {
                val queryUrl : Uri = Uri.parse("https://www.google.com/search?q=${appData.appName}")
                val intent = Intent(Intent.ACTION_VIEW, queryUrl)
                context?.startActivity(intent)
            }
        })

        //TODO firebase parsing
        FirebaseUtils.getAllApps(object : IFirebaseListener {
            override fun onSuccess(data: ArrayList<AppData>) {
                Log.e("firebase" ,"got data" )
                val appDataList = ArrayList<AppData>()
                for(tuple : AppData in data) {
                    if(tuple.appType == 1) {
                        appDataList.add(tuple)
                    }
                }

                gameAdapterSquare.updateData(appDataList)
                gameAdapterBigger.updateData(appDataList)

                println("dhuksi Mammaaaa!!!!!!")
            }

            override fun onFailed(message: String?) {
                Log.e("firebase","failed")
                println("pailam na :(")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}