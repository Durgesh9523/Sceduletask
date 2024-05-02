package com.example.schedultask

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModel(application: Application) : AndroidViewModel(application) {
    private val requirementDetailDao: RequirementDao
    private val candidateDetailDao: CandidateDao

    val allRequirements: LiveData<List<Requirement>>
    val allCandidates: LiveData<List<Candidate>>

    init {
        val database = AppDatabase.getInstance(application)
        requirementDetailDao = database.requirementDetailDao()
        candidateDetailDao = database.candidateDetailDao()
        allRequirements = requirementDetailDao.getAllRequirements()
        allCandidates = candidateDetailDao.getAllCandidates()
    }

    fun getRequirementById(id: Int): LiveData<Requirement> {
        return requirementDetailDao.getRequirementById(id)
    }

    fun getCandidateById(id: Int): LiveData<Candidate> {
        return candidateDetailDao.getCandidateById(id)
    }

    // Function to delete candidate by ID
    fun deleteCandidateById(candidateId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            candidateDetailDao.deleteCandidateById(candidateId)
        }
    }
    fun deleteRequirementById(requirementId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            requirementDetailDao.deleteRequirementById(requirementId)
        }
    }
}
