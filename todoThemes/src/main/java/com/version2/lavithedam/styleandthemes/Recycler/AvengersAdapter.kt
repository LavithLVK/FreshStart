package com.version2.lavithedam.styleandthemes.Recycler

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper
import com.version2.lavithedam.styleandthemes.R

/**
 * Created by Lavith on 8/13/2018.
 */

class AvengersAdapter(avengerList: ArrayList<Avenger>) : RecyclerView.Adapter<AvengersAdapter.AvengerViewHolder>() {

    var avengerList: ArrayList<Avenger>

    init {
        this.avengerList = avengerList
    }

    class AvengerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView
        var description: TextView
        var image: ImageView

        init {
            title = view.findViewById(R.id.txtItemTitle)
            description = view.findViewById(R.id.txtItemDesc)
            image = view.findViewById(R.id.imgItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvengerViewHolder {
        var inflater = LayoutInflater.from(parent.context)
        var view = inflater.inflate(R.layout.list_item, parent, false)
//        view.minimumHeight = parent.measuredHeight / 4
        var avengerVH = AvengerViewHolder(view)
        return avengerVH
    }

    override fun getItemCount(): Int {
        return avengerList.size
    }

    override fun onBindViewHolder(holder: AvengerViewHolder, position: Int) {
        holder.title.setText(avengerList.get(position).title)
        holder.description.setText(avengerList.get(position).description)
        try {
            UrlImageViewHelper.setUrlDrawable(holder.image,avengerList.get(position).imageUri);
//            val url = URL(avengerList.get(position).imageUri)
//            val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
//            holder.image.setImageBitmap(bmp)
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }


}