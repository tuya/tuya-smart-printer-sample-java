Tuya Android Smart Planter Sample
========================
功能概述
------------------------

Tuya Android Smart Printer Sample 提供简单示例用于对智能打印机的远程控制,该智能打印机基于Tuya IoTOS Embeded WiFi &Ble SDK实现。

Tuya Android Smart Printer Sample 在基于[tuya-home-android-sdk-sample-java](https://github.com/tuya/tuya-home-android-sdk-sample-java) 的功能之上实现以下功能：

- 智能打印机设备控制功能（富文本编辑、打印控制等）

开始
------------------------

#### 一、项目配置

1、根据[准备工作](https://developer.tuya.com/zh/docs/app-development/android-app-sdk/preparation?id=Ka7mqlxh7vgi9)文档说明，注册涂鸦开发者账号，并完成应用的创建，将Sample的包名替换成创建时设置的包名。

2、根据[集成SDK](https://developer.tuya.com/zh/docs/app-development/android-app-sdk/integration/integrated?id=Ka69nt96cw0uj)文档说明，集成安全图片以及重新设置AppKey和AppSecret。

#### 二、界面

智能打印机设备控制界面如下：

<img src="https://github.com/tuya/tuya-smart-printer-sample-java/blob/master/IMG/sample_img_5.jpg" width="30%;" />
<br/>
<img src="https://github.com/tuya/tuya-smart-printer-sample-java/blob/master/IMG/sample_img_6.jpg" width="30%;" />

#### 三、使用须知

智能打印机设备控制界面实现如下：
- 打印内容富文本编辑器
- 内容转单位图
- 单位图上传云端获取下载链接（这部分需开发者自己实现）
- 下发打印指令
- 打印反馈

交互图如下：
<img src="https://github.com/tuya/tuya-smart-printer-sample-java/blob/master/IMG/sample_img_3.png" width="70%;" />

<br/>

**特别注意**
开发者需自己实现文件上传功能，如下
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

智能打印机DP控制点：

101.打印份数

102.下发第一段url：app将图片存储到第三方，生成链接（包含打印份数）供设备下载

110.下发第二段url：将两段url拼接成完成的url

103.开始打印：打印机访问第三方，下载图片成功后打印机就绪开始下载

104.打印状态：可能导致打印失败的原因

105.缺纸警报

106.电量显示：10%~20%之间的值不显示

107.低电量报警

108.正在打印份数：显示当前正在打印的份数

109.打印结果：显示到app

111.校验值：hmac，文件下载API入参，用于校验下载后的文件是否完整

112.图片总大小：total_len，文件下载API入参

问题反馈
------------------------

您可以通过**Github Issue** 或通过[**工单**](https://service.console.tuya.com)来进行反馈您所碰到的问题

LICENSE
------------------------
Tuya Android Smart Printer Sample是在MIT许可下提供的。更多信息请参考[LICENSE](LICENSE)文件

Thanks
------------------------
- [richeditor-android](https://github.com/wasabeef/richeditor-android)

