package com.example.android.politicalpreparedness.features.election

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.data.ElectionRepository
import com.example.android.politicalpreparedness.data.Result
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch
import timber.log.Timber

//TODO: Construct ViewModel and provide election datasource
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
        viewModelScope.launch {
            electionRepository.refreshElectionList()
        }
        getUpcomingElections()
        getSavedElections()
    }

    //TODO: Create functions to navigate to saved or upcoming election voter info



}