package com.example.schedultask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.room.Room

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar:Toolbar=findViewById(R.id.toolbar1)
        val req:LinearLayout=findViewById(R.id.req);
        val can:LinearLayout=findViewById(R.id.can);
        supportFragmentManager.beginTransaction()
            .replace(R.id.frag, RequirementFragment()).commit()
        req.setOnClickListener {
            supportFragmentManager.beginTransaction()
            .replace(R.id.frag, RequirementFragment()).commit()
        }
        can.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frag, Candidate_Fragment()).commit()
        }
    }
}