package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        Map<String, String> myargs = new HashMap<String, String>();

        String[] getargstemp;
        for(int i = 0; i < args.length; i++){
            if(args[i] != null){
                getargstemp = args[i].split("=");
                myargs.put(getargstemp[0], getargstemp[1]);
            }
        }

//        graphics mygraph = new graphics(Integer.parseInt(myargs.get("window_size")));
//        mygraph.setVisible(true);
        settings mysettings = new settings();
        mysettings.setVisible(true);
    }
}
