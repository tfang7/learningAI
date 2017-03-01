package ch.idsia.agents.controllers;

import ch.idsia.benchmark.mario.engine.LevelScene;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;

public class ActionController {
	//Nodes
	//0: left: Mario.key_left
	//1: left+jump: key_left + key_jump
	//2 up: key_jump
	//3 down: key_down
	//4: right+jump
	//5: right
	public static boolean[][] actionlist = null;
	public boolean[] Action = new boolean[Environment.numberOfButtons];
	public Node[] searchspace;
	public ActionController(){
		init();
	}
	public static void init()
	{
		
	//	actionType[] actionSize = actionType.values();
		//System.out.println(actionSize);
		int actionSize = actionType.values().length;
		boolean[] leftAction = new boolean[Environment.numberOfButtons];
		leftAction[Mario.KEY_LEFT] = true;

		boolean[] leftJumpAction = new boolean[Environment.numberOfButtons];
		leftJumpAction[Mario.KEY_JUMP] = true;
		leftJumpAction[Mario.KEY_LEFT] = true;
		
		boolean[] jumpAction = new boolean[Environment.numberOfButtons];
		jumpAction[Mario.KEY_JUMP] = true;
		//jumpAction[Mario.KEY_UP] = true;
		
		boolean[] downAction = new boolean[Environment.numberOfButtons];
		downAction[Mario.KEY_DOWN] = true;
		
		boolean[] rightAction = new boolean[Environment.numberOfButtons];
		rightAction[Mario.KEY_RIGHT] = true;
		//rightAction[Mario.KEY_SPEED] = true;
		
		boolean[] rightJumpAction = new boolean[Environment.numberOfButtons];
		rightJumpAction[Mario.KEY_JUMP] = true;
		//rightJumpAction[Mario.KEY_RIGHT] = true;
		
		actionlist = new boolean[actionSize][Environment.numberOfButtons];
		//make enums for these
		actionlist[0] = leftAction;
		actionlist[1] = leftJumpAction;
		actionlist[2] = jumpAction;
		actionlist[3] = downAction;
		actionlist[4] = rightAction;
		actionlist[5] = rightJumpAction;
		
	}
	public static boolean[] getAction(int index){
		return actionlist[index];
	}
    public static double calcManhattanDist(float x1, float y1, float x2,  float y2) 
    {
    	double dist = Math.abs(x1 - x2) + Math.abs(y1 - y2);
    	return Math.sqrt( dist );
    }	
    public void setAction(boolean[] action){
		
		Action = action;
		
	}
	
}
