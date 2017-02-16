package ch.idsia.evolution;

import java.util.Random;

public class FangNN implements FA<double[], double[]>, Evolvable 
{
	private int numInputs;
	private int numOutputs;
	private int hiddenNodes;
	// i/o layers
	private double[] input;
	private double[] output;
	
	// hidden layers
	private double[][] firstConnection;
	private double[][] secondConnection;
	
	private double[][] hiddenLayers;
	
	
    public static double mean = 0.0f;        // initialization mean
    public static double deviation = 0.1f;   // initialization deviation
    public static double mutationRate = 0.1f;
    
    
	public FangNN(int in, int hidden, int out){
		numInputs = in;
		numOutputs = out;
		hiddenNodes = hidden;
		input = new double[numInputs];
		output = new double[numOutputs];
		
		firstConnection = new double[numInputs][hiddenNodes];
		secondConnection = new double[hiddenNodes][numOutputs];
		
		initLayer(firstConnection);
		initLayer(secondConnection);
	}
	public void initLayer(double[][] layer){
		for (int i = 0; i < layer.length; i++){
			for (int j = 0; j < layer[0].length; i++){
				layer[i][j] = 0.0f;
			}
		}
	}
	public void connectLayer(double[] aLayer, double[] bLayer, double[][] connection){
		for (int i = 0; i < aLayer.length; i++){
			for (int j = 0; j < bLayer.length;j++){
				Random rand = new Random();
				connection[i][j] = rand.nextGaussian() * mutationRate;
			}
		}
	}
	public double sigmoid(double input){
		return 1.0f / (1.0f + Math.exp(-1.0f * input));
		
	}
	@Override
	public Evolvable getNewInstance() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Evolvable copy() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mutate() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public double[] approximate(double[] i) {
		// TODO Auto-generated method stub
		return null;
	}
}

