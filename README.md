
# react-native-usedesk

## Getting started

`$ npm install react-native-usedesk --save`

### Mostly automatic installation

`$ react-native link react-native-usedesk`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-usedesk` and add `RNUsedesk.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNUsedesk.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNUsedeskPackage;` to the imports at the top of the file
  - Add `new RNUsedeskPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-usedesk'
  	project(':react-native-usedesk').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-usedesk/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-usedesk')
  	```

#### Windows
[Read it! :D](https://github.com/ReactWindows/react-native)

1. In Visual Studio add the `RNUsedesk.sln` in `node_modules/react-native-usedesk/windows/RNUsedesk.sln` folder to their solution, reference from their app.
2. Open up your `MainPage.cs` app
  - Add `using Usedesk.RNUsedesk;` to the usings at the top of the file
  - Add `new RNUsedeskPackage()` to the `List<IReactPackage>` returned by the `Packages` method


## Usage
```javascript
import RNUsedesk from 'react-native-usedesk';
```
  