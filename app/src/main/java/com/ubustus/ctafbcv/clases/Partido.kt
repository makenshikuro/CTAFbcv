package com.ubustus.ctafbcv.clases

import java.util.*


data class Partido (var codigo:String,
                    var encuentro: String,
                    var fecha: Date,
                    var categoria:String,
                    var cuota: Float,
                    var origen: String,
                    var distancia: Float,
                    var localidad: String,
                    var tiempo: Int,
                    var desplazamiento: Float,
                    var arbPri: String,
                    var arbAux: String,
                    var arbAux2: String,
                    var omActa: String,
                    var omCrono: String,
                    var om24: String,
                    var tipoPartido: Int)