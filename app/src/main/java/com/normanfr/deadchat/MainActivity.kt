package com.normanfr.deadchat

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var myWebView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myWebView = findViewById(R.id.webview)
        myWebView.webViewClient = WebViewClient() // Prevent opening in browser
        val webSettings: WebSettings = myWebView.settings
        webSettings.javaScriptEnabled = true // Enable JavaScript

        // Check battery level before loading the WebView
        checkBatteryAndLoadUrl()
    }

    private fun checkBatteryAndLoadUrl() {
        val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = registerReceiver(null, ifilter)
        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val batteryPct: Float = level / scale.toFloat() * 100

        if (batteryPct > 5) {
            // Show dialog and close the app
            showLowBatteryDialog()
        } else {
            myWebView.loadUrl("https://www.example.com") // Load your URL here
        }
    }

    private fun showLowBatteryDialog() {
        AlertDialog.Builder(this)
            .setTitle("Dead Chat #1 rule.")
            .setMessage("Only use if below 5%")
            .setCancelable(false)
            .setPositiveButton("OK") { dialog: DialogInterface, _: Int ->
                finish() // Close the app
            }
            .show()
    }

    override fun onBackPressed() {
        if (myWebView.canGoBack()) {
            myWebView.goBack() // Navigate back in WebView history
        } else {
            super.onBackPressed()
        }
    }
}
