package com.example.appstore.ui.games

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

class GameAdapterBigger(
    var context : Context,
    var data : ArrayList<AppData>,
) : RecyclerView.Adapter<GameAdapterBigger.GameBiggerViewHolder>() {
    var iClickListener : IClickListener? = null

    class GameBiggerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var appNameTextView : TextView
        var iconImageView: ImageView
        var bannerImageView: ImageView
        var appStatusTextView: TextView
        var addData : AppData? = null

        fun loadData(appData: AppData) {
            this.addData = addData
            //to do
        }

        init {
            appNameTextView = itemView.findViewById(R.id.cell_app_name)
            appStatusTextView = itemView.findViewById(R.id.cell_app_status)
            iconImageView = itemView.findViewById(R.id.cell_image)
            bannerImageView = itemView.findViewById(R.id.cell_bigger_image)
        }
    }

    fun setOnClickListener(iClickListener: IClickListener) {
        this.iClickListener = iClickListener
    }

    fun updateData(datax: ArrayList<AppData>){
        val size = data.size
        data.clear()
        data.addAll(datax)
        notifyItemRangeInserted(size , datax.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameBiggerViewHolder {
        val layoutInflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = layoutInflater.inflate(R.layout.app_cell_bigger, parent, false)

        return GameBiggerViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameBiggerViewHolder, position: Int) {
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

        val storageReference2 = FirebaseStorage.getInstance().reference.child(
            data[position].bannerPath)
        val localFile2 = File.createTempFile("tempImage2", "jpg")
        storageReference2.getFile(localFile2).addOnSuccessListener {
            val bitmap2 = BitmapFactory.decodeFile(localFile.absolutePath)
            holder.bannerImageView.setImageBitmap(bitmap2)
            holder.bannerImageView.scaleType = ImageView.ScaleType.FIT_XY
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to retrieve the image", Toast.LENGTH_SHORT).show()
        }

        holder.appNameTextView.text = data[position].appName
        holder.appStatusTextView.text = data[position].appStatus
        holder.loadData(data[position])

        holder.itemView.setOnClickListener {
            iClickListener?.clicked(data[position])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}