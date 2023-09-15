package com.example.assignment1_part3.model;

import java.io.Serializable;

public class Module implements Serializable{
    private String name;
    private String code;
    private int semester;


    public Module(String name, String code, int sem){
        this.name = name;
        this.code = code;
        this.semester = sem;
    }

    public String getName(){
        return name;
    }
    public int getSemester(){
        return semester;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSemester(int sem) {
        this.semester = sem;
    }

    public String toString(){
        return name + " " + code + "\n";
    }


}

