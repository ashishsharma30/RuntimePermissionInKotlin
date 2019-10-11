package com.kotlin.aks.runtimepermissioninkotlin

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val permissionsRequestCode = 123
    private lateinit var managePermissions: ManagePermissions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize a list of required permissions to request runtime
        val list = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_PHONE_STATE
        )

        // Initialize a new instance of ManagePermissions class
        managePermissions = ManagePermissions(this, list, permissionsRequestCode)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            managePermissions.checkPermissions()
    }

    // Receive the permissions request result
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            permissionsRequestCode -> {
                val isPermissionsGranted = managePermissions
                    .processPermissionsResult(grantResults)
                if (isPermissionsGranted) {
                    // Do the task now
                    toast(getString(R.string.permissions_granted))
                } else {
                    toast(getString(R.string.permissions_denied))
                }
                return
            }
        }
    }

    // Extension function to show toast message
    private fun Context.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
