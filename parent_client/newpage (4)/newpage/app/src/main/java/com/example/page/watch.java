package com.example.page;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class watch extends AppCompatActivity{

    Button btn_monitor, btn_monitor_end, btn_time_table, btn_edit_nickname, btn_delete_nickname, btn_end;
    Button btn_gps;
    String nickname, ip, index;
    String location="";
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    int port;
    int flag = 1;

    void delete(){
        Log.w("connect","연결 하는중");

        Thread checkUpdate = new Thread() {
            public void run() {
                try {
                    socket = new Socket(ip,port);
                    Log.w("서버 접속됨", "서버 접속됨");
                } catch (IOException e1) {
                    Log.w("서버접속못함", "서버접속못함");
                    e1.printStackTrace();
                }

                Log.w("edit 넘어가야 할 값 : ","안드로이드에서 서버로 연결요청");

                try {
                    dos = new DataOutputStream(socket.getOutputStream()); // output에 보낼꺼 넣음
                    dos.writeUTF("delete"+","+nickname);
                } catch (IOException  e) {
                    e.printStackTrace();
                    Log.w("버퍼", "버퍼생성 잘못됨");
                }
                nickname = "";
                Log.w("버퍼","버퍼생성 잘됨");

                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        checkUpdate.start();
    }

    void set_Initial() {
        btn_monitor = findViewById(R.id.btn_monitor);
        btn_monitor_end = findViewById(R.id.btn_monitor_end);
        btn_time_table = findViewById(R.id.btn_time_table);
        btn_edit_nickname = findViewById(R.id.btn_edit_nickname);
        btn_delete_nickname = findViewById(R.id.btn_delete_nickname);
        btn_end = findViewById(R.id.btn_end);
        btn_gps = findViewById(R.id.btn_get_gps);
        nickname = getIntent().getStringExtra("nickname");
        ip = getIntent().getStringExtra("ip");
        port = getIntent().getIntExtra("port", 10000);
        index = getIntent().getStringExtra("index");

    }

    public void show() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("긴급!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        builder.setContentText("아들이 롤중입니다!!!!!!!!!!!!!!!");

        builder.setColor(Color.RED);
        // 사용자가 탭을 클릭하면 자동 제거
        builder.setAutoCancel(true);

        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        // id값은
        // 정의해야하는 각 알림의 고유한 int값
        notificationManager.notify(1, builder.build());
    }

    void watch_start(){
        Log.w("connect","연결 하는중");
        Log.w("flag", String.valueOf(flag));
        Log.w("init_in_watch",ip);
        Thread check = new Thread() {
            public void run() {

                while (true) {
                    try {
                        socket = new Socket(ip, port);
                        Log.w("서버 접속", "서버 접속됨");
                    } catch (IOException e1) {
                        Log.w("서버접속못함", "서버접속못함");
                        e1.printStackTrace();
                    }

                    Log.w("edit 넘어가야 할 값 : ", "안드로이드에서 서버로 연결요청");

                    try {
                        dos = new DataOutputStream(socket.getOutputStream()); // output에 보낼꺼 넣음
                        dis = new DataInputStream(socket.getInputStream()); // input에 받을꺼 넣어짐
                        dos.writeUTF("watch,"+nickname);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.w("버퍼", "버퍼생성 잘못됨");
                    }

                    Log.w("버퍼", "버퍼생성 잘됨");

                    try {
                        int isGaming = 0;

                        Log.w("위치","서버값받는부분");
                        isGaming = (int) dis.read();
                        Log.w("서버에서 받아온 값 ",""+isGaming);
                        if(isGaming==1){
                            show();
                            break;
                        }
                        if(isGaming==2){
                            break;
                        }
                    } catch (Exception e) {
                    }
                    Log.w("받는", "받는거잘됨");

                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        check.start();
    }
    void watch_end(){
        Log.w("connect","연결 하는중");
        Thread check = new Thread() {
            public void run() {
                    try {
                        socket = new Socket(ip, port);
                        Log.w("서버 접속", "서버 접속됨");
                    } catch (IOException e1) {
                        Log.w("서버접속못함", "서버접속못함");
                        e1.printStackTrace();
                    }

                    Log.w("edit 넘어가야 할 값 : ", "안드로이드에서 서버로 연결요청");

                    try {
                        dos = new DataOutputStream(socket.getOutputStream()); // output에 보낼꺼 넣음
                        dis = new DataInputStream(socket.getInputStream()); // input에 받을꺼 넣어짐
                        dos.writeUTF("watch_end,"+nickname);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.w("버퍼", "버퍼생성 잘못됨");
                    }
                    Log.w("버퍼", "버퍼생성 잘됨");
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

        };
        check.start();
    }

    void request_gps(Intent gps){
        Log.w("connect","연결 하는중");

        Thread request_gps_to_server = new Thread() {
            public void run() {
                try {
                    socket = new Socket(ip,port);
                    Log.w("서버 접속됨", "서버 접속됨");
                } catch (IOException e1) {
                    Log.w("서버접속못함", "서버접속못함");
                    e1.printStackTrace();
                }

                Log.w("edit 넘어가야 할 값 : ","안드로이드에서 서버로 연결요청");

                try {
                    dos = new DataOutputStream(socket.getOutputStream()); // output에 보낼꺼 넣음
                    dis = new DataInputStream(socket.getInputStream());
                    dos.writeUTF("getgps"+","+nickname);
                } catch (IOException  e) {
                    e.printStackTrace();
                    Log.w("버퍼", "버퍼생성 잘못됨");
                }

                try{
                    location = dis.readUTF();
                    gps.putExtra("location",location);
                    Log.w("location",location);
                    startActivity(gps);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        request_gps_to_server.start();
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);
        set_Initial();

        btn_monitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("monitor_btn_on", "감시모드 버튼 ON ");
                watch_start();
            }
        });

        btn_monitor_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("monitor_btn_off", "감시모드 버튼 OFF ");
                watch_end();
            }
        });

        btn_time_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent show_time = new Intent(getApplicationContext(), time_table.class);
                show_time.putExtra("index", index);
                show_time.putExtra("ip", ip);
                show_time.putExtra("port", port);
                show_time.putExtra("nickname",nickname);
                startActivity(show_time);
            }
        });

        btn_edit_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit_nick = new Intent(getApplicationContext(), register_id.class);
                edit_nick.putExtra("ip", ip);
                edit_nick.putExtra("port", port);
                edit_nick.putExtra("index", index);
                startActivityForResult(edit_nick, 200);
            }
        });

        btn_delete_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // edit_nick일때의 request는 200
        if (requestCode == 200 && resultCode == RESULT_OK)
            nickname = data.getStringExtra("nickname");
    }

    public void btn_end_Click(View view){
        Intent intent = new Intent();
        intent.putExtra("nickname",nickname);
        setResult(RESULT_OK,intent);
        finish();
    }

    public void btn_get_gps(View view){
        Intent gps = new Intent(getApplicationContext(),get_gps.class);
        request_gps(gps);
        //gps.putExtra("location",location);
        //startActivity(gps);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent();
        intent.putExtra("nickname",nickname);
        setResult(RESULT_OK,intent);
        finish();
    }
}