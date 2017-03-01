package ch.idsia.agents.controllers;

import ch.idsia.benchmark.mario.environments.Environment;

public class Node {
	private boolean[] action;
	public float[] pos;
	public double value;
	public Node(){
		init();
	}
	public void init()
	{
		action = new boolean[Environment.numberOfButtons];
	}
	public void setAction(boolean[] input)
	{
		action = input;
	}
	public boolean[] action()
	{
		return action;
	}
	
}
