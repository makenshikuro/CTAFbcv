package com.ubustus.ctafbcv.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ubustus.ctafbcv.app.preferences

class FirstActivity : AppCompatActivity() {


    var isFirstRun:Boolean = preferences.firstRun

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(isFirstRun){
            startActivity(Intent(this, MainActivity::class.java))
        }else{
            startActivity(Intent(this, PrincipalActivity::class.java))
        }
    }
}
