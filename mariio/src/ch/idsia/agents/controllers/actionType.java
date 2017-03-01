package ch.idsia.agents.controllers;

public enum actionType 
{
	LEFT,
	LEFTJUMP,
	JUMP,
	DOWN,
	RIGHT,
	RIGHTJUMP;

	//
	//make enums for these
	//actionlist[0] = leftAction;
	//actionlist[1] = leftJumpAction;
	//actionlist[2] = jumpAction;
	//actionlist[3] = downAction;
	//actionlist[4] = rightAction;
	//actionlist[5] = rightJumpAction;
	public static boolean[] getAction(actionType type)
	{
		if (ActionController.actionlist == null) ActionController.init();
		switch(type){
			case LEFT:
				return ActionController.getAction(0);
			case LEFTJUMP:
				return ActionController.getAction(1);
			case JUMP:
				return ActionController.getAction(2);
			case DOWN:
				return ActionController.getAction(3);
			case RIGHT:
				return ActionController.getAction(4);
			case RIGHTJUMP:
				return ActionController.getAction(5);
		default:
				return null;
		}
	}


	
	
}
