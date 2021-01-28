
package com.reactlibrary;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ru.usedesk.chat_sdk.external.IUsedeskChat;
import ru.usedesk.chat_sdk.external.UsedeskChatSdk;
import ru.usedesk.chat_sdk.external.entity.IUsedeskActionListener;
import ru.usedesk.chat_sdk.external.entity.UsedeskChat;
import ru.usedesk.chat_sdk.external.entity.UsedeskChatConfiguration;
import ru.usedesk.chat_sdk.external.entity.UsedeskFileInfo;
import ru.usedesk.chat_sdk.external.entity.UsedeskMessage;
import ru.usedesk.chat_sdk.external.entity.UsedeskMessageType;
import ru.usedesk.chat_sdk.internal.domain.entity.UsedeskFile;
import ru.usedesk.common_sdk.external.entity.exceptions.UsedeskException;
import ru.usedesk.common_sdk.external.entity.exceptions.UsedeskSocketException;

public class RNUsedeskModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RNUsedeskModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "UseDeskChat";
  }

  @ReactMethod
  public void initChat(String companyID, String url, String port, String api_token, String email, String phone, String name, String nameChat) {
      UsedeskChatSdk.release();
    UsedeskChatConfiguration usedeskChatConfiguration = new UsedeskChatConfiguration(companyID, email, "https://"+url+":"+port, url, name, Long.valueOf(phone), null);
    UsedeskChatSdk.setConfiguration(usedeskChatConfiguration);
    UsedeskChatSdk.init(reactContext, new IUsedeskActionListener() {
      @Override
      public void onConnected() {
        Log.d("onConnected", "onConnected ");
      }

      @Override
      public void onMessageReceived(@NonNull UsedeskMessage usedeskMessage) {
        sendEvent(reactContext, "onMessage", getModelFromMessage(usedeskMessage));
      }

      @Override
      public void onMessagesReceived(@NonNull List<UsedeskMessage> list) {
        Log.d("onMessagesReceived", list.size() + "");
        WritableArray array = new WritableNativeArray();
        for (int i = 0; i < list.size(); i++) {
          array.pushMap(getModelFromMessage(list.get(i)));
        }
        sendEvent(reactContext, "onConnected", array);
      }

      @Override
      public void onFeedbackReceived() {

      }

      @Override
      public void onOfflineFormExpected(@NonNull UsedeskChatConfiguration usedeskChatConfiguration) {

      }

      @Override
      public void onDisconnected() {

      }

      @Override
      public void onException(@NonNull UsedeskException e) {
        Log.d("onException", e.toString());
      }
    });

    try {
      UsedeskChatSdk.getInstance().connect();
    } catch (UsedeskSocketException e) {
      Log.d("ChatException", e.getError().name());
    } catch (UsedeskException e) {
      e.printStackTrace();
    }
  }

  @ReactMethod
  public void sendMessage(String text) {
    try {
      UsedeskChatSdk.getInstance().send(text);
    } catch (UsedeskException e) {
      e.printStackTrace();
    }
  }

  @ReactMethod
  public void sendFile(String uri, String type) {
    try {
      UsedeskChatSdk.getInstance().send(new UsedeskFileInfo(Uri.parse(uri), UsedeskFileInfo.Type.getByMimeType(type)));
    } catch (UsedeskException e) {
      e.printStackTrace();
    }
  }

  private void sendEvent(ReactContext reactContext,
                          String eventName,
                          @Nullable WritableMap params) {
    reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit(eventName, params);
  }

  private void sendEvent(ReactContext reactContext,
                         String eventName,
                         @Nullable WritableArray params) {
    reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit(eventName, params);
  }

  private WritableMap getModelFromMessage(UsedeskMessage message) {
      WritableMap map = Arguments.createMap();
      map.putString("text", message.getText() );
      map.putBoolean("me", message.getType().equals(UsedeskMessageType.CLIENT_TO_OPERATOR));
      map.putString("name", message.getName() );
      Log.d("testDate", message.getCreatedAt());
      map.putString("date", formatDateFromString("yyyy-MM-dd'T'HH:mm:ss'Z'", "HH:mm E, d MMM", message.getCreatedAt()));
      map.putString("avatar", message.getUsedeskPayload().getAvatar());
      if (message.getFile() != null) {
          UsedeskFile file = message.getFile();
          WritableMap fileMap = Arguments.createMap();
          fileMap.putString("name", file.getName());
          fileMap.putString("url", file.getContent());
          map.putMap("file", fileMap);
      }
      return map;
  }

    private static String formatDateFromString(String inputFormat, String outputFormat, String inputDate){

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            Log.d("Date parse error", e.toString());
        }

        return outputDate;

    }
}