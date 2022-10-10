package com.example.covidrun.model;

public class DependentModel {
    private String dependentname, dependentphone;
    private int id;

    public String getDependentname(){
        return dependentname;
    }

    public void setDependentname(String dependentname){
        this.dependentname = dependentname;
    }

    public String getDependentphone(){ return dependentphone; }

    public void setDependentphone(String dependentphone) { this.dependentphone = dependentphone; }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public DependentModel(String dependentname, String dependentphone){
        this.dependentname = dependentname;
        this.dependentphone = dependentphone;
    }
}
