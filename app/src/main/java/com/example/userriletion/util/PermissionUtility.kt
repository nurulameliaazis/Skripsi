package com.example.userriletion.util

import android.Manifest
import android.content.Context
import com.vmadalin.easypermissions.EasyPermissions

object PermissionUtility {

    fun isPermissionGranted(context: Context) = EasyPermissions.hasPermissions(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
}