package com.example.appstore.ui.app

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.appstore.model.AppData
import com.example.appstore.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class AppAdapterHorizontal(
    var context : Context,
    var data : ArrayList<AppData>,
) : RecyclerView.Adapter<AppAdapterHorizontal.AppHorizontalViewHolder>() {

    class AppHorizontalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var appNameTextView : TextView
        var appSizeTextView: TextView
        var iconImageView: ImageView

        init {
            appNameTextView = itemView.findViewById(R.id.cell_app_name)
            appSizeTextView = itemView.findViewById(R.id.cell_app_size)
            iconImageView = itemView.findViewById(R.id.cell_image)
        }
    }
    fun updateData(datax: ArrayList<AppData>){
        val size = data.size
        data.clear()
        data.addAll(datax)
        notifyItemRangeInserted(size , datax.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppHorizontalViewHolder {
        val layoutInflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = layoutInflater.inflate(R.layout.app_cell_horizontal, parent, false)

        return AppHorizontalViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppHorizontalViewHolder, position: Int) {
        val storageReference : StorageReference = FirebaseStorage.getInstance().reference.child(
            data[position].iconPath)
        val localFile = File.createTempFile("tempImage", "jpg")
        storageReference.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            holder.iconImageView.setImageBitmap(bitmap)
            holder.iconImageView.scaleType = ImageView.ScaleType.FIT_XY
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to retrieve the image", Toast.LENGTH_SHORT).show()
        }

        holder.appNameTextView.text = data[position].appName
        holder.appSizeTextView.text = data[position].appSize
    }

    override fun getItemCount(): Int {
        return data.size
    }
}