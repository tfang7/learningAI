package ch.idsia.agents.learning;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.evolution.Evolvable;
import ch.idsia.evolution.FangNN;

public class FangLearnerAgent implements Agent, Evolvable {
	
    private Environment environment;

    /*final*/
    protected byte[][] levelScene;
    /*final */
    protected byte[][] enemies;
    protected byte[][] mergedObservation;

    protected float[] marioFloatPos = null;
    protected float[] enemiesFloatPos = null;

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
	
    public FangNN neuralNet;

    public FangLearnerAgent(){
    	neuralNet = new FangNN(10, 10, Environment.numberOfButtons);
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
	public void mutate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean[] getAction() {
		// TODO Auto-generated method stub
		return null;
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

}
