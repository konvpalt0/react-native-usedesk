import Foundation
import UseDesk_SDK_Swift

@objc(UseDeskChat)
class UseDeskChat: RCTEventEmitter {
    let usedesk = UseDeskSDK()
    @objc func initChat(_ companyID: String, url: String, port: String, api_token: String, email: String, phone: String, name: String, nameChat: String) -> Void {
        self.usedesk.startWithoutGUICompanyID(companyID: companyID, isUseBase: false, api_token: api_token, email: email, phone: phone, url: url, port: port, name: name, nameChat: nameChat, connectionStatus: { (success, error) in
            if (success) {
                let historyMess: NSMutableArray = []
                
                for message in self.usedesk.historyMess {
                    historyMess.add(self.getMessageDict(message))
                }
                self.sendEvent(withName: "onConnected", body: historyMess)
            }
        })
        self.usedesk.newMessageBlock = { success, message in
            if ((message) != nil) {
                self.sendEvent(withName: "onMessage", body: self.getMessageDict(message!))
            }
        }
    }
    
    @objc func sendMessage(_ message: String) -> Void {
        self.usedesk.sendMessage(message)
    }
    
    @objc func sendFile(_ message: String, fileName: String, fileType: String, contentBase64: String) -> Void {
        self.usedesk.sendMessage(message, withFileName: fileName, fileType: fileType, contentBase64: contentBase64)
    }
    
    func getMessageDict(_ message: RCMessage) -> NSMutableDictionary {
        let messageDic: NSMutableDictionary = [:]
        messageDic["text"] = message.text
        messageDic["me"] = !message.incoming
        messageDic["avatar"] = message.avatar
        if ((message.file) != nil) {
            messageDic["file"] = [
                "name": message.file?.name,
                "url": message.file?.content,
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
