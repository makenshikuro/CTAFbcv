package com.ubustus.ctafbcv.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.ubustus.ctafbcv.R
import com.ubustus.ctafbcv.app.Globals
import com.ubustus.ctafbcv.app.preferences
import com.ubustus.ctafbcv.clases.Partido
import org.jetbrains.anko.doAsync
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.regex.Pattern




class DesignacionesActivity : AppCompatActivity(){


    //val g = application as Globals

    var g = Globals()
    //var strGlobalVar = mApp.globalVar
    //var eurosxkm = g.eurosxkm
    //var minimo = g.minimo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_designaciones)
        Toast.makeText(this, "designaciones", Toast.LENGTH_SHORT).show()
        //Log.i("TAG", "yei!")
        designacion()
        //Log.i("TAG", "size: ${da.size}")
    }

    fun designacion() {

        doAsync {
            var result = buscaDesignacion()
        }

    }


    private fun buscaDesignacion(): MutableList<Partido> {
        var partidos:MutableList<Partido> = mutableListOf<Partido>()

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
            val npartidos:Int = rows.size - 2

            val updated_at = doc.select("#fechaPubTextBox").attr("value")



            for (i in 0..npartidos){
                var fecha = doc.getElementById("designacionesDataGrid_fechaLabel_$i")
                        .html()
                        .replace("<b>Día:</b>", "")
                        .replace("<br><b>Hora:</b>", "")
                        .replace("\\(([^)]+)\\) ".toRegex(), "")

                var encuentro = doc.getElementById("designacionesDataGrid_partidoLabel_$i")
                        .html()

                var localidad = doc.getElementById("designacionesDataGrid_campoLabel_$i")
                        .html()
                        .replace(" ", "")
                var estado = doc.getElementById("designacionesDataGrid_aceptadaLabel_$i")
                        .html()
                        .replace(" ", "")
                var designacion = doc.getElementById("designacionesDataGrid_designacionLabel_$i")
                        .html()


                var lines = encuentro
                        .split("<b>".toRegex())
                        .dropLastWhile {
                            it.isEmpty()
                        }.toTypedArray()

                encuentro = lines[1]
                        .replace("</b><br>".toRegex(), "")
                        .replace("\\(([^)]+)\\)".toRegex(), "")

                var codigo = lines[1]
                        .replace("</b><br>".toRegex(), "")

                var categoria = lines[3]
                        .replace("Cat.:</b>".toRegex(), "")
                        .replace("<br>".toRegex(), "")

                lines = localidad
                        .split("<b>".toRegex())
                        .dropLastWhile {
                            it.isEmpty()
                        }.toTypedArray()


                localidad = lines[3]
                        .replace("Localidad:</b>".toRegex(), "")
                        .replace("<br>".toRegex(), "")

                localidad = Normalizer.normalize(localidad, Normalizer.Form.NFD)
                        .replace("\\p{InCombiningDiacriticalMarks}+", "");
                //localidad = Normalizer.normalize(localidad, Normalizer.Form.NFD);
                //localidad = localidad.replace("\\p{M}", "")


                var Mcod = Pattern.compile("\\(([^)]+)\\)").matcher(codigo)
                while (Mcod.find()) {
                    codigo = Mcod.group(1)
                }

                lines = designacion
                        .split("<br>".toRegex())
                        .dropLastWhile {
                            it.isEmpty()
                        }.toTypedArray()

                val arbPrin = lines[0]
                        .replace("<\\w>(.*?)<\\/\\w>".toRegex(), "")
                        .replace("\\(([^)]+)?\\)".toRegex(), "")

                val arbAux = lines[1]
                        .replace("<\\w>(.*?)<\\/\\w>".toRegex(), "")
                        .replace("\\(([^)]+)?\\)".toRegex(), "")

                val arbAux2 = lines[2]
                        .replace("<\\w>(.*?)<\\/\\w>".toRegex(), "")
                        .replace("\\(([^)]+)?\\)".toRegex(), "")

                val omActa = lines[3]
                        .replace("<\\w>(.*?)<\\/\\w>".toRegex(), "")
                        .replace("\\(([^)]+)?\\)".toRegex(), "")

                val omCrono = lines[4]
                        .replace("<\\w>(.*?)<\\/\\w>".toRegex(), "")
                        .replace("\\(([^)]+)?\\)".toRegex(), "")

                val om24 = lines[5]
                        .replace("<\\w>(.*?)<\\/\\w>".toRegex(), "")
                        .replace("\\(([^)]+)?\\)".toRegex(), "")


                val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
                val format = SimpleDateFormat("dd.MM.yyyy")
                val formatoSQL = SimpleDateFormat("yyyy-MM-dd")
                try {
                    fecha = formatter.parse(fecha).toString()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                var cat = ""

                if (categoria.contains("Ben")) {
                    cat = "Benjamin"
                } else if (categoria.contains("Ale")) {
                    cat = "Alevin"
                } else if (categoria.contains("Inf")) {
                    cat = "Inf"
                } else if (categoria.contains("Cad")) {
                    cat = "Cad"
                } else if (categoria.contains("Jr")) {
                    cat = "Jr"
                } else if (categoria.contains("Sr")) {
                    cat = "Sr"
                }

                if (categoria.contains("Masc")) {
                    cat = cat + "Masc"
                } else if (categoria.contains("Fem")) {
                    cat = cat + "Fem"
                    if (categoria.contains("Nivel 2") || categoria.contains("Nivel2")) {
                        cat = cat + "2"
                    } else if (categoria.contains("Nivel 1") || categoria.contains("Nivel1")) {
                        cat = cat + "1"
                    }
                }

                if (categoria.contains("IR")) {
                    cat = cat + "IR"
                    if (categoria.contains("Nivel 3") || categoria.contains("Nivel3")) {
                        cat = cat + "3"
                    } else if (categoria.contains("Nivel 2") || categoria.contains("Nivel2")) {
                        cat = cat + "2"
                    } else if (categoria.contains("Nivel 1") || categoria.contains("Nivel1")) {
                        cat = cat + "1"
                    }else{
                        cat = "-"
                    }
                } else if (categoria.contains("Ayto") || categoria.contains("Cons")) {
                    cat = cat + "Ayto"
                }

                if (categoria.contains("Zonal")) {
                    if (categoria.contains("2ª")) {
                        cat = cat + "2"
                    } else if (categoria.contains("1ª")) {
                        cat = cat + "1"
                    } else {
                        cat = cat + "Zonal"
                    }

                } else if (categoria.contains("Pref")) {
                    cat = cat + "Pref"
                } else if (categoria.contains("Aut")) {
                    cat = cat + "Aut"
                } else if (categoria.contains("Divi")) {
                    cat = cat + "Nac"
                } else if (categoria.contains("EBA")) {
                    cat = cat + "EBA"
                } else if (categoria.contains("PLATA")) {
                    cat = cat + "Plata"
                } else if (categoria.contains("L.F.-2")) {
                    cat = cat + "LF2"
                } else if (categoria.contains("Conso")) {
                    cat = cat + "Consorcio"
                }

                var origen = preferences.origen

                //val urlGoogleAPI = "https://maps.googleapis.com/maps/api/distancematrix/json?language=es&origins=$origen&destinations=$localidad&avoid=tolls&key=AIzaSyAvdO229uC6xMjLJB1WiQTznncBEHotoUw"
                val urlGoogleAPI = "http://open.mapquestapi.com/directions/v2/route?key=XNtAJb6ihLTY6BQOWEpTemgv8on7DoIT&from=$origen&to=$localidad"

                Log.i("TAG", "partidos - $i")
                Log.i("TAG", "fecha - $fecha")
                Log.i("TAG", "codigo - $codigo")
                Log.i("TAG", "encuentro - $encuentro")
                Log.i("TAG", "categoria - $categoria")
                Log.i("TAG", "categoriaSQL - $cat")
                Log.i("TAG", "estado - $estado")
                Log.i("TAG", "arbP - $arbPrin")
                Log.i("TAG", "arbA - $arbAux")
                Log.i("TAG", "arbA2 - $arbAux2")
                Log.i("TAG", "acta - $omActa")
                Log.i("TAG", "crono - $omCrono")
                Log.i("TAG", "24s - $om24")
                Log.i("TAG", "localidad - $localidad")
                Log.i("TAG", "Response - $urlGoogleAPI")

                getDistanciaAPI(i, urlGoogleAPI)

                var partido = Partido(codigo,encuentro,fecha,categoria,0.0f, 0.0f,localidad,0, arbPrin,arbAux,arbAux2,omActa,omCrono,om24)

                partidos.add(partido)
                //Log.i("TAG", "data$i - $data")
            }
            //Log.i("TAG", "partidos - $npartidos")

        }catch (e:Exception){

        }

        return partidos
    }

    fun getDistanciaAPI(pos: Int, url:String){
        val queue = Volley.newRequestQueue(this)


        // Request a string response from the provided URL.
        val stringRequest = StringRequest(Request.Method.GET, url,
                Response.Listener<String> { response ->
                    Log.i("TAG", "Response$pos - $response")
                    calculaDatos(pos,response)
                },
                Response.ErrorListener { Log.i("TAG", "Error Response") })

        // Add the request to the RequestQueue.
        queue.add(stringRequest)

        //Log.i("TAG", "ResponseOut - $d")
    }
    fun calculaDatos(pos:Int, response:String){
        Log.i("TAG", "ruta1 - $response")
        val jObject = JSONObject(response)
        val jObject2 = jObject.getJSONObject("route")
        //val aJsonInteger = jObject2.getString("distance")
        val aFloat = jObject2.getInt("time")
        //val gson = GsonBuilder().setPrettyPrinting().create() // for pretty print feature

        //Log.i("TAG", "ruta2 - $aJsonInteger")
        Log.i("TAG", "ruta2F - $aFloat")
    }
}
