package com.example.android.politicalpreparedness.data

import com.example.android.politicalpreparedness.network.models.Election

interface ElectionDataSource {

    suspend fun getElectionList():Result<List<Election>>
    suspend fun deleteAllElections()
    suspend fun saveElection(election: Election) {

    }
}