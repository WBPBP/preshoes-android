package com.jaehee.model;

public class FootData implements Comparable<FootData>{
    private int num;
    private int[] l_pressure;
    private int[] r_pressure;

    public FootData(){}

    public FootData(int num, int[]l_pressure, int[] r_pressure){
        this.num = num;
        this.l_pressure = l_pressure.clone();
        this.r_pressure = r_pressure.clone();
    }

    public void setData(int num, int[]l_pressure, int[] r_pressure){
        this.num = num;
        this.l_pressure = l_pressure.clone();
        this.r_pressure = r_pressure.clone();
    }

    public int getNum(){
        return num;
    }

    public int[][] getPressure(){
        int [][]res = {l_pressure, r_pressure};
        return res;
    }

    public int compareTo(FootData d){
        if(this.num < d.num) return -1;
        else if(this.num == d.num) return 0;
        else return 1;
    }
}