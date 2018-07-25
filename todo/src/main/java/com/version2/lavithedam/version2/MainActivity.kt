package com.version2.lavithedam.version2

import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.View

class MainActivity : AppCompatActivity() {

     lateinit var fabAdd: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateView(parent: View?, name: String?, context: Context?, attrs: AttributeSet?): View {
//        fabAdd = parent!!.findViewById(R.id.fab_add)
        fabAdd.setOnClickListener(View.OnClickListener {
        })
        return super.onCreateView(parent, name, context, attrs)

    }

}
