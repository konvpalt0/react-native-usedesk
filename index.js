import { NativeModules, NativeEventEmitter } from 'react-native';

const { UseDeskChat } = NativeModules;
const MessageEvents = new NativeEventEmitter(UseDeskChat);

export class UseDesk {
    static initChat = ({companyID, url, port, api_token, email, phone, name, nameChat}) => {
        UseDeskChat.initChat(companyID, url, port, api_token, email, phone, name, nameChat);
    }

    static sendMessage = (text) => {
        UseDeskChat.sendMessage(text);
    }

    static sendFile = (message = "", fileName, fileType, base64) => {
        UseDeskChat.sendFile(message, fileName, fileType, base64);
    }

    static addEventListener = (event, listener) => {
        return MessageEvents.addListener(event, listener);
    }

    static removeEventListener = (event) => {
        return MessageEvents.removeAllListeners(event);
    }
}
