package tw.com.jingyu.messagedemo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private Button toastBtn,snackBarBtn,alertBtn1,alertBtn2,alertBtn3,notifyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
    }

    public void findViews(){
        toastBtn = findViewById(R.id.toastBtn);
        snackBarBtn = findViewById(R.id.snackBarBtn);
        alertBtn1 = findViewById(R.id.alertBtn1);
        alertBtn2 = findViewById(R.id.alertBtn2);
        alertBtn3 = findViewById(R.id.alertBtn3);
        notifyBtn = findViewById(R.id.notifyBtn);

        toastBtn.setOnClickListener(v -> {

            //Toast.makeText:文字框會顯示在正下方
            //context:要作用在哪個畫面  text:顯示的訊息  Toast.LENGTH_SHORT:訊息持續時間
            Toast.makeText(MainActivity.this,"這是toastBtn",Toast.LENGTH_SHORT).show();
        });

        snackBarBtn.setOnClickListener(v -> {

            //Snackbar.make:文字框會顯示在正下方
            //同35行Toast.makeText，傳入參數v表示要作用在哪個View
            Snackbar.make(v,"這是snackBarBtn",Snackbar.LENGTH_SHORT).show();

            //可互動Snackbar:文字框最右方會新增跳頁連結
            Snackbar.make(v,"這是互動型snackBar",Snackbar.LENGTH_SHORT).setAction("開啟",v1 -> {

                //Intent(來源頁面,目標頁面):跳頁
                Intent intent = new Intent(MainActivity.this,MainActivity2.class);

                //啟動跳頁動作
                startActivity(intent);

            }).setActionTextColor(Color.RED).show();
        });

        alertBtn1.setOnClickListener(v -> {

            //AlertDialog.Builder(建立在哪個畫面):建立對話框
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            //設定標題(setTitle)，設定內容(setMessage)，顯示(show)
            //跳出的提示框只要點到旁邊螢幕便會消失
            builder.setTitle("一般對話框")
                    .setMessage("一般對話框(無按鈕)")
                    .show();
        });

        alertBtn2.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            //setCancelable(false):是否可按旁邊螢幕做取消動作(false表不行)
            builder.setTitle("詢問對話框")
                    .setMessage("是否跳轉？")
                    .setCancelable(false);

            /*
            *   builder能設定的按鈕有三種(確定、否定、其他)
            *   確定：builder.setPositiveButton
            *   否定：builder.setNegativeButton
            *   其他：builder.setNeutralButton
            */

            //用於進行確定動作的按鈕
            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                //which:記錄由哪個按鈕進行動作
                public void onClick(DialogInterface dialog, int which) {
                    //選擇「是」後，alertBtn2按鈕會從「詢問對話框」變成「選擇 YES」
                    alertBtn2.setText("選擇 YES");
                }
            });

            //用於進行否定動作之按鈕
            builder.setNegativeButton("否",(dialog,which) -> {
                //選擇「否」後，alertBtn2按鈕會從「詢問對話框」變成「選擇 NO」
                alertBtn2.setText("選擇 NO");
            });

            //進行其他選項之按鈕
            builder.setNeutralButton("稍後詢問",(dialog, which) -> {
                alertBtn2.setText("我考慮一下");
            });

            builder.show();
        });

        alertBtn3.setOnClickListener(v -> {
            String[] items = {"買一送醫","滿千送百","謝謝再見"};
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("請選擇")
                    .setCancelable(false);

            builder.setItems(items, (dialog, which) -> {
                //which:從0開始(index的概念)
                alertBtn3.setText(items[which]);
            });

            builder.show();
        });

        //推播(下拉式提示訊息)
        notifyBtn.setOnClickListener(v -> {
            NotifyMessage("設定標題","設定副標題","設定內容");
        });

    }

    //推播的版本設定(Android 8.0前適用)，程式碼178行開始
    @SuppressLint("WrongConstant")
    //設定推播的程式碼 NotifyMessage (自己推播自己)
    private void NotifyMessage(String title , String subTitle , String msg){

        //Android 8.0 版本後需要的變數
        String channelID = "Lcc";   //透過channelID進行推播
        String channelName = "Android 8.0版本後的語法";   //給使用者看的

        //所有 Android 版本適用
        int notifyID = 1;   //推播的依據

        //設定使用推播服務
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(MainActivity.this,channelID);

        builder.setContentTitle(title)  //設定標題
                .setSubText(subTitle)   //設定副標題，可省略
                .setContentText(msg)    //設定推播內容
                .setSmallIcon(R.mipmap.ic_launcher) //圖示
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);  //優先權

        //判斷版本(8.0之前或之後)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Android 8.0 之後適用的程式碼 ( Build.VERSION_CODES.O = Android 8.0 )

            //IMPORTANCE_HIGH:提醒的層級重要性_最高(會發出音效及震動)
            NotificationChannel channel = new NotificationChannel(
                    channelID,channelName,NotificationManager.IMPORTANCE_HIGH);

            builder.setChannelId(channelID);
            manager.createNotificationChannel(channel);

        }else{
            //Android 8.0 之前適用的程式碼

            builder.setDefaults(Notification.DEFAULT_ALL)
                    .setVisibility(Notification.VISIBILITY_PUBLIC); //需要新增標註(程式碼141行)
        }

        //執行推播
        manager.notify(notifyID,builder.build());
    }

    /*  遠端推播(firebase)，從雲端發送訊息給手機：
            網頁搜尋 Google Firebase Console > 建立專案 > 關閉分析(Google Analytics) > 建立專案
            > Cloud Messaging >選擇平台(Ex:Android)
            > 註冊應用程式的「Android套件名稱」填:這支程式的package(Ex:tw.com.jingyu.messagedemo)
            > 另外兩個選填 > 註冊 > 下載google-services.json > 將json檔放在專案/app/src目錄下 > 繼續

            > 開啟 Gradle Scripts 下的 build.gradle(Project:專案名稱)
            > 對比 build.gradle 和網頁的 Firebase SDK :

                buildscript {
                    repositories {
                        google()    V這行要有
                    }
                    dependencies {
                        classpath 'com.google.gms:google-services:4.3.8'    V這行要有(以網頁的為準)
                    }
                }
                allprojects {
                    repositories {
                        google()    V這行要有
                    }
                }

            > 開啟 Gradle Scripts 下的 build.gradle(Module:專案名稱.app)
            > 新增 build.gradle 內缺少的程式碼

                dependencies {

                    V//dependencies內要有這行(以網頁內的為準) :
                    implementation platform('com.google.firebase:firebase-bom:28.3.0')
                }

                V要有下面兩行:
                apply plugin: 'com.android.application'
                apply plugin: 'com.google.gms.google-services'

            > 兩個檔案的程式碼都新增完成後，Android Studio上方有提示要Sync Now

     */


}















