package pub.chara.miuipadmeta

import android.os.Build
import com.github.kyuubiran.ezxhelper.init.EzXHelperInit
import com.github.kyuubiran.ezxhelper.utils.Log
import com.github.kyuubiran.ezxhelper.utils.Log.logexIfThrow
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage
import pub.chara.miuipadmeta.hook.*

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
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
                XposedBridge.log("MiuiPadMeta: Using Android 11 hooks.")
                initHooks(MIUIHotkeyHooksAndroid11)
                initHooks(AndroidHotkeyHooks)
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.S || Build.VERSION.SDK_INT == Build.VERSION_CODES.S_V2) {
                XposedBridge.log("MiuiPadMeta: Using Android 12 hooks.")
                initHooks(MIUIHotkeyHooksAndroid12)
                initHooks(AndroidHotkeyHooks)
            } else if (Build.VERSION.SDK_INT >= 33) { // Android 13
                XposedBridge.log("MiuiPadMeta: Using Android 13 hooks.")
                initHooks(MIUIHotkeyHooksAndroid13)
                initHooks(AndroidHotkeyHooks)
            }/* else { // unsupported os version
                XposedBridge.log("MiuiPadMeta: This version of Android is not supported. Only disable Alt-Tab will be used.")
                initHooks(AndroidHotkeyHooks)
            }*/

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
