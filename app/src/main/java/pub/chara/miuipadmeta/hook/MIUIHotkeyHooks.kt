package pub.chara.miuipadmeta.hook

import com.github.kyuubiran.ezxhelper.utils.*
import pub.chara.miuipadmeta.MyFakeList
import de.robv.android.xposed.XposedBridge

object MIUIHotkeyHooks : BaseHook() {
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
            }.hookBefore { param ->
                param.result = false
            }
            XposedBridge.log("MiuiPadMeta: MIUIHotkeyHooks success!")
        } catch (e: Throwable) {
            XposedBridge.log("MiuiPadMeta: MIUIHotkeyHooks failed!")
            XposedBridge.log(e)
        }
    }
}