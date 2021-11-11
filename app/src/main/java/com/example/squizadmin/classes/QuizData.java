package com.example.squizadmin.classes;

public class QuizData {
    public String que, _1, _2, _3, _4, key, name;

    public QuizData(String que, String _1, String _2, String _3, String _4) {
        this.que = que;
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
        this._4 = _4;
    }

    public QuizData(String key, String name) {
        this.key = key;
        this.name = name;
    }


    public String getQue() {
        return que;
    }

    public String get_1() {
        return _1;
    }

    public String get_2() {
        return _2;
    }

    public String get_3() {
        return _3;
    }

    public String get_4() {
        return _4;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }
}
