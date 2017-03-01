package fang.NeuralNetwork;

import ch.idsia.agents.learning.LargeMLPAgent;
import ch.idsia.agents.learning.SimpleMLPAgent;
import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.benchmark.tasks.ProgressTask;
import ch.idsia.evolution.Evolvable;
import ch.idsia.evolution.ea.ES;
import ch.idsia.scenarios.oldscenarios.Stats;
import ch.idsia.tools.CmdLineOptions;
import ch.idsia.utils.wox.serial.Easy;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Jun 14, 2009
 * Time: 2:15:51 PM
 */
public class EvolveSingle
{
    final static int generations = 1000;
    final static int populationSize = 100;
    public static void main(String[] args)
    {
        CmdLineOptions options = new CmdLineOptions(new String[0]);
//        options.setNumberOfTrials(1);
        options.setPauseWorld(false);
        Evolvable initial = new FangMLPAgent();
        
//        RegisterableAgent.registerAgent ((Agent) initial);
        options.setFPS(GlobalOptions.MaxFPS);
        options.setLevelDifficulty(0);
        options.setVisualization(false);
        ProgressTask task = new ProgressTask(options);
      // options.setLevelRandSeed(1);
         options.setLevelRandSeed((int) (Math.random() * 10));
        FangES es = new FangES(task, initial, populationSize);
        int counter = 0;
        System.out.println("Evolving " + initial + " with task " + task);
        final String fileName = "evolved" + (int) (Math.random() * Integer.MAX_VALUE) + ".xml";
          for (int gen = 0; gen < generations; gen++)
        {
        	es.setGen(gen);
            es.nextGeneration();
            es.checkBest();
        //    System.out.println(es.getPopulation());
            
            Easy.save(es.getBests()[0], fileName);

      //      best = es.getBests()[0];
        }
       Stats.main(new String[]{fileName, "1"});
    }
}
