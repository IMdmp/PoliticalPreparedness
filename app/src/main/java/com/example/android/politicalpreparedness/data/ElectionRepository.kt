package com.example.android.politicalpreparedness.data

import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class ElectionRepository(
        private val electionRemoteDataSource: ElectionDataSource,
        private val electionLocalDataSource: ElectionDataSource,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val electionLiveData = MutableLiveData<Result<List<Election>>>()

    suspend fun getElectionList(): Result<List<Election>> {
        return  electionLocalDataSource.getElectionList()
    }

    suspend fun refreshElectionList(){
        val remoteElectionList = electionRemoteDataSource.getElectionList()

        if(remoteElectionList is Result.Success){
            electionLocalDataSource.deleteAllElections()

            remoteElectionList.data.forEach { election->
                electionLocalDataSource.saveElection(election)
            }
        }else if (remoteElectionList is Result.Error){
            throw remoteElectionList.exception
        }
    }

    suspend fun getVoterInfo(address:String,electionId:Int):Result<VoterInfoResponse> {
        return electionRemoteDataSource.getVoterInfo(address,electionId)
    }
}