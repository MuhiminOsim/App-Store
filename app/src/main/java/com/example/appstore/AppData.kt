package com.example.appstore

import java.io.Serializable

class AppData(
    var id: String,
    var appName: String,
    var appSize: String,
    var appStatus: String,
    var appType: Int,
    var iconPath: String,
    var bannerPath: String
) : Serializable{
//    var _id : String = id
//        set(value) {
//            field = value
//        }
//
//    var _appName : String = appName
//        set(value) {
//            field = value
//        }
//
//    var _appStatus : String = appStatus
//        set(value) {
//            field = value
//        }
//
//    var _appType : Int = appType
//        set(value) {
//            field = value
//        }
//
//    var _iconPath : String = iconPath
//        set(value) {
//            field = value
//        }
//
//    var _bannerPath : String = bannerPath
//        set(value) {
//            field = value
//        }
}