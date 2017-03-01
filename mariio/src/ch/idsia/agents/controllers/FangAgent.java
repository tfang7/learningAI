package ch.idsia.agents.controllers;
import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.engine.sprites.Sprite;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.mario.environments.MarioEnvironment;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy
 * Date: Apr 25, 2009
 * Time: 12:27:07 AM
 * Package: ch.idsia.agents.controllers;
 */

public class FangAgent extends BasicMarioAIAgent implements Agent
{
	int trueJumpCounter = 0;
	int fireCounter = 0;
	int blockedCounter = 0;
	boolean stuck = false;
	int[] direction = new int[2];
	int timer = 10;
	ActionController ac = new ActionController();
    public FangAgent()
    {
        super("ForwardJumpingAgent");
        reset();
    }

    public boolean[] getAction()
    {
        MarioEnvironment env = MarioEnvironment.getInstance();
        //
        
        float[] marioPos = env.getMarioFloatPos();
        float[] enPos = env.getEnemiesFloatPos();
        String string = "";
        direction[0] = 1;
        
        int x = marioCenter[0];
        int y = marioCenter[1];
        float[] pos;
        
        checkScene();
        jumpCheck();        
        if (enPos.length > 0) {
        	for (int i = 0; i <= enPos.length-1; i+=3)
        	{
        		float eType = enPos[i];
        		float eX = enPos[i+1];
        		float eY = enPos[i+2];
        		pos = new float[2];
        		pos[0] = eX;
        		pos[1] = eY;
        		double dist = calcManhattanDist(x,y,eX,eY);
        		//System.out.println("Enemy(" + i % 2 + "): " +dist);
        		fireWeapon(fireCounter, action);
        		//check if enemy is too close to mario.
        		if (dist < 16)
        		{
        			if (fireCounter > 10 || !isMarioAbleToShoot) {
        				action[Mario.KEY_SPEED] = false;
        			}
        		}
        		else{
        			direction[0] = 1;
        			action[Mario.KEY_SPEED] = false;
        		}
        	}
        }
        else{
        	//if no enemies in range, keep firing
        	fireWeapon(fireCounter, action);
        	//System.out.println(checkWall());
        	//check if wall in front of mario is bigger than he is
        	// if true start a counter if mario's x position has not changed in a while.
        	if (checkWall() > 5) 
        	{
        		blockedCounter++;      
        		System.out.println(blockedCounter);
        		if (blockedCounter > 10 && !stuck)
        		{
        			stuck = true;
        			timer = 20;
        		}
        	}
        }
        if (stuck){
        	direction[0] = -1;
        	if (timer <= 0){
            	stuck = false;
            	blockedCounter = 0;        		
        	}
        	timer--;

        }
        //Using direction flags to tell mario how to move
        if (direction[0] >= 1){
        	//action[Mario.KEY_RIGHT] = true;
        	//action[Mario.KEY_LEFT] = false;
        }
        else if (direction[0] < 0){
        	//action[Mario.KEY_LEFT] = true;
        	//action[Mario.KEY_RIGHT] = false;
        }
        return action;
    }
    
    public void fireWeapon(int counter, boolean[] action)
    {
    	if (counter > 1){
    		action[Mario.KEY_SPEED] = false;
    		counter = 0;
    	}
    	else{
    		action[Mario.KEY_SPEED] = true;
    		counter++;
    	}
    	
    	
    }
    public float[] getNearestEnemy(float[] enemyList) {
		
    	return enemyList;
    	
    }
    private boolean isCreature(int c)
    {
        switch (c)
        {
            case Sprite.KIND_GOOMBA:
            case Sprite.KIND_RED_KOOPA:
            case Sprite.KIND_RED_KOOPA_WINGED:
            case Sprite.KIND_GREEN_KOOPA_WINGED:
            case Sprite.KIND_GREEN_KOOPA:
                return true;
        }
        return false;
    }
    //debugging function, loops through level scene and displays as 2d text of zeros and ones
    private void checkScene(){
    	String result = "";
    	//System.out.println(levelScene.length);
    	double minDist = 0;
    	for (int x = 0; x < levelScene.length; x++){
    		result += '\n';
    		for (int y = 0; y < levelScene[0].length; y++)
    		{
    			if(levelScene[x][y] == 0) {
    				result+='0';
    			}
    			else result += '1';//levelScene[x][y];//result += ' ' + levelScene[x][y] + ' ';
    		}
    		//System.out.println(levelScene[x][y]);//
    	}
    //	System.out.println(result + '\n');
    //	System.out.println("The board is " + levelScene.length + 'x' + levelScene[0].length);
    }
    //Usage: Checks a wall by using Mario's x,y pos.
    //The function checks the spot in front of mario and then
    //iterates through the y values until it reaches the
    //end of the receptive field
    private int checkWall(){
    	//check height loop ( for walls);
        int fromX = marioCenter[0];
        int fromY = marioCenter[1];
        int blocked = 0;
        int maxHeight = 0;
        int maxX = 0;
        if (fromX > 3)
        {
            fromX -= 2;
        }
        for (int y = fromY; y < receptiveFieldHeight; ++y)
        {
        	
            if (getReceptiveFieldCellValue(y, marioCenter[0]+1) != 0) {
            	if (y > maxHeight){
            		maxHeight = y;
            		maxX = marioCenter[0]+1;
            	}
            	blocked++;
            }
                
            
        }
        if (maxHeight > 5) {
        	if (marioCenter[0] < maxX){
        		// System.out.println("Halp blocked");
        	}
        }
       // System.out.println(maxX + ", " + maxHeight);
        return blocked;
    }
    private void jumpCheck ()
    {
        if (getReceptiveFieldCellValue(marioCenter[0], marioCenter[1] + 2) != 0 ||
                getReceptiveFieldCellValue(marioCenter[0], marioCenter[1] + 1) != 0 ||
                gapCheck(levelScene))
        {
            if (isMarioAbleToJump || (!isMarioOnGround && action[Mario.KEY_JUMP]))
            {
                action[Mario.KEY_JUMP] = true;
            }
            ++trueJumpCounter;
        }
        else
        {
            action[Mario.KEY_JUMP] = false;
            trueJumpCounter = 0;
        }

        if (trueJumpCounter > 12)
        {
        	trueJumpCounter = 0;
            action[Mario.KEY_JUMP] = false;
        }
    }
    public double calcManhattanDist(float x1, float y1, float x2,  float y2) 
    {
    	double dist = Math.abs(x1 - x2) + Math.abs(y1 - y2);
    	return Math.sqrt( dist );
    }	

    private boolean gapCheck(byte[][] levelScene)
    {
        int fromX = receptiveFieldWidth / 2;
        int fromY = receptiveFieldHeight / 2;

        if (fromX > 3)
        {
            fromX -= 2;
        }

        for (int x = fromX; x < receptiveFieldWidth; ++x)
        {
            boolean f = true;
            for (int y = fromY; y < receptiveFieldHeight; ++y)
            {
                if (getReceptiveFieldCellValue(y, x) != 0)
                    f = false;
                
            }
            if (f ||
                    getReceptiveFieldCellValue(marioCenter[0] + 1, marioCenter[1]) == 0 ||
                    (marioState[1] > 0 &&
                            (getReceptiveFieldCellValue(marioCenter[0] + 1, marioCenter[1] - 1) != 0 ||
                                    getReceptiveFieldCellValue(marioCenter[0] + 1, marioCenter[1]) != 0)))
                return true;
        }
        return false;
    }
    public void reset()
    {
    	trueJumpCounter = 0;
    	fireCounter = 0;
        action = new boolean[Environment.numberOfButtons];
    }
}
