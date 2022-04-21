package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class graphics extends JFrame implements ItemListener {
//    this will be for drawing the gui and everything graphics wise
    int width = 900, height = 900;
    graph mygraph;

    String[] bench_strings = {
            "DeJong's",
            "Axis Parallel Hyper-Ellipsoid",
            "Rotated Hyper-Ellipsoid",
            "Rosenbrock's Vally",
            "Rastrigin's",
            "Schwefel's",
            "Griewangk's",
            "Sum of Different Power",
            "Ackley's",
            "Langermann's",
            "Michalewicz's m=1",
            "Michalewicz's m=10",
            "Brianins's",
            "Easom's",
            "Goldstein-Price’s",
            "Six-hump Camel Back",
            "Fifth function of De Jong",
            "Drop wave",
            "Shubert’s",
            "Shekel’s",
            "Deceptive",
    };

    graphics() {


        mygraph = new graph(5.0F, 5.0F, 0, 900, 3.0F);
        JPanel input_panel = new JPanel(new BoxLayout());

        JComboBox function_dropdown = new JComboBox(bench_strings);
        JLabel function_dropdown_label = new JLabel("Function");
//        function_dropdown_label.setBounds(910, 10, 70, 20);
//        function_dropdown.setBounds(980,10,200,20);
        function_dropdown.addItemListener(this::itemStateChangedFunctionDropdown);
        input_panel.add(function_dropdown_label);input_panel.add(function_dropdown);

        JLabel bounds_label = new JLabel("Bounds");
        JTextField bounds = new JTextField(1);
//        bounds_label.setBounds(910,40,200,20);
//        bounds.setBounds(980,40,70,20);
        input_panel.add(bounds_label);input_panel.add(bounds);
        Miata
//        addWindowListener (new WindowAdapter() {public void windowClosing (WindowEvent e) {dispose();}});
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setTitle("Evolutionary Algorithm");setResizable(true);
        setVisible(true);
        setResizable(true);
        Insets myinsets = getInsets();
//        System.out.println(myinsets);
        setSize(width + myinsets.left+myinsets.right + 300, height+myinsets.top+myinsets.bottom);
        input_panel.setLayout(null);
        input_panel.setVisible(true);

//        add(mygraph);
//        add(function_dropdown_label);add(function_dropdown);
//        add(bounds_label);add(bounds);
//        pack();
        add(input_panel, BorderLayout.EAST);

    }

    public void paint(Graphics g){
        super.paint(g);
    }

    public void itemStateChanged(ItemEvent e){
//        System.out.println("test");
    }

    public void itemStateChangedFunctionDropdown(ItemEvent e){
        if(e.getStateChange() == 1){
            //item has been selected
            JComboBox tempbox = (JComboBox)e.getSource();
            mygraph.setFunction(tempbox.getSelectedIndex());
            repaint();
        }
    }
}
