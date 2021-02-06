package com.example.android.politicalpreparedness.features.representative

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.politicalpreparedness.base.BaseFragment
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.features.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.features.representative.adapter.RepresentativeListener
import com.example.android.politicalpreparedness.network.models.Address
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import timber.log.Timber
import java.util.Locale

class DetailFragment : BaseFragment(), RepresentativeListener {

    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
        private const val REQUEST_CHECK_SETTINGS = 90

    }

    private lateinit var viewModel: RepresentativeViewModel
    private lateinit var adapter: RepresentativeListAdapter
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var binding: FragmentRepresentativeBinding
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentRepresentativeBinding.inflate(inflater)

        viewModel = ViewModelProvider(this).get(RepresentativeViewModel::class.java)
        adapter = RepresentativeListAdapter(this)
        binding.rvRepresentatives.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRepresentatives.adapter = adapter
        binding.repViewModel = viewModel
        binding.lifecycleOwner = this
        binding.buttonSearch.setOnClickListener {
            Timber.d("searching...")

            val address1 = binding.addressLine1.text
            val address2 = binding.addressLine2.text
            val state = binding.state.getItemAtPosition(binding.state.selectedItemPosition)
            val city = binding.city.text
            val zip = binding.zip.text

            val completeAddress = "$address2 $address1, $city, $state, $zip"
            viewModel.findRepresentatives(completeAddress)
        }

        binding.buttonLocation.setOnClickListener {
            if (checkLocationPermissions()) {
                checkLocationSettings()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.representativeList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.address.observe(viewLifecycleOwner, Observer {
            viewModel.findRepresentatives(it)
        })

        viewModel.showLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                binding.pbLoading.visibility = View.VISIBLE
            } else {
                binding.pbLoading.visibility = View.GONE
            }
        })
        mFusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireActivity());
    }

    private fun checkLocationSettings() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(requireActivity())
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener { locationSettingsResponse ->
            // All location settings are satisfied. The client can initialize
            // location requests here.
            // ...
            getDeviceLocation()

            Timber.d("done. $locationSettingsResponse")
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                Timber.d("request denied.")
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
//                    exception.startResolutionForResult(requireActivity(),
//                            REQUEST_CHECK_SETTINGS)

                    startIntentSenderForResult(exception.resolution.intentSender,
                            REQUEST_CHECK_SETTINGS,
                            null,
                            0,
                            0,
                            0,
                            null);

                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
        task.addOnCanceledListener {
            viewModel.showErrorMessage.value = "Request denied."
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.d("on activity resu;t/")
        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        Timber.d("OK")
                        checkLocationSettings()
                    }
                }
            }
        }
    }

    private fun getDeviceLocation() {
        /*
      * Get the best and most recent location of the device, which may be null in rare
      * cases when a location is not available.
      */


        try {
            val locationResult = mFusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val lastKnownLocation = task.result
                    Timber.d("last known location? $lastKnownLocation")
                    if (lastKnownLocation == null) {
                        makeLocationRequest()
                    }
                    lastKnownLocation?.let {
                        geoCodeLocationAndInputFields(it)
                    }
                } else {
                    Timber.e("Exception:${task.exception}")
                    Timber.d("Current location is null. Using defaults.")
                }
            }

        } catch (e: SecurityException) {
            Timber.e(e)
        }
    }

    @SuppressLint("MissingPermission")
    private fun makeLocationRequest() {

        val locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(20 * 1000);
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    if (location != null) {
                        val lat = location.latitude;
                        val long = location.longitude;
                        geoCodeLocationAndInputFields(lat, long)
                        mFusedLocationProviderClient.removeLocationUpdates(this)
                    }
                }
            }
        };
        mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Timber.d("permission result")
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    Timber.d("permission granted!")
                    checkLocationSettings()
                }
            }
        }
    }

    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            true
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
            false
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
                requireActivity().applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun geoCodeLocationAndInputFields(lat: Double, long: Double): Address {
        val address = geoCodeLocation(lat, long)
        Timber.d("got addresss: $address")
        viewModel.updateAddress(address)
        return address
    }

    private fun geoCodeLocation(lat: Double, long: Double): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(lat, long, 1)
                .map { address ->
                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
                }
                .first()
    }

    private fun geoCodeLocationAndInputFields(location: Location): Address {
        return geoCodeLocationAndInputFields(location.latitude, location.longitude)
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    override val _viewModel: BaseViewModel
        get() = viewModel

    override fun openUrl(url: String) {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        }
    }

}