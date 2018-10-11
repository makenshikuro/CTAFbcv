package com.ubustus.ctafbcv.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ubustus.ctafbcv.R

class PrincipalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)


    }
    private fun goToDesignacionesPage()= startActivity(Intent(this, PrincipalActivity::class.java))

}
