package com.example.android.politicalpreparedness.network.models

import androidx.room.*
import com.squareup.moshi.*
import java.util.*

@Entity(tableName = "election_table")
data class Election(
        @PrimaryKey @ColumnInfo(name = "id")val id: Int,
        @ColumnInfo(name = "name")val name: String,
        @ColumnInfo(name = "electionDay")val electionDay: Date,
        @Embedded(prefix = "division_") @Json(name="ocdDivisionId") val division: Division,
        @ColumnInfo(name="isFavorite") @Transient val isFavorite:Boolean = false
)