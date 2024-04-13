package br.edu.infnet.aula5.projetokotlin.Domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo")
data class Tarefa (
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    @ColumnInfo(name = "title") val title : String? = null,
    @ColumnInfo(name = "description") val note : String? = null,
    @ColumnInfo(name = "status") var status : String? = null
) : java.io.Serializable