package com.example.android.politicalpreparedness.features.representative

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.features.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.network.models.Address
import kotlinx.android.synthetic.main.fragment_voter_info.*
import timber.log.Timber
import java.util.Locale

class DetailFragment : Fragment() {

    companion object {
        //TODO: Add Constant for Location request
    }

    //TODO: Declare ViewModel
    private lateinit var viewModel: RepresentativeViewModel
    private lateinit var adapter:RepresentativeListAdapter
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentRepresentativeBinding.inflate(inflater)
        //TODO: Establish bindings

        //TODO: Define and assign Representative adapter

        //TODO: Populate Representative adapter

        //TODO: Establish button listeners for field and location search
        viewModel = ViewModelProvider(this).get(RepresentativeViewModel::class.java)
        adapter  = RepresentativeListAdapter()
        binding.rvRepresentatives.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRepresentatives.adapter = adapter
        binding.buttonSearch.setOnClickListener {
            Timber.d("searching...")

            val address1 = binding.addressLine1.text
            val address2 = binding.addressLine2.text
            val state = binding.state.getItemAtPosition(binding.state.selectedItemPosition)
            val city= binding.city.text
            val zip = binding.zip.text

            val completeAddress = "$address2 $address1, $city, $state, $zip"
            viewModel.findRepresentatives(completeAddress)
        }

        binding.buttonLocation.setOnClickListener {
            Timber.d("get user location")
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.representativeList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //TODO: Handle location permission result to get location on permission granted
    }

    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            true
        } else {
            //TODO: Request Location permissions
            false
        }
    }

    private fun isPermissionGranted() : Boolean {
        //TODO: Check if permission is already granted and return (true = granted, false = denied/other)
        return false
    }

    private fun getLocation() {
        //TODO: Get location from LocationServices
        //TODO: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
                }
                .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

}