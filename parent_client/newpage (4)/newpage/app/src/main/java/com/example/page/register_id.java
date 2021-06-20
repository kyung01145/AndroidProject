package com.example.page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import android.util.Log;


public class register_id extends Activity {

    String nickname = "";
    Intent fromMain = getIntent();
    String ip,index;
    int port;
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;

    void register(){
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
                    dis = new DataInputStream(socket.getInputStream()); // input에 받을꺼 넣어짐
                    dos.writeUTF("register,"+nickname+","+index);
                } catch (IOException  e) {
                    e.printStackTrace();
                    Log.w("버퍼", "버퍼생성 잘못됨");
                }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_id);

        Intent fromMain = getIntent();
        ip = fromMain.getExtras().getString("ip");
        port = fromMain.getExtras().getInt("port",10000);
        index = fromMain.getExtras().getString("index");
    }

    public void btn_register_Click(View view){
        EditText input = (EditText)findViewById(R.id.input);
        nickname = input.getText().toString();
        register();
    }

    public void btn_end_Click(View view){
        Intent intent = new Intent();
        intent.putExtra("nickname",nickname);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent();
        intent.putExtra("nickname",nickname);
        setResult(RESULT_OK,intent);
        finish();
    }
}