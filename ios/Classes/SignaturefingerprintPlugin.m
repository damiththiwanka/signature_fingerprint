#import "SignaturefingerprintPlugin.h"
#if __has_include(<signaturefingerprint/signaturefingerprint-Swift.h>)
#import <signaturefingerprint/signaturefingerprint-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "signaturefingerprint-Swift.h"
#endif

@implementation SignaturefingerprintPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftSignaturefingerprintPlugin registerWithRegistrar:registrar];
}
@end
