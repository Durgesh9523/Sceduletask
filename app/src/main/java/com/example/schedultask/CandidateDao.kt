package com.example.schedultask

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CandidateDao {
    @Query("SELECT * FROM candidate_detail")
    fun getAllCandidates(): LiveData<List<Candidate>> // Use LiveData instead of List

    @Query("SELECT * FROM candidate_detail WHERE id = :id")
    fun getCandidateById(id: Int):LiveData<Candidate>

    @Update
    suspend fun updateCandidate(candidate: Candidate)

    @Query("DELETE FROM candidate_detail WHERE id = :id")
    suspend fun deleteCandidate(id: Int)
    @Insert
    suspend fun insertCandidate(candidateDetail: Candidate)

    @Query("DELETE FROM candidate_detail WHERE id = :candidateId")
    suspend fun deleteCandidateById(candidateId: Int)
}