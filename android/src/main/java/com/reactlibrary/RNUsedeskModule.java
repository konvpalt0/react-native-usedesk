package com.reactlibrary;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.usedesk.chat_sdk.UsedeskChatSdk;
import ru.usedesk.chat_sdk.domain.IUsedeskChat;
import ru.usedesk.chat_sdk.entity.IUsedeskActionListener;
import ru.usedesk.chat_sdk.entity.UsedeskChatConfiguration;
import ru.usedesk.chat_sdk.entity.UsedeskFile;
import ru.usedesk.chat_sdk.entity.UsedeskFileInfo;
import ru.usedesk.chat_sdk.entity.UsedeskMessage;
import ru.usedesk.chat_sdk.entity.UsedeskMessageAgentFile;
import ru.usedesk.chat_sdk.entity.UsedeskMessageAgentImage;
import ru.usedesk.chat_sdk.entity.UsedeskMessageAgentText;
import ru.usedesk.chat_sdk.entity.UsedeskMessageClientFile;
import ru.usedesk.chat_sdk.entity.UsedeskMessageClientImage;
import ru.usedesk.chat_sdk.entity.UsedeskMessageClientText;
import ru.usedesk.common_sdk.entity.exceptions.UsedeskException;
import ru.usedesk.common_sdk.entity.exceptions.UsedeskSocketException;

public class RNUsedeskModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    IUsedeskChat usedeskChat;

    public RNUsedeskModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    private static String formatDateFromString(String inputFormat, String outputFormat, String inputDate) {

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

    @Override
    public String getName() {
        return "UseDeskChat";
    }

    @ReactMethod
    public void initChat(String companyID, String url, String urlToSendFile, String port, String api_token, String email, String phone, String name, String nameChat, String signature) {
        UsedeskChatSdk.release();
        UsedeskChatConfiguration usedeskChatConfiguration = new UsedeskChatConfiguration("https://" + url + ":" + port, url, urlToSendFile, companyID, signature, email, name, "", Long.valueOf(phone));
        UsedeskChatSdk.setConfiguration(usedeskChatConfiguration);
        usedeskChat = UsedeskChatSdk.init(reactContext);
        usedeskChat.addActionListener(new IUsedeskActionListener() {
            @Override
            public void onMessageReceived(@NotNull UsedeskMessage usedeskMessage) {
            }

            @Override
            public void onException(@NotNull Exception e) {
                Log.d("ChatException", e.getMessage());
            }

            @Override
            public void onNewMessageReceived(@NotNull UsedeskMessage usedeskMessage) {
                sendEvent(reactContext, "onMessage", getModelFromMessage(usedeskMessage));
            }

            @Override
            public void onMessagesReceived(@NotNull List<? extends UsedeskMessage> list) {
                Log.d("onMessagesReceived", list.size() + "");
                WritableArray array = new WritableNativeArray();
                for (int i = 0; i < list.size(); i++) {
                    array.pushMap(getModelFromMessage(list.get(i)));
                }
                sendEvent(reactContext, "onConnected", array);
            }

            @Override
            public void onMessageUpdated(@NotNull UsedeskMessage usedeskMessage) {

            }


            @Override
            public void onConnectedState(boolean b) {

            }


            @Override
            public void onFeedbackReceived() {

            }

            @Override
            public void onOfflineFormExpected(@NonNull UsedeskChatConfiguration usedeskChatConfiguration) {

            }
        });

        try {
            usedeskChat.connect();
        } catch (UsedeskSocketException e) {
            Log.d("ChatException", e.getError().name());
        } catch (UsedeskException e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void sendMessage(String text) {
        try {
            usedeskChat.send(text);
        } catch (UsedeskException e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void sendFile(String uri, String type, String name) {
        List<UsedeskFileInfo> files = new ArrayList<>();
        Log.d("sendFile", Uri.parse(uri).toString());
        files.add(new UsedeskFileInfo(Uri.parse(uri), type, name));
        try {
            usedeskChat.send(files);
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
        Log.d("MessageType", message.getType().toString());
        WritableMap map = Arguments.createMap();
        switch (message.getType()) {
            case TYPE_AGENT_TEXT:
                UsedeskMessageAgentText messageAgent = ((UsedeskMessageAgentText) message);
                map.putString("text", messageAgent.getText());
                map.putString("name", messageAgent.getName());
                map.putString("avatar", messageAgent.getAvatar());
                map.putBoolean("me", false);
                break;
            case TYPE_CLIENT_TEXT:
                UsedeskMessageClientText messageClient = ((UsedeskMessageClientText) message);
                map.putString("text", messageClient.getText());
                map.putBoolean("me", true);
                break;
            case TYPE_CLIENT_FILE:
                UsedeskMessageClientFile fileClient = ((UsedeskMessageClientFile) message);
                UsedeskFile file = fileClient.getFile();
                WritableMap fileMap = Arguments.createMap();
                fileMap.putString("name", file.getName());
                fileMap.putString("url", file.getContent());
                map.putMap("file", fileMap);
                map.putBoolean("me", true);
                break;
            case TYPE_AGENT_FILE:
                UsedeskMessageAgentFile fileAgent = ((UsedeskMessageAgentFile) message);
                UsedeskFile fileAgentMessage = fileAgent.getFile();
                WritableMap fileMapAgent = Arguments.createMap();
                fileMapAgent.putString("name", fileAgentMessage.getName());
                fileMapAgent.putString("url", fileAgentMessage.getContent());
                map.putMap("file", fileMapAgent);
                map.putBoolean("me", false);
                break;
            case TYPE_AGENT_IMAGE:
                UsedeskMessageAgentImage imageAgent = ((UsedeskMessageAgentImage) message);
                UsedeskFile imageAgentMessage = imageAgent.getFile();
                WritableMap imageMapAgent = Arguments.createMap();
                imageMapAgent.putString("name", imageAgentMessage.getName());
                imageMapAgent.putString("url", imageAgentMessage.getContent());
                map.putMap("file", imageMapAgent);
                map.putBoolean("me", false);
                break;
            case TYPE_CLIENT_IMAGE:
                UsedeskMessageClientImage imageClient = ((UsedeskMessageClientImage) message);
                UsedeskFile imageClientMessage = imageClient.getFile();
                WritableMap imageMapClient = Arguments.createMap();
                imageMapClient.putString("name", imageClientMessage.getName());
                imageMapClient.putString("url", imageClientMessage.getContent());
                map.putMap("file", imageMapClient);
                map.putBoolean("me", true);
                break;
        }
        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm E, d MMM", java.util.Locale.getDefault());
        map.putString("date", format1.format(message.getCreatedAt().getTime()));
        return map;
    }
}
