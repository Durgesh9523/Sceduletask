package com.example.schedultask

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "candidate_detail")
data class Candidate(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var candidateName: String,
    var candidateSpecial:String,
    var candidateEmail:String,
    var candidatePhone:Int,
    var website:String,
    var isInterseted:String,
    var discussionDetails: String,
)
