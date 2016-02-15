package com.example.lorenzo.donatethedistance;

import java.io.Serializable;

/**
 * Created by Lorenzo on 2/15/2016.
 */
public class User implements Serializable {
    String first_name, last_name;
    int height_ft, height_in, weight_lbs;

    public User(String first_name, String last_name, int height_ft, int height_in, int weight_lbs) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.height_ft = height_ft;
        this.weight_lbs = weight_lbs;
        this.height_in = height_in;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public int getHeight_ft() {
        return height_ft;
    }

    public void setHeight_ft(int height_ft) {
        this.height_ft = height_ft;
    }

    public int getHeight_in() {
        return height_in;
    }

    public void setHeight_in(int height_in) {
        this.height_in = height_in;
    }

    public int getWeight_lbs() {
        return weight_lbs;
    }

    public void setWeight_lbs(int weight_lbs) {
        this.weight_lbs = weight_lbs;
    }

    @Override
    public String toString() {
        return "User{" +
                "first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", height_ft=" + height_ft +
                ", height_in=" + height_in +
                ", weight_lbs=" + weight_lbs +
                '}';
    }
}
