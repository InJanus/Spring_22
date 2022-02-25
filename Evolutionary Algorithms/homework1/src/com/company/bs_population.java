package com.company;
import java.util.Random;

public class bs_population {

    private int[][] mypopulation;

    private long get_fitness(long x, long y){
        //this will change based off the problem that is being done, make sure to change this to get different results
        //I tried originally to do this based off of setting a function but maybe for the future
        //This was originally multiplication (x*y) to put more emphasis on smaller bits and worked with 16 total bit length but was hitting the overflow for 32 bits so addition it is

        //what i have noticed with the first design is that negative numbers really dont do well for the fitness function. so i need to account for that
        //maybe these should be signed binary bit string numbers to take in account of negative numbers then the fitness function will work with negative numbers
        final int a = 1, b = 100; //constants a and b for the equation below
        long myret;
//        myret = (long)(-1*(Math.pow((a-x),2)+b*(y-Math.pow(x,2)))); //since most of the function is negative, I added a -1 to the fitness function making it work for the roulette_wheel_selection function
        myret = x*y;
//        myret = (long)(-1*(Math.pow(x/b,2)) + y);
//        if(myret < 0){
//            return 0;
//        }else{
            return myret;
//        }
    }

    public bs_population(int population_size, int bs_length, boolean random){
        mypopulation = new int[population_size][bs_length];
        //now I need to initialize the population with random bit strings?
        //yep with random bit string
        if(!random){
            for(int i = 0; i < population_size; i++){
                mypopulation[i] = new int[bs_length];
                for(int j = 0; j < bs_length; j++){
                    mypopulation[i][j] = 0;
                }
            }
        }else{
            Random rand = new Random();
            for(int i = 0; i < population_size; i++){
                mypopulation[i] = new int[bs_length];
                for(int j = 0; j < bs_length; j++){
                    mypopulation[i][j] = rand.nextInt(2);
                }
            }
        }
    }

    public String toString(){
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



    public long fitness_function(int index){
//        int ret_sum = 0;
//        for(int i = 0; i < mypopulation[index].length; i++) {
//            ret_sum = ret_sum + mypopulation[index][i];
//        }
//        return ret_sum;
//        this old fitness function is more about the function itself but i am going to change this to a two variable function
//        this will first be hard to split the bit string into two variables and then change it into an actual function that is used

        long x = 0, y = 0;
        if(mypopulation[index] != null){
            final int indexable_length = mypopulation[index].length/2;
            for(int i = indexable_length, base = 1; i > 0; i--, base=base*2){
                y = y + mypopulation[index][i + indexable_length - 1]*base;
                x = x + mypopulation[index][i - 1]*base;
            }
        }
//        System.out.println(x);
//        System.out.println(y);
//        System.out.println(get_fitness(x,y));
        return get_fitness(x,y);
    }

    public int[] bit_string_swap(int swap_bit, int index1, int index2){
        //the bit string will swap bits at the index of the location swap_bit
        //no mutation as of right now but i will get to that
        int[] child_ret = new int[1]; // temporary init
        if(mypopulation[index1].length == mypopulation[index2].length) {
            child_ret = new int[mypopulation[index1].length];
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



    public int[] roulette_wheel_selection(){
        //this will select one person out of the population based off of the roulette wheel model
        //for this, I need to know the "percentage" of each one needed or the total weight and the random number that needs to be generated
        long temp, total_weight = 0;
        long[] fitness_wight = new long[mypopulation.length];
        for(int i = 0; i < mypopulation.length; i++){
            temp = fitness_function(i);
            fitness_wight[i] = temp;
            total_weight = total_weight + temp;
        }
        Random rand = new Random();
        long selection;
        selection = rand.nextLong(total_weight);
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
                    mypopulation[index][i] = (mypopulation[index][i] + 1) % 2;
                }
            }
        }
    }

    public void set(int[] data, int index){
        if(mypopulation[index] != null){
            mypopulation[index] = data;
        }else{
            //you called beyond the index of population. insert no data
        }

    }

    public int[] get(int index){
        if(mypopulation[index] != null){
            return mypopulation[index];
        }else{
            //you called beyond the population, throw error
        }
        return null;
    }

    //below is termination functions, when should the algorithm terminate
    public int[] get_most_fit(){
        long ret = 0, temp = 0;
        int[] mostfitret = new int[mypopulation[0].length];
        for(int i = 0; i < mypopulation.length; i++){
            temp = fitness_function(i);
            if(temp > ret){
                ret = temp;
                mostfitret = mypopulation[i];
            }
        }
        return mostfitret;
    }

    public long most_fit(){
        long ret = 0, temp = 0;
        for(int i = 0; i < mypopulation.length; i++){
            temp = fitness_function(i);
            if(temp > ret){
                ret = temp;
            }
        }
        return ret;
    }

    public String most_fit_toString(){
        int[] mostfitret = get_most_fit();
        String str_ret = "WINNER | ";
        for(int j = 0; j < mostfitret.length; j++){
            if(j == (mostfitret.length/2)){
                str_ret = str_ret + " ";
            }
            str_ret = str_ret + mostfitret[j];
        }
        str_ret = str_ret + " | ";
        long x = 0, y = 0;
        final int indexable_length = mostfitret.length/2;
        for(int i = indexable_length, base = 1; i > 0; i--, base=base*2){
            y = y + mostfitret[i + indexable_length - 1]*base;
            x = x + mostfitret[i - 1]*base;
        }
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
