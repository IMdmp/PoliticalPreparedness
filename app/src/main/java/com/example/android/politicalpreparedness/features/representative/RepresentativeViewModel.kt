package com.example.android.politicalpreparedness.features.representative

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.features.representative.model.Representative
import com.example.android.politicalpreparedness.network.CivicsApi
import kotlinx.coroutines.launch
import retrofit2.await

class RepresentativeViewModel: BaseViewModel() {

    //TODO: Establish live data for representatives and address

    //TODO: Create function to fetch representatives from API from a provided address
    var representativeList = MutableLiveData<List<Representative>>()
    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    //TODO: Create function get address from geo location

    //TODO: Create function to get address from individual fields
    fun findRepresentatives(address:String){
        viewModelScope.launch {
            val (offices, officials) = CivicsApi.retrofitService.getRepresentatives(address).await()
            representativeList.value = offices.flatMap { office ->office.getRepresentatives(officials) }
        }
    }
}
