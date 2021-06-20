package com.example.page;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class time_table extends AppCompatActivity {
    float mon, tue, wed, thu, fri, set, sun;
    String nickname, ip, index;
    String time_str = "";
    private String[] times;
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    int port;
    boolean time_exist;

    void set_init(){
        ip = getIntent().getStringExtra("ip");
        port = getIntent().getIntExtra("port", 10000);
        index = getIntent().getStringExtra("index");
        nickname = getIntent().getStringExtra("nickname");
        time_str="";
    }

    void set_time(){
        Log.w("time",time_str);
        times = time_str.split(",");
        mon = Float.parseFloat(times[0]);
        tue = Float.parseFloat(times[1]);
        wed = Float.parseFloat(times[2]);
        thu = Float.parseFloat(times[3]);
        fri = Float.parseFloat(times[4]);
        set = Float.parseFloat(times[5]);
        sun = Float.parseFloat(times[6]);
    }

    void getTimes(){
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
                    dos.writeUTF("gettime,"+nickname);

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.w("버퍼", "버퍼생성 잘못됨");
                }

                Log.w("버퍼","버퍼생성 잘됨");

                try{
                    while(time_str.equals("")) {
                        time_str = dis.readUTF();
                    }
                    Log.w("시간","get_time");
                    Log.w("time_str",time_str);

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
        checkUpdate.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        BarChart chart = findViewById(R.id.btn_barchart);
        set_init();
        getTimes();
        while(time_str.equals("")){

        }
        Log.w("now","get_time이후");
        set_time();

        ArrayList ArrL = new ArrayList();

        ArrL.add(new BarEntry(mon,0));
        ArrL.add(new BarEntry(tue,1));
        ArrL.add(new BarEntry(wed,2));
        ArrL.add(new BarEntry(thu,3));
        ArrL.add(new BarEntry(fri, 4));
        ArrL.add(new BarEntry(set,5));
        ArrL.add(new BarEntry(sun,6));
        ArrayList year = new ArrayList();

        year.add("Mon");
        year.add("Tue");
        year.add("Wed");
        year.add("Thu");
        year.add("Fri");
        year.add("Sat");
        year.add("Sun");

        BarDataSet bardataset = new BarDataSet(ArrL, "How Long did your child play LoL");
        chart.animateY(5000);
        BarData data = new BarData(year, bardataset);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.setData(data);
        Log.w("time",String.valueOf(mon));
    }
}