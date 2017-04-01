package com.jiesean.androidsiper_jianshu.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiesean on 2017/4/1.
 */

public class Author {

    private String name;
    private int articleNum;
    private int followerNum;
    private int followNum;
    private List<Follower> followerList;

    public Author() {
        followerList = new ArrayList<Follower>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getArticleNum() {
        return articleNum;
    }

    public void setArticleNum(int articleNum) {
        this.articleNum = articleNum;
    }

    public int getFollowerNum() {
        return followerNum;
    }

    public void setFollowerNum(int followerNum) {
        this.followerNum = followerNum;
    }

    public int getFollowNum() {
        return followNum;
    }

    public void setFollowNum(int followNum) {
        this.followNum = followNum;
    }

    public List<Follower> getFollowerList() {
        return followerList;
    }

    public void setFollowerList(List<Follower> followerList) {
        this.followerList = followerList;
    }
}
