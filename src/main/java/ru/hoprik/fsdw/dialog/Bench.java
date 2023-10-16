package ru.hoprik.fsdw.dialog;


import java.io.*;

public class Bench implements Serializable {
    public String playerSay;
    public Dialog dialog;

    public Bench(String playerSay, Dialog dialog){
        this.playerSay = playerSay;
        this.dialog = dialog;
    }

    public Bench(String end){
        this.playerSay = end;
    }

}
