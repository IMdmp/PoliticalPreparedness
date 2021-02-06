package com.example.android.politicalpreparedness.data

import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
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

    override suspend fun getVoterInfo(address:String,electionId:Int):Result<VoterInfoResponse> {
        return try{
            val res = CivicsApi.retrofitService.getVoterInfo(address,electionId).await()
            Result.Success(res)
        }catch (e:Exception){
            Result.Error(e)
        }
    }

    override suspend fun getElection(electionId: Int): Election? {
        //no op.
        return null
    }

    override suspend fun getAllSavedElections(): Result<List<Election>> {
        //no op.
        TODO("Not yet implemented")
    }


}