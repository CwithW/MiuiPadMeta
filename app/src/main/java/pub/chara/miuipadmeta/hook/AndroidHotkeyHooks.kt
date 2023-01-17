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
                    // alt-tab and win-d
                    if ((arg1.isAltPressed && arg1.keyCode == 61) || (arg1.isMetaPressed && arg1.keyCode == 32)) {
                        param.result = 0L;
                    }
                }
            }
/*            //test: intent constructor hooker
            findAllConstructors("android.content.Intent"){
                true
            }.forEach { constructor -> run{
                constructor.hookBefore { param -> run{
                    try{
                        1/0
                    }catch(e:Throwable){
                        XposedBridge.log("new Intent")
                        XposedBridge.log(e)
                    }
                } }
            } }*/


            XposedBridge.log("MiuiPadMeta: AndroidHotkeyHooks success!")
        } catch (e: Throwable) {
            XposedBridge.log("MiuiPadMeta: AndroidHotkeyHooks failed!")
            XposedBridge.log(e)
        }
    }
}