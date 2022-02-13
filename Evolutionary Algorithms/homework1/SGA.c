
/****************************************************************************/
/* Program:     SGA Demo                                                    */
/* Programmer:  John C. Gallagher                                           */
/* Version:     3.0                                                         */
/* Date:        December 29, 2022                                           */
/* Purpose:     This program is a VERY simple demo of the SGA (Simple GA).  */  
/*              It is not written to be particuarly efficent of otherwise   */
/*              awesome.  Rather, it's intended to be a highly commented    */
/*              reference implementation.  This code is meant to illustrate */
/*              basic concepts.  Optimization is up to you.                 */
/*                                                                          */
/*              Oh wait, it's LIGHTLY commented.... because I removed many  */
/*              of the substantiative comments so that this could be a      */
/*              reverse engineering exercise for those of you that learn    */
/*              more effectively that way.  As you do homework #1, you can  */
/*              proceed from descriptions of algorithms to custom code, or  */
/*              you can proceed from this code TO the algorithms and see    */
/*              how things might be implemented on your way to your own     */
/*              code :)                                                     */
/*                                                                          */
/* Dependencies: This code depends on jcg's ran.c library.  In this sample  */
/*               implementation, I'm #including the code.  Yes.. that's     */
/*               evil and horrible.  In any production system you should    */
/*               compile separately and link.                               */
/*                                                                          */
/****************************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include "rng.h"		
#include "rng.c"

/*****************************************************************************/
/* Data Structures and Type Definitions                                      */
/*****************************************************************************/

/* We're going to define an individual genome as a string of characters with the
   understanding that each character is going to hold a '0' or a '1' to represent
   a bit.  Yes, this is rediculously wasteful, as we're using an eight-bit
   character type to represent a single bit.  On the other hand, it makes 
   things slightly more straightforward to code up.  We'll just go with this for
   now since the basic gig here is to illustrate principles.  You may want to 
   do better than this when you write up your own version.  Using a representation
   like this means that all our populations are AT LEAST eight times bigger 
   in memory than they need to be.  Keeping our populations large is, at least 
   for some kinds of EA, something we care about, so such wastefulness may not
   be all that cool.

   Also note that the struct will keep a count of the length of the genome (in
   bits) and the fitness score associated with that genome.  It should be clear
   that a genome will be a dynamically allocated memory structure (I'm using
   a pointer to a block of characters that will obviously need to be allocated
   at some point) */

typedef struct genome_s
	{ int genome_len;		/* The length in bits of the genome */
	
	  char *bit;            /* pointer to an array of chars, each one of which will hold
	                           a single bit of the genome as an ASCII character */
	                           
	  double fitness;       /* The fitness score of the genome.  Right now we're assuming
	                           that each genome maps to a unique and unchanging fitness
	                           score.  That is, the fitness function is NOT dynamic.
	                           How long do you think we'll let THAT assumption stand? */
	} genome_t;

/* Not surprisingly, a population will be represented as a collection of genomes
   This is also a dynamically allocated structure */
 	
typedef struct population_s
	{ int member_count;     /* The number of members (genomes) in the population */
	
	  genome_t *member;     /* An array of pointers to genome structures.  A population
	                           is a struct that contains a population count and then
	                           an array of genome structures that hold various info on 
	                           each individual genome. */
	} population_t;
		
		

/********************************************************************************/
/* Population Memory Allocation Routines                                        */
/* These are utility routines that are used to allocate and deallocate memory   */
/* for populations.  There are also individual genome allocation routines, but  */
/* those should rarely, if ever, be called by a casual user.  Note that a       */
/* population MUST be allocaed before it's used by any of the SGA processing    */
/* routines.                                                                    */
/********************************************************************************/

void SGA_Genome_Malloc(genome_t *genome, int length)
/* This routine allocates memory for a genome of <length> bits.  It should be passed
   a POINTER to the genome pointer so that the genome pointer can be modified to point
   to the allocated character block.... */

{   (*genome).bit = (char *)malloc(sizeof(char)*length);
    (*genome).genome_len = length;
    (*genome).fitness = 0.0;
}


void SGA_Genome_Free(genome_t *genome)
/* This routine unallocates the memory used by a genome */
{  free((*genome).bit);
   (*genome).genome_len = 0;
}

void SGA_Population_Malloc(population_t *population, int member_count, int genome_length)
/* This routine allocates a population of <member_count> bit string genomes of length
   <genome_length>.  The population size and genome length will be stored in the population
  structure, so you need not keep track of those numbers yourself */

{  int member_c;

   (*population).member_count = member_count;
   (*population).member = (genome_t *)malloc(member_count * sizeof(genome_t));
    for (member_c=0; member_c < member_count; member_c++)
	   SGA_Genome_Malloc(&((*population).member[member_c]), genome_length);	
}

void SGA_Population_Free(population_t *population)
{   printf("I guess I was just too lazy to implement SGA_Population_Free()\n");
    printf("because I'm never really going to in practice deallocate a population\n");
    printf("depending, rather, on the process terminating to return all process\n");
    printf("memory to the system.  I asked the duck about this and he's not too\n");
    printf("keen on the idea.  So it goes... so it goes\n\n");
}


/********************************************************************************/
/* Population Utility Routines                                                  */
/* These are all utility routines that manipulate populations and genomes.      */
/* Mostly these are things meant to copy, merge, or print information about     */
/* populations.  They are not "GA" functions specifically, but are used to      */
/* get status information and/or to implement portions of GA functions.         */
/* You may use these if you really like, but keep in mind that you MAY want     */
/* to modify and/or extend them to meet the needs of the current (and future)   */
/* homework assignments.                                                        */
/********************************************************************************/


void SGA_Genome_Print(genome_t *genome)
/* This is a utility routine that just prints out a genome as a bitstring with its current
   fitness score in parenthesis */

{ int bit_count;
  double SGA_Genome_Decode();

  for (bit_count=0; bit_count < (*genome).genome_len; bit_count++)
      printf("%d",(*genome).bit[bit_count]);
  printf(" (%lf)\n",(*genome).fitness);
	
}

void SGA_Population_Print(population_t *population)
/* Prints out ALL the genomes in a population, with fitness scores... */
{ int p_count;

  for (p_count=0; p_count < (*population).member_count; p_count++)
      SGA_Genome_Print(&((*population).member[p_count]));
}

void SGA_Genome_Copy(genome_t *source_genome, genome_t *destination_genome)
/* Copies the infornmation in <*source_genome> to <*destination_genome>.  Note
   that it is ASSUMED that both genomes have been alloacated memory.  The
   source genome is left unmodified and any information in the destination
   genome is overwritten with a copy of the information in the source 
   genome */

{ int bit_pos;
  if ((*source_genome).genome_len != (*destination_genome).genome_len)
    {fprintf(stderr, "ERROR in SGA_Genome_Copy: Source and Destination of Unequal Length\n");
     exit(0);
    }
 
  (*destination_genome).fitness = (*source_genome).fitness;
  for (bit_pos=0; bit_pos<(*destination_genome).genome_len; bit_pos++)
      (*destination_genome).bit[bit_pos] = (*source_genome).bit[bit_pos];
}


void SGA_Population_Copy(population_t *source_population, population_t *destination_population)
/* This copies one population to another.  It works like SGA_Genome_Copy(), except 
   for whole populations.  Again, the source population is left unmodified.  The
   destination population is overwritten with the data from the source.  Note that
   there isn't NEARLY enough error checking in here.  Expect that if the population 
   sizes and the sizes of genomes IN those populations are not the same that you'll
   see segmentation faults.  The duck told me this, but I'm now in the habit of 
   ignoring the duck */
   
{ int p_count;
  if ((*source_population).member_count != (*destination_population).member_count)
    {fprintf(stderr, "ERROR in SGA_Population_Copy: Source and Destination of Unequal Sizes\n");
     exit(0);
    }
   for (p_count=0; p_count < (*source_population).member_count; p_count++)
	   SGA_Genome_Copy(&((*source_population).member[p_count]),&((*destination_population).member[p_count]));
}


/********************************************************************************/
/* Population Initialization Routines                                           */
/* These functions provide initialization functionality for the SGA.  They      */
/* either randomize all the genomes in a population or a randomize a single     */
/* genome.  Note that you'll almost never need to randomize a genome directly,  */
/* as that functionality is included in the initialize population function.     */
/********************************************************************************/

void SGA_Genome_Init(genome_t *genome, RNG *rng)
/* This routine sets an already allocated genome to a random bitstring.
   each bit position is set to 1/0 with a 50/50 probability. It also sets
   the genome's fitness to 0.0   NOTE: A casual user should rarely need
   to call this function directly.  The population init routine will more
   normally be used */

{ int bit_count;
  for (bit_count=0; bit_count < (*genome).genome_len; bit_count++)
      if (rng_uniform01(rng) < 0.5)
	      (*genome).bit[bit_count] = 0;
	  else
	      (*genome).bit[bit_count] = 1;
   (*genome).fitness = 0.0;
}

void SGA_Population_Init(population_t *population, RNG *rng)

/* This function randomizes all the genomes in a popuation.  It also sets each
   of those genomes fitness score to 0.0 */

{ int m_count;
  for (m_count=0; m_count < (*population).member_count; m_count++)
	SGA_Genome_Init(&((*population).member[m_count]), rng);
}	


/********************************************************************************/
/* Genome Decode Routine                                                        */
/* This single function decodes a portion of a genome as a double precision     */
/* floating point number.  Note that this routine represents a naieve genome    */
/* encoding and it would rarely be a great idea to do things as they're done    */
/* here.  Still, this is pretty much how people rolled in the 80's.  Glad you   */
/* live in a new century, then?                                                 */
/********************************************************************************/

double SGA_Genome_Decode(genome_t *genome, int start, int end, double min, double max)

/* DANGER DANGER DANGER....  This routine will take ALL the bits between positions 
   <start> and <end> on genome <*genome> and treat them as a fixed precision ratio
   between 0.0 and 1.0.  This ratio will be multiplied by the distance between <max>
   and <min> and added to <min> to produce the returned value.  In essesce, this
   decodes a segment of a genome as a number between <min> and <max>.  Note again.. 
   this is a VERY naive and problematic encoding... certainly after taking the EC
   class you can do better.  I'm going with this because it is "very SGA" and is 
   the first kind of encoding that most people use.  Feel free to modify this
   to suit your own preferences on encodings when you're doing HW1

   As an example, presume the following genome: 0011100101000

   This call: SGA_Genome_Decode(&genome, 0, 3, 0.0, 1.0)

   will return:  0.0 + (3/15)*(1.0 - 0.0) = 0.2

   This value represents "20 percent of the distance from min to max"  The "20%"
   comes from the value represented in the four bits selected (3) divided by the
   maximum value one could represent in four bits (15)

   THERE IS NO ERROR CHECKING IN THIS ROUTINE.  YOU CAN MISUSE IT IN ANY NUMBER OF
   WAYS.  You are warned.... this time by me and not the duck, who is also worried
   
*/

{ double return_value;
  double max_decimal_value;
  double decimal_value;
  int    bit_count;	
  int    bit_pos;
  
  decimal_value = 0.0;
  max_decimal_value = 0.0;
  bit_count = 0;
  for (bit_pos=end; bit_pos>=start; bit_pos--)
    { decimal_value     += (double)((*genome).bit[bit_pos])*pow(2.0, (double)bit_count);
	  max_decimal_value += pow(2.0,(double)bit_count);
	  bit_count++;
	}
	
   return_value = min + (max-min)*(decimal_value/max_decimal_value);	
   return return_value;

}

/********************************************************************************/
/* Genome Variation Functions                                                   */
/* Mutation and One-Point Crossover Functions.                                  */
/********************************************************************************/

void SGA_Genome_Mutate(genome_t *genome, double mutation_rate, RNG *rng)
/* This function will mutate (flip) each bit in the genome with a probability
   specified by <mutation_rate>.  The rate should be between 0.0 and 1.0.
   There is no error checking on probability values.  <*rng> is 
   a random number generator structure allocated and initialized using
   code in rng.c 
   
   Is this code the most efficient way to do business?  Not really....  */

{ int bit_count;
  for (bit_count=0; bit_count < (*genome).genome_len; bit_count++)
      if (rng_uniform01(rng) < mutation_rate)
         switch ((*genome).bit[bit_count])
	   	    { case 0: (*genome).bit[bit_count] = 1;
				      break;
			  case 1: (*genome).bit[bit_count] = 0;
			          break;
			}
	 
}

void SGA_Genome_1P_Crossover(genome_t *parent_genome_1, genome_t *parent_genome_2, RNG *rng)
/* This function implements one point crossover of the two genomes.  There is 
   no error checking and it is assumed that both genomes are properly 
   malloced and of the same lengths.  I'm not telling you too much here, because 
   you need to reverse engineering _something_   */

{ int bit_count;
  int crossover_point;	
  genome_t child_genome_1, child_genome_2;

  SGA_Genome_Malloc(&child_genome_1, (*parent_genome_1).genome_len);
  SGA_Genome_Malloc(&child_genome_2, (*parent_genome_1).genome_len);
  crossover_point = (int)round(rng_uniform(rng, 0.0, (double)(*parent_genome_1).genome_len));

  for (bit_count = 0; bit_count < crossover_point; bit_count++)
      { (child_genome_1).bit[bit_count] = (*parent_genome_1).bit[bit_count];
		(child_genome_2).bit[bit_count] = (*parent_genome_2).bit[bit_count];
      }

  for (; bit_count < (*parent_genome_1).genome_len; bit_count++)
      { (child_genome_1).bit[bit_count] = (*parent_genome_2).bit[bit_count];
		(child_genome_2).bit[bit_count] = (*parent_genome_1).bit[bit_count];
      }	 
	
  SGA_Genome_Copy(&child_genome_1, parent_genome_1);
  SGA_Genome_Copy(&child_genome_2, parent_genome_2); 
  
  SGA_Genome_Free(&child_genome_1);
  SGA_Genome_Free(&child_genome_2); 
}


void SGA_Population_Mutate(population_t *population, double mutation_rate, RNG *rng)
/* This function goes through a whole population and mutates each bit in each 
   member with a probability <mutation_rate>.  As is par for the course, no error
   checking...  also few operational comments because again -- reverse engineering
   is the game here */

{ int m_count;
	
  for (m_count = 0; m_count < (*population).member_count; m_count++)
	SGA_Genome_Mutate(&((*population).member[m_count]), mutation_rate, rng);

}

void SGA_Population_Make_New_Generation(population_t *old_population, 
                                        population_t *new_population,
                                        double       mutation_rate,
                                        double       crossover_rate,
										RNG          *rng)

/* This function creates a new population from an old population by application 
   of fitness proportional selection to generate parent pairs from the old population.
   The members of each pair are subjected to crossover and mutation according to 
   given rates and then copied into the new population.  When the new population is 
   full, this routine ends.
   
   Note a few things here...
   
   First, I'm combining parent selection and variation operations in a pairwise
   form to fill the new population.  This is technically NOT how it's presented in 
   the book, but is functionally equivalent.  Note that this way of thing things is
   kind of wasteful of memory, but again, that train left the station already.
   
   Note that THIS routine does not implement survivor selection.  THAT would be 
   implemented by copying ALL the members of new population to the old population.
   In a sense, "old population" is the population that exists on an ongoing basis.
   "new population" is scratch space to generate all the members of the next generation,
   all of which "survive" by overwriting the old generation.  If you wanted to 
   implement some kind of survivor selection, that simple copy (which is not done)
   in THIS routine, would likely be implemented by some kind of "survivor copy"
   that did something more complex than just overwriting the old with the new.
  
    I'm going light on operational comments because...  reverse engineering exercise */


{  double sum_of_fitness_scores;
   double *roulette_wheel;
   double max_raw_fitness, min_raw_fitness;
   int m_count;

   int parent_one_index;
   int parent_two_index;
   double die_roll;


   /* First we're going to construct that biased roulette wheel.  We'll need this
      to select parent pairs */

    // Make an array that holds the cumulative prob density function of the roulette wheel slots.
    // There should be one slot per population member...  and that's what we allocate...

	roulette_wheel = (double *)malloc(sizeof(double)*(*old_population).member_count); 
	
	// Now, let's find out what the maximum and minimum fitnesses scores are.  We need this 
	// to compute the offset we'll need upon normalization of the fitness scores.  This
	// is just a loop that goes through all the members of the population and finds the
	// max and min scores.
	
    max_raw_fitness = min_raw_fitness = 0.0;
	for (m_count = 0; m_count < (*old_population).member_count; m_count++)
	   { if (((*old_population).member[m_count]).fitness > max_raw_fitness)
		     max_raw_fitness = ((*old_population).member[m_count]).fitness;	
		 if (((*old_population).member[m_count]).fitness < min_raw_fitness)
			     min_raw_fitness = ((*old_population).member[m_count]).fitness;
		}	    


    // Ok.. let's start to consturct the roulette wheel.  First we're going to 
    // copy all the raw fitnesses into the roulette wheel slots.

	for (m_count = 0; m_count < (*old_population).member_count; m_count++)
	    roulette_wheel[m_count] = ((*old_population).member[m_count]).fitness;
	
	// Ok, now let's zero offset the fitnesses.  This means the worst member 
	// of the population will have a zero fitness and the rest of them will
	// have fitnesses measured realative to that worst one.
	
	for (m_count = 0; m_count < (*old_population).member_count; m_count++)
		roulette_wheel[m_count] -= min_raw_fitness;
		
	// Now at this point, it's possible for all members to have zero fitness.
	// That would be bad...  so we're going to offset all the fitnesses again,
	// This time by and arbitrary constant... This may or may not be a good idea
	// and may have implications for functionality and efficacy...  expect an 
	// exam question about this ;)
	
	for (m_count = 0; m_count < (*old_population).member_count; m_count++)
		roulette_wheel[m_count] += 1.0;
		
	// Awesome... now let's compute the sum of all fitnesses and use it to normalize
	// the slot sizes inside the roulette wheel...
	
	sum_of_fitness_scores = 0.0;
	for (m_count = 0; m_count < (*old_population).member_count; m_count++)
		sum_of_fitness_scores += roulette_wheel[m_count];
	
	for (m_count = 0; m_count < (*old_population).member_count; m_count++)
		roulette_wheel[m_count] /= sum_of_fitness_scores;
		
    // Now we make a cumulative Prob Distribution Function (PDF) out of all that....
    for (m_count = 1; m_count < (*old_population).member_count; m_count++)
		roulette_wheel[m_count] += roulette_wheel[m_count-1];
		
	// Wow.. that was alot of work... now we have a PDF table to enalbe 
	// parent selection....  So, let the REAL work begin. Let's make a loop that fills 
	// up the new population with children from
    // the old...

    for (m_count = 0; m_count < (*old_population).member_count-1; m_count++)
	   {  // Let's compute the parent one and parent two indices...
           die_roll = rng_uniform01(rng);
  		   parent_one_index = 0;
   		   while (roulette_wheel[parent_one_index] < die_roll) parent_one_index++;
		   die_roll = rng_uniform01(rng);
		   parent_two_index = 0;
		   while (roulette_wheel[parent_two_index] < die_roll) parent_two_index++;
		
		   // Now that we got the indicies of our parents... let's apply variation 
		   // operations to generate the two genomes that actually get copied into
		   // the new (scratch) population
		    
		  SGA_Genome_Copy( &((*old_population).member[parent_one_index]),&((*new_population).member[m_count]));
		  SGA_Genome_Copy( &((*old_population).member[parent_two_index]),&((*new_population).member[m_count+1]));
		
	  	  SGA_Genome_Mutate(&((*new_population).member[m_count]), mutation_rate,rng);
	  	  SGA_Genome_Mutate(&((*new_population).member[m_count+1]), mutation_rate,rng);
	
	      if (rng_uniform01(rng) < crossover_rate)
			SGA_Genome_1P_Crossover(&((*new_population).member[m_count]),&((*new_population).member[m_count+1]),rng);
			
	   }
		
}

/********************************************************************************/
/* Genome Fitness Functions                                                     */
/* Here's a bunch of fitness functions for you to play with.  Note you should   */
/* only use ONE of these for any run :).  All of them take in a genome and      */
/* return a double precision fitness value.  Bigger is better.                  */
/********************************************************************************/

double SGA_Max_Ones_Fitness(genome_t *genome)
/* This one is on you */
{return 0.0;}

double SGA_Half_Ones_Fitness(genome_t *genome)
/*  This is a strange ditty.  Genomes that have 50% of their bit positions
    set to 1 will have the highest fitness.  I'm not commenting this, as there's
    some practical techniques in here that you may extract via reverse engineering
    that we'll eventually talk about in class.
    
    Note though.... THIS fitness function is different than the two that would appear
    in your homework. THIS one has MORE than one genome that all score maximum fitness.
    Interesting, right?  What do you think will happen and what do you think the 
    implications are for the EA search?  We'll talk about this more in class, unless
    you take it to the discord :) 
*/

{ int bit_position;
  double one_count = 0.0;
  double ratio = 0.0;
  double distance = 0.0;
  
  for (bit_position = 0; bit_position < (*genome).genome_len; bit_position++)
      one_count += (double)(*genome).bit[bit_position];

  ratio    = one_count / (double)(*genome).genome_len;
  distance = sqrt(pow(0.5 - ratio,2));
  return -distance;
}

double SGA_Quarter_Ones_Fitness(genome_t *genome)
/*  Another strange ditty.  Genomes that have 25% of their bit positions
    set to 1 will have the highest fitness.  I'm not commenting this, as there's
    some practical techniques in here that you may extract via reverse engineering
    that we'll eventually talk about in class.
    
    Note though.... well... everything we prompted for in SGA_Half_Ones_Fitness().
*/

{ int bit_position;
  double one_count = 0.0;
  double ratio = 0.0;
  double distance = 0.0;
  
  for (bit_position = 0; bit_position < (*genome).genome_len; bit_position++)
      one_count += (double)(*genome).bit[bit_position];

  ratio    = one_count / (double)(*genome).genome_len;
  distance = sqrt(pow(0.25 - ratio,2));
  return -distance;
}


double SGA_Rosenbrock(genome_t *genome)
/* This one is on you :)
*/

{return 0.0;}


double SGA_Fitness_Function(genome_t *genome)
/* This is a wrapper for the fitness function you really want to use.  All other
   routines call THIS one... you should use it to encapsulate the thing you 
   actually want to maximize.  As it's written below, this will be a maximizer
   of 1's in the genome... */

{ return SGA_Quarter_Ones_Fitness(genome);
}

void SGA_Population_Compute_Fitness(population_t *population)
/* This function goes through a whole population and assigns a 
   fitness score to each genome.  The fitness scores are stored
   in each genome structure */

{ int m_count;
	
  for (m_count = 0; m_count < (*population).member_count; m_count++)
	((*population).member[m_count]).fitness = SGA_Fitness_Function(&((*population).member[m_count]));

}



/************************************************************/

int main()
{
	RNG *random_number_generator;
	population_t POPULATION,POPULATION2;
	int generation_count;
	
	/* Initialize random number generator.  This RNG will be used for all calls where
	   random numbers are needed */
	
	random_number_generator = rng_create();
	
	/* Allocate memory for two populations... */
	SGA_Population_Malloc(&POPULATION,  75,36);  //This will be our primary population
	SGA_Population_Malloc(&POPULATION2, 75,36); // This will be scratch space... it had better be the same size as the first one
	
	/* Initialize the primary population to random bits... */
	SGA_Population_Init(&POPULATION,random_number_generator);
	SGA_Population_Compute_Fitness(&POPULATION);
	
	printf("Initial Population\n");
	SGA_Population_Print(&POPULATION); //Let's see how it looks 
    
	/* Now we begin the evolution loop... I'm going to just go 1000 generations... just because... */
	
	for (generation_count = 0; generation_count < 2000000; generation_count++)
	    {  SGA_Population_Compute_Fitness(&POPULATION);  // Evaluate Everyone in the population
		   SGA_Population_Make_New_Generation(&POPULATION, &POPULATION2, 0.001, 0.6, random_number_generator); //Make the new generation
		   SGA_Population_Copy(&POPULATION2, &POPULATION); //Copy the new population back into the primary population
		}
		
		printf("\n");	
	
	SGA_Population_Compute_Fitness(&POPULATION);  // Evaluate Everyone so we print out valid scores...
	
	printf("Final Population\n");
    SGA_Population_Print(&POPULATION); //Let's see how it looks 
	
}
