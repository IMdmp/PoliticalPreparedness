package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.database.Converters
import com.example.android.politicalpreparedness.network.jsonadapter.DateAdapter
import com.example.android.politicalpreparedness.network.jsonadapter.ElectionAdapter
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://www.googleapis.com/civicinfo/v2/"


// TODO: Add adapters for Java Date and custom adapter ElectionAdapter (included in project)
private val moshi = Moshi.Builder()
        .add(ElectionAdapter())
        .add(DateAdapter())
        .add(KotlinJsonAdapterFactory())
        .build()
private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(CivicsHttpClient.getClient())
        .baseUrl(BASE_URL)
        .build()

/**
 *  Documentation for the Google Civics API Service can be found at https://developers.google.com/civic-information/docs/v2
 */

interface CivicsApiService {
    @GET("elections")
    fun getElectionList(): Call<ElectionResponse>

    @GET("voterinfo")
    fun getVoterInfo(@Query("address")address:String,@Query("electionId") electionId:Int):Call<VoterInfoResponse> //currently no data. (have to use election id 2000.) handle errors for now.

    @GET("representatives")
    fun getRepresentatives(@Query("address")address:String):Call<RepresentativeResponse>
}

object CivicsApi {
    val retrofitService: CivicsApiService by lazy {
        retrofit.create(CivicsApiService::class.java)
    }
}