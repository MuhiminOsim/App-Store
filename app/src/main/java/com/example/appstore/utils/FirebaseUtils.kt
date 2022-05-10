package com.example.appstore.utils

import android.util.Log
import androidx.annotation.NonNull
import com.example.appstore.AppData
import com.google.firebase.database.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class FirebaseUtils {
    companion object {
        fun getAllApps(listener: IFirebaseListener) {
            try {
                val databaseReference: DatabaseReference =
                    FirebaseDatabase.getInstance().getReference("/AppData")

                databaseReference.addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val dataArray = ArrayList<AppData>()
                        if (snapshot.exists()) {
                            for(dataID : DataSnapshot in snapshot.children) {
                                Log.e("firebase" , dataID.key.toString())
                                val data = AppData("", "", "", "", 0, "", "")
                                val appData  = dataID
                                if(appData.child("id").exists()) {
                                    data.id = Objects.requireNonNull(appData.child("id").value.toString())
                                }
                                if(appData.child("appName").exists()) {
                                    data.appName = Objects.requireNonNull(appData.child("appName").value.toString())
                                }
                                if(appData.child("appSize").exists()) {
                                    data.appSize = Objects.requireNonNull(appData.child("appSize").value.toString())
                                }
                                if(appData.child("appStatus").exists()) {
                                    data.appStatus = Objects.requireNonNull(appData.child("appStatus").value.toString())
                                }
                                if(appData.child("appType").exists()) {
                                    data.appType = Objects.requireNonNull(appData.child("appType").value.toString().toInt())
                                }
                                if(appData.child("iconPath").exists()) {
                                    data.iconPath = Objects.requireNonNull(appData.child("iconPath").value.toString())
                                }
                                if(appData.child("bannerPath").exists()) {
                                    data.bannerPath = Objects.requireNonNull(appData.child("bannerPath").value.toString())
                                }

                                dataArray.add(data)
                            }
                        }
                        listener.onSuccess(dataArray)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        listener.onFailed(error.message)
                    }

                })
            } catch (e : Exception) {
                listener.onFailed(e.message)
            }
        }
    }
}

interface IFirebaseListener {
    fun onSuccess(data: ArrayList<AppData>)
    fun onFailed(message: String?)
}




