package com.example.chatapp.model

class Pesan {
    private var isiPesan: String = ""
    private var pengirim: String = ""
    private var penerima: String = ""
    private var tanggal: String = ""

    constructor()

    constructor(isiPesan: String, pengirim: String, penerima: String, tanggal: String) {
        this.isiPesan = isiPesan
        this.pengirim = pengirim
        this.penerima = penerima
        this.tanggal = tanggal
    }

    fun getIsiPesan(): String {
        return isiPesan
    }

    fun setIsiPesan(isiPesan: String) {
        this.isiPesan = isiPesan
    }

    fun getPengirim(): String {
        return pengirim
    }

    fun setPengirim(pengirim: String) {
        this.pengirim = pengirim
    }

    fun getPenerima(): String {
        return penerima
    }

    fun setPenerima(penerima: String) {
        this.penerima = penerima
    }

    fun getTanggal(): String {
        return tanggal
    }

    fun setTanggal(tanggal: String) {
        this.tanggal = tanggal
    }
}
