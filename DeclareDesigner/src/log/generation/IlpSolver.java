package log.generation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Map.Entry;

import net.sf.javailp.Linear;
import net.sf.javailp.OptType;
import net.sf.javailp.Problem;
import net.sf.javailp.Result;
import net.sf.javailp.Solver;
import net.sf.javailp.SolverFactory;
import net.sf.javailp.SolverFactoryLpSolve;

public class IlpSolver {

	public static void purifyLog(ArrayList<String> combinlist,
			LinkedHashMap<String, Alphabet> abMapx) {
		LinkedHashMap<String, Alphabet> abMaptemp = new LinkedHashMap<String, Alphabet>();
		
		for (Entry<String, Alphabet> activity : abMapx.entrySet()) {
			String k = activity.getKey();
			Alphabet filter = activity.getValue();
			if (!filter.logGenerate){
				System.out.println("Not fitted" + k);
				abMaptemp.put(k, filter);	
				
			} else
			{
				System.out.println("fitted" + k);
			}
		}
		
		for (Entry<String, Alphabet> activity : abMaptemp.entrySet()) {
			
			abMapx.remove(activity.getKey());
			int temp = -1;
			for (int cmb=0; cmb < combinlist.size(); cmb++ ){
				if (combinlist.get(cmb).replaceAll(" ", "").trim().equals(activity.getKey())){
					temp=cmb;
					break;
				}
			}
			
			if (temp>=0){
				combinlist.remove(temp);
			}
		}
		
	}

	public static void CheckIlpConditions(ArrayList<String> combinlist,
			LinkedHashMap<String, Alphabet> abMapx) {
		for (Entry<String, Alphabet> activity : abMapx.entrySet()) {
			String k = activity.getKey();
			Alphabet filter = activity.getValue();

			if (filter.ilpCondition.isEmpty()) {
				filter.maxValue = 0;
				filter.minValue = 0;
				filter.logGenerate = true;
			} else {
				SolverFactory factory = new SolverFactoryLpSolve(); // use
																	// lp_solve
				factory.setParameter(Solver.VERBOSE, 0);
				factory.setParameter(Solver.TIMEOUT, 100); // set timeout to 100
															// seconds

				Problem problem = new Problem();
				Problem problem2 = new Problem();
				String[] res = filter.ilpCondition.split("::");// "x <= 2 :: x <= 6 :: x <= 3".split("::");
				ArrayList<Linear> lnr = new ArrayList<Linear>();
				for (int i = 0; i < res.length; i++) {
					String[] ops = res[i].trim().split(" ");
					String payload = ops[0].substring(1);
					String payloadValue = ops[2].substring(0,
							ops[2].length() - 1);
					String opt = ops[1];
					if (opt.length() == 1) {
						opt = opt + "=";
					}
					Linear linear = new Linear();
					linear.add(1, payload);
					lnr.add(linear);

					problem.add(linear, opt, Integer.parseInt(payloadValue));
					problem.setObjective(linear, OptType.MAX);
					problem.setVarType(payload, Integer.class);

					problem2.add(linear, opt, Integer.parseInt(payloadValue));
					problem2.setObjective(linear, OptType.MIN);
					problem2.setVarType(payload, Integer.class);
				}

				Solver solver = factory.get(); // you should use this solver
												// only once for one problem
				net.sf.javailp.Result result = solver.solve(problem);

				System.out.println(filter.ilpCondition);
				System.out.println(result);

				// Solver solver2 = factory.get(); // you should use this solver
				// only once for one problem
				net.sf.javailp.Result result2 = solver.solve(problem2);

				System.out.println(result2);

				if ((result == null) || (result2 == null)) {
					filter.maxValue = 0;
					filter.minValue = 0;
					filter.logGenerate = false;
				} else {
					filter.maxValue = result.getObjective().intValue();
					filter.minValue = result2.getObjective().intValue();
					filter.logGenerate = true;
				}

				abMapx.put(k, filter);

			} // end of isemty
		}

	}

	public static String CheckLinear(String activationCondition,
			String maxCondition, int maxValue, String minCondition, int minValue) {

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
		try {
			String[] values = null;
			values = c.split(" ");
			return values[0].substring(1);
		} catch (IndexOutOfBoundsException e) {
			return "None";
		}

	}

	public static String getCondition(String c) {
		try {
			String[] values = null;
			values = c.split(" ");
			String s = values[1];
			return s;
		} catch (IndexOutOfBoundsException e) {
			return "";
		}
	}

	public static String getPayloadValue(String c) {
		try {
			String[] values = null;
			values = c.split(" ");
			String s = values[2];
			return values[2].substring(0, values[2].length() - 1);
		} catch (IndexOutOfBoundsException e) {
			return "0";
		}
	}

	public static int getILPValue(String leftCondtion, String rightCodition) {
		int ret = 0;

		String leftX = getPayload(leftCondtion); // A.x
		String leftOpt = getCondition(leftCondtion); // <
		int leftValue = Integer.parseInt(getPayloadValue(leftCondtion)); // 2

		if (leftOpt.length() == 1)
			leftOpt = leftOpt + "=";

		SolverFactory factory = new SolverFactoryLpSolve(); // use lp_solve
		factory.setParameter(Solver.VERBOSE, 0);
		factory.setParameter(Solver.TIMEOUT, 100); // set timeout to 100 second
		Problem problem = new Problem();

		problem.setVarType(leftX, Integer.class);
		problem.setVarLowerBound(leftX, Integer.MIN_VALUE);
		problem.setVarUpperBound(leftX, Integer.MAX_VALUE);
		Linear settings = new Linear();
		settings.add(1, leftX);

		problem.setObjective(settings, OptType.MIN);
		Linear eq1 = new Linear();
		eq1.add(1, leftX);
		problem.add(eq1, leftOpt, leftValue);

		Solver minsolver = factory.get(); // you should use this solver only
											// once for one problem
		Result minresult = minsolver.solve(problem);
		System.out.println(minresult);

		if (rightCodition.isEmpty()) {
			String rightX = getPayload(rightCodition); // A.x
			String rightOpt = getCondition(rightCodition); // <
			int rightValue = Integer.parseInt(getPayloadValue(rightCodition)); // 2
			if (rightOpt.length() == 1)
				rightOpt = rightOpt + "=";

			Linear eq2 = new Linear();
			eq2.add(1, rightX);
			problem.add(eq2, rightOpt, rightValue);

			problem.setObjective(settings, OptType.MAX);
			Solver maxsolver = factory.get(); // you should use this solver only
												// once for one problem
			Result maxresult = maxsolver.solve(problem);
			System.out.println(maxresult);

			if ((minresult != null) && (maxresult != null)) {
				ret = getRandomValue(minresult.getObjective().intValue(),
						maxresult.getObjective().intValue());

			}
		} else {
			ret = getRandomValue(0, minresult.getObjective().intValue());
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
	// public static int getIlpValue(String activationCondition, int rightvalue
	// ){
	// int ret= 0;
	//
	// String left = getPayload(activationCondition); //A.x
	// String opt = getCondition(activationCondition); // <
	// int value = Integer.parseInt(getPayloadValue(activationCondition)); //2
	//
	// SolverFactory factory = new SolverFactoryLpSolve(); // use lp_solve
	// factory.setParameter(Solver.VERBOSE, 0);
	// factory.setParameter(Solver.TIMEOUT, 100); // set timeout to 100 seconds
	//
	//
	// Problem problem = new Problem();
	//
	// String x= left;
	// problem.setVarType(x, Integer.class);
	// problem.setVarLowerBound(x, Integer.MIN_VALUE);
	// problem.setVarUpperBound(x, Integer.MAX_VALUE);
	// Linear settings = new Linear();
	// settings.add(1,x );
	//
	// if (opt.length() > 1)
	// opt = opt +"=";
	//
	// boolean ab =false;
	//
	// // problem.setObjective(linear, OptType.MIN);
	// // if (opt.contains(">"))
	// // {
	// // ab=true;
	// // problem.setObjective(settings, OptType.MIN);
	// // } else
	// // {
	// // problem.setObjective(settings, OptType.MAX);
	// // }
	//
	// problem.setObjective(settings, OptType.MIN);
	//
	//
	//
	//
	// Linear eq1 = new Linear();
	// eq1.add(1, left1x);
	// problem.add(eq1, op1, value1);
	// // if (ab){problem.add(linear, "<=", value);} //x >2
	// // else {problem.add(linear, ">=", value);}
	//
	// // x >=5
	//
	// Linear eq2 = new Linear();
	// eq2.add(1, left2x);
	// problem.add(eq2, op2, value2);
	//
	// // if (ab){problem.add(linear, ">=", rightvalue);} // x < 6
	// // else {problem.add(linear, "<=", rightvalue);}
	// //problem.add(linear, "<=", 6);
	// Solver minsolver = factory.get(); // you should use this solver only once
	// for one problem
	// Result minresult = minsolver.solve(problem);
	// System.out.println(minresult);
	// // // ------------------------------------------------------------
	//
	//
	// problem.setObjective(settings, OptType.MAX);
	//
	//
	//
	// Solver maxsolver = factory.get(); // you should use this solver only once
	// for one problem
	// Result maxresult = maxsolver.solve(problem);
	// System.out.println(maxresult);
	//
	// if ( result == null)
	// {ret = -1; }
	// else
	// {
	// ret = Integer.parseInt(result.getObjective().toString());
	// }
	//
	// return ret;
	// }

}
