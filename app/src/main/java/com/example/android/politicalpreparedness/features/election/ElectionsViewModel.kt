package com.example.android.politicalpreparedness.features.election

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.data.ElectionRepository
import com.example.android.politicalpreparedness.data.Result
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch
import java.lang.Exception

class ElectionsViewModel(private val electionRepository: ElectionRepository): BaseViewModel() {

    val upcomingElections = MutableLiveData<List<Election>>()
    val savedElections = MutableLiveData<List<Election>>()


    private fun getUpcomingElections(){
        viewModelScope.launch {
            val electionList = electionRepository.getElectionList()

            if(electionList is Result.Success){
                upcomingElections.value = electionList.data
            }
        }
    }
    private fun getSavedElections(){
        viewModelScope.launch {
            val electList = electionRepository.getSavedElectionList()

            if(electList is Result.Success){
                savedElections.value = electList.data
            }
        }
    }

    fun refresh(){
        showLoading.value = true
        viewModelScope.launch {
            try{
                electionRepository.refreshElectionList()
            }catch(e:Exception){
                showErrorMessage.value= "Unable to retrieve Election details. ${e.message}"
            }
        }
        getUpcomingElections()
        getSavedElections()
        showLoading.value = false
    }
}