package com.jiesean.androidsiper_jianshu;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.jiesean.androidsiper_jianshu.Bean.Article;
import com.jiesean.androidsiper_jianshu.Bean.Author;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private int index = 0;
    private List<Article> articleList;
    private Author author ;

    private LineChart mLineChart;

    private Handler mHandler ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        articleList = new ArrayList<>();
        author = new Author();


        new Thread() {
            @Override
            public void run() {
                int page = 1;

                try {

                    String uid = "1441f4ae075d";
                    String baseURL = "http://www.jianshu.com/u/" + uid;

                    Document personalInfo= Jsoup.connect(baseURL).get();
                    author.setName(personalInfo.select("a.name").text());
                    author.setArticleNum(Integer.parseInt(personalInfo.select("div.meta-block").select("p").get(2).text().trim()));

                    while (true){

                        Document doc = Jsoup.connect(baseURL +"?page=" + page).get();
                        Elements noteList = doc.select("ul.note-list");
                        Elements li = noteList.select("li");

                        if (li == null || articleList.size() == author.getArticleNum()){
                            break;
                        }
                        else{
                            for (Element element : li) {

                                Article article = new Article();
                                article.setTitle(element.select("a.title").text());
                                article.setAbstractStr(element.select("p.abstract").toString());
                                article.setReadNum(Integer.parseInt(element.select("div.meta").get(0).select("a").get(0).text().trim()));
                                article.setCommentNum(Integer.parseInt(element.select("div.meta").get(0).select("a").get(1).text().trim()));
                                article.setLikeNum(Integer.parseInt(element.select("div.meta").get(0).select("span").get(0).text().trim()));

                                if (element.select("div.meta").get(0).select("span").size() == 2) {
                                    article.setMoneyNum(Integer.parseInt(element.select("div.meta").get(0).select("span").get(1).text().trim()));
                                }

                                if(!articleList.contains(article)){

                                    articleList.add(article);

                                    System.out.println(article.getTitle());
                                    System.out.println(article.getReadNum());
                                    System.out.println(article.getCommentNum());
                                    System.out.println(article.getLikeNum());
                                    System.out.println(article.getMoneyNum());

                                }
                            }
                            
                            page++;

                        }
                    }

                    System.out.println("总共获得文章篇数 ：" + articleList.size());
                }
                catch(Exception e){
                    e.printStackTrace();
                    System.out.println("Exception");
                }
            }
        }.start();

    }



}
