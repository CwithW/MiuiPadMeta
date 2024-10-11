package pub.chara.miuipadmeta.hook

import android.view.KeyEvent
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.github.kyuubiran.ezxhelper.utils.hookReturnConstant

class TestHook:BaseHook() {
    override fun init() {
//        findMethod("com.android.server.policy.PhoneWindowManagerStubImpl") {
//            name == "interceptKeyWithMeta"
//        }.hookReturnConstant(false)

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

        /*
        XposedHelpers.findAndHookMethod("com.android.server.policy.BaseMiuiPhoneWindowManager", classLoader, "handleMetaKey", android.view.KeyEvent.class, new XC_MethodHook() {
    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);
    }
    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);
    }
});
         */
        findMethod("com.android.server.policy.BaseMiuiPhoneWindowManager") {
            name == "handleMetaKey"
        }.hookReturnConstant(null);
    }
}