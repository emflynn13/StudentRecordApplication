package com.example.assignment1_part3.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.Date;

public class Student implements Serializable{
    private String name;
    private String id;
    private Date dob;

    private int semester;
    private final ArrayList<Module> modules;

    public Student(String name, String id, Date dob, int semester){
        this.name = name;
        this.id = id;
        this.dob = dob;
        this.semester = semester;
        modules = new ArrayList<>();
    }

    public String getName(){
        return name;
    }

    public String getId(){
        return id;
    }

    public String getDob(){
        DateFormat dateFor = new SimpleDateFormat("dd/MM/yyy");
        String stDOB = dateFor.format(dob);
        return stDOB;
    }
    public int getSemester(){
        return semester;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setDob(Date dob){
        this.dob = dob;
    }
    public void setSemester(int semester){
        this.semester = semester;
    }

    public void addModule(Module newModule){
        modules.add(newModule);
    }

    public ArrayList<Module> getModules(){
        return modules;
    }

    public int getNumModules(){
        return modules.size();
    }

    public String toString(){
        return name + " " + id + " " + dob + "\n";
    }
}

