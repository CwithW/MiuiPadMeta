package pub.chara.miuipadmeta.hook

import android.view.KeyEvent
import com.github.kyuubiran.ezxhelper.utils.*
import pub.chara.miuipadmeta.MyFakeList
import de.robv.android.xposed.XposedBridge

object MIUIHotkeyHooksAndroid11 : BaseHook() {
    override fun init() {
        try {
            // miui have a whitelist, only app in whitelist can receive meta key, else it is blocked
            // force every package to be in whitelist
            findField("com.android.server.policy.PhoneWindowManager"){
                name == "sDeliveMetaKeyAppList"
            }.set(null, MyFakeList())

            //disable miui hotkeys
            findMethod("com.android.server.policy.MiuiKeyShortcutManager") {
                name == "getEnableKsFeature"
            }.hookReturnConstant(false)
            XposedBridge.log("MiuiPadMeta: MIUIHotkeyHooksAndroid11 success!")


            //disable alt-tab
            //this works for any android version
            findMethod("com.android.server.policy.PhoneWindowManager") {
                name == "interceptKeyBeforeDispatching"
            }.hookBefore { param ->
                run {
                    val arg1: KeyEvent = param.args[1] as KeyEvent;
                    // alt-tab
                    if ((arg1.isAltPressed && arg1.keyCode == 61)) {
                        param.result = 0L;
                    }
                }
            }
        } catch (e: Throwable) {
            XposedBridge.log("MiuiPadMeta: MIUIHotkeyHooksAndroid11 failed!")
            XposedBridge.log(e)
        }
    }
}