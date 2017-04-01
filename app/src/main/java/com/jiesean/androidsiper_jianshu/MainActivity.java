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

//        Looper.prepare();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x123) {
                    System.out.println("收到消息");
                    setChartConf();
                }
            }
        };

        articleList = new ArrayList<>();
        author = new Author();

        mLineChart = (LineChart) findViewById(R.id.line_chart);
        setChartConf();

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
                    //1.创建Message对象
                    Message sendMsg = new Message();
                    //在Message的what字段中携带少量信息，用于消息的身份识别
                    sendMsg.what = 0x123;
                    //使用接收线程创建的Handler对象的sendMessage函数向接收线程发送消息
                    mHandler.sendMessage(sendMsg);



                }
                catch(Exception e){

                    e.printStackTrace();
                    System.out.println("Exception");


                }
            }
        }.start();


    }

    private void setChartConf(){

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < author.getArticleNum(); i++) {
            xVals.add(i + 1 + "");
        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < author.getArticleNum(); i++) {
            yVals.add(new Entry(articleList.get(i).getReadNum(), i));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "");

        set1.setDrawCubic(false);  //设置曲线为圆滑的线
        set1.setCubicIntensity(0.2f);
        set1.setDrawFilled(false);  //设置包括的范围区域填充颜色
        set1.setDrawCircles(true);  //设置有圆点
        set1.setLineWidth(2f);    //设置线的宽度
        set1.setCircleSize(5f);   //设置小圆的大小
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setColor(Color.rgb(244, 117, 117));    //设置曲线的颜色

        // create a data object with the datasets
        LineData data = new LineData(xVals, set1);

        // set data
        mLineChart.setData(data);
        mLineChart.invalidate();
    }


}
