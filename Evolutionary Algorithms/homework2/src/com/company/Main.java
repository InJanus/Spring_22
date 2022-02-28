package com.company;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Map<String, String> myargs = new HashMap<String, String>();

        String[] getargstemp;
        for(int i = 0; i < args.length; i++){
            if(args[i] != null){
                getargstemp = args[i].split("=");
                myargs.put(getargstemp[0], getargstemp[1]);
            }
        }

        System.out.print("EA ALGORITHM - ");
        System.out.println(myargs.get("name"));
        System.out.println(myargs);

        ea_population mypopulation = new ea_population(Integer.parseInt(myargs.get("mu")), Double.parseDouble(myargs.get("dimension_size")), Double.parseDouble(myargs.get("sigma")));
        ea_population mychildren = new ea_population(Integer.parseInt(myargs.get("lambda")), 0, Double.parseDouble(myargs.get("sigma")));
        mychildren.set_mydimension(Double.parseDouble(myargs.get("dimension_size")));

        System.out.println(mypopulation);

        int generation_count = 0;
        while(generation_count < Integer.parseInt(myargs.get("generation_maxcount"))){
            //first, we need to generate the chlidren of the population
            //get a random number of my parents
            for(int i = 0; i < Integer.parseInt(myargs.get("lambda")); i++){mychildren.set_individual(mypopulation.parentSelection_uniformRandom(), i);}
            //recombination of parent genes
            double[] temp = mychildren.get_individual(0);
            for(int i = 0; i < Integer.parseInt(myargs.get("lambda")); i++){
                if(i == (Integer.parseInt(myargs.get("lambda"))-1)){
                    mychildren.set_individual(mychildren.recombination_discrete(mychildren.get_individual(i), temp), i);
                }else {
                    mychildren.set_individual(mychildren.recombination_discrete(mychildren.get_individual(i), mychildren.get_individual(i + 1)), i);
                }
            }
            //now mutate children
            for(int i = 0; i < Integer.parseInt(myargs.get("lambda")); i++){
                mychildren.mutation_gaussian(i);
            }
            mypopulation = mychildren.survivorSelection_mu_lambda(Integer.parseInt(myargs.get("mu")));
            generation_count++;
            System.out.println(mypopulation);
        }

        System.out.println(mypopulation);
    }
}
