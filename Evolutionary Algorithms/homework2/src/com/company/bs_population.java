package com.company;

import java.util.Random;

public class bs_population {
    byte[][] mypopulation; //byte this time to save memory space
    double dimension = 1.0; //defult value of the size of the search area

    private double fitness_function(double x, double y){
        //here will be the calculated fitness function
        double a = -11.0, b = -7.0; //to be used in the equation
        //equation from wiki page 'Himmelblau's function'
        double myfit = ((x*x+y+a)*(x*x+y+a))+((x+y*y+b)*(x+y*y+b));
        return -myfit; // to find the max of the function
    }

    bs_population(int population_size, int bitstring_length) {
        //declare new population of empty bit strings
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

    bs_population(int population_size, int bitstring_length, double distance){
        this(population_size, bitstring_length);
        dimension = distance;
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
        double distance = Math.pow(2.0,(end_index-start_index));
        double starting_distance = distance;
        //the all 1s value of maximum value represented by bits is
        double runningSum = 0.0;
        for(int i = start_index; i < end_index; i++){
            if(i == start_index && mypopulation[pop_index][i] == 1){flip_bits = true;} //1 was encounterd in starting position
            if(flip_bits){ //basic operation
                if(mypopulation[pop_index][i] == 0){
                    runningSum += distance;
                }
            }else {
                runningSum += mypopulation[pop_index][i] * distance;
            }
            distance /= 2;
        }
        //not adding one in this case so both sides of the complement it is abs(value)
        if(flip_bits){runningSum = -(runningSum);} //flip the sum
        return (runningSum/starting_distance)*dimension;
    }

    public byte[] recombination_discrete(byte[] parent1, byte[] parent2) {
        //for this it is a property that one or the other will get the equal amount of chance
        if(parent1.length == parent2.length) {
            Random rand = new Random();
            byte[] ret = new byte[parent1.length];
            for (int i = 0; i < mypopulation[0].length; i++) {
                if (rand.nextDouble() > 0.5) {
                    ret[i] = parent2[i];
                }else{
                    ret[i] = parent1[i];
                }
            }
            return ret;
        }
        return new byte[0]; //size of both parents is not the same
    }

    public byte[] single_point_swap(int swap_bit, int index1, int index2){
        //the bit string will swap bits at the index of the location swap_bit
        //mutation is not in here and will be implemented somewhere else
        byte[] child_ret = new byte[1];
        if(mypopulation[index1].length == mypopulation[index2].length) {
            child_ret = new byte[mypopulation[index1].length];
            for (int i = 0; i < swap_bit; i++){
                child_ret[i] = mypopulation[index1][i];
            }
            for (int i = swap_bit; i < mypopulation[index1].length; i++){
                child_ret[i] = mypopulation[index2][i];
            }
        }else{
            //something is really wrong!

        }
        return child_ret;
    }
}
