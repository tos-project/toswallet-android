TosWallet for Android
===============

Our goal is to support every cryptocurrency with an active development team. Store all the best cryptocurrency through a single app, without sacrificing security. Private keys are stored on your own device. Instead of having one backup file for every coin, you get a master key that can be memorized or stored on a piece of paper. Restore all wallets from a single recovery phrase.

TODOs:

* Create instrumentation tests to test a signed APK


## Building the app

Install [Android Studio](https://developer.android.com/sdk/installing/studio.html). Once it is
running, import TosWallet-android by navigating to where you cloned or downloaded it and selecting
settings.gradle. When it is finished importing, click on the SDK Manager ![SDK Manager](https://developer.android.com/images/tools/sdk-manager-studio.png). You will need to install SDK version 21.

<br/>
Make sure that you have JDK 7 installed before building. You can get it [Here](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html). Once you have that installed, navigate to File > Project Structure > SDK Location and change the path of your current JDK to the path of the new JDK. **The project will not build with JDK 8**. 

<br/>
Once it is finished installing, you will need to enable developer options on your phone. To do so,
go into settings, About Phone, locate your Build Number, and tap it 7 times, or until it says
"You are now a Developer". Then, go back to the main Settings screen and scroll once again to the
bottom. Select Developer options and enable USB Debugging.

<br/>
Then plug your phone into your computer and hit the large green play button at the top of
Android Studio. It will load for a moment before prompting you to select which device to install
it on. Select your device from the list, and hit continue.

**NOTE**
If you are attempting to build on a Lollipop emulator, please ensure that you are using *Android 5.*.* armeabi-v7*. It will not build on an x86/x86_64 emulator.

## Contributions

Your contributions are very welcome, be it translations, extra features or new coins support. Just
fork this repo and create a pull request with your changes.

For new coins support read this [document](https://gist.github.com/erasmospunk/4dac398935e9dc86eed1).
Generally you need:

* Electrum-server support
* Coinomi core support
* A beautiful vector icon
* Entry to the [BIP44 registry](https://github.com/satoshilabs/docs/blob/master/slips/slip-0044.rst) that is maintained by Satoshi labs


## Releasing the app

To release the app follow the steps.

1) Change the following:

* in strings.xml app_name string to "toscoin" and app_package to com.toscoin.wallet
* in build.gradle the package from "com.toscoin.wallet.dev" to "com.toscoin.wallet"
* in AndroidManifest.xml the android:icon to "ic_launcher" and all "com.toscoin.wallet.dev.*"  to "com.toscoin.wallet.*"
* remove all ic_launcher_dev icons with `rm wallet/src/main/res/drawable*/ic_launcher_dev.png`
* setup ACRA and ShapeShift

2) Then in the Android Studio go to:

* Build -> Clean Project and without waiting...
* Build -> Generate Signed APK and generate a signed APK. ... and now you can grab yourself a nice cup of tea.

3) Test this APK (TODO: with instrumentation tests).

For now test it manually by installing it `adb install -r wallet/wallet-release.apk`

> This one is in the TODOs and must be automated
> because it will be here that you take a break ;)

4) Upload to Play Store and check for any errors and if all OK publish in beta first.

5) Create a GIT release commit:

* Create a commit with the log entry similar to the description in the Play Store
* Create a tag with the version of the released APK with `git tag vX.Y.Z <commit-hash>`

