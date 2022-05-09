package com.example.appstore

import java.io.Serializable

class AppData(
    var id: String,
    var appName: String,
    var appSize: String,
    var appType: Int,
    var iconPath: String,
    var bannerPath: String
) : Serializable{

}