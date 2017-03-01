package ch.idsia.scenarios;

import ch.idsia.agents.Agent;
import ch.idsia.agents.controllers.ForwardAgent;
import fang.NeuralNetwork.*;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.mario.environments.MarioEnvironment;
import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.benchmark.tasks.MarioCustomSystemOfValues;
import ch.idsia.tools.CmdLineOptions;

/**
 * Created by IntelliJ IDEA. User: Sergey Karakovskiy, sergey at idsia dot ch Date: Mar 17, 2010 Time: 8:28:00 AM
 * Package: ch.idsia.scenarios
 */
public final class mariio
{
public static void main(String[] args)
{
        //final String argsString = "-vis on -ld 25 -ag ch.idsia.agents.controllers.ScaredShooty";
final String argsString = "-vis on -ld 0 -ag fang.NeuralNetwork.FangMLPAgent";
        final CmdLineOptions cmdLineOptions = new CmdLineOptions(argsString);
//        final Environment environment = new MarioEnvironment();
 //       final Agent agent = new ForwardJumpingAgent();
//        final Agent agent = cmdLineOptions.getAgent();
//        final Agent a = AgentsPool.load("ch.idsia.controllers.agents.controllers.ForwardJumpingAgent");
    final BasicTask basicTask = new BasicTask(cmdLineOptions);
    int seed = 0;
        for (int i = 0; i < 10; ++i)
        {
            
            do
            {
                //cmdLineOptions.setLevelDifficulty(5);                
                cmdLineOptions.setLevelRandSeed(seed++);
    basicTask.reset(cmdLineOptions);
    basicTask.runOneEpisode();
    System.out.println(basicTask.getEnvironment().getEvaluationInfoAsString());
            } while (basicTask.getEnvironment().getEvaluationInfo().marioStatus != Environment.MARIO_STATUS_WIN);
        }
//
    final MarioCustomSystemOfValues sov = new MarioCustomSystemOfValues();
    System.out.println(basicTask.getEnvironment().getEvaluationInfo().computeWeightedFitness(sov));
    System.exit(0);
}

}