package com.kotlin.aks.runtimepermissioninkotlin

import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class ManagePermissions(
    private val activity: Activity,
    private val list: List<String>,
    private val code: Int
) {

    // Check permissions at runtime
    fun checkPermissions() {
        if (isPermissionsGranted() != PackageManager.PERMISSION_GRANTED) {
            showAlert()
        } else {
            Toast.makeText(
                activity,
                activity.getString(R.string.permissions_already_granted),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    // Check permissions status
    private fun isPermissionsGranted(): Int {
        // PERMISSION_GRANTED : Constant Value: 0
        // PERMISSION_DENIED : Constant Value: -1
        var counter = 0
        for (permission in list) {
            counter += ContextCompat.checkSelfPermission(activity, permission)
        }
        return counter
    }


    // Find the first denied permission
    private fun deniedPermission(): String {
        for (permission in list) {
            if (ContextCompat.checkSelfPermission(activity, permission)
                == PackageManager.PERMISSION_DENIED
            ) return permission
        }
        return ""
    }


    // Show alert dialog to request permissions
    private fun showAlert() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(activity.getString(R.string.need_permissions))
        builder.setMessage(activity.getString(R.string.some_permissions))
        builder.setPositiveButton(activity.getString(R.string.ok_alert)) { _, _ -> requestPermissions() }
//        builder.setNeutralButton(activity.getString(R.string.cancel_alert), null)
        builder.setNeutralButton(activity.getString(R.string.cancel_alert)) { _, _ ->
            Toast.makeText(
                activity,
                activity.getString(R.string.permissions_denied), Toast.LENGTH_SHORT
            ).show()
        }
        val dialog = builder.create()
        dialog.show()
    }


    // Request the permissions at run time
    private fun requestPermissions() {
        val permission = deniedPermission()
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            // Show an explanation asynchronously
            Toast.makeText(
                activity,
                activity.getString(R.string.some_permissions),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            ActivityCompat.requestPermissions(activity, list.toTypedArray(), code)
        }
    }


    // Process permissions result
    fun processPermissionsResult(
        grantResults: IntArray
    ): Boolean {
        var result = 0
        if (grantResults.isNotEmpty()) {
            for (item in grantResults) {
                result += item
            }
        }
        if (result == PackageManager.PERMISSION_GRANTED) return true
        return false
    }
}