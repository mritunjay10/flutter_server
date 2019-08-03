package in.indilabz.flutter_task;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import io.flutter.app.FlutterActivity;
import io.flutter.embedding.engine.dart.DartExecutor;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugins.GeneratedPluginRegistrant;
import io.flutter.view.AccessibilityBridge;
import io.flutter.view.FlutterCallbackInformation;
import io.flutter.view.FlutterMain;
import io.flutter.view.FlutterNativeView;
import io.flutter.view.FlutterRunArguments;
import io.flutter.view.FlutterView;

public class MainActivity extends FlutterActivity implements MethodChannel.MethodCallHandler{

    public static final String CHANNEL = "indilabz.in/service";

    private FlutterService appService;

    private boolean isConnected = false;

    //private FlutterNativeView flutterView;
    private FlutterView flutterView;
    //private PluginRegistrantCallback callback;
    //private PluginRegistry registry;
    //private FlutterCallbackInformation callbackInformation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GeneratedPluginRegistrant.registerWith(this);
        flutterView = getFlutterView();

        new MethodChannel(getFlutterView(), CHANNEL).setMethodCallHandler(this);

    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

            FlutterService.FlutterServiceBinder binder = (FlutterService.FlutterServiceBinder) service;
            appService = binder.getService();
            isConnected = true;

            final int[] i = {0};

            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    //your method
                    i[0]++;

                    /*FlutterNativeView backgroundView = new FlutterNativeView(getApplicationContext(),true);
                    GeneratedPluginRegistrant.registerWith(backgroundView.getPluginRegistry());
                    FlutterRunArguments bvargs = new FlutterRunArguments();
                    bvargs.bundlePath = FlutterMain.findAppBundlePath(getApplicationContext());
                    bvargs.entrypoint = "ble_service_main";
                    backgroundView.runFromBundle(bvargs);

                    Log.d("PATH", bvargs.bundlePath);*/

                    /* new MethodChannel(flutterView, CHANNEL)
                             .setMethodCallHandler(new MethodChannel.MethodCallHandler() {
                                 @Override
                                 public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {

                                 }
                             });*/

                    MethodChannel methodChannel=new MethodChannel(flutterView, CHANNEL);
                    methodChannel.invokeMethod("HELLO_THERE - "+ i[0],"");

                    Log.d("TAG_SCHEDULED", "HELLO_THERE"+ i[0]);

                }
            }, 0, 1000);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

        }
    };


    @Override
    public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {

        if(!isConnected){

            Intent service = new Intent(this, FlutterService.class);
            startService(service);
            bindService(service, connection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //FlutterMain.ensureInitializationComplete();
    }
}
