package log.generation;

import java.util.Random;

import net.sf.javailp.Linear;
import net.sf.javailp.OptType;
import net.sf.javailp.Problem;
import net.sf.javailp.Result;
import net.sf.javailp.Solver;
import net.sf.javailp.SolverFactory;
import net.sf.javailp.SolverFactoryLpSolve;

public class IlpSolver {
	public static String CheckLinear(String activationCondition, String maxCondition,
			int maxValue, String minCondition, int minValue) {

		String[] souce = activationCondition.split("");
		String[] dest = minCondition.split("");

		String sorucecond = souce[1]; // <=
		String destcond = dest[1]; // <=
		SolverFactory factory = new SolverFactoryLpSolve(); // use lp_solve
		factory.setParameter(Solver.VERBOSE, 0);
		factory.setParameter(Solver.TIMEOUT, 100); // set timeout to 100 seconds
		Problem problem = new Problem();
		problem.setVarUpperBound(activationCondition, maxValue); // set upper
																// bond
		Linear linear = new Linear();
		linear.add(1, activationCondition);
		problem.setObjective(linear, OptType.MAX);
		problem.setVarType(activationCondition, Integer.class);
		// ------------------------------------
		linear = new Linear();
		problem.add(linear, sorucecond, maxValue);
		Solver solver = factory.get(); // you should use this solver only once
										// for one problem
		Result result = solver.solve(problem);
		// -------------------------------------------------------
		linear = new Linear();
		linear.add(1, activationCondition);
		problem.add(linear, destcond, minValue);
		result = solver.solve(problem);
		// ------------------------------------------------------------
		return result.toString();

	}	
	
	
	public static int selectRandom(int max) {
		Random rand = new Random();
		return rand.nextInt(max); 
	}

	public static String getPayload(String c) {
		String[] values = null;
		values = c.split(" ");
		return values[0].substring(1);
	}

	public static String getCondition(String c) {
		String[] values = null;
		values = c.split(" ");
		String s =values[1]; 
		return s;
	}

	public static String getPayloadValue(String c) {
		String[] values = null;
		values = c.split(" ");
		String s = values[2];
		return values[2].substring(0, values[2].length() - 1);
	}
	
	
	public static int getILPValue(String leftCondtion, String rightCodition){
		int ret =0;
		
		String leftX = getPayload(leftCondtion); //A.x
		String leftOpt = getCondition(leftCondtion); // <
		int leftValue = Integer.parseInt(getPayloadValue(leftCondtion)); //2
		
		String rightX = getPayload(rightCodition); //A.x
		String rightOpt = getCondition(rightCodition); // <
		int rightValue = Integer.parseInt(getPayloadValue(rightCodition)); //2
		
		if (leftOpt.length() ==1) leftOpt = leftOpt +"=";
		if (rightOpt.length() ==1) rightOpt = rightOpt +"=";
		
		   SolverFactory factory = new SolverFactoryLpSolve(); // use lp_solve
	        factory.setParameter(Solver.VERBOSE, 0);
	        factory.setParameter(Solver.TIMEOUT, 100); // set timeout to 100 second
	        Problem problem = new Problem();
	    
	        problem.setVarType(leftX, Integer.class);
	        problem.setVarLowerBound(leftX, Integer.MIN_VALUE);
	        problem.setVarUpperBound(leftX, Integer.MAX_VALUE);
	        Linear settings = new Linear();
	        settings.add(1,leftX);
	        
	        
	        problem.setObjective(settings, OptType.MIN);     
	        Linear eq1 = new Linear();
	        eq1.add(1, leftX);
	        problem.add(eq1, leftOpt, leftValue);
	        
	        Linear  eq2 = new Linear();
	        eq2.add(1, rightX);
	        problem.add(eq2, rightOpt, rightValue);
	        
	        Solver minsolver = factory.get(); // you should use this solver only once for one problem
	        Result minresult = minsolver.solve(problem);
	        System.out.println(minresult);	
	        
	        problem.setObjective(settings, OptType.MAX);	        
	        Solver maxsolver = factory.get(); // you should use this solver only once for one problem
	        Result maxresult = maxsolver.solve(problem);
	        System.out.println(maxresult);	   
	    
	        if ((minresult != null) && (maxresult != null))
	        {
	        	ret = getRandomValue(minresult.getObjective().intValue(), maxresult.getObjective().intValue());
	        	
	        }
	        
	        
		return ret;
	}
	
	public static int getRandomValue(int min, int max) {
		try {
			Random random = new Random();
			return random.nextInt(max - min) + min;
		} catch (IllegalArgumentException e) {
			return 0;
		}

	}
	
//	
//	public static int getIlpValue(String activationCondition, int rightvalue ){
//		int ret= 0;
//		
//		String left = getPayload(activationCondition); //A.x
//		String opt = getCondition(activationCondition); // <
//		int value = Integer.parseInt(getPayloadValue(activationCondition)); //2
//		
//        SolverFactory factory = new SolverFactoryLpSolve(); // use lp_solve
//        factory.setParameter(Solver.VERBOSE, 0);
//        factory.setParameter(Solver.TIMEOUT, 100); // set timeout to 100 seconds
//
//
//        Problem problem = new Problem();
//
//        String x= left;
//        problem.setVarType(x, Integer.class);
//        problem.setVarLowerBound(x, Integer.MIN_VALUE);
//        problem.setVarUpperBound(x, Integer.MAX_VALUE);
//        Linear settings = new Linear();
//        settings.add(1,x );
//        
//        if (opt.length() > 1)
//        	opt = opt +"=";
//
//        boolean ab =false;
//        
//       // problem.setObjective(linear, OptType.MIN);
////        if (opt.contains(">"))
////        {
////        	ab=true;
////        	problem.setObjective(settings, OptType.MIN);	
////        } else
////        {
////        	problem.setObjective(settings, OptType.MAX);
////        }
//        
//        problem.setObjective(settings, OptType.MIN);
//       
//
//
//        
//        Linear eq1 = new Linear();
//        eq1.add(1, left1x);
//        problem.add(eq1, op1, value1);
////        if (ab){problem.add(linear, "<=", value);} //x >2
////        else {problem.add(linear, ">=", value);} 
//   
//        // x >=5
//
//        Linear  eq2 = new Linear();
//        eq2.add(1, left2x);
//        problem.add(eq2, op2, value2);
//        
////        if (ab){problem.add(linear, ">=", rightvalue);} // x < 6
////        else {problem.add(linear, "<=", rightvalue);}
//        //problem.add(linear, "<=", 6);
//        Solver minsolver = factory.get(); // you should use this solver only once for one problem
//        Result minresult = minsolver.solve(problem);
//        System.out.println(minresult);		
////	// ------------------------------------------------------------
//   
//        
//        problem.setObjective(settings, OptType.MAX);
//        
//        
//        
//        Solver maxsolver = factory.get(); // you should use this solver only once for one problem
//        Result maxresult = maxsolver.solve(problem);
//        System.out.println(maxresult);	
//	
//		if ( result == null)
//			{ret = -1; }
//			else
//				{				
//					ret =  Integer.parseInt(result.getObjective().toString());
//				}
//	
//		return ret;
//	}




}
