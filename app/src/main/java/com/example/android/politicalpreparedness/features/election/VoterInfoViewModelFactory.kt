package com.example.android.politicalpreparedness.features.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.data.ElectionRepository

//TODO: Create Factory to generate VoterInfoViewModel with provided election datasource
class VoterInfoViewModelFactory(private val electionRepository: ElectionRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(VoterInfoViewModel::class.java)){
            return VoterInfoViewModel(electionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }

}