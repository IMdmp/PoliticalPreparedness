package com.example.android.politicalpreparedness.features.election

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.ElectionRepository
import com.example.android.politicalpreparedness.data.Result
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.launch

class VoterInfoViewModel(private val dataSource: ElectionRepository,
                         private val voterInfoFragmentArgs: VoterInfoFragmentArgs) : ViewModel() {

    var voterInfoData = MutableLiveData<VoterInfoResponse>()
    private var voterInfo:VoterInfoResponse?=null

    fun getVoterInfo() {
        viewModelScope.launch {
            val voterResult = dataSource.getVoterInfo(voterInfoFragmentArgs.argDivision.state
                    .plus(" ")
                    .plus(voterInfoFragmentArgs.argDivision.country),
                    voterInfoFragmentArgs.argElectionId)


            when(voterResult){
                is Result.Success->{
                    voterInfoData.value = voterResult.data
                }
                is Result.Error ->{

                }
                is Result.Loading -> {
                }
            }
        }

    }

    fun updateData() {
        voterInfo = voterInfoData.value

    }
    //TODO: Add live data to hold voter info

    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}