package com.example.android.politicalpreparedness.features.election

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.ElectionRepository
import com.example.android.politicalpreparedness.data.Result
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(private val electionRepository: ElectionRepository): ViewModel() {

    //TODO: Create live data val for upcoming elections
    val upcomingElections = MutableLiveData<List<Election>>()
    //TODO: Create live data val for saved elections
    val savedElections = MutableLiveData<List<Election>>()
    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database


    fun getUpcomingElections(){
        viewModelScope.launch {
            val electionList = electionRepository.getElectionList()

            if(electionList is Result.Success){
                upcomingElections.value = electionList.data
            }
        }
    }

    fun refresh(){
        viewModelScope.launch {
            electionRepository.refreshElectionList()
        }
        getUpcomingElections()
    }

    //TODO: Create functions to navigate to saved or upcoming election voter info

}