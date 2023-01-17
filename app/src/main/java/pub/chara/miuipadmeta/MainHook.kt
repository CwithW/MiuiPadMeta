package pub.chara.miuipadmeta

import android.os.Build
import com.github.kyuubiran.ezxhelper.init.EzXHelperInit
import com.github.kyuubiran.ezxhelper.utils.Log
import com.github.kyuubiran.ezxhelper.utils.Log.logexIfThrow
import pub.chara.miuipadmeta.hook.BaseHook
import pub.chara.miuipadmeta.hook.MIUIHotkeyHooks
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage
import pub.chara.miuipadmeta.hook.AndroidHotkeyHooks

private const val PACKAGE_NAME_HOOKED = "android"
private const val TAG = "MiuiPadMeta"

class MainHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName == PACKAGE_NAME_HOOKED) {
            // Init EzXHelper
            EzXHelperInit.initHandleLoadPackage(lpparam)
            EzXHelperInit.setLogTag(TAG)
            EzXHelperInit.setToastTag(TAG)
            // Init hooks
            if (Build.VERSION.SDK_INT != Build.VERSION_CODES.R) {
                XposedBridge.log("MiuiPadMeta: Not Android 11, not guaranteed to work.")
            }
            initHooks(MIUIHotkeyHooks)
            initHooks(AndroidHotkeyHooks)
        }
    }

    private fun initHooks(vararg hook: BaseHook) {
        hook.forEach {
            runCatching {
                if (it.isInit) return@forEach
                it.init()
                it.isInit = true
                Log.i("Inited hook: ${it.javaClass.simpleName}")
            }.logexIfThrow("Failed init hook: ${it.javaClass.simpleName}")
        }
    }
}
