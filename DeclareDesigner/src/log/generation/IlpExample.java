package log.generation;

import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.naming.spi.DirStateFactory.Result;

import net.sf.javailp.Linear;
import net.sf.javailp.OptType;
import net.sf.javailp.Problem;
import net.sf.javailp.Solver;
import net.sf.javailp.SolverFactory;
import net.sf.javailp.SolverFactoryLpSolve;

public class IlpExample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		runILP();
	}
	
	public static void runILP(){
		
		SolverFactory factory = new SolverFactoryLpSolve(); // use lp_solve
		factory.setParameter(Solver.VERBOSE, 0); 
		factory.setParameter(Solver.TIMEOUT, 100); // set timeout to 100 seconds

		/**
		* Constructing a Problem: 
		* Maximize: 143x+60y 
		* Subject to: 
		* 120x+210y <= 15000 
		* 110x+30y <= 4000 
		* x+y <= 75
		* 
		* With x,y being integers
		*
		*/
		Problem problemx = new Problem();
		Linear linearx = new Linear();
		linearx.add(1,"text");

		problemx.add(linearx, ">=", 2);
		problemx.setObjective(linearx, OptType.MAX);
		problemx.setVarType("text", Integer.class);
		Solver solverx = factory.get(); // you should use this solver only once for one problem
		net.sf.javailp.Result resultx = solverx.solve(problemx);
		System.out.println("problemx: "+resultx);
		
		Problem problem = new Problem();
		Problem problem2 = new Problem();
		String[] res = "x <= 2 :: x <= 6 :: x <= 3".split("::");
		ArrayList<Linear> lnr = new ArrayList<Linear>();
		for (int i=0; i < res.length; i++){
			String[] ops = res[i].trim().split(" ");
			Linear linear = new Linear();
			linear.add(1,ops[0]);
			lnr.add(linear);
			problem.add(linear, ops[1], Integer.parseInt(ops[2]));
			problem.setObjective(linear, OptType.MAX);
			problem.setVarType(ops[0], Integer.class);
			
			problem2.add(linear, ops[1], Integer.parseInt(ops[2]));
			problem2.setObjective(linear, OptType.MIN);
			problem2.setVarType(ops[0], Integer.class);
		}
		
		Solver solver = factory.get(); // you should use this solver only once for one problem
		net.sf.javailp.Result result = solver.solve(problem);

		System.out.println(result);
		
		Solver solver2 = factory.get(); // you should use this solver only once for one problem
		net.sf.javailp.Result result2 = solver.solve(problem2);

		System.out.println(result2);


		/**
		* Extend the problem with x <= 16 and solve it again
		*/
/*		problem.setVarUpperBound("x", Integer.MAX_VALUE);
		solver = factory.get();
		result = solver.solve(problem);
		System.out.println(result);	
		
		problem2.setVarUpperBound("x", Integer.MIN_VALUE);
		solver = factory.get();
		result = solver.solve(problem2);
		System.out.println(result);	
*/	}

}
