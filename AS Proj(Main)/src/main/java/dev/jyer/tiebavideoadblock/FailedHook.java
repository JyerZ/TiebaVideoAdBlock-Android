package dev.jyer.tiebavideoadblock;

import android.app.Application;
import android.content.Context;
import android.widget.MediaController;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import com.baidu.tbadk.TbPageContext;
import tbclient.VideoInfo;

public class FailedHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        //MutilDexHook
        if (lpparam.packageName.contains("com.baidu.tieba")) {
            XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    ClassLoader cl = ((Context)param.args[0]).getClassLoader();

                    Class<?> hookClass;
                    try {
                        hookClass = cl.loadClass("com.baidu.tieba.ad.play.VideoControllerView");
                    } catch (Exception e) {
                        XposedBridge.log("NotFound VideoControllerView");
                        XposedBridge.log(e);
                        return;
                    }
                    XposedBridge.log("Found VideoControllerView");
                    try {
                        XposedHelpers.findAndHookMethod(hookClass, "setPlayer", MediaController.MediaPlayerControl.class, new XC_MethodReplacement() {
                            @Override
                            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                                XposedBridge.log("Hooked setPlayer");
                                return null;
                            }
                        });
                    }catch (Exception e){
                        XposedBridge.log(e);
                    }

                    Class<?> hookClass1;
                    try {
                        hookClass1 = cl.loadClass("com.baidu.tieba.ad.play.a");
                    } catch (Exception e) {
                        XposedBridge.log("NotFound play.a");
                        XposedBridge.log(e);
                        return;
                    }
                    XposedBridge.log("Found play.a");
                    try {
                        XposedHelpers.findAndHookMethod(hookClass1, "setVideoPath", String.class, new XC_MethodReplacement() {
                            @Override
                            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                                XposedBridge.log("Hooked setVideoPath");
                                return null;
                            }
                        });
                    }catch (Exception e){
                        XposedBridge.log(e);
                    }

                    Class<?> hookClass2;
                    try {
                        hookClass2 = cl.loadClass("com.baidu.tieba.recapp.view.DistributeVideoView");
                    } catch (Exception e) {
                        XposedBridge.log("NotFound DistributeVideoView");
                        XposedBridge.log(e);
                        return;
                    }
                    XposedBridge.log("Found DistributeVideoView");
                    XposedHelpers.findAndHookMethod(hookClass, "setData", VideoInfo.class, new XC_MethodHook() {

                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            XposedBridge.log("Hooked setData1");
                            for (int i = 0;i<param.args.length;i++){
                                param.args[i] = null;
                            }
                        }
                    });
                    XposedHelpers.findAndHookMethod(hookClass2, "setData", VideoInfo.class,int.class,TbPageContext.class, new XC_MethodHook(){
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            XposedBridge.log("Hooked setData2");
                            for (int i = 0;i<param.args.length;i++){
                                param.args[i] = null;
                            }
                        }
                    });
                    XposedHelpers.findAndHookMethod(hookClass2, "setData",VideoInfo.class,int.class,int.class,int.class, new XC_MethodHook(){
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            XposedBridge.log("Hooked setData3");
                            for (int i = 0;i<param.args.length;i++){
                                param.args[i] = null;
                            }
                        }
                    });

                }
            });
        }

        //ClassLoaderHook
        XposedBridge.hookAllMethods(ClassLoader.class, "loadClass", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                Class<?> cls = (Class<?>) param.getResult();
                if (cls!=null) {
                    String name = cls.getName();
                    if (name.contains("com.baidu.tieba")){
                        if (name.contains("com.baidu.tieba.recapp.view.DistributeVideoView")){
                            XposedBridge.log("Found DistributeVideoView");
                            XposedBridge.hookAllMethods(cls,"setData",new XC_MethodHook() {
                                @Override
                                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                    XposedBridge.log("Hooked setData");
                                    for (int i = 0;i<param.args.length;i++){
                                        param.args[i] = null;
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });

    }
}
