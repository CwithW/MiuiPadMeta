# MiuiPadMeta

为MIUI设备恢复键盘Meta/Win键功能（禁用掉小米的快捷键），同时禁用Alt-Tab快捷键。这样就可以在远程桌面里用Meta键和Alt-Tab了。适配小米平板5系列以及Android12+的MIUI手机。

Restore Meta/Win key function on MIUI devices(disable Xiaomi hotkeys), and disable Alt-Tab hotkey. Allows you to use Meta and Alt-Tab key in Remote Desktop。Adapted for Xiaomi Pad 5 Series and MIUI phones with Android12+.



### 系统版本支持情况 OS version support

| 系统版本           | 恢复Meta键 | 禁用Alt-Tab |
| ------------------ | ---------- | ----------- |
| MIUI12.5 Android11 | ? *         | ?           |
| MIUI13 Android11   | √ **         | √           |
| MIUI13 Android12   | √          | √           |
| MIUI14 Android13   | √          | √           |
| HyperOS         | √? ****       | √? ***        |
| 非MIUI系统         | N/A        | ? ***        |

√ 表示经过测试，目前支持

? 表示没有经过测试，可能支持

× 表示经过测试，目前不支持

> *Android11上仅支持小米平板5系列，不支持MIUI手机
>
> **MIUI for Pad Android11: PC模式下的快捷键依旧可用，PC模式的快捷键是另一套逻辑实现，请退出PC模式
>
> ***禁用Alt-Tab理论上在所有Android系统上支持。
>
> **** 小米在HyperOS加强了对Bootloader权限的管控，我暂未将我的Xiaomi Pad 5 Pro设备更新到HyperOS，所以无法测试。
>
> [DisableAltTab](https://modules.lsposed.org/module/pub.chara.disablealttab) 模块为独立出来的禁用Alt-Tab功能，适配所有Android设备。



#### 在以下系统版本实机测试可以使用：

小米平板5Pro MIUI稳定版13.0.8.0 Android11

小米平板5Pro MIUI稳定版13.0.10 Android11

小米平板5Pro MIUI稳定版13.1.4.0 Android12

小米平板5Pro MIUI开发版14.0.23.1.9 Android13

红米K50 MIUI稳定版13.0.24 Android12

红米K50 MIUI稳定版14.0.4 Android13



### 鸣谢 Special thanks

本模块借鉴了 [MiuiPadESC](https://github.com/YifePlayte/MiuiPadESC) 的框架代码。

[MiuiPadESC](https://modules.lsposed.org/module/com.yifeplayte.miuipadesc) 配合此模块可以恢复ESC和禁用Win-D快捷键，实现远程桌面下全部键位可用

[MaxMiPadInput](https://modules.lsposed.org/module/com.yifeplayte.maxmipadinput) MIUI14 下 MiuiPadESC失效 需要用这个模块恢复ESC

### 实现方法 Implementation detail

#### Android11

1. 让`com.android.server.policy.MiuiKeyShortcutManager.getEnableKsFeature`固定返回false，MIUI的快捷键就不会使能
   （或：`setprop persist.sys.enable_custom_shortcut_user 0`）

   实现方法：hook `com.android.server.policy.MiuiKeyShortcutManager.getEnableKsFeature` 固定返回false

2. 让`com.android.server.policy.PhoneWindowManager.interceptKeyBeforeDispatching` 不拦截meta key 就是包名为com.ss.android.lark.kami时的效果（MIUI给自家app开后门，只有自家app运行时不拦截meta key 其他app都拦截）

   实现方法：自定义类重写List的contains方法 替换掉`static List<String> sDeliveMetaKeyAppList`

3. 修改逻辑禁用安卓自带的alt-tab

   实现方法：hook `com.android.server.policy.PhoneWindowManager.interceptKeyBeforeDispatching` 如果是Alt-Tab则不继续运行

#### Android12

1. 同Android11 （但是这个类不在services.jar里在miui-services.jar里）

   (内部实现从setprop变成了`settings put system is_custom_shortcut_effective 0`)

2. 存在`com.android.server.policy.PhoneWindowManagerStubImpl.DELIVE_META_APPS` 被`com.android.server.policy.PhoneWindowManagerStubImpl.interceptKeyWithMeta()`使用 但是这个函数P用没有

   所以和alt-tab的实现方法一样：直接hook `com.android.server.policy.PhoneWindowManager.interceptKeyBeforeDispatching` 如果有按下meta键就直接返回0不拦截 简单粗暴

3. 同Android11

#### Android13

​	同Android12 不需要单独适配

#### HyperOS

部分功能（如按下Meta唤出Dock栏）放到了`com.android.server.policy.BaseMiuiPhoneWindowManager.handleMetaKey`里处理（在miui-services.jar）

win+xx快捷键，没找着在哪handle的，建议小米设置里关了得了




### 截图 Screenshot

![Screenshot_2023-01-18-03-08-54-671_com.microsoft.rdc.androidx](README.assets/Screenshot_2023-01-18-03-08-54-671_com.microsoft.rdc.androidx-16741303149715.jpg)
![Screenshot_2023-01-18-03-09-31-674_com.microsoft.rdc.androidx](README.assets/Screenshot_2023-01-18-03-09-31-674_com.microsoft.rdc.androidx.jpg)

### 第三方开源引用 Open Source License

##### Apache License 2.0

[KyuubiRan/EzXHelper](https://github.com/KyuubiRan/EzXHelper)