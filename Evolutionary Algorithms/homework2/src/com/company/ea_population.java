package com.company;

import java.util.Random;

public class ea_population {

/*
* for this ea, i am going with the second strategy with one sigma value per value
* */
    private double[][] mypopulation; //byte this time to save memory space
    private double mydimension = 1.0; //defult value of the size of the search area
    final int pop_dimension = 4; //this shouldn't change unless you need more variables
    private double mysigma;
    Random rand = new Random();
    static int performance = 0;


    private double fitness_function(double x, double y){
        //here will be the calculated fitness function
        double a = -11.0, b = -7.0; //to be used in the equation
        //equation from wiki page 'Himmelblau's function'
        performance++;
        double myfit = ((x*x+y+a)*(x*x+y+a))+((x+y*y+b)*(x+y*y+b));
        return -myfit; // to find the max of the function
    }

    ea_population(int population_size, double dimension, double initial_sigma) {
        mysigma = initial_sigma;
        mydimension = dimension;
        mypopulation = new double[population_size][pop_dimension]; //since this is a 2 variable fitness function you only need 4 values
        for (int i = 0; i < population_size; i++) {
            for(int j = 0; j < pop_dimension/2; j++) {
                if(dimension == 0){
                    mypopulation[i][j] = 0.0;
                }else{
                    mypopulation[i][j] = rand.nextDouble(-dimension, dimension);
                }
            }
            for(int j = pop_dimension/2; j < pop_dimension; j++) {
                mypopulation[i][j] = initial_sigma;
            }
        }
    }

    public String toString(){
        //this is a copy from homework 1 code, just used to see what the population is
        String ret = "";
        int max_width = 20;
        for(int i = 0; i < mypopulation.length; i++) {
            ret = ret + "ID: " + i + " | ";
            for (int j = 0; j < mypopulation[i].length; j++) {
                ret = ret + String.format(("%-25s"),mypopulation[i][j]);
            }
            ret = ret + " | " + String.format(("%s"), fitness_function(mypopulation[i][0], mypopulation[i][1]) + " |\n");
        }
        return ret;
    }

    //two simple set and get functions
    public double[] get_individual(int index){return mypopulation[index];}
    public void set_individual(double[] data, int index){mypopulation[index] = data;}
    public void set_mydimension(double dimension){mydimension = dimension;}
    public int get_performance(){return performance;}
    public void reset_performance(){performance = 0;}

    //i implemented both discrete recombination and intermediate recombination. I seemed to get better answers when i ran
    //intermediate recombination

    public double[] recombination_discrete(double[] parent1, double[] parent2) {
        //for this it is a property that one or the other will get the equal amount of chance
        if(parent1.length == parent2.length) {
            double[] ret = new double[parent1.length];
            for (int i = 0; i < mypopulation[0].length; i++) {
                if (rand.nextDouble() > 0.5) {
                    ret[i] = parent2[i];
                }else{
                    ret[i] = parent1[i];
                }
            }
            return ret;
        }
        return new double[0]; //size of both parents is not the same
    }

    public double[] recombination_intermediate(double[] parent1, double[] parent2, double alpha){
        if(parent1.length == parent2.length) {
            double[] ret = new double[parent1.length];
            for (int i = 0; i < mypopulation[0].length; i++) {
                ret[i] = parent1[i]*alpha + parent2[i]*(1-alpha);
            }
            return ret;
        }
        return new double[0]; //size of both parents is not the same
    }

    private void shuffle(int runs){
        //this will just shuffle the mypopulation up
        int[] position = new int[2];
        double[] temp;
        for(int i = 0; i < mypopulation.length*runs; i++){
            for(int j = 0; j < 2; j++){position[j] = rand.nextInt(mypopulation.length);}
            temp = mypopulation[position[0]];
            mypopulation[position[0]] = mypopulation[position[1]];
            mypopulation[position[1]] = temp;
        }
    }

    public void mutation_gaussian(int index){
        if(mypopulation[index] != null) {
            for (int i = 0; i < mypopulation[index].length / 2; i++) {
                mypopulation[index][i] = rand.nextGaussian() * mypopulation[index][i + (mypopulation[index].length / 2)] + mypopulation[index][i]; //function taken from
                if(mypopulation[index][i] > mydimension){
                    mypopulation[index][i] = mydimension;
                }else if(mypopulation[index][i] < -mydimension){
                    mypopulation[index][i] = -mydimension;
                }
            }
            for (int i = mypopulation[index].length / 2; i < mypopulation[index].length; i++) {
                mypopulation[index][i] = rand.nextGaussian() * mysigma + mypopulation[index][i];
            }
        }
    }

    public double[] parentSelection_uniformRandom(){
        Random rand = new Random();
        return mypopulation[rand.nextInt(mypopulation.length)];
    }

    public ea_population survivorSelection_mu_lambda(int number){
        //this is a selection mechanisim for choosing survivors
        //number is the number of Selection is used
//        ea_population(int population_size, double dimension, double initial_sigma)
        ea_population ret = new ea_population(number, mydimension, mysigma);
        //first have to sort the list of inital population
        double tempval, fit_temp;
        int index = -1, count = 0;
        for(int i = 0; i < number; i++){
            tempval = -1*Math.pow(10,300);
            for(int j = 0; j < mypopulation.length; j++){
                fit_temp = fitness_function(mypopulation[j][0], mypopulation[j][1]);
                if(tempval < fit_temp){
                    tempval = fit_temp;
                    index = j;
                }
            }
            ret.set_individual(mypopulation[index], count++);
            mypopulation[index] = new double[pop_dimension];
        }
        ret.shuffle(3);
        return ret;
    }

    public ea_population survivorSelection_mu_plus_lambda(){
        //i am trying this one because i am seeing a pattern where the
        return null;
    }

    public double get_average_fitness(){
        double ret = 0.0, temp = 0;
        for(int i = 0; i < mypopulation.length; i++){
            temp = fitness_function(mypopulation[i][0], mypopulation[i][1]);
            ret = ret + temp;
        }
        return ret/mypopulation.length;
    }

    public double most_fit(){
        double ret = -1*Math.pow(10,300), temp = 0;
        for(int i = 0; i < mypopulation.length; i++){
            temp = fitness_function(mypopulation[i][0], mypopulation[i][1]);
            if(temp > ret){
                ret = temp;
            }
        }
        return ret;
    }

    //output points to get the position and then plot all the points
    public String get_points(int generation_count){
        //this is a copy from homework 1 code, just used to see what the population is
        String ret = "";
        for(int i = 0; i < mypopulation.length; i++) {
            ret = ret + generation_count;
            for (int j = 0; j < mypopulation[i].length/2; j++) {
                ret = ret + String.format((",%s"),mypopulation[i][j]);
            }
            ret += "\n";
        }
        return ret;
    }
}
