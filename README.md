
# react-native-usedesk

Библиотека для работы с нативным SDK Usedesk
(https://github.com/usedesk/UseDeskSwift && https://github.com/usedesk/Android_SDK) 

### Установка

```sh
yarn add react-native-usedesk
```

### IOS
#### Добавить в Podfile проекта
```sh
 pod 'QBImagePickerController', '~> 3.4', :modular_headers => true
 pod 'UseDesk_SDK_Swift', :git => 'https://github.com/usedesk/UseDeskSwift.git', :modular_headers => true
 pod 'MBProgressHUD', :modular_headers => true
 pod 'NYTPhotoViewer', :modular_headers => true
 pod 'ProgressHUD', :modular_headers => true
 pod 'UIAlertController+Blocks', :modular_headers => true
```
#### и до target 'App' do
```sh
 install! 'cocoapods', :disable_input_output_paths => true
 target 'App' do
```
### Использование
```
 const chatConfig: UseDeskConfig = {
     companyID: '1111111',
     url: 'test.usedesk.ru',
     url: 'test.usedesk.ru/sendFiles',
     port: '443',
     api_token: 'api_token',
     email: 'test@test.com',
     phone: '+7999999999',
     name: `Тестов Тестер`,
     nameChat: 'Чат с поддержкой',
     signature: 'signature',
   };
```
