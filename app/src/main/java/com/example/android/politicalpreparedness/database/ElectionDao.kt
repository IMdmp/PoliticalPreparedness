package com.example.android.politicalpreparedness.database

import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertElection(election: Election)

    @Query("SELECT * FROM election_table")
    suspend fun getElectionList():List<Election>

    @Query("SELECT * FROM election_table WHERE id= :electionId")
    suspend fun getElectionById(electionId:Int):Election?

    @Query("DELETE FROM election_table WHERE id= :electionId")
    suspend fun deleteElectionById(electionId: Int)

    @Query("DELETE FROM election_table")
    suspend fun deleteAllElections()

    @Query("SELECT * FROM election_table WHERE isFavorite =1")
    suspend fun getAllSaved():List<Election>

    @Query("UPDATE election_table SET isFavorite =:isFavorite WHERE id= :electionId")
    suspend fun setFavorite(isFavorite:Boolean,electionId: Int)
}