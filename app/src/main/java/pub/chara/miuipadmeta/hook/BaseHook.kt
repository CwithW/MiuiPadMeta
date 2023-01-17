package pub.chara.miuipadmeta.hook

abstract class BaseHook {
    var isInit: Boolean = false
    abstract fun init()
}