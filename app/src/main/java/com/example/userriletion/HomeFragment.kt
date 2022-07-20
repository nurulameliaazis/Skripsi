package com.example.userriletion

import android.Manifest
import android.annotation.SuppressLint
import android.content.IntentSender
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.userriletion.databinding.FragmentHomeBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog


class HomeFragment : Fragment(), OnMapReadyCallback, EasyPermissions.PermissionCallbacks {
    companion object {
        const val PERMISSION_LOCATION_REQUEST_CODE = 1
    }

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var map: GoogleMap
    private lateinit var lastLocation: Location
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        return binding.root

    }

    private fun hasLocationPermission() =
        EasyPermissions.hasPermissions(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    private fun requestLocationPermission() {
        EasyPermissions.requestPermissions(
            this, "This Apllication cannot work without location permission.",
            PERMISSION_LOCATION_REQUEST_CODE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms.first())) {
            SettingsDialog.Builder(requireActivity()).build().show()
        } else {
            requestLocationPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(
            requireContext(),
            "Permission Granted", Toast.LENGTH_SHORT
        ).show()
        setViewVisibility()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }



    private fun setViewVisibility() {
        if (hasLocationPermission()) {
            binding.getlocation.visibility = View.VISIBLE
        } else {
            binding.getlocation.visibility = View.GONE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.getlocation.setOnClickListener {
            requestLocationPermission()
            val task = LocationServices.getSettingsClient(requireContext())
                .checkLocationSettings(
                    LocationSettingsRequest.Builder()
                        .addLocationRequest(LocationRequest.create())
                        .setAlwaysShow(true)
                        .build()
                )

            task.addOnFailureListener {
                if (it is ResolvableApiException) {
                    try {
                        it.startResolutionForResult(requireActivity(), 200)
                    } catch (sendEx: IntentSender.SendIntentException) {
                    }
                }
            }
            setUpMap()
            setViewVisibility()

        }

                val mapFragment =
                    childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment?
                mapFragment?.getMapAsync(this)
            }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.apply {
            uiSettings.isZoomControlsEnabled = true
            isMyLocationEnabled = true
        }
        val latitude = -6.36687612195
        val longitude = 106.819707178
        val zoomLevel = 15f

        val homeLatLng = LatLng(latitude, longitude)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, zoomLevel))
        map.addMarker(MarkerOptions().position(homeLatLng))
    }
    @SuppressLint("MissingPermission")
    fun setUpMap(){

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
              map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                Log.d("MapsFragment", "Ini lokasi $currentLatLng")            }
        }

    }


}





