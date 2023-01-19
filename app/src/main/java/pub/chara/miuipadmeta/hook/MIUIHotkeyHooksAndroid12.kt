package pub.chara.miuipadmeta.hook

import com.github.kyuubiran.ezxhelper.utils.*
import pub.chara.miuipadmeta.MyFakeList
import de.robv.android.xposed.XposedBridge

object MIUIHotkeyHooksAndroid12 : BaseHook() {
    override fun init() {
        try {
            // miui have a whitelist, only app in whitelist can receive meta key, else it is blocked
            // force every package to be in whitelist
            findMethod("com.android.server.policy.PhoneWindowManagerStubImpl") {
                name == "interceptKeyWithMeta"
            }.hookReturnConstant(false)

            //disable miui hotkeys
            findMethod("com.android.server.policy.MiuiKeyShortcutManager") {
                name == "getEnableKsFeature"
            }.hookReturnConstant(false)
            XposedBridge.log("MiuiPadMeta: MIUIHotkeyHooksAndroid12 success!")
        } catch (e: Throwable) {
            XposedBridge.log("MiuiPadMeta: MIUIHotkeyHooksAndroid12 failed!")
            XposedBridge.log(e)
        }
    }
}