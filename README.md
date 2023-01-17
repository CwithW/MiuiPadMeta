# MiuiPadMeta


为小米平板5系列恢复键盘Meta/Win键功能，同时禁用Alt-Tab快捷键，仅测试基于Android11的MIUI，不保证Android12可以运行
Restore Meta/Win key function on Mi Pad 5 series, and disable Alt-Tab hotkey, only tested Android11 MIUI, no promise Android12 will work 

PC模式下的快捷键依旧可用

### 实现方法 Implementation detail
1. 让`com.android.server.policy.MiuiKeyShortcutManager.getEnableKsFeature`固定返回false，MIUI的快捷键就不会使能
（或：`setprop persist.sys.enable_custom_shortcut_user 0`）
    实现方法：hook `com.android.server.policy.MiuiKeyShortcutManager.getEnableKsFeature` 固定返回false

2. 让`com.android.server.policy.PhoneWindowManager.interceptKeyBeforeDispatching` 不拦截meta key 就是包名为com.ss.android.lark.kami时的效果（MIUI给自家app开后门，只有自家app运行时不拦截meta key 其他app都拦截）
    实现方法：自定义类重写List的contains方法 替换掉`static List<String> sDeliveMetaKeyAppList`

3. 修改逻辑禁用安卓自带的alt-tab
    实现方法：hook `com.android.server.policy.PhoneWindowManager.interceptKeyBeforeDispatching` 如果是Alt-Tab则不继续运行

### 鸣谢 Special thanks

[MiuiPadESC](https://github.com/YifePlayte/MiuiPadESC) 配合此模块可以恢复ESC和禁用Win-D快捷键