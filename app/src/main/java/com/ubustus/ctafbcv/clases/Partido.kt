package com.ubustus.ctafbcv.clases

data class Partido (var codigo:String,
                    var encuentro: String,
                    var fecha: String,
                    var categoria:String,
                    var cuota: Float,
                    var distancia: Float,
                    var localidad: String,
                    var tiempo: Int,
                    var arbPri: String,
                    var arbAux: String,
                    var arbAux2: String,
                    var omActa: String,
                    var omCrono: String,
                    var om24: String)