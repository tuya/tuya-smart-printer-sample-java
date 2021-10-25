Tuya Smart Printer Sample for Android
========================
Feature overview
------------------------

Tuya Smart Printer Sample for Android provides a simple example of remote control for smart printers. The features of this smart printer are implemented based on Tuya IoTOS Embedded Wi-Fi and Bluetooth Low Energy (LE) SDK.

Tuya Smart Printer Sample for Android supports the following feature on top of [tuya-home-android-sdk-sample-java](https://github.com/tuya/tuya-home-android-sdk-sample-java):

- Control the smart printer. For example, edit rich text and control printing.

Get started
------------------------

#### I. Configure the project

1. Based on the instructions in [Preparation for Integration](https://developer.tuya.com/en/docs/app-development/android-app-sdk/preparation?id=Ka7mqlxh7vgi9), register a Tuya developer account, create an application, and then specify the package name of the sample as the target application name.

2. Based on the instructions in [Fast Integration with Smart Life App SDK for Android](https://developer.tuya.com/en/docs/app-development/android-app-sdk/integration/integrated?id=Ka69nt96cw0uj), configure the security image and reset the AppKey, and AppSecret.

#### II. UI

The following figure shows the UI of the smart printer panel:

<img src="https://github.com/tuya/tuya-smart-printer-sample-java/blob/master/IMG/sample_img_1.jpg" width="30%;" />
<br/>
<img src="https://github.com/tuya/tuya-smart-printer-sample-java/blob/master/IMG/sample_img_2.jpg" width="30%;" />

#### III. Usage notes

The smart printer panel implements the following control features:
- Edit rich text to be printed
- Convert content into a one-bit .bmp file.
- Upload the .bmp file to the cloud and get the download URL (depends on your implementation)
- Send the printing command
- Return the response of the printing request

The following figure shows the interaction process.

<br/>
<img src="https://github.com/tuya/tuya-smart-printer-sample-java/blob/master/IMG/sample_img_4.jpg" width="70%;" />

<br/>

**Things to note**
You must implement the following file upload feature:

```java
package com.tuya.appsdk.sample.printer;

public class FileUploader extends AbsFileUploader {

        @Override
    boolean uploadStart() {
        //TODO
        // Need to implement your own logic to upload files and get download links
        return false;
    }
}
```

The following list describes the data points (DPs) that are created for the smart printer:

101: the number of copies.

102: send the first URL: The app saves the image to a third-party storage location and generates the URL for the printer to download the content. The number of copies to be printed is included in the URL.

110: send the second URL: Concatenate both URLs into a finished URL.

103: start printing: The smart printer accesses the third-party storage location, downloads the image, and then gets ready for printing.

104: printing status: the reason for a printing failure that possibly occurs.

105: an alert of a shortage of paper

106: display of the battery level: A value from 10% to 20% is not displayed.

107: low battery level alert

108: the number of copies to be printed in the current task.

109: the printing result that appears on the app.

111: the checksum `hmac`. It is the request parameter for the file download API method and is used to check whether the downloaded file is integral.

112: the total size of the image `total_len`. It is the request parameter for the file download API method.

Feedback
------------------------

You can provide your feedback by submitting a **GitHub issue** or [**ticket**](https://service.console.tuya.com).

License
------------------------
Tuya Smart Printer Sample for Android is provided with the MIT license. For more information, see [LICENSE](LICENSE).

References
------------------------
- [richeditor-android](https://github.com/wasabeef/richeditor-android)
