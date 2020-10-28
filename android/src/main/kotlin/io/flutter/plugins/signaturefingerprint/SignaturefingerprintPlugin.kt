package io.flutter.plugins.signaturefingerprint

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateEncodingException
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate




/** SignaturefingerprintPlugin */
class SignaturefingerprintPlugin : FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel


  companion object {
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val instance = SignaturefingerprintPlugin()
      instance.buildEngine(registrar.activeContext())
    }


    lateinit var applicationContext: Context
  }

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "signaturefingerprint")
    channel.setMethodCallHandler(this)
    buildEngine(flutterPluginBinding.applicationContext)
  }

  private fun buildEngine(context: Context) {
    applicationContext = context
//    contentResolver = contentResolver
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "getPlatformVersion") {
      result.success("Android ${android.os.Build.VERSION.RELEASE}")
    } else if (call.method == "getSignatureFingerprint") {
      getSignatureFingerprint(result)
    } else {
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  fun getSignatureFingerprint(@NonNull result: Result) {

    val packageName: String = applicationContext.packageName
    val pm: PackageManager = applicationContext.packageManager
    val flags: Int = PackageManager.GET_SIGNATURES
    var packageInfo: PackageInfo? = null
    try {
      packageInfo = pm.getPackageInfo(packageName, flags)
    } catch (e: PackageManager.NameNotFoundException) {
      e.printStackTrace()
    }
    val signatures: Array<Signature> = packageInfo!!.signatures
    val cert: ByteArray = signatures[0].toByteArray()
    val input: InputStream = ByteArrayInputStream(cert)
    var cf: CertificateFactory? = null
    try {
      cf = CertificateFactory.getInstance("X509")
    } catch (e: CertificateException) {
      e.printStackTrace()
    }
    var c: X509Certificate? = null
    try {
      c = cf!!.generateCertificate(input) as X509Certificate
    } catch (e: CertificateException) {
      e.printStackTrace()
    }
    var hexString: String? = null
    try {
      val md: MessageDigest = MessageDigest.getInstance("SHA1")
      val publicKey: ByteArray = md.digest(c!!.getEncoded())
      hexString = byte2HexFormatted(publicKey)
    } catch (e1: NoSuchAlgorithmException) {
      e1.printStackTrace()
    } catch (e: CertificateEncodingException) {
      e.printStackTrace()
    }
    result.success(hexString)
  }

  fun byte2HexFormatted(arr: ByteArray): String {
    val str = StringBuilder(arr.size * 2)
    for (i in arr.indices) {
      var h = Integer.toHexString(arr[i].toInt())
      val l = h.length
      if (l == 1) h = "0$h"
      if (l > 2) h = h.substring(l - 2, l)
      str.append(h.toUpperCase())
      if (i < arr.size - 1) str.append(':')
    }
    return str.toString()
  }
}
