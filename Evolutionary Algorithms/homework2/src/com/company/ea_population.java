package com.company;

import java.util.Random;

public class ea_population {

/*
* this will be done simalarly to homework one with bitstring but instead of doing
* it with itegers i will do it with floating point numbers
*
* some functions in here is a copy of homework one to give my own custom EA librairy for use
* in the future. NOT ALL FUNCTIONS WILL BE USED IN FINAL EA IN MAIN
*
* properties implemented in ea librairy
* recombination - Discrete and Intermediate
*   I chose to do both to implement both and to test both in homework 2
* mutation - Uniform random and Gaussian Perturbation
*   Uniform random is from homework1 and Gaussian Perturbation is from homework 2
* Parent selection - Uniform Random and Roullet wheel selection
*   again roullet wheel selection is from homework 1 and Uniform random is from homework 2
*
*
* */
    byte[][] mypopulation; //byte this time to save memory space
    double[] X_dimension = new double[2], Y_dimension = new double[2];

    private double fitness_function(double x, double y){
        //here will be the calculated fitness function
        double a = -11.0, b = -7.0; //to be used in the equation
        //equation from wiki page 'Himmelblau's function'
        double myfit = ((x*x+y+a)*(x*x+y+a))+((x+y*y+b)*(x+y*y+b));
        return -myfit; // to find the max of the function
    }

    ea_population(int population_size, int bitstring_length) {
        //declare new population of empty bit strings
        X_dimension[0] = 1.0; X_dimension[1] = -1.0; Y_dimension[0] = 1.0; Y_dimension[1] = -1.0;
        mypopulation = new byte[population_size][bitstring_length];
        //now it needs to be initlized with random bits for a random population
        Random rand = new Random();
        for (int i = 0; i < population_size; i++) {
            mypopulation[i] = new byte[bitstring_length];
            for (int j = 0; j < bitstring_length; j++) {
                //just gets a random int from 0 to 1 and casts it to byte
                mypopulation[i][j] = (byte) rand.nextInt(2);
            }
        }
    }

    ea_population(int population_size, int bitstring_length, double distance){
        this(population_size, bitstring_length);
        X_dimension[0] = distance; X_dimension[1] = -distance; Y_dimension[0] = distance; Y_dimension[1] = -distance;
    }

    public String toString(){
        //this is a copy from homework 1 code, just used to see what the population is
        String ret = "";
        for(int i = 0; i < mypopulation.length; i++){
            ret = ret + "ID: " + i + " | ";
            for(int j = 0; j < mypopulation[i].length; j++){
                if(j == (mypopulation[i].length/2)){
                    ret = ret + " ";
                }
                ret = ret + mypopulation[i][j];
            }
            ret = ret + " |\n";
        }
        return ret;
    }
    //two simple set and get functions
    public byte[] get_individual(int index){return mypopulation[index];}
    public void set_individual(byte[] data, int index){mypopulation[index] = data;}

    public double getValue(int start_index, int end_index, int pop_index){
        //ok so after looking at the example code take the whole decimal and represent it as a ratio from 1 to 0
        //then you multiply it by the distance the max and min are apart and then add the min to it to get your
        //value

        //to represent the negative numbers there will be a twos complement of the numbers
        //if one is encounterd first then flip all the bits and then add one then get the number
        boolean flip_bits = false;
        double distance = end_index-start_index;
        //the all 1s value of maximum value represented by bits is
        double runningSum = 0;

        for(int i = start_index; i < end_index; i++){
            if(i == start_index && mypopulation[pop_index][i] == 1){flip_bits = true;} //1 was encounterd in starting position
            if(flip_bits){
                if(mypopulation[pop_index][i] == 0){
                    runningSum += 1*Math.pow(2.0,distance--);
                }else{
                    runningSum += 0*Math.pow(2.0,distance--);
                }
            }else
            runningSum += mypopulation[pop_index][i]*Math.pow(2.0,distance--);


        }
    }




}
