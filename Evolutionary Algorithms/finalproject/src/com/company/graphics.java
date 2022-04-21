package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class graphics extends JFrame implements WindowListener{
    int mysize;
    graph newgraph;

    graphics(int size) {
        mysize = size;
        addWindowListener(this);
        newgraph = new graph(size, size, 0, 100, 255);
        newgraph.setBounds(0,0,size,size);
        add(newgraph);
        setLayout(null);setResizable(false);
    }

    public void setGraph(int index){
        newgraph.setFunction(index);
    }
    public void setSearchSpace(int search){ newgraph.setSearchSpace(search, search);}
    public void setLimit(float limit){ newgraph.setLimit(limit);}

    public void windowOpened(WindowEvent e) {
        Insets myinsets = getInsets();
        setSize(mysize + myinsets.left+myinsets.right, mysize + myinsets.top+myinsets.bottom);
    }
    public void windowClosing(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
}
