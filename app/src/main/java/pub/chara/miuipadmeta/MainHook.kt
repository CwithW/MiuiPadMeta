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
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                XposedBridge.log("MiuiPadMeta: Using Android 12+ hooks.")
                initHooks(MIUIHotkeyHooksAndroid12)
            }

//            initHooks(TestHook())
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
