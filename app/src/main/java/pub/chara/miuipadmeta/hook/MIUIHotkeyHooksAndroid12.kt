package pub.chara.miuipadmeta.hook

import android.view.KeyEvent
import com.github.kyuubiran.ezxhelper.utils.*
import pub.chara.miuipadmeta.MyFakeList
import de.robv.android.xposed.XposedBridge

object MIUIHotkeyHooksAndroid12 : BaseHook() {
    override fun init() {
        try {
            // 有这两个函数 但是都TM不起作用。。。源码反编译不完整 也看不太懂 应该interceptKeyWithMeta就是决定是否拦截meta的 和Android11的白名单机制一样 但是他就是P用没有。。。
            /*
            findMethod("com.android.server.policy.PhoneWindowManagerStubImpl") {
                name == "interceptKeyWithMeta"
            }.hookReturnConstant(false)

            findMethod("com.android.server.policy.PhoneWindowManagerStubImpl") {
                name == "isPad"
            }.hookReturnConstant(false)
            */
            //所以和alt-tab的实现方法一样：直接hook 如果有按下meta键就直接返回0不拦截 简单粗暴
            findMethod("com.android.server.policy.PhoneWindowManager") {
                name == "interceptKeyBeforeDispatching"
            }.hookBefore { param ->
                run {
                    val arg1: KeyEvent = param.args[1] as KeyEvent;
                    // meta key and alt-tab 两个放同一个hook函数里 少用一个hook函数 提高一点性能
                    if ((arg1.isMetaPressed) || (arg1.isAltPressed && arg1.keyCode == 61)) {
                        param.result = 0L;
                    }
                }
            }

            // disable miui hotkeys
            // 用orNull为了兼容不是miui的设备 也可以禁用alt-tab
            var methodOrNull =
                findMethodOrNull("com.android.server.policy.MiuiKeyShortcutManager") {
                    name == "getEnableKsFeature"
                }
            if (methodOrNull == null) {
                XposedBridge.log("MiuiPadMeta: MIUIHotkeyHooksAndroid12 hook getEnableKsFeature failed! (method not found)")
            }else{
                methodOrNull.hookReturnConstant(false)
            }
            XposedBridge.log("MiuiPadMeta: MIUIHotkeyHooksAndroid12 success!")
        } catch (e: Throwable) {
            XposedBridge.log("MiuiPadMeta: MIUIHotkeyHooksAndroid12 failed!")
            XposedBridge.log(e)
        }
    }
}