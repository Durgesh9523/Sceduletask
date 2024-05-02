package com.example.schedultask

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "requirement_detail")
data class Requirement(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var requirementName: String,
    var requirementSpecial:String,
    var requirementd: String,
    var businessRequ:String,
    var fileUrl:String,
)
