package fang.NeuralNetwork;

import java.util.Random;
import java.util.Stack;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.tasks.Task;
import ch.idsia.evolution.EA;
import ch.idsia.evolution.Evolvable;

public class FangES implements EA {
	private Evolvable[] population;
	private final Task task;
	private final float[] fitness;
	private final int elite;
	public double best = 0.0d;
	private final int evaluationRepetitions = 1;
	public int bestCounter = 0;
	public Evolvable[] elites;
	private int generation;
	public Evolvable BestAgentSoFar = null;
	public FangES(Task task, Evolvable initial, int populationSize)
	{
		this.task = task;
		this.fitness = new float[populationSize];
		this.elite = 10;
		this.elites = new Evolvable[elite];
		this.population = new Evolvable[populationSize];
		for (int i = 0; i < population.length; i++)
		{
			population[i] = initial.getNewInstance();
		}
	}
	public void setGen(int gen){
		generation = gen;
	}
	@Override
	public Evolvable[] getBests() {
		// TODO Auto-generated method stub
		//System.out.println(population[0]);
		 FangMLPAgent bestAgent = (FangMLPAgent) population[0];

		if (bestAgent.fitness > best){
			BestAgentSoFar = bestAgent.copy();
		}
		return new Evolvable[]{population[0]};
	}
	public Evolvable[] getPopulation(){
		return population;
	}
	public Evolvable[] eliteSelection(int amount){
		amount+=1;
		Evolvable[] elites = new Evolvable[elite];
		for (int i = 1; i < amount; i++){
			//elites[i-1] = population[i].copy();
			elites[i-1] = population[i].copy();
		}
		return elites;
	}
	@Override
	public float[] getBestFitnesses() {
		// TODO Auto-generated method stub
		return new float[]{fitness[0]};
	}
	public void cullTheWeak(){
		for (int i = 0; i < population.length; i++){
			if (fitness[i] < best/2){
			//	if (i % 2 == 0)population[i].reset();
				//population[i].mutate();
			//	System.out.println("best:" + best + ", weakling"+fitness[i]);
			}
		}
	}
	@Override
	public void nextGeneration() {
		 sortPopulationByFitness();
		 FangMLPAgent bestAgent = (FangMLPAgent) population[0];
		 Random r = new Random();
		 
		 Evolvable[] eliteArray = eliteSelection(10);
		 //Randomly pick out of 10 elites
		 FangMLPAgent nextBest = (FangMLPAgent) eliteArray[r.nextInt(eliteArray.length-1)];

		// int eliteThreshold = population.length/5;
		//for (int i = 0; i < eliteThreshold ; i++){
	//		 evaluate(i);
		//	System.out.println("Recombining Top " + population.length/10 + " : " + i);
		//}
		//sortPopulationByFitness();
	//	 FangMLP nextBest =  population[1];
	//	 FangMLP worst = population[population.length-1];
		for (int j = 0; j < population.length;  j++)
		{
			//System.out.println("Evaluating child: " + j);
				 //population[0].mutate();
			if (bestCounter > 5)
			{
				population[j].tuneMutation(0.0001);
				population[j].tuneDeviation(0.00001);
			}
			else if (bestCounter <= 5)
			{
				population[j].tuneMutation(-0.1);
				population[j].tuneDeviation(-0.00001);

			}
			
			if (j < 50){
				 r = new Random();
				 nextBest = (FangMLPAgent) eliteArray[r.nextInt(eliteArray.length-1)];
				 population[j].recombine(bestAgent.neuralNet, nextBest.neuralNet);
				 population[j].mutate();
			}
			else 
				population[j].mutate();
			evaluate(j);
		}
		/*for (int i = 0; i < population.length; i++){
			System.out.println("selecting elite" + i);
			population[i] = population[i - elite].copy();
			population[i].mutate();
			population[i].tuneMutation(0.001d);
			
			evaluate(i);
		}*/
        shuffle();
        sortPopulationByFitness();
      //  System.out.println("Best: " + population[0] + " ," + fitness[0]);
      //  System.out.println("Second: " + population[1] + " ," + fitness[1]);
     //   System.out.println("Worst" + population[population.length-1] + " ," + fitness[population.length-1]);
       // System.out.println("Found best" + fitness[0]);
		// TODO Auto-generated method stub
		
	}
	private void evaluate(int which){
		fitness[which] = 0;
		
        for (int i = 0; i < evaluationRepetitions; i++)
        {
            population[which].reset();
            fitness[which] += task.evaluate((Agent) population[which])[0];
            System.out.println(generation + ":" + fitness.length + " which " + which + " fitness " + fitness[which] + " dev: " + population[i].getDeviation());
        }
        //population[which].fitness = fitness[which];
        fitness[which] = fitness[which] / evaluationRepetitions;
	}
    private void shuffle()
    {
        for (int i = 0; i < population.length; i++)
        {
            swap(i, (int) (Math.random() * population.length));
        }
    }
    private void sortPopulationByFitness()
    {
        for (int i = 0; i < population.length; i++)
        {
            for (int j = i + 1; j < population.length; j++)
            {
                if (fitness[i] < fitness[j])
                {
                    swap(i, j);
                }
            }
        }
    }
    public void checkBest(){
        double bestResult = getBestFitnesses()[0];
        if (best < bestResult){
        	best = bestResult;
        	System.out.println("Found global optimum: " + best);
        	bestCounter = 0;
        }
        else
        {
        	bestCounter++;
        	
        }
        if (bestCounter > 10){
        	bestCounter = 0;
        	//System.out.println("Should tune mutation, since last global optimum" + bestCounter);
        }
        //System.out.println("Best in gen " + bestResult);
    }
    private void swap(int i, int j)
    {
        float cache = fitness[i];
        fitness[i] = fitness[j];
        fitness[j] = cache;
        Evolvable gcache = population[i];
        population[i] = population[j];
        population[j] = gcache;
    }
	@Override
	public void nextGeneration(boolean mutate) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
