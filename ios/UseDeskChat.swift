import Foundation
import UseDesk_SDK_Swift

@objc(UseDeskChat)
class UseDeskChat: RCTEventEmitter {
    let usedesk = UseDeskSDK()
    @objc func initChat(_ companyID: String, url: String, urlToSendFile: String, port: String, api_token: String, email: String, phone: String, name: String, nameChat: String, signature: String, channelId: String) -> Void {
        self.usedesk.startWithoutGUICompanyID(companyID: companyID, chanelId: channelId, api_token: api_token, email: email, phone: phone, url: url, urlToSendFile: urlToSendFile + "send_file", port: port, name: name, nameChat: nameChat, signature:signature, connectionStatus: { (success, error) in
            if (success) {
                let historyMess: NSMutableArray = []

                for message in self.usedesk.historyMess {
                    historyMess.add(self.getMessageDict(message))
                }
                self.sendEvent(withName: "onConnected", body: historyMess)
            }
        })
        self.usedesk.newMessageBlock = { success, message in
            print("newMessageBlock")
            if ((message) != nil) {
                self.sendEvent(withName: "onMessage", body: self.getMessageDict(message!))
            }
        }
    }

    @objc func sendMessage(_ message: String) -> Void {
        self.usedesk.sendMessage(message)
    }

    @objc func sendFile(_ message: String, fileName: String, fileType: String, contentBase64: String) -> Void {

        guard let fileData = try? Data(base64Encoded: contentBase64, options: .ignoreUnknownCharacters) else {
            return
        }
        self.usedesk.sendFile(fileName: fileName, data: fileData) { (success, error) in
            if (error != "") {
                print(error)
            }
        }
    }

    func getMessageDict(_ message: UDMessage) -> NSMutableDictionary {
        let messageDic: NSMutableDictionary = [:]
        messageDic["text"] = message.text
        messageDic["me"] = !message.incoming
        messageDic["avatar"] = message.avatar
        messageDic["name"] = message.name;
        let formatter = DateFormatter()
        formatter.locale = Locale(identifier: "ru")
        formatter.timeZone = TimeZone(identifier: "Europe/Moscow")
        formatter.dateFormat = "HH:mm E, d MMM"
        let date = formatter.string(from: message.date!)
        messageDic["date"] = date
        if ((message.file.name) != "") {
            messageDic["file"] = [
                "name": message.file.name,
                "url": message.file.content,
            ]
        }
        return messageDic;
    }

    override public static func requiresMainQueueSetup() -> Bool {
        return true;
    }

    override func supportedEvents() -> [String]! {
        return ["onMessage", "onConnected"]
    }
}
