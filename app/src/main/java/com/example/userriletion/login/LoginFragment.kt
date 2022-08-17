package com.example.userriletion.login

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.userriletion.R
import com.example.userriletion.databinding.FragmentLoginBinding
import com.example.userriletion.util.PermissionUtility
import com.google.firebase.auth.FirebaseAuth
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class LoginFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        checkPermission()

        binding.masuk.setOnClickListener {
            val email = binding.edittextemail.text.toString()
            val password = binding.edittextpassw.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {

                if (checkPermission()) {
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(requireActivity(), "Sukses", Toast.LENGTH_SHORT)
                                    .show()
                                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)

                            } else {
                                Toast.makeText(
                                    requireActivity(),
                                    "Email dan Kata sandi Tidak Sesuai",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                } else {
                    Toast.makeText(requireContext(), "Anda harus memberikan akses lokasi kepada aplikasi", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(requireActivity(), "Tidak Boleh Kosong", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        binding.regist.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
//        binding.lupasandi.setOnClickListener {
//            findNavController().navigate(R.id.action_loginFragment_to_lupakatasandiFragment)
//        }
    }

    private fun checkPermission() : Boolean {
        if (!PermissionUtility.isPermissionGranted(requireContext())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                EasyPermissions.requestPermissions(
                    this,
                    "This app cannot work without Location Permission",
                    PERMISSION_LOCATION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
                return false
            } else {
                EasyPermissions.requestPermissions(
                    this,
                    "This app cannot work without Location Permission",
                    PERMISSION_LOCATION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                return false
            }
        }
        return true
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            checkPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    companion object {
        const val PERMISSION_LOCATION_REQUEST_CODE = 1
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
