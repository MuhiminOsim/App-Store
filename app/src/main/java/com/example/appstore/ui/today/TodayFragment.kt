package com.example.appstore.ui.today

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.appstore.model.AppData
import com.example.appstore.R
import com.example.appstore.databinding.FragmentTodayBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.IOException


class TodayFragment : Fragment() {
    private val REQ_CODE = 1
    private var _binding: FragmentTodayBinding? = null

    private var appName: String? = null
    private var appSize: String? = null
    private var appStatus: String? = null
    private var appType: Int = 0
    private var bitmap: Bitmap? = null
    private var imageURI: Uri? = null
    private var iconPath: String? = null
    private var bannerPath: String? = null


    private lateinit var databaseReference : DatabaseReference

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(TodayViewModel::class.java)

        _binding = FragmentTodayBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onStart() {
        super.onStart()
        appName = requireView().findViewById<EditText>(R.id.edit_app_name).text.toString()
        appSize = requireView().findViewById<EditText>(R.id.edit_app_size).text.toString()
        appStatus = requireView().findViewById<EditText>(R.id.edit_app_status).text.toString()
        appType = 0
        if (requireView().findViewById<CheckBox>(R.id.is_game).isChecked) {
            appType = 1
        }
        println(appName)

        val appIconButton = requireView().findViewById<Button>(R.id.app_icon)
        appIconButton.setOnClickListener {
            bitmap = null
            imageURI = null
            flag = true
            showImageChooser()
        }

        val appBannerButton = requireView().findViewById<Button>(R.id.app_banner)
        appBannerButton.setOnClickListener {
            bitmap = null
            imageURI = null
            flag = false
            showImageChooser()
        }

        val addAppButton = requireView().findViewById<Button>(R.id.add_app)
        addAppButton.setOnClickListener {
            addApp()
        }
    }

    private fun addApp() {
        if (appName != "" && appSize != "" && iconPath != "" &&
            bannerPath != "" && appStatus != "" &&
            (requireView().findViewById<CheckBox>(R.id.not_game).isChecked != requireView().findViewById<CheckBox>(R.id.is_game).isChecked)) {
            val db: FirebaseDatabase = FirebaseDatabase.getInstance()
            databaseReference = db.getReference("AppData")

            val id: String? = databaseReference.push().key
            val appData = AppData(id!!, appName!!, appSize!!, appStatus!!, appType, iconPath!!, bannerPath!!)
            databaseReference.child(id!!).setValue(appData).addOnSuccessListener {
                Toast.makeText(requireContext(), "Successfully Added", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Unsuccessful!", Toast.LENGTH_SHORT).show()
            }
        }
        else {
            Toast.makeText(requireContext(), "Enter Valid Data!", Toast.LENGTH_SHORT).show()
        }
    }
    var flag = false


    private fun showImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        try {
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQ_CODE)
        } catch (e: Exception) {

        }
    }

    private fun getImageUri(context: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 60, bytes)
        val path = MediaStore.Images.Media.insertImage(
            context.contentResolver,
            inImage,
            "Title",
            null
        )

        println(path)
        return Uri.parse(path)
    }

    override fun onActivityResult(req_code: Int, res_code: Int, data: Intent?) {
        super.onActivityResult(req_code, res_code, data)
        if (req_code == REQ_CODE && res_code == RESULT_OK && null != data) {
            val imageUrix = data.data
            try {
                bitmap =
                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUrix)
                        ?: return
                val result = Bitmap.createScaledBitmap(
                    bitmap!!,
                    bitmap!!.width / 2,
                    bitmap!!.height / 2,
                    false
                )
                imageURI = getImageUri(requireContext(), result)

                val loc = "AppStore"
                uploadImageInFirebaseStorage(loc)
            } catch (e: IOException) {
                e.printStackTrace()
                // toaster(e.message)
            }
        }
    }


    private fun uploadImageInFirebaseStorage(loc: String) {
        val alertDialog = AlertDialog.Builder(requireContext()).setCancelable(false).setTitle("Uploading...").create()
        alertDialog.show()
        val profileImageRef = FirebaseStorage.getInstance()
            .getReference(loc)
        if (imageURI != null) {
            val curTime = System.currentTimeMillis().toString()
            val photoStorageReference =
                profileImageRef.child("$curTime.jpg")

            photoStorageReference.putFile(imageURI!!).addOnCompleteListener { task ->
                alertDialog.dismiss()
                if (task.isSuccessful) {
                    val imageURL = "AppStore/$curTime.jpg"
                    if(flag) {
                        iconPath = imageURL
                    }
                    else {
                        bannerPath = imageURL
                    }
                    println("uploaded image $curTime")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}