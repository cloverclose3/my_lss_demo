package com.example.my_lss_demo2;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import com.baidu.cloud.bdrtmpsession.BDRtmpSessionBasic;
import com.baidu.cloud.bdrtmpsession.OnSessionEventListener;
import com.baidu.cloud.mediaprocess.listener.OnFinishListener;
import com.baidu.cloud.mediastream.config.LiveConfig;
import com.baidu.cloud.mediastream.session.LiveStreamSession;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class StreamingActivity extends AppCompatActivity  implements OnSessionEventListener
{
    private static final String TAG = "StreamingActivity";

    private LiveStreamSession mSession;

    // rtmp://pushtest.ivc.gz.baidubce.com/aa/bb
    private int role;
    private String room;
    private String url_push;
    private String url_play;
    private BDRtmpSessionBasic.UserRole mRole = BDRtmpSessionBasic.UserRole.Host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        win.requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_streaming);

        Intent intent=getIntent();
        role =intent.getIntExtra("role",0);
        mRole = getUserRoleByType(role); // 1 主播
        room =intent.getStringExtra("room");
        url_push =intent.getStringExtra("url_push");
        url_play =intent.getStringExtra("url_play");
        LiveConfig.Builder builder = new LiveConfig.Builder();
        // 1. 配置摄像头参数
        builder.setVideoWidth(720)//高度720
                .setVideoHeight(1280)//宽度1280
                .setCameraOrientation(90)//相机角度
                .setVideoFPS(15)//帧率15
                .setInitVideoBitrate(400000)//初始码率400kb
                .setMinVideoBitrate(100000)//最小码率100kb
                .setMaxVideoBitrate(800000)//最大码率800kb
                .setVideoEnabled(true)//是否开启视频
                .setAudioSampleRate(44100)//音频采样率
                .setAudioBitrate(64000)//音频码率
                .setAudioEnabled(true)//是否开启音频
                .setCameraId(Camera.CameraInfo.CAMERA_FACING_FRONT);//采用前置摄像头
        // 2. 初始化 session
        mSession = new LiveStreamSession(this, builder.build());
        // 3. 设置 rtmp listener
        mSession.setRtmpEventListener(this);
        // 4. 初始化设备
        mSession.setupDevice();
        // 5. 设置预览窗口控件
        SurfaceView local_preview;
        local_preview = findViewById(R.id.local_preview);
        mSession.setSurfaceHolder(local_preview.getHolder());
        // 6. 配置rtmp地址
        mSession.configRtmpSession(url_push + room + mRole.toString(), mRole);
        // 7. 之后，底层库会自动完成rtmp handshake connect和createStream等，如果一切成功会回调 onSessionConnected
    }

    private BDRtmpSessionBasic.UserRole getUserRoleByType(int type) {
        switch (type) {
            case 1:
                return BDRtmpSessionBasic.UserRole.Host;
            case 2:
                return BDRtmpSessionBasic.UserRole.Guest;
            default:
                return BDRtmpSessionBasic.UserRole.Audience;
        }
    }

    @Override
    public void onSessionConnected() {
        Log.d(TAG, "onSessionConnected: ");
        // 8. rtmp session建立完成，开始推流
        mSession.startStreaming();
    }

    @Override
    public void onError(int errorCode) {
        Log.d(TAG, "onError: " + errorCode);
    }

    @Override
    public void onConversationRequest(final String callerUrl, final String userId) {
        Log.d(TAG, "onConversationRequest:");
    }

    private OnFinishListener innerErrorListener = new OnFinishListener() {
        @Override
        public void onFinish(boolean isSuccess, int whatReason, String extraNote) {
            Log.d(TAG, "onFinish:");
        }
    };

    @Override
    public void onConversationStarted(String userId) {
        Log.d(TAG, "onConversationStarted: " + userId);
    }

    @Override
    public void onConversationFailed(final String userId, final FailureReason failReasonCode) {
        Log.d(TAG, "onConversationFailed: ");
    }

    @Override
    public void onConversationEnded(final String userId) {
        Log.d(TAG, "onConversationEnded: ");
    }
}