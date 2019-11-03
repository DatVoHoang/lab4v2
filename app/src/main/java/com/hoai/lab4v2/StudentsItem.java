package com.hoai.lab4v2;

import java.util.ArrayList;

public class StudentsItem {
    int id;
    String Title, Content;


    public StudentsItem() {
    }


    public StudentsItem(int id, String title, String content) {
        this.id=id;
        Title = title;
        Content = content;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setContent(String content) {
        Content = content;
    }


    public String getTitle() {
        return Title;
    }

    public String getContent() {
        return Content;
    }



}
