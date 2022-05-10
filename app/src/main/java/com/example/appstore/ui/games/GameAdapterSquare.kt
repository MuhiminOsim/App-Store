package com.example.appstore.ui.games

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.appstore.AppData
import com.example.appstore.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class GameAdapterSquare(
    var context : Context,
    var data : ArrayList<AppData>,
) : RecyclerView.Adapter<GameAdapterSquare.GameSquareViewHolder>() {

    class GameSquareViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var appNameTextView : TextView
        lateinit var appSizeTextView: TextView
        lateinit var iconImageView: ImageView

        init {
            appNameTextView = itemView.findViewById(R.id.cell_app_name)
            appSizeTextView = itemView.findViewById(R.id.cell_app_size)
            iconImageView = itemView.findViewById(R.id.cell_image)
        }
    }

    fun updateData(datax: ArrayList<AppData>){
        val size = data.size
        data.addAll(datax)
        notifyItemRangeInserted(size , datax.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameSquareViewHolder {
        val layoutInflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = layoutInflater.inflate(R.layout.app_cell_square, parent, false)

        return GameSquareViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameSquareViewHolder, position: Int) {
        val storageReference : StorageReference = FirebaseStorage.getInstance().reference.child(
            data!![position].iconPath)
        val localFile = File.createTempFile("tempImage", "jpg")
        storageReference.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            holder.iconImageView.setImageBitmap(bitmap)
            holder.iconImageView.scaleType = ImageView.ScaleType.FIT_XY
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to retrieve the image", Toast.LENGTH_SHORT).show()
        }

        holder.appNameTextView.text = data!![position].appName
        holder.appSizeTextView.text = data!![position].appSize
    }

    override fun getItemCount(): Int {
        return data!!.size
    }
}