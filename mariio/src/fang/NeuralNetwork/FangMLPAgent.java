package fang.NeuralNetwork;

import ch.idsia.agents.Agent;
import ch.idsia.agents.controllers.BasicMarioAIAgent;
import ch.idsia.agents.learning.SimpleMLPAgent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.mario.environments.MarioEnvironment;
import ch.idsia.evolution.Evolvable;
import ch.idsia.evolution.MLP;

public class FangMLPAgent extends BasicMarioAIAgent implements Agent, Evolvable {
	
    private Environment environment;
    
    /*final*/
    protected byte[][] levelScene;
    /*final */
    protected byte[][] enemies;
    protected byte[][] mergedObservation;

  //  protected float[] marioFloatPos = null;
   // protected float[] enemiesFloatPos = null;

    protected int[] marioState = null;

    protected int marioStatus;
    protected int marioMode;
    protected boolean isMarioOnGround;
    protected boolean isMarioAbleToJump;
    protected boolean isMarioAbleToShoot;
    protected boolean isMarioCarrying;
    protected int getKillsTotal;
    protected int getKillsByFire;
    protected int getKillsByStomp;
    protected int getKillsByShell;
    public int[] direction = new int[2];
    private int[] marioCenter;
    int trueJumpCounter = 0;
    int fireCounter = 0;
    int blockedCounter = 0;
    int timer = 0; 
 //   byte[][] scene = levelScene;
    
    private int numInputs = 7;
    private int numHidden = 75;
    boolean isStuck = false;
    public FangMLP neuralNet;
    MarioEnvironment env;
    public FangMLPAgent(){
        super("FangNeuralNet");

    	neuralNet = new FangMLP(numInputs, numHidden, Environment.numberOfButtons);
    	//neuralNet.evalLayer(layer);
        env = MarioEnvironment.getInstance();
        this.marioCenter = env.getMarioReceptiveFieldCenter();

       // integrateObservation(env);
    	getAction();
        reset();

    }
    private FangMLPAgent(FangMLP mlp)
    {
    	super("FangNN");
        this.neuralNet = mlp;
        env = MarioEnvironment.getInstance();
        this.marioCenter = env.getMarioReceptiveFieldCenter();

        //integrateObservation(env);
    }
    public double nearestEnemy(){
    //	if (env == null) env = MarioEnvironment.getInstance();
    	int[] marioPos = env.getMarioReceptiveFieldCenter();
    			//  System.out.println(marioPos[0] + "," + marioPos[1]);
        float[] enPos = env.getEnemiesFloatPos();
      //  String string = "";
       // direction[0] = 1;
      //  System.out.println(enPos.length);
        
        int x = marioPos[0];
       int y = marioPos[1];
          
        float eType, eX, eY;
         if (enPos.length > 0) {
        	 fireWeapon(fireCounter);
        	for (int i = 0; i <= enPos.length-1; i+=3){
        		eType = enPos[i];
        		eX = enPos[i+1];
        		eY = enPos[i+2];
        		double dist = calcManhattanDist(x,y,eX,eY);
        		if (dist > 16){
        			direction[0] = 1;
        		}
        		return dist;
        	}
        }
         else{
         	if (checkWall() > 5){
        		blockedCounter++;
        		if (blockedCounter > 10 && !isStuck){
        			isStuck = true;
        			timer = 20;
        		}
        	}
        	if (isStuck){
        		direction[0] = -1;
        		if (timer <= 0){
        			isStuck = false;
        			blockedCounter = 0;
        		}
        		timer --;
        	}
         }
        return 0.0d;
    }
   /* private boolean checkStuck(int walls, int enemies){
    	if (enemies > 0){
    		
    	}
    	else {
        	if (walls > 5){
        		blockedCounter++;
        		if (blockedCounter > 10 && !isStuck){
        			isStuck = true;
        			timer = 20;
        		}
        	}
        	if (isStuck){
        		direction[0] = -1;
        		if (timer <= 0){
        			isStuck = false;
        			blockedCounter = 0;
        		}
        		timer --;
        	}
        	return isStuck;
    	}

    }*/
    private boolean gapCheck(byte[][] levelScene)
    {
    	marioCenter = MarioEnvironment.getInstance().getMarioReceptiveFieldCenter();
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
    private boolean jumpCheck ()
    {
    	boolean jump = false;
    	marioCenter = env.getMarioReceptiveFieldCenter();
        if (
                gapCheck(levelScene))
        {
            if (isMarioAbleToJump || (!isMarioOnGround && action[Mario.KEY_JUMP]))
            {
                jump = true;
            }
            ++trueJumpCounter;
        }
        else
        {
            jump = false;
            trueJumpCounter = 0;
        }

        if (trueJumpCounter > 12)
        {
        	trueJumpCounter = 0;
            jump = false;
        }
        return jump;
    }
    public double calcManhattanDist(float x1, float y1, float x2,  float y2) 
    {
    	double dist = Math.abs(x1 - x2) + Math.abs(y1 - y2);
    	return Math.sqrt( dist );
    }
    public double fitness = 0.0d;

	@Override
	public Evolvable getNewInstance() {
		// TODO Auto-generated method stub
		return new FangMLPAgent(neuralNet.getNewInstance());
	}
	//public Evolvable recombine(){
	//	return new FangMLPAgent(neuralNet.recombine(last, pBest, gBest))
	//}
	@Override
	public Evolvable copy() {
		// TODO Auto-generated method stub
		return new FangMLPAgent(neuralNet.copy());
	}

	@Override
	public void mutate() {
		neuralNet.mutate();
		// TODO Auto-generated method stub
		
	}
	public void determineActions(){
		
	}
	@Override
	public boolean[] getAction() {
		//System.out.println("Nearest enemy:" + nearestEnemy());
    	boolean[] action = new boolean[Environment.numberOfButtons];
		double[] inputs = new double[numInputs];
		for (int i = 0; i < numInputs;i++){
			inputs[i] = neuralNet.random();
		}
		int numWalls = checkWall();
		float[] numEnemies = env.getEnemiesFloatPos();
		inputs[0] = isMarioAbleToJump ? 1 : 0;
		inputs[1] = isMarioOnGround ? 1 : 0;
		inputs[2] = isMarioAbleToShoot ? 1 : 0;
		inputs[3] = nearestEnemy() > 16 ? 1 : 0;
		inputs[4] = numWalls > 0 ? 1 : 0;
		inputs[5] = jumpCheck() == true ? 1 : 0;
		inputs[6] = direction[0] == 1 ? 1 : 0;
		//inputs[6] = checkStuck(numWalls, numEnemies.length) == true ? 1 : 0;
		//inputs[5] = Mario.coins;
		//inputs[6] = marioStatus;
		//inputs[5] = fireCounter > 0 ? 1 : 0;
		
	//	System.out.println(nearestEnemy());
	//	System.out.println(inputs.length);
	    double[] out = neuralNet.getOutput(inputs.length, numHidden, Environment.numberOfButtons, inputs);
	    //neuralNet.evalLayer(out);
    	//action[0] = nearestEnemy() > 0 ? 1 : 0;
	//    String res = "";
        for (int i = 0; i < action.length; i++)
        {
            action[i] = out[i] > 0;
            
            if (i == Mario.KEY_RIGHT){
            	action[i] =(out[i] > 0) || (direction[0] == 1 ? true : false);
            	
            }
  //          res += action[i] + " ";
        }
      //  action[Mario.KEY_RIGHT] = out[Mario.KEY_RIGHT] > 0.2 ? true : false;
        //System.out.println(out[Mario.KEY_LEFT]);
       // action[Mario.KEY_LEFT] = false;
    //     action[Mario.KEY_LEFT] = out[Mario.KEY_LEFT] < 0.2 ? true : false;;
//		 System.out.println(res);
		// TODO Auto-generated method stub
		return action;
	}
    
    public void fireWeapon(int counter)
    {
    	if (counter > 1){
    		//action[Mario.KEY_SPEED] = false;
    		counter = 0;
    	}
    	else{
    		//action[Mario.KEY_SPEED] = true;
    		counter++;
    	}
    	
    	
    }

    private int checkWall(){
    	//check height loop ( for walls);
        int[] marioCenter = env.getMarioReceptiveFieldCenter();
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
        	//	 System.out.println("Halp blocked");
        	}
        }
      //  System.out.println(maxX + ", " + maxHeight);
        return blocked;
    }
	@Override
	public void integrateObservation(Environment environment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void giveIntermediateReward(float intermediateReward) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		neuralNet.reset();
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public double getMutation()
	{
		return neuralNet.getMutation();
	}
	@Override
	public void tuneMutation(double val) {
		// TODO Auto-generated method stub
		neuralNet.tuneMutation(val);
	}
	@Override
	public void recombine(FangMLP pBest, FangMLP gBest) {
	//	pBest.debug();
		neuralNet.recombine(pBest, gBest);
		// TODO Auto-generated method stub
		//Evolvable cpy = new FangMLP(neuralNet.copy());
		//neuralNet.recombine(pBest, gBest);
		
		//return 
	}
	@Override
	public void tuneDeviation(double val) {
		// TODO Auto-generated method stub
		neuralNet.tuneDeviation(val);
	}
	@Override
	public double getDeviation() {
		// TODO Auto-generated method stub
		return neuralNet.getDeviation();
	}

}
