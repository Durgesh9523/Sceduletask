package com.example.schedultask

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RequirementDao {
    @Query("SELECT * FROM requirement_detail")
    fun getAllRequirements(): LiveData<List<Requirement>> // Use LiveData instead of List

    @Query("SELECT * FROM requirement_detail WHERE id = :id")
    fun getRequirementById(id: Int):LiveData<Requirement>

    @Insert
    suspend fun insertRequirement(requirementDetail: Requirement)

    @Update
    suspend fun updateRequirement(requirement: Requirement)

    @Query("DELETE FROM requirement_detail WHERE id = :RequirementId")
    suspend fun deleteRequirementById(RequirementId: Int)
}