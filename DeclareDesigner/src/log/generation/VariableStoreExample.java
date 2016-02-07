package log.generation;


import net.sf.javailp.Constraint;
import net.sf.javailp.Linear;
import net.sf.javailp.Operator;
import net.sf.javailp.OptType;
import net.sf.javailp.Problem;
import net.sf.javailp.Result;
import net.sf.javailp.Solver;
import net.sf.javailp.SolverFactory;
import net.sf.javailp.SolverFactoryLpSolve;

import net.sf.javailp.*;
import net.sourceforge.jeval.Eval;
import net.sourceforge.jeval.MalformedExpression;
import net.sourceforge.jeval.VariableStore;

public class VariableStoreExample implements VariableStore {

	/**
	 * Not a very useful getValue, just summs the ASCII value of the given
	 * string and returns it.
	 */
	@Override
	public double getValue(String variable) throws MalformedExpression {
		double b = 0;
		
		for(char c : variable.toCharArray())
			b += Character.getNumericValue(c);
		
		return b;
	}
	
	
	public void CheckLinear(String val){
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
		Problem problem = new Problem();

		Linear linear = new Linear();
		linear.add(143, "x");
		linear.add(60, "y");

		problem.setObjective(linear, OptType.MAX);

		linear = new Linear();
		linear.add(120, "x");
		linear.add(210, "y");

		problem.add(linear, "<=", 15000);

		linear = new Linear();
		linear.add(110, "x");
		linear.add(30, "y");

		problem.add(linear, "<=", 4000);

		linear = new Linear();
		linear.add(1, "x");
		linear.add(1, "y");

		problem.add(linear, "<=", 75);

		problem.setVarType("x", Integer.class);
		problem.setVarType("y", Integer.class);

		Solver solver = factory.get(); // you should use this solver only once for one problem
		Result result = solver.solve(problem);

		System.out.println(result);

		/**
		* Extend the problem with x <= 16 and solve it again
		*/
		problem.setVarUpperBound("x", 16);

		solver = factory.get();
		result = solver.solve(problem);

		System.out.println(result);	
		System.out.println("ILP=Result");
		System.out.println(result);
	}
	
	
	
	public boolean CheckExample(String val)
	{
		boolean ret =false;
VariableStore vse = new VariableStoreExample();
		
		try {
			
			System.err.println("Condtion2" +Eval.eval("$a - $a", vse));
			System.err.println("Condtion3" +Eval.eval("$a - $a", vse));
			System.err.println("Condtion4" +Eval.eval("$abc - $cba", vse));
			
			// Spaces can be included in variable names, if they're enclosed 
			// within ( and ) or "s
			System.err.println("Condtion5" +Eval.eval("$(hello world) - $(world hello)", vse));
			System.err.println("Condtion6" +Eval.eval("$hello(world) - $world(hello)", vse));
			System.err.println("Condtion7" +Eval.eval("$hello(\"world\") - $world(\"hello\")", vse));
			System.err.println("Condtion:::" + Eval.eval(val, vse));
			System.out.println("Condtion" + Eval.eval(val, vse));
			ret = true;
		} catch (MalformedExpression e) {
			e.printStackTrace();
		}	
		
		return ret;	
		
	}
	
}
