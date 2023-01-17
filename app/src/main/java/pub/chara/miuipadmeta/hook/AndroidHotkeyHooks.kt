package pub.chara.miuipadmeta.hook

import android.content.Intent
import android.view.KeyEvent
import com.github.kyuubiran.ezxhelper.utils.*
import pub.chara.miuipadmeta.MyFakeList
import de.robv.android.xposed.XposedBridge

object AndroidHotkeyHooks : BaseHook() {
    override fun init() {
        try {
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

            XposedBridge.log("MiuiPadMeta: AndroidHotkeyHooks success!")
        } catch (e: Throwable) {
            XposedBridge.log("MiuiPadMeta: AndroidHotkeyHooks failed!")
            XposedBridge.log(e)
        }
    }
}