package com.example.my_lss_demo2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity   implements View.OnClickListener {
    // 默认的推拉流地址以及房间号，请在baidu LSS 平台申请
    private static final String PUSH_URL = "rtmp://xx.xx.xx.xx.com/xx/xx";
    private static final String PLAY_URL = "rtmp://xx.xx.xx.xx.com/xx/xxxxxx";
    private static final String ROOM = "123";

    private static final int REQUEST_PERMISSION_CODE_CAMERA = 100;
    private static final int REQUEST_PERMISSION_CODE_AUDIO = 101;
    private static final int REQUEST_PERMISSION_CODE_EXTSTO = 102;

    EditText push_url_txt;
    EditText pull_url_txt;
    EditText room_num_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions();

        push_url_txt=(EditText)findViewById(R.id.et_url_push);
        pull_url_txt=(EditText)findViewById(R.id.et_url_pull);
        room_num_txt=(EditText)findViewById(R.id.et_room);
        push_url_txt.setText(PUSH_URL);
        pull_url_txt.setText(PLAY_URL);
        room_num_txt.setText(ROOM);

        Button btn1 = (Button) findViewById(R.id.btn_push);
        Button btn2 = (Button) findViewById(R.id.btn_play);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CODE_CAMERA);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_PERMISSION_CODE_AUDIO);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE_EXTSTO);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE_CAMERA:
                if (grantResults[0] == -1) {
                    Toast toast = Toast.makeText(this, "请选择权限", Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                }
                break;
            case REQUEST_PERMISSION_CODE_EXTSTO:
                if (grantResults[0] == -1) {
                    Toast toast = Toast.makeText(this, "请选择权限", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
            case REQUEST_PERMISSION_CODE_AUDIO:
                if (grantResults[0] == -1) {
                    Toast toast = Toast.makeText(this, "请选择权限", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_push:
                String pushUrl = push_url_txt.getText().toString();
                String playUrl = pull_url_txt.getText().toString();
                String mRoom = room_num_txt.getText().toString();

                Intent i = new Intent(this, StreamingActivity.class);
                i.putExtra("role", 1);
                i.putExtra("room", mRoom);
                i.putExtra("url_play", playUrl);
                i.putExtra("url_push", pushUrl);
                startActivity(i);
                break;
            case R.id.btn_play:
                break;
            default:
                break;
        }
    }
}
