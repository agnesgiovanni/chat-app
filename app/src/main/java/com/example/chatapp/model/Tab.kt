package com.example.chatapp.model

class Tab {
    private var ID: String = ""
    private var noTelp: String = ""
    private var nama: String = ""
    private var keterangan: String = ""
    private var tanggal: String = ""

    constructor(ID: String, noTelp: String, nama: String, keterangan: String, tanggal: String) {
        this.ID = ID
        this.noTelp = noTelp
        this.nama = nama
        this.keterangan = keterangan
        this.tanggal = tanggal
    }

    fun getID(): String {
        return ID
    }

    fun setID(ID: String) {
        this.ID = ID
    }

    fun getNoTelp(): String {
        return noTelp
    }

    fun setNoTelp(noTelp: String) {
        this.noTelp = noTelp
    }

    fun getNama(): String {
        return nama
    }

    fun setNama(nama: String) {
        this.nama = nama
    }

    fun getKeterangan(): String {
        return keterangan
    }

    fun setKeterangan(keterangan: String) {
        this.keterangan = keterangan
    }

    fun getTanggal(): String {
        return tanggal
    }

    fun setTanggal(tanggal: String) {
        this.tanggal = tanggal
    }
}
