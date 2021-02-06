package com.example.android.politicalpreparedness.features.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.features.representative.model.Representative
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import kotlinx.coroutines.launch
import retrofit2.await
import java.lang.Exception


class RepresentativeViewModel : BaseViewModel() {

    private var _representativeList = MutableLiveData<List<Representative>>()
    val representativeList: LiveData<List<Representative>>
        get() = _representativeList
    private var _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    fun findRepresentatives(address: Address) {
        val address1 = address.line1
        val address2 = address.line2
        val state = address.state
        val city = address.city
        val zip = address.zip

        val completeAddress = "$address2 $address1, $city, $state, $zip"
        findRepresentatives(completeAddress)
    }

    fun findRepresentatives(address: String) {

        showLoading.value = true
        viewModelScope.launch {
            try {
                val (offices, officials) = CivicsApi.retrofitService.getRepresentatives(address).await()
                _representativeList.value = offices.flatMap { office -> office.getRepresentatives(officials) }
            } catch (e: Exception) {
                showErrorMessage.value = "Unable to retrieve representatives: ${e.message}"
            }
            showLoading.value = false
        }
    }


    fun updateAddress(address: Address) {
        _address.value = address
    }
}
