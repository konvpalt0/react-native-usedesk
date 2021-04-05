import { NativeModules, NativeEventEmitter } from 'react-native';

const { UseDeskChat } = NativeModules;
const MessageEvents = new NativeEventEmitter(UseDeskChat);

export class UseDesk {
    static initChat = ({companyID, url, urlToSendFile, port, api_token, email, phone, name, nameChat, signature, channelId}) => {
        UseDeskChat.initChat(companyID, url, urlToSendFile, port, api_token, email, phone, name, nameChat, signature, channelId);
    }

    static sendMessage = (text) => {
        UseDeskChat.sendMessage(text);
    }

    static sendFile = (message = "", fileName, fileType, base64) => {
        UseDeskChat.sendFile(message, fileName, fileType, base64);
    }

    static sendFileAndroid = (uri = "", fileType = "", name = "") => {
        console.log('uri', uri);
        console.log('fileType', fileType);
        UseDeskChat.sendFile(uri, fileType, name);
    }

    static addEventListener = (event, listener) => {
        return MessageEvents.addListener(event, listener);
    }

    static removeEventListener = (event) => {
        return MessageEvents.removeAllListeners(event);
    }
}
