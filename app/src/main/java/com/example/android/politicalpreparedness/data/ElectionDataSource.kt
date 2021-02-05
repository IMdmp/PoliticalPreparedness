package com.example.android.politicalpreparedness.data

import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse

interface ElectionDataSource {

    suspend fun getElectionList():Result<List<Election>>
    suspend fun deleteAllElections(){

    }
    suspend fun saveElection(election: Election) {

    }


    suspend fun getVoterInfo(address: String, electionId: Int):Result<VoterInfoResponse>{
        return Result.Error(UninitializedPropertyAccessException())
    }
}