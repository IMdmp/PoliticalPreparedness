package com.example.android.politicalpreparedness.features.election

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.data.ElectionRepository
import com.example.android.politicalpreparedness.data.Result
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.launch
import timber.log.Timber

class VoterInfoViewModel(private val dataSource: ElectionRepository,
                         private val voterInfoFragmentArgs: VoterInfoFragmentArgs) : ViewModel() {

    var voterInfoData = MutableLiveData<VoterInfoResponse>()
    var currentElection = MutableLiveData<Election>()
    var electionIsFavorite = MutableLiveData<Boolean>()
    private val _eventOpenUrl = MutableLiveData<String>()
    val eventOpenUrl: LiveData<String>
        get() = _eventOpenUrl

    init {
        viewModelScope.launch {
            val curElect = dataSource.getElection(voterInfoFragmentArgs.argElectionId)
            curElect?.let {
                currentElection.value = curElect
                electionIsFavorite.value = curElect.isFavorite
            }
        }
        _eventOpenUrl.value=""
    }

    fun getVoterInfo() {
        viewModelScope.launch {
            val voterResult = dataSource.getVoterInfo(voterInfoFragmentArgs.argDivision.state
                    .plus(" ")
                    .plus(voterInfoFragmentArgs.argDivision.country),
                    voterInfoFragmentArgs.argElectionId)


            when (voterResult) {
                is Result.Success -> {
                    voterInfoData.value = voterResult.data
                }
                is Result.Error -> {

                }
                is Result.Loading -> {
                }
            }
        }

    }

    //TODO: Add live data to hold voter info

    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs


    fun votingLocationSelected() {
        val voteData = voterInfoData.value
        val voteLocationUrl = voteData?.state?.elementAtOrNull(0)?.electionAdministrationBody?.votingLocationFinderUrl
                ?: ""

        if (voteLocationUrl.isNotEmpty()) {
            //trigger event.
            _eventOpenUrl.value = voteLocationUrl
        }
    }


    fun ballotInfoSelected() {
        val voteData = voterInfoData.value

        val ballotInfoUrl = voteData?.state?.elementAtOrNull(0)?.electionAdministrationBody?.ballotInfoUrl
                ?: ""

        if (ballotInfoUrl.isNotEmpty()) {
            //trigger event.
            _eventOpenUrl.value = ballotInfoUrl

        }
    }

    fun onOpenUrlComplete(){
        _eventOpenUrl.value=""
    }

    fun toggleElection() {
        val currentState = electionIsFavorite.value
        Timber.d("setting state to: $currentState")
        currentState?.let {
            viewModelScope.launch {
                dataSource.saveElection(voterInfoFragmentArgs.argElectionId, !currentState)
            }
            electionIsFavorite.value = !currentState
        }
    }
}