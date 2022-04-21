package com.company;

import java.util.Random;

public class bs_population {
    byte[][] mypopulation; //byte this time to save memory space
    double mydimension = 1.0; //defult value of the size of the search area
    Random rand = new Random();
    int performance = 0;

    private double fitness_function(double x, double y){
        //here will be the calculated fitness function
        double a = -11.0, b = -7.0; //to be used in the equation for himmelblau
//        double a = 1.0, b = 100.0;
        //equation from wiki page 'Himmelblau's function'
        double myfit = ((x*x+y+a)*(x*x+y+a))+((x+y*y+b)*(x+y*y+b));
//        simple maximization problem
//        double myfit =  x*y;
//        rosenbrock function
//        double myfit = (a-x)*(a-x)+b*(y-(x*x))*(y-(x*x));
//        return myfit;
        performance++;
        return -myfit; // to find the max of the function
    }

    bs_population(int population_size, int bitstring_length) {
        //declare new population of empty bit strings
        mypopulation = new byte[population_size][bitstring_length];
        //now it needs to be initlized with random bits for a random population
        for (int i = 0; i < population_size; i++) {
            mypopulation[i] = new byte[bitstring_length];
            for (int j = 0; j < bitstring_length; j++) {
                //just gets a random int from 0 to 1 and casts it to byte
                mypopulation[i][j] = (byte) rand.nextInt(2);
            }
        }
    }

    public void reset_performance(){
        performance = 0;
    }

    public int getPerformance(){
        return performance;
    }

    bs_population(int population_size, int bitstring_length, double distance){
        this(population_size, bitstring_length);
        mydimension = distance;
    }
    public void set_dimension(double dimension){
        mydimension = dimension;
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

    public double get_value(int start_index, int end_index, int pop_index){
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
        return (runningSum/starting_distance)*mydimension;
    }

    public double fitness_function(int index){
//        this old fitness function is more about the function itself but i am going to change this to a two variable function
//        this will first be hard to split the bit string into two variables and then change it into an actual function that is used

        double x = 0, y = 0;
        if(mypopulation[index] != null){
            x = get_value(0, mypopulation[index].length/2-1, index);
            y = get_value(mypopulation[index].length/2, mypopulation[index].length-1, index);
        }
        return fitness_function(x,y);
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

    public byte[] roulette_wheel_selection(){
        //this will select one person out of the population based off of the roulette wheel model
        //for this, I need to know the "percentage" of each one needed or the total weight and the random number that needs to be generated
        double temp, total_weight = 0.0;
        double low = 1*Math.pow(10, 300);
        double[] fitness_wight = new double[mypopulation.length];
        for(int i = 0; i < mypopulation.length; i++){
            temp = fitness_function(i);
            if(low > temp){low = temp;}
            fitness_wight[i] = temp;
            total_weight = total_weight + temp;
        }
        for(int i = 0; i < mypopulation.length; i++){
            fitness_wight[i] = fitness_wight[i] - low;
            total_weight = total_weight - low;
        }

        //with the lowest value that is the base
        double selection = rand.nextDouble(total_weight);
        int count = 0;
        while(total_weight > selection){
            total_weight = total_weight - fitness_wight[count++];
        }
        //the selected then should be --count
        return mypopulation[--count];
    }

    public void mutation(double rate, int index){
        if(mypopulation[index] != null){
            Random rand = new Random();
            for(int i = 0; i < mypopulation[index].length; i++){
                if(rand.nextDouble() <= rate){
                    //this bit should mutate
                    mypopulation[index][i] = (byte)((mypopulation[index][i] + 1) % 2);
                }
            }
        }
    }

    public int most_fit_index(){
        double ret = -1*Math.pow(10,300), temp = 0;
        int index = -1;
        for(int i = 0; i < mypopulation.length; i++){
            temp = fitness_function(i);
            if(temp > ret){
                ret = temp;
                index = i;
            }
        }
        return index;
    }

    public double most_fit(){
        double ret = -1*Math.pow(10,300), temp = 0;
        for(int i = 0; i < mypopulation.length; i++){
            temp = fitness_function(i);
            if(temp > ret){
                ret = temp;
            }
        }

        return ret;
    }

    public byte[] get_most_fit(){
        double ret = -1*Math.pow(10,300), temp = 0;
        byte[] mostfitret = new byte[mypopulation[0].length];
        for(int i = 0; i < mypopulation.length; i++){
            temp = fitness_function(i);
            if(temp > ret){
                ret = temp;
                mostfitret = mypopulation[i];
            }
        }
        return mostfitret;
    }

    public String most_fit_toString(){
        byte[] mostfitret = get_most_fit();
        String str_ret = "WINNER | ";
        for(int j = 0; j < mostfitret.length; j++){
            if(j == (mostfitret.length/2)){
                str_ret = str_ret + " ";
            }
            str_ret = str_ret + mostfitret[j];
        }
        str_ret = str_ret + " | ";
        double x = 0, y = 0;
        final int indexable_length = mostfitret.length/2;
        x = get_value(0, mostfitret.length/2-1, most_fit_index());
        y = get_value(mostfitret.length/2, mostfitret.length-1, most_fit_index());
        return str_ret + "X : " + x + " Y : " + y;
    }

    public double average_fit(){
        double temp = 0;
        for(int i = 0; i < mypopulation.length; i++){
            temp = temp + fitness_function(i);
        }
        return (temp/(double)mypopulation.length);
    }

    private boolean equal_genomes(int index1, int index2){
        if(mypopulation[index1] != null && mypopulation[index2] != null) {
            if(mypopulation[index1].length == mypopulation[index2].length) {
                for (int i = 0; i < mypopulation[index1].length; i++){
                    if(mypopulation[index1][i] != mypopulation[index2][i]){
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public int identical_genomes(){
        //this will return the number of genomes that are identical
        int count = 0;
        for(int i = 0; i < mypopulation.length; i++){
            for(int j = i+1; j < mypopulation.length; j++){
                if(equal_genomes(i, j)){count++;}
            }
        }
        if (count > mypopulation.length){
            count = mypopulation.length;
        }
        return count;
    }
}
