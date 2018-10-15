package com.ubustus.ctafbcv.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.ubustus.ctafbcv.R
import com.ubustus.ctafbcv.app.Globals
import com.ubustus.ctafbcv.app.preferences
import com.ubustus.ctafbcv.clases.Partido
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.jsoup.Connection
import org.jsoup.Jsoup


class DesignacionesActivity : AppCompatActivity() {

    val g = application as Globals
    //var eurosxkm = g.eurosxkm
    //var minimo = g.minimo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_designaciones)
        Toast.makeText(this, "designaciones", Toast.LENGTH_SHORT).show()
        //designacion()
    }

    fun designacion() {

        doAsync {
            var result = buscaDesignacion()
            uiThread {
                //toast(result)
            }
        }


    }

    private fun buscaDesignacion(): List<Partido> {

        Jsoup.connect("https://www.google.co.in/search?q=this+is+a+test").get().run {
            //2. Parses and scrapes the HTML response
            select("div.rc").forEachIndexed { index, element ->
                val titleAnchor = element.select("h3 a")
                val title = titleAnchor.text()
                val url = titleAnchor.attr("href")
                //3. Dumping Search Index, Title and URL on the stdout.
                println("$index. $title ($url)")
            }
        }


        try {

            var con = Jsoup.connect(g.urlOficina)
                    .method(Connection.Method.GET)
                    .execute()

            val loginPage = con.parse()
            val eventValidation = loginPage.select("input[name=__EVENTVALIDATION]").first()
            val viewState = loginPage.select("input[name=__VIEWSTATE]").first()

            con = Jsoup.connect(g.urlOficina)
                    .data("__VIEWSTATE", viewState.attr("value"))
                    .data("__EVENTVALIDATION", eventValidation.attr("value"))
                    .data("usuarioTextBox", preferences.dni)
                    .data("passwordTextBox", preferences.passWeb)
                    .data("autenticarLinkButton", "")
                    .data("ambitoDropDownList", "2")
                    .method(Connection.Method.POST)
                    .execute()

            val doc = Jsoup.connect(g.urlDesignaciones)
                    .cookies(con.cookies())
                    .get()

            val rows = doc.select("#designacionesDataGrid tr")
            var npartidos = rows.size - 2

            Toast.makeText(this, "partidos - $npartidos", Toast.LENGTH_SHORT).show()
        }catch (e:Exception){

        }

        return emptyList()
    }
}
