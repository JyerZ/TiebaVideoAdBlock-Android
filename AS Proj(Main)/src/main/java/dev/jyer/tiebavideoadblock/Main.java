package dev.jyer.tiebavideoadblock;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Main implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.contains("com.baidu.tieba")){
            XposedBridge.log("Found Tieba");
            /*
            NetworkInfo.getType/getTypeName Hook
            Bypass ad data auto load and play with WIFI
            Anyway, it's a trickery
             */
            XposedHelpers.findAndHookMethod(NetworkInfo.class, "getType", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("Hooked getType");
                    param.setResult(ConnectivityManager.TYPE_MOBILE);
                }
            });
            XposedHelpers.findAndHookMethod(NetworkInfo.class, "getTypeName", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("Hooked getTypeName");
                    param.setResult("MOBILE");
                }
            });
        }
    }
}
