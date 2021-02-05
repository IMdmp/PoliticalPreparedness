package com.example.android.politicalpreparedness.data

import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class ElectionLocalDataSource(private val electionDao: ElectionDao,
                              private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO):ElectionDataSource {

    override suspend fun getElectionList(): Result<List<Election>> = withContext(ioDispatcher){
        return@withContext try{
            Result.Success(electionDao.getElectionList())
        }catch (e:Exception){
            Result.Error(e)
        }
    }

    override suspend fun deleteAllElections() {
        electionDao.deleteAllElections()
    }

    override suspend fun saveElection(election:Election) {
        electionDao.insertElection(election)
    }
}