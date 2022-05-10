package com.example.appstore.ui.app

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appstore.AppData
import com.example.appstore.databinding.FragmentAppBinding
import com.example.appstore.utils.FirebaseUtils
import com.example.appstore.utils.IFirebaseListener

class AppFragment : Fragment() {
    private lateinit var appAdapterSquare : AppAdapterSquare
    private lateinit var appAdapterHorizontal: AppAdapterHorizontal
    private var _binding: FragmentAppBinding? = null
    private lateinit var recyclerViewLinear : RecyclerView
    private lateinit var recyclerViewGrid: RecyclerView

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val appViewModel =
            ViewModelProvider(this).get(AppViewModel::class.java)

        _binding = FragmentAppBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewLinear = binding.recyclerViewLinear
        recyclerViewGrid = binding.recyclerViewGrid

        appAdapterSquare = AppAdapterSquare(requireContext(), ArrayList())
        appAdapterHorizontal = AppAdapterHorizontal(requireContext(), ArrayList())

        recyclerViewLinear.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewLinear.adapter = appAdapterSquare

        recyclerViewGrid.layoutManager = GridLayoutManager(context, 3, GridLayoutManager.HORIZONTAL, false)
        recyclerViewGrid.adapter = appAdapterHorizontal

        //TODO firebase parsing
        FirebaseUtils.getAllApps(object : IFirebaseListener {
            override fun onSuccess(data: ArrayList<AppData>) {
                Log.e("firebase" ,"got data ${data.size}" )
                val appDataList = ArrayList<AppData>()
                for(tuple : AppData in data) {
                    if(tuple.appType == 0) {
                        appDataList.add(tuple)
                    }else{
                        Log.e("type" , tuple.appType.toString())
                    }
                }
                appAdapterSquare.updateData(appDataList)
                appAdapterHorizontal.updateData(appDataList)

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