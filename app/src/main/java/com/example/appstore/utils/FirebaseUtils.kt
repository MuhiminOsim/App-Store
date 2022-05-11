package com.example.appstore.utils

import android.util.Log
import com.example.appstore.model.AppData
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
                                if(dataID.child("id").exists()) {
                                    data.id = Objects.requireNonNull(dataID.child("id").value.toString())
                                }
                                if(dataID.child("appName").exists()) {
                                    data.appName = Objects.requireNonNull(dataID.child("appName").value.toString())
                                }
                                if(dataID.child("appSize").exists()) {
                                    data.appSize = Objects.requireNonNull(dataID.child("appSize").value.toString())
                                }
                                if(dataID.child("appStatus").exists()) {
                                    data.appStatus = Objects.requireNonNull(dataID.child("appStatus").value.toString())
                                }
                                if(dataID.child("appType").exists()) {
                                    data.appType = Objects.requireNonNull(dataID.child("appType").value.toString().toInt())
                                }
                                if(dataID.child("iconPath").exists()) {
                                    data.iconPath = Objects.requireNonNull(dataID.child("iconPath").value.toString())
                                }
                                if(dataID.child("bannerPath").exists()) {
                                    data.bannerPath = Objects.requireNonNull(dataID.child("bannerPath").value.toString())
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




