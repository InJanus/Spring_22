package com.company;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class settings extends JFrame {
    graphics mygraph;
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

    settings(){
        setTitle("Controller");
        JPanel input_box = new JPanel(new GridLayout(0,2));
        JPanel generation_control = new JPanel(new FlowLayout());
        JButton next = new JButton("Next");
        JButton prev = new JButton("Prev");
        JButton play_stop = new JButton("Play");
        play_stop.setText("Stop");
        generation_control.add(prev);generation_control.add(play_stop);generation_control.add(next);

        setLayout(new BorderLayout(10,10));
        JComboBox benchmark = new JComboBox(bench_strings);
        JLabel benchmark_label = new JLabel("Benchmark", SwingConstants.CENTER);
        JTextField search_area = new JTextField(1);
        JLabel search_area_label = new JLabel("Search Area", SwingConstants.CENTER);
        JTextField limit = new JTextField(1);
        JLabel limit_label = new JLabel("Limit", SwingConstants.CENTER);

        benchmark.addItemListener(this::benchmark_change);
        search_area.addActionListener(this::search_area_change);
        limit.addActionListener(this::limit_change);
        input_box.add(benchmark_label);
        input_box.add(benchmark);
        input_box.add(search_area_label);
        input_box.add(search_area);
        input_box.add(limit_label);
        input_box.add(limit);
        add(input_box);
        add(generation_control, BorderLayout.SOUTH);
        setSize(500, 300);
        mygraph = new graphics(400);
        mygraph.setVisible(true);
    }

    public void paint(Graphics g){
        super.paint(g);
        mygraph.repaint();
    }

    public void benchmark_change(ItemEvent e){
        if(e.getStateChange() == 1){
            //item has been selected
//            System.out.println(e);
            JComboBox tempbox = (JComboBox)e.getSource();
            mygraph.setGraph(tempbox.getSelectedIndex());
            repaint();
        }
    }

    public void search_area_change(ActionEvent e){
        JTextField myfield = (JTextField) e.getSource();
        mygraph.setSearchSpace(Integer.parseInt(myfield.getText()));
        repaint();
    }

    public void limit_change(ActionEvent e){
        JTextField myfield = (JTextField) e.getSource();
        mygraph.setLimit(Integer.parseInt(myfield.getText()));
        repaint();
    }


}
