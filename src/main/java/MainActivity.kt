package com.example.telaoff

import android.app.Activity
import android.app.admin.DeviceAdminReceiver
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle

class AdminReceiver : DeviceAdminReceiver()

class MainActivity : Activity() {

    private val REQ_ADMIN = 1

    private val dpm by lazy { getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager }
    private val admin by lazy { ComponentName(this, AdminReceiver::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (dpm.isAdminActive(admin)) {
            lockAndClose()
        } else {
            val i = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
                .putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, admin)
                .putExtra(
                    DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                    "Necessario para desligar a tela ao tocar no icone."
                )
            startActivityForResult(i, REQ_ADMIN)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_ADMIN && dpm.isAdminActive(admin)) lockAndClose() else finish()
    }

    private fun lockAndClose() {
        dpm.lockNow()
        finish()
    }
}
