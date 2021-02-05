package com.example.android.politicalpreparedness.data

import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import retrofit2.await
import java.lang.Exception

class ElectionRemoteDataSource :ElectionDataSource{

    override suspend fun getElectionList(): Result<List<Election>> {
        return try{
            val electionList = CivicsApi.retrofitService.getElectionList().await()
            Result.Success(electionList.toDomain())
        }catch (e:Exception){
            Result.Error(e)
        }
    }

    override suspend fun deleteAllElections() {
        // NO OP
    }
}