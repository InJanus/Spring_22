package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class graph extends JPanel {

    int Xdimension, Ydimension; //dimensions of the frame
    float XSearch, YSearch; //search dimensions
    int setFunction; //function set flag
    int precisionRatio; //how many blocks on the top axis as a ratio
    float heightLimit; //how tall the color is for the height

    graph(float X, float Y, int index, int ratio, float limit){
        Xdimension = 900;
        Ydimension = 900;
        XSearch = X;
        YSearch = Y;
        setFunction = index;
        precisionRatio = ratio;
        heightLimit = limit;
        setSize(Xdimension, Ydimension);
        setLayout(null);
        setVisible(true);

//        addWindowListener (new WindowAdapter() {public void windowClosing (WindowEvent e) {dispose();}});
    }

    public void paint(Graphics g) {
        Insets myinsets = getInsets();
//        System.out.println(myinsets.top);
        Xdimension = getWidth();
        Ydimension = getHeight();
        super.paint(g);
        //i now need to get the search space together and how much percision I need to generate the blocks

        //the search space is determined by using the +/- search variables
        int fillHeight = Ydimension/precisionRatio;
        int fillWidth = Xdimension/precisionRatio;

        float value;
        for(int x = 0; x < Xdimension; x = x + fillWidth){
            for(int y = 0; y < Xdimension; y = y + fillHeight){
                //here is where each block is drawn in the calculator
                value = fitnessFunction(xDecode(x), yDecode(y));
                if(value >= 0) {
                    if (value > heightLimit){
                        g.setColor(Color.getHSBColor(0.0F, 1.0F, 1.0F));
                    }else {
                        g.setColor(Color.getHSBColor(0.0F, value / heightLimit, 1.0F));
                    }
                }else{
                    if ((-1)*value > heightLimit){
                        g.setColor(Color.getHSBColor(0.66F, 1.0F, 1.0F));
                    }else{
                        g.setColor(Color.getHSBColor(0.66F, (-1)*value/heightLimit, 1.0F));
                    }
                }
                g.fillRect(x,y,fillWidth,fillHeight);
            }
        }

        g.setColor(Color.GRAY);
        g.drawLine(Xdimension / 2, 0, Xdimension / 2, Ydimension);
        g.drawLine((0), (Ydimension / 2), (Xdimension), (Ydimension / 2));
        g.drawOval((Xdimension / 2) - 5, (Ydimension / 2) - 5, 10, 10);

    }

    private float xDecode(int x){return ((x/((Xdimension/XSearch)/2))-XSearch);}
    private float yDecode(int y){return ((y/((Ydimension/YSearch)/2))-YSearch);}
    private float yInset(int y){return (0);}

    public void setSearchSpace(float X, float Y){
        XSearch = X;
        YSearch = Y;
    }

    public void setFunction(int index){
//        for this refer to the pdf file that i have to set the search function
//        red is high
//        blue is Low
        setFunction = index;
    }

    public void setPrecision(int ratio){
        //this is for setting the ratio to how much percision you want int he graph
        //when you set the ratio the ratio is how many blocks per unit on the top axis
        //say you want to have a high precision, the bnumber would be close the the dimension
        //lower percision would have the graph show more generalized blocks
        precisionRatio = ratio;
    }

    private float fitnessFunction(float x, float y){
        switch(setFunction){
            case 0:
                //this is the general minimization function
                return ((x*x)+(y*y));
            case 1:
                return ((x*x)+2*(y*y));
            case 2:
                return (2*(x*x)+(y*y));
            case 3:
                return (100*(y-(x*x))*(y-(x*x))+((1-x)*(1-x)));
            case 4:
                return (float)(20+((x*x)-10*Math.cos(2*Math.PI*x))+((y*y)-10*Math.cos(2*Math.PI*y)));
            case 5:
                return (float)(((-1)*x*Math.sin(Math.sqrt(Math.abs(x))))-(y*Math.sin(Math.sqrt(Math.abs(y)))));
            case 6:
                return (float)(((((x*x)+(y*y))/4000))-(Math.cos(x)*Math.cos(y/Math.sqrt(2)))+1);
            case 7:
                return ((Math.abs(x)*Math.abs(x))+(Math.abs(y)*Math.abs(y)*Math.abs(y)));
            case 8:
//                return (float)(-(x*Math.sin(Math.sqrt(Math.abs(x))))-(y*Math.sin(Math.sqrt(Math.abs(y)))));
                return (float)(-20*Math.exp(-0.2*Math.sqrt(0.5*((x*x)+(y*y)))));
            case 9:
                float[] a = {3,5,2,1,7}, b = {5,2,1,4,9}, c = {1,2,5,2,3};
                float sum = 0.0F;
                for(int m = 0; m < 5; m++){
                    sum += c[m]*Math.exp(-((x-a[m])*(x-a[m]))/Math.PI-((y-b[m])*(y-b[m]))/Math.PI)*Math.cos(Math.PI*((x-a[m])*(x-a[m]))+Math.PI*((y-b[m])*(y-b[m])));
                }
                return sum;
            case 10:
                return (float)(-Math.pow(Math.sin(x)*Math.sin((x*x)/Math.PI),2)-Math.pow(Math.sin(y)*Math.sin((y*y)/Math.PI),2));
            case 11:
                return (float)(-Math.pow(Math.sin(x)*Math.sin((x*x)/Math.PI),20)-Math.pow(Math.sin(y)*Math.sin((y*y)/Math.PI),20));
            case 12:
                return (float)(Math.pow((y-(5.1/(4*Math.PI*Math.PI))*(x*x)+(5/Math.PI)*x-6),2)+10*(1-(1/(8*Math.PI))*Math.cos(x)+10));
            case 13:
                return (float)-(Math.cos(x)*Math.cos(y)*Math.exp(-((x-Math.PI)*(x-Math.PI))-((y-Math.PI)*(y-Math.PI))));
            case 14:
                return (1+((x+y+1)*(x+y+1))*(19-14*x+3*y*y-14*y+6*x*y+3*y*y));
            case 15:
                return (float)(((4-2.1*(x*x)+((x*x*x*x)/3))*(x*x))+(x*y)+(-4+(4*y*y))*(y*y));
            case 16:
                float[] z = {-32,-16,0,16,32};
                float ret = 0.0F;


            default:
                return 0.0F;
        }
    }
}
