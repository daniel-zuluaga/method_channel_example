package com.example.method_channel_demo
import android.content.Context
import android.os.BatteryManager
import android.os.Bundle
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.NumberParseException

import io.flutter.embedding.android.FlutterActivity

class MainActivity : FlutterActivity() {
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        val CHANNEL = "com.example.app"

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler {
            call, result ->
            if (call.method == "getBatteryLevel") {
                val batteryLevel = getBatteryLevel()

                if (batteryLevel != -1) {
                    result.success(batteryLevel)
                } else {
                    result.error("UNAVAILABLE", "Battery level not available.", null)
                }
            } else if (call.method == "formatNumber") {
                val number = call.argument<String>("number")
                if (number != null) {
                    val formattedNumber = formatNumber(number)
                    result.success(formattedNumber)
                } else {
                    result.error("INVALID_ARGUMENT", "Phone number is required.", null)
                }
            } else {
                result.notImplemented()
            }
        }
    }

    private fun getBatteryLevel(): Int {
        val batteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }

    private fun formatNumber(number: String): String {
        val phoneUtil = PhoneNumberUtil.getInstance()
        try {
            val swissNumberProto = phoneUtil.parse(number, "CO")
            return phoneUtil.format(swissNumberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
        } catch (e: NumberParseException) {
            System.err.println("NumberParseException was thrown: " + e.toString())
            return "Error"
        }
    }
}
