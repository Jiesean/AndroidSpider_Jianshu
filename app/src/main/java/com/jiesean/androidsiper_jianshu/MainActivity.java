package com.jiesean.androidsiper_jianshu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect("http://www.jianshu.com/users/1441f4ae075d/followers").get();

//                    Elements userList = doc.getElementsByClass("info");
                    Elements userList = doc.getElementsByTag("li");
                    for (int i = 0; i < userList.size(); i++) {
                        System.out.println(userList.get(i).getElementsByClass("meta").toString());
                    }

                    System.out.println(userList.size());
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }



}
