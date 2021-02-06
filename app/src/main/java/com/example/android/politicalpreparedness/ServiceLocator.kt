package com.example.android.politicalpreparedness

import android.content.Context
import com.example.android.politicalpreparedness.data.ElectionLocalDataSource
import com.example.android.politicalpreparedness.data.ElectionRemoteDataSource
import com.example.android.politicalpreparedness.data.ElectionRepository
import com.example.android.politicalpreparedness.database.ElectionDatabase

object ServiceLocator {

    @Volatile
    var electionRepository :ElectionRepository?=null

    fun provideElectionRepository(context:Context):ElectionRepository{
        synchronized(this){
            return electionRepository?: createElectionRepository(context)
        }
    }

    private fun createElectionRepository(context: Context): ElectionRepository {
        return ElectionRepository(createRemoteRepo(),createLocalRepo(context))
    }

    private fun createLocalRepo(context: Context): ElectionLocalDataSource {
        val db = ElectionDatabase.getInstance(context)
        return ElectionLocalDataSource(db.electionDao)
    }

    private fun createRemoteRepo(): ElectionRemoteDataSource {
        return ElectionRemoteDataSource()
    }

}