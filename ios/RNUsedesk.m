#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

@interface RCT_EXTERN_MODULE(UseDeskChat, RCTEventEmitter)
RCT_EXTERN_METHOD(initChat:(NSString *)companyID url: (NSString *)url urlToSendFile: (NSString *)urlToSendFile port: (NSString *)port api_token: (NSString *)api_token email: (NSString *)email phone: (NSString *)phone name: (NSString *)name nameChat: (NSString *)nameChat signature: (NSString *)signature channelId: (NSString *)channelId);
RCT_EXTERN_METHOD(sendMessage:(NSString *)message);
RCT_EXTERN_METHOD(sendFile:(NSString *)message fileName:(NSString *)fileName fileType:(NSString *)fileType contentBase64:(NSString *)contentBase64);
@end
