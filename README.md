# Signature Fingerprint

Get the fingerprint signature of your app's certificate.

Especially useful for adding yet another layer of security in your API calls.

## Getting Started

**Use**

1. Add this to your package's pubspec.yaml file 
```yaml
dependencies:
  signaturefingerprint: ^1.0.0
```
2. Install it
```shell script
$ flutter pub get
```
3. Import it
```dart
import 'package:signaturefingerprint/signaturefingerprint.dart';
```

4. Use it
```dart

String androidFingerprint = await Signaturefingerprint.getSignatureFingerprint();
```

### Platform Support

OS |
-- |
Android |

License
----

MIT
