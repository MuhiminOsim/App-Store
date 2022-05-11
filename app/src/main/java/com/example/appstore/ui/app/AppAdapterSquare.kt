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
import com.example.appstore.model.IClickListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class AppAdapterSquare(
    var context: Context,
    var data: ArrayList<AppData>,
) : RecyclerView.Adapter<AppAdapterSquare.AppSquareViewHolder>() {

    var iClickListener : IClickListener? = null

    class AppSquareViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var appNameTextView: TextView
        var appSizeTextView: TextView
        var iconImageView: ImageView

        var appData : AppData? = null

        fun loadData(appData: AppData){
            this.appData = appData
            //todo load the views here
        }

        init {
            appNameTextView = itemView.findViewById(R.id.cell_app_name)
            appSizeTextView = itemView.findViewById(R.id.cell_app_size)
            iconImageView = itemView.findViewById(R.id.cell_image)
        }
//        fun loadData(appData: AppData){
//            appSizeTextView.text = appData.appSize
//        }
    }

    fun setOnClickListener(iClickListener: IClickListener){
        this.iClickListener = iClickListener
    }

    fun updateData(datax: ArrayList<AppData>){
        val size = data.size
        data.clear()
        data.addAll(datax)
        notifyItemRangeInserted(size , datax.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppSquareViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = layoutInflater.inflate(R.layout.app_cell_square, parent, false)

        return AppSquareViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppSquareViewHolder, position: Int) {
        val storageReference: StorageReference = FirebaseStorage.getInstance().reference.child(
            data[position].iconPath
        )
        val localFile = File.createTempFile("tempImage", "jpg")
        storageReference.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            holder.iconImageView.setImageBitmap(bitmap)
            holder.iconImageView.scaleType = ImageView.ScaleType.FIT_XY
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to retrieve the image", Toast.LENGTH_SHORT).show()
        }
        holder.loadData(data[position])

        holder.appNameTextView.text = data[position].appName
        holder.appSizeTextView.text = data[position].appSize
        holder.itemView.setOnClickListener{
            iClickListener?.clicked(data[position])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}