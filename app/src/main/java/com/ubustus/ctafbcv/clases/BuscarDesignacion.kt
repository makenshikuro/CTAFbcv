package com.ubustus.ctafbcv.clases

import android.os.AsyncTask
import java.io.BufferedReader
import java.io.InputStreamReader
import org.json.JSONObject
import org.json.JSONArray
import com.sun.org.apache.xml.internal.security.utils.IdResolver.getElementById
import java.util.Collections.replaceAll
import java.nio.file.Files.size
import org.jsoup.Jsoup
import android.R.attr.data
import android.util.Log
import jdk.nashorn.internal.runtime.PropertyDescriptor.GET
import java.io.IOException


class BuscarDesignacion() : AsyncTask<Void, Void, String>() {

    var urlLogin = "https://intrafeb.feb.es/OficinaWebArbitro/"

    override fun doInBackground(vararg params: Void?): String? {
        try {

            var resp: Connection.Response = Jsoup.connect(urlLogin)
                    .method(Connection.Method.GET)
                    .execute()

            val loginPage = resp.parse()
            val eventValidation = loginPage.select("input[name=__EVENTVALIDATION]").first()
            val viewState = loginPage.select("input[name=__VIEWSTATE]").first()

            resp = Jsoup.connect(urlLogin)
                    .data("__VIEWSTATE", viewState.attr("value"))
                    .data("__EVENTVALIDATION", eventValidation.attr("value"))
                    .data("usuarioTextBox", dni)
                    .data("passwordTextBox", passWeb)
                    .data("autenticarLinkButton", "")
                    .data("ambitoDropDownList", "2")
                    .method(Connection.Method.POST)
                    .execute()

            val doc = Jsoup.connect("https://intrafeb.feb.es/OficinaWebArbitro/Paginas/Designaciones.aspx")
                    .cookies(resp.cookies())
                    .get()

            val rows = doc.select("#designacionesDataGrid tr")
            npartidos = rows.size() - 2

            val update = doc.select("#fechaPubTextBox")
            lastupdate = update.attr("value")
            //lastupdate = lastupdate.replaceAll("\\.","-");
            //Log.d("Fecha","Date: "+lastupdate);
            try {
                dateLastWeek = format.parse(lastupdate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            val competiciones = db.competicionDAO().getAll()


            for (i in 0 until npartidos) {

                val fechaLabel = doc.getElementById("designacionesDataGrid_fechaLabel_$i")
                val fecha = fechaLabel.html().replaceAll("<b>Día:</b>", "").replaceAll("<br><b>Hora:</b>", "").replaceAll("\\(([^)]+)\\)", "")
                val partidoLabel = doc.getElementById("designacionesDataGrid_partidoLabel_$i")
                var encuentro = partidoLabel.html().replace(" ", "")
                var codigo: String
                var categoria: String
                val localidadLabel = doc.getElementById("designacionesDataGrid_campoLabel_$i")
                var localidad = localidadLabel.html().replace(" ", "")
                val estadoLabel = doc.getElementById("designacionesDataGrid_aceptadaLabel_$i")
                val estado = estadoLabel.html().replace(" ", "")
                var lines = encuentro.split("<b>".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                encuentro = lines[1].replace("</b><br>".toRegex(), "").replace("\\(([^)]+)\\)".toRegex(), "")
                //System.out.println("encuentro"+i+" : "+ encuentro);
                codigo = lines[1].replace("</b><br>".toRegex(), "")
                categoria = lines[3].replace("Cat.:</b>".toRegex(), "").replace("<br>".toRegex(), "")

                lines = localidad.split("<b>".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                localidad = lines[3].replace("Localidad:</b>".toRegex(), "").replace("<br>".toRegex(), "")

                val Mcod = Pattern.compile("\\(([^)]+)\\)").matcher(codigo)
                while (Mcod.find()) {
                    codigo = Mcod.group(1)
                }




                try {
                    datePartido = formatter.parse(fecha)
                } catch (e: ParseException) {
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

                var cuota = 0f

                /* Extraemos la cuota del partido segun el Rol */
                for (c in competiciones) {
                    if (c.getId().equals(cat)) {
                        if (rol.equals("ARB")) {
                            cuota = c.getCuotaARB()
                        } else {
                            cuota = c.getCuotaOM()
                        }
                        categoria = c.getNombre()
                    }
                }


                val urlGoogleAPI = "https://maps.googleapis.com/maps/api/distancematrix/json?language=es&origins=$origen&destinations=$localidad&avoid=tolls"

                try {
                    val json = JSONObject(readUrl(urlGoogleAPI))
                    //String title = (String) json.get("rows");
                    val matrixArray = json.getJSONArray("rows")
                    if (json != null && json.get("status") == "OK") {
                        for (indice in 0 until matrixArray.length()) {
                            val elementArray = matrixArray.getJSONObject(indice).getJSONArray("elements")
                            for (indice2 in 0 until elementArray.length()) {
                                val distanceObj = elementArray.getJSONObject(indice2).getJSONObject("distance")
                                val distance = distanceObj.getString("value")
                                distancia = Integer.valueOf(distance) / 1000
                                val durationObj = elementArray.getJSONObject(indice2).getJSONObject("duration")
                                val duration = durationObj.getString("value")
                                tiempo = Integer.valueOf(duration) / 60
                                Log.d("JSON", "" + distancia)
                                Log.d("JSON2", "" + tiempo)
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                desplazamiento = eurosxkm * distancia
                if (desplazamiento * 2 < 4.0f) {
                    desplazamiento = 4.0f
                }


                total = total + cuota + desplazamiento

                Log.d("CATEGOR", "Cate: $cat")
                Log.d("CUOTA", "Cuota: $cuota")
                Log.d("DES", "Desp: $desplazamiento")


                //puesto por defecto (hay que recoger este valor con jsoup)

                val p = Partido(codigo, encuentro, datePartido, categoria, localidad, cuota, distancia, tiempo, desplazamiento, total, estado)
                al.add(p)

            }
            Log.d("TOTAL", "Total: $total")

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    override fun onPreExecute() {
        super.onPreExecute()
        // ...
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        listAdapter.notifyDataSetChanged();
        tvNumPartidos.setText("Partidos: "+npartidos);
        tvLastUpdate.setText("Semana: "+lastupdate);
        tvPrevisto.setText("Previsto: "+total+"€");
    }

    @Throws(Exception::class)
    private fun readUrl(urlString: String): String {
        var reader: BufferedReader? = null
        try {
            val url = URL(urlString)
            reader = BufferedReader(InputStreamReader(url.openStream()))
            val buffer = StringBuffer()
            var read: Int
            val chars = CharArray(1024)
            while ((read = reader!!.read(chars)) != -1)
                buffer.append(chars, 0, read)

            return buffer.toString()
        } finally {
            if (reader != null)
                reader!!.close()
        }
    }
}