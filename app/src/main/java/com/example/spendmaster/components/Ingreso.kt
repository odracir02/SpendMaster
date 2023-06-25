package com.example.spendmaster.components

data class Ingreso(
    var Id : String,
    var category : String,
    var description : String,
    var isIncome : String,
    var title : String,
    var value : String,
    var nombreGrupo : String,
    var usuario : String

){
    constructor():this("","","","","","","","")
}
