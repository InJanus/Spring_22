package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class graphics extends JFrame {
//    this will be for drawing the gui and everything graphics wise
    int width, height, width_of_titlebar, starting_buffer;
    int dimension = 560;
    graphics() {
        width = 1000;
        height = 637;
        String[] bench_strings = {"Test1", "Test2", "Test3"};
        JComboBox benchmarks = new JComboBox(bench_strings);
        benchmarks.setSize(100,20);
        benchmarks.setLocation(800,300);
        benchmarks.setSelectedIndex(0);
        add(benchmarks);

        setSize(width, height);setTitle("Evolutionary Algorithm");
        setLayout(null);setVisible(true);setResizable(false);



//        repaint();


        addWindowListener (new WindowAdapter() {public void windowClosing (WindowEvent e) {dispose();}});
    }

    private int getChordY(int Y){return Y + width_of_titlebar + starting_buffer;}
    private int getChordX(int X){return X + starting_buffer;}

    public void paint(Graphics g){
        super.paint(g);
        Insets myinsets = getInsets();
        width_of_titlebar = myinsets.top + myinsets.bottom; //this is the height of the title bar
        starting_buffer = (height-width_of_titlebar-dimension)/2;
        System.out.println(starting_buffer);
//        gridlines
//        g.setColor(Color.GRAY);
//        for(int x = starting_buffer+10; x < dimension+starting_buffer; x = x + 10){
//            g.drawLine(x, starting_buffer+width_of_titlebar, x, dimension+starting_buffer+width_of_titlebar);
//        }
//        for(int y = starting_buffer+10; y < dimension+starting_buffer; y = y + 10){
//            g.drawLine(starting_buffer, y+width_of_titlebar, dimension+starting_buffer, y+width_of_titlebar);
//        }
//        g.drawLine(30, 20+width_of_titlebar, 30, 580+width_of_titlebar);
//      i am first going to setup a class to get the chrodinates of the graph


        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(getChordX(dimension/2),getChordY(0), getChordX(dimension/2), getChordY(dimension));
        g.drawLine(getChordX(0),getChordY(dimension/2),getChordX(dimension),getChordY(dimension/2));
        g.drawOval(getChordX(dimension/2)-5,getChordY(dimension/2)-5,10,10);
        g.setColor(Color.BLACK);
        g.draw3DRect(starting_buffer, starting_buffer+width_of_titlebar, dimension, dimension, false);
    }
}
