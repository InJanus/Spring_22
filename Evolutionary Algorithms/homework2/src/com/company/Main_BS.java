package com.company;

import javax.script.ScriptException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main_BS {
    public static void main(String[] args) throws ScriptException {
        //this is for homework one and generating a EA for Homework one
        //i may need some extra classes for implementing Homework 1 but just keep getting back into java

        Map<String, String> myargs = new HashMap<String, String>();

        String[] getargstemp;
        for(int i = 0; i < args.length; i++){
            if(args[i] != null){
                getargstemp = args[i].split("=");
                myargs.put(getargstemp[0], getargstemp[1]);
            }
        }

        System.out.print("BS ALGORITHM - ");
        System.out.println(myargs.get("name"));
        System.out.println(myargs);
        Random rand = new Random();
        bs_population population, parents, children;
        bs_population most_fit = new bs_population(Integer.parseInt(myargs.get("terminate_gen_value")),Integer.parseInt(myargs.get("bit_string_length")), 0.0);
        most_fit.set_dimension(Double.parseDouble(myargs.get("dimension")));
        int gen_number = 0;


        population = new bs_population(Integer.parseInt(myargs.get("pop_size")), Integer.parseInt(myargs.get("bit_string_length")), Double.parseDouble(myargs.get("dimension"))); //initializing random population

        //There is more emphasis on getting the whole population fit rather than one individual fit to have a "consensus" on the answer
        //the termination value is set in arguments where it is the amount of the population where it is the same
        while(gen_number < Integer.parseInt(myargs.get("terminate_gen_value")) && ((double)population.identical_genomes() / Double.parseDouble(myargs.get("pop_size"))) <= Double.parseDouble(myargs.get("terminate_value")) && (population.most_fit() < 0.0000001)) {
            //System.out.println(((double)population.most_fit()-population.average_fit()));
            most_fit.set_individual(population.get_most_fit(), gen_number); //this is for tracking the most fit individual in each generation
            System.out.println(++gen_number + " " + population.most_fit() + " " + population.average_fit() + " " + ((double) population.identical_genomes() / Double.parseDouble(myargs.get("pop_size"))));
//            System.out.println(population);
            //loop below code

            parents = new bs_population(Integer.parseInt(myargs.get("pop_size")), Integer.parseInt(myargs.get("bit_string_length")), 0.0); //a non-random population just generates a blank population that can be filled
            parents.set_dimension(Double.parseDouble(myargs.get("dimension")));
            //parent selection based off roulette wheel selection
            //this is to add some elitism to help with the selection
            for(int i = 0; i < Integer.parseInt(myargs.get("elitism")); i++) {
                parents.set_individual(population.get_most_fit(), i);
            }
            for (int i = Integer.parseInt(myargs.get("elitism")); i < Integer.parseInt(myargs.get("pop_size")); i++) {
                parents.set_individual(population.roulette_wheel_selection(), i);
            }

            children = new bs_population(Integer.parseInt(myargs.get("pop_size")), Integer.parseInt(myargs.get("bit_string_length")), 0.0);
            children.set_dimension(Double.parseDouble(myargs.get("dimension")));
            //children will have a single bit flip with parents 0 and 1, 1 and 2 and so on. last case being 9 and 0. giving a total of the same amount of children
            //children will also be given a chance to mutate, with the chance given in the arguments
            for (int swap_bit = rand.nextInt(Integer.parseInt(myargs.get("bit_string_length"))), i = 0; i < Integer.parseInt(myargs.get("pop_size")); i++, swap_bit = rand.nextInt(Integer.parseInt(myargs.get("bit_string_length")))) {
                if (rand.nextDouble() <= Double.parseDouble(myargs.get("crossover_rate"))) { //this is the setting for crossover rate higher the crossover rate the more it will flip
                    if (i + 1 >= Integer.parseInt(myargs.get("pop_size"))) {
                        //last case
                        children.set_individual(parents.single_point_swap(swap_bit, i, 0), i);
                    } else {
                        children.set_individual(parents.single_point_swap(swap_bit, i, i + 1), i);
                    }
                } else {
                    //it will go here if there is no crossover needed by the value from above
                    children.set_individual(parents.get_individual(i), i);
                }
            }
            for (int i = 0; i < Integer.parseInt(myargs.get("pop_size")); i++) {
                //mutation is set by an argument of a percentage
                children.mutation(Double.parseDouble(myargs.get("mutation_rate")), i);
            }
            population = children; //this is the uniform replacement, all children replace the population
        }
        System.out.println(++gen_number + " " + population.most_fit() + " " + population.average_fit() + " " + ((double) population.identical_genomes() / Double.parseDouble(myargs.get("pop_size"))));
        System.out.println(population); //uncomment here to see your ending population
        System.out.println("Most fit in population");
        System.out.println(population.most_fit_toString());
        System.out.println("Most fit overall");
        System.out.println(most_fit.most_fit_toString());
    }

}
