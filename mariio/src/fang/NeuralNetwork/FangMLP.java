package fang.NeuralNetwork;

import java.util.Random;

import ch.idsia.evolution.Evolvable;
import ch.idsia.evolution.FA;
import ch.idsia.evolution.MLP;

public class FangMLP implements FA<double[], double[]>, Evolvable 
{
	private int numInputs;
	private int numOutputs;
	private int hiddenNodes;
	// i/o layers
	private double[] input;
	private double[] output;
	
	
	// hidden layers
	public double[][] firstConnection;
	public double[][] secondConnection;
	
	private double[] hiddenLayer;
	
	public static final Random random = new Random();
    public static double mean = 0.0f;        // initialization mean
    public static double deviation = 0.05d;   // initialization deviation
    private double mutationRate = 0.015d;
    
    public FangMLP(double[][] firstConnection, double[][] secondConnection, int numberOfHidden,
            int numberOfOutputs)
    {
	     this.firstConnection = firstConnection;
	     this.secondConnection = secondConnection;
	     input = new double[firstConnection.length];
	     hiddenLayer = new double[numberOfHidden];
	     output = new double[numberOfOutputs];
 	}

	public FangMLP(int numInputs, int hiddenNodes, int numOutputs )
	{
		this.numInputs = numInputs;
		this.numOutputs = numOutputs;
		this.hiddenNodes = hiddenNodes;
		init(hiddenNodes);
	//	 double[] act = getOutput(in, hidden, out);

		//connectLayer()
	}
	public FangMLP(Evolvable newInstance) {
		// TODO Auto-generated constructor stub
	}

	public void init(int hidden){
		output = new double[numOutputs];
		hiddenLayer = new double[hidden];
		firstConnection = new double[numInputs][hiddenNodes];
		secondConnection = new double[hiddenNodes][numOutputs];

	}
	public double[] getOutput( double[] input){
		clear(output);
		this.input = input;
		initLayer(firstConnection);
		initLayer(secondConnection);
		linkNodes();
		return output;
	}
	public double[] getOutput(int in, int hidden, int out, double[] input){
	//	numInputs = in;
//		numOutputs = out;
	//	hiddenNodes = hidden;
		this.input = input;
		//input = new double[numInputs];
	//	System.out.println("input:" + input.length);
		//System.out.println("output:" + output.length);

		
		initLayer(firstConnection);
		initLayer(secondConnection);
		
		linkNodes();
		/* String res = "";
		 for (int i = 0; i < output.length; i++)
		 {
			 res += String.format("%.2f", output[i] ) + " ";
		 }
		 System.out.println(res);*/
		evalLayer(output);
		return output;
	}
	private void initLayer(double[][] layer)
	{
	//	System.out.println("initializing layers to " + layer.length + " , " + layer[0].length);
		for (int i = 0; i < layer.length; i++){
			for (int j = 0; j < layer[0].length; j++){
				layer[i][j] = random();
			}
		}
	}
	private void linkNodes()
	{	//System.out.println("connecting layers " + input.length + " , " + hiddenLayer.length);
		//clear(this.input);
		clear(output);

		connectLayer(this.input, hiddenLayer, firstConnection );
		connectLayer(hiddenLayer, output, secondConnection);
		
		propagate(this.input, hiddenLayer, firstConnection );
		evalLayer(hiddenLayer);
		
		propagate(hiddenLayer, output, secondConnection );
		evalLayer(output);
	}
	private void connectLayer(double[] aLayer, double[] bLayer, double[][] connection)
	{
		for (int i = 0; i < connection.length; i++)
		{
			for (int j = 0; j < connection[0].length;j++)
			{
				connection[i][j] = random();
			}
		}
	}
	@Override
	public void recombine(FangMLP pBest, FangMLP gBest){
		//System.out.println(secondConnection.length + " , " + pBest.firstConnection[0][0]);
       // System.out.println(" LAST:" + last);
        //System.out.println(" PBEST:" + pBest);
        //System.out.println(" GBEST:" + gBest);
        //System.out.println(" THIS:" + toString());
		////System.out.println(pBest.firstConnection[0].length);
		for (int i = 0; i < pBest.firstConnection.length; i++)
		{
			for (int j = 0; j < pBest.firstConnection[0].length; j++)
			{
				if (j % 2 == 0)
					this.firstConnection[i][j] = pBest.firstConnection[i][j];
				else
					this.firstConnection[i][j] = gBest.firstConnection[i][j];
				
					//this.firstConnection[i][j] = gBest.firstConnection[i][j];
					//this.secondConnection[i][j] = gBest.secondConnection[i][j];
				
				
				
			}
			
		}
		for (int l = 0; l < gBest.secondConnection.length; l++)
		{
			for (int k = 0; k < gBest.secondConnection[0].length; k++)
			{
				if (k % 2 == 1)
					this.secondConnection[l][k] = pBest.secondConnection[l][k];
				else 
					this.secondConnection[l][k] = gBest.secondConnection[l][k];
				
					//this.firstConnection[i][j] = gBest.firstConnection[i][j];
					//this.secondConnection[i][j] = gBest.secondConnection[i][j];
				
			}
			
		}
		

		
		
	}
	/*
	  public void psoRecombine(MLP last, MLP pBest, MLP gBest)
	    {
	        // Those numbers are supposed to be constants. Ask Maurice Clerc.
	        final double ki = 0.729844;
	        final double phi = 2.05;

	        double phi1 = phi * random.nextDouble();
	        double phi2 = phi * random.nextDouble();
	        //System.out.println("phi1: "+phi1+" phi2: "+phi2);
	        //System.out.println(" LAST:" + last);
	        //System.out.println(" PBEST:" + pBest);
	        //System.out.println(" GBEST:" + gBest);
	        //System.out.println(" THIS:" + toString());
	        for (int i = 0; i < inputs.length; i++)
	        {
	            for (int j = 0; j < hiddenNeurons.length; j++)
	            {
	                firstConnectionLayer[i][j] = (double) (firstConnectionLayer[i][j] + ki * (firstConnectionLayer[i][j] - ((double[][]) (last.firstConnectionLayer))[i][j]
	                        + phi1 * (((double[][]) (pBest.firstConnectionLayer))[i][j] - firstConnectionLayer[i][j])
	                        + phi2 * (((double[][]) (gBest.firstConnectionLayer))[i][j] - firstConnectionLayer[i][j])));
	            }
	        }

	        for (int i = 0; i < hiddenNeurons.length; i++)
	        {
	            for (int j = 0; j < outputs.length; j++)
	            {
	                secondConnectionLayer[i][j] = (double) (secondConnectionLayer[i][j] + ki * (secondConnectionLayer[i][j] - ((double[][]) (last.secondConnectionLayer))[i][j]
	                        + phi1 * (((double[][]) (pBest.secondConnectionLayer))[i][j] - secondConnectionLayer[i][j])
	                        + phi2 * (((double[][]) (gBest.secondConnectionLayer))[i][j] - secondConnectionLayer[i][j])));
	            }
	        }

	    }
	    */

	private void propagate(double[] src, double[] dest, double[][] connections) 
	{
		clear(dest);
	//	System.out.println("connecting layers " + connections.length + " , " + connections[0].length);
	//	System.out.println("input:" + input.length);
	//	System.out.println("output:" + dest.length);
		for (int i = 0; i < connections.length; i++ ){
			for (int j = 0; j < connections[0].length; j++ )
			{
			//	System.out.println("loop layers " + src[i] + " , " + j);
				dest[j] += src[i] * connections[i][j];
			}
		}
	}
	public void debug(){
		System.out.println("Input " + this.numInputs);
		System.out.println("Output " + this.numOutputs);
		System.out.println("Mutation Rate " + this.mutationRate);
		System.out.println("First connection length " + this.firstConnection.length);
		System.out.println("First con. second array " + this.firstConnection[0].length);

	}
	private void clear(double[] arr){
		for (int i = 0; i < arr.length; i++){
			arr[i] = 0.0d;
		}
	}
	private void evalLayer(double[] layer)
	{
		for (int i = 0; i < layer.length; i++)
		{
			
			layer[i] = max(layer[i]);//layer[i] > 0.0d ? layer[i] : 0;
			//System.out.println(layer[i]);
		}
	}
	private double sigmoid( double input )
	{
		return 1.0d / (1.0d + Math.exp(-input));
	}
	private double atanh( double input ) 
	{
		return Math.atan(input);
	}
	public double max( double input )
	{
		//System.out.println(input);
		return input > 0.0d ? input : 0.0d;
	}
	public void setMutationRate(double mut){
		this.mutationRate = mut;
	}
	public double getMutationRate(){
		return this.mutationRate;
	}
    private void mutate(double[] array)
    {
        for (int i = 0; i < array.length; i++)
        {
            array[i] += random.nextGaussian() * mutationRate;
        }
    }
    private void mutate(double[][] array)
    {
        for (double[] anArray : array)
        {
            mutate(anArray);
        }
    }
    public double random(){
		return random.nextGaussian() + deviation * mean;
	}
	
	@Override
	public FangMLP getNewInstance() {
		// TODO Auto-generated method stub
		 return new FangMLP(firstConnection.length, secondConnection.length, numOutputs);
	}
	@Override
	public FangMLP copy() 
	{
		FangMLP cpy = new FangMLP(copy(this.firstConnection), copy(this.secondConnection), hiddenNodes, numOutputs);
		cpy.setMutationRate(mutationRate);
		return cpy;
	}
	public void modifyMutation(double mut){
		mutationRate += mut;
	}
    private double[][] copy(double[][] original)
    {
        double[][] copy = new double[original.length][original[0].length];
        for (int i = 0; i < original.length; i++)
        {
            System.arraycopy(original[i], 0, copy[i], 0, original[i].length);
        }
        return copy;
    }
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mutate() {
	//	mutate(firstConnection);
	//	mutate(secondConnection);
		// TODO Auto-generated method stub
		
	}
	@Override
	public double[] approximate(double[] i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void tuneMutation(double val) {
		// TODO Auto-generated method stub
		mutationRate += val;
	}

	@Override
	public void tuneDeviation(double val) {
		deviation += val;
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getDeviation() {
		// TODO Auto-generated method stub
		return deviation;
	}

	@Override
	public double getMutation() {
		// TODO Auto-generated method stub
		return mutationRate;
	}
}

