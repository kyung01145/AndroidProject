package com.example.page;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.net.Socket;

//intent보낼시 main에서는 100,sub에서는 1로 하도록 설정
public class MainActivity extends AppCompatActivity {
    private String nick1,nick2,nick3;
    private Button btn_1, btn_2, btn_3, btn_find_nickname;
    private String ip = "192.168.0.2"; // 사용시 내 ip로 변경
    private int port = 10000; //
    //새로시작했을때 이전값들 설정 + 버튼값들 설정

    void Set_on_create(){
        SharedPreferences nicknames = getSharedPreferences("nickFile",MODE_PRIVATE);
        nick1 = nicknames.getString("nick1","");
        nick2 = nicknames.getString("nick2","");
        nick3 = nicknames.getString("nick3","");

        btn_1 = findViewById(R.id.btn1);
        btn_2 = findViewById(R.id.btn2);
        btn_3 = findViewById(R.id.btn3);
        btn_1.setText(nick1);
        btn_2.setText(nick2);
        btn_3.setText(nick3);
        //btn_find_nickname = findViewById(R.id.btn_find_nickname);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Set_on_create();


        btn_1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(nick1.equals("")) { //등록된 닉네임이 없을경우
                    Intent register_nick1 = new Intent(getApplicationContext(), register_id.class);
                    register_nick1.putExtra("ip",ip);
                    register_nick1.putExtra("port",port);
                    register_nick1.putExtra("index","First");
                    startActivityForResult(register_nick1,101);
                }

                else{
                    Intent nick1_page = new Intent(getApplicationContext(),watch.class);
                    nick1_page.putExtra("nickname",nick1);
                    nick1_page.putExtra("ip",ip);
                    nick1_page.putExtra("port",port);
                    nick1_page.putExtra("index","First");
                    startActivityForResult(nick1_page,101);
                }
            }
        });
        btn_2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(nick2.equals("")) { //등록된 닉네임이 없을경우
                    Intent register_nick2 = new Intent(getApplicationContext(), register_id.class);
                    register_nick2.putExtra("ip",ip);
                    register_nick2.putExtra("port",port);
                    register_nick2.putExtra("index","Second");
                    startActivityForResult(register_nick2,102);
                }

                else{
                    Intent nick2_page = new Intent(getApplicationContext(),watch.class);
                    nick2_page.putExtra("nickname",nick2);
                    nick2_page.putExtra("ip",ip);
                    nick2_page.putExtra("port",port);
                    nick2_page.putExtra("index","Second");
                    startActivityForResult(nick2_page,102);
                }
            }
        });

        btn_3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(nick3.equals("")) { //등록된 닉네임이 없을경우
                    Intent register_nick3 = new Intent(getApplicationContext(), register_id.class);
                    register_nick3.putExtra("ip",ip);
                    register_nick3.putExtra("port",port);
                    register_nick3.putExtra("index","Third");
                    startActivityForResult(register_nick3,103);
                }

                else{
                    Intent nick3_page = new Intent(getApplicationContext(),watch.class);
                    nick3_page.putExtra("nickname",nick3);
                    nick3_page.putExtra("ip",ip);
                    nick3_page.putExtra("port",port);
                    nick3_page.putExtra("index","Third");
                    startActivityForResult(nick3_page,103);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //101~103은 닉네임 받는 부분
        if(requestCode == 101){
            if(resultCode == RESULT_OK){
                nick1 = data.getStringExtra("nickname");
                btn_1.setText(nick1);
            }
        }
        if(requestCode == 102){
            if(resultCode == RESULT_OK){
                nick2 = data.getStringExtra("nickname");
                btn_2.setText(nick2);
            }
        }
        if(requestCode == 103){
            if(resultCode == RESULT_OK){
                nick3 = data.getStringExtra("nickname");
                btn_3.setText(nick3);
            }
        }

    }

    //stop시 기존 저장된 닉네임 데이터 저장
    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences sharedPreferences = getSharedPreferences("nickFile",MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nick1",nick1);
        editor.putString("nick2",nick2);
        editor.putString("nick3",nick3);

        editor.commit();
    }
}