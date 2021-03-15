export interface UseDeskConfig {
    companyID: string, url: string, urlToSendFile: string, port: string, api_token: string, email: string, phone: string, name: string, nameChat: string, signature: string
}

declare module 'react-native-usedesk' {
    export class UseDesk {
        public static initChat(config: UseDeskConfig);
        public static sendFile(message: string, fileName: string, fileType: string, base64: string);
        public static sendFileAndroid(uri: string, fileType: string, name: string);
        public static sendMessage(message: string);
        public static addEventListener(event: string, listener: (data: any) => void);
        public static removeEventListener(event: string);
    }
}
