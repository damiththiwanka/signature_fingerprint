
import 'dart:async';

import 'package:flutter/services.dart';

class Signaturefingerprint {
  static const MethodChannel _channel =
      const MethodChannel('signaturefingerprint');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
  static Future<String> getSignatureFingerprint() async {
    final String signature = await _channel.invokeMethod('getSignatureFingerprint');
    return signature;
  }
}
