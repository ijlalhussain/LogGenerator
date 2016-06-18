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

public class ILPSolverUtil {

	public static void purifyLog(ArrayList<String> combinlist,
			LinkedHashMap<String, Alphabet> abMapx) {
		LinkedHashMap<String, Alphabet> abMaptemp = new LinkedHashMap<String, Alphabet>();

		for (Entry<String, Alphabet> activity : abMapx.entrySet()) {
			String k = activity.getKey();
			Alphabet alphabet = activity.getValue();
			if (!alphabet.logGenerate) {
				System.out.println("ILP Not Allowed:" + k);
				abMaptemp.put(k, alphabet);

			} else {
				System.out.println("ILP Allowed : " + k);
			}
		}

		for (Entry<String, Alphabet> activity : abMaptemp.entrySet()) {

			abMapx.remove(activity.getKey());
			int temp = -1;
			for (int cmb = 0; cmb < combinlist.size(); cmb++) {
				if (combinlist.get(cmb).replaceAll(" ", "").trim()
						.equals(activity.getKey())) {
					temp = cmb;
					break;
				}
			}

			if (temp >= 0) {
				combinlist.remove(temp);
			}
		}

	}
	
	
	public static void CheckIkpValue(String condition){
		
		ArrayList<String> linearList = new ArrayList();
		int MaxValue = 0;
		int MinValue = 0;
		boolean isGreater =false;
				SolverFactory factory = new SolverFactoryLpSolve(); // use
																	// lp_solve
				factory.setParameter(Solver.VERBOSE, 0);
				factory.setParameter(Solver.TIMEOUT, 100); // set timeout to 100
															// seconds

				Problem problem = new Problem();
				Problem problem2 = new Problem();
				String[] res = condition.split("::");// "x <= 2 :: x <= 6 :: x <= 3".split("::");
				//ArrayList<Linear> lnr = new ArrayList<Linear>();
				linearList.clear();
				String xplayload = "";
				int XplayloadValue =0;
				for (int i = 0; i < res.length; i++) {
					String[] ops = res[i].trim().split(" ");
					String payload = ops[0].substring(1);
					String payloadValue = ops[2].substring(0,ops[2].length() - 1);
					String opt = ops[1];
					if (opt.length() == 1) {
						opt = opt + "=";
					}
					
					if (payloadValue.isEmpty()){
						payloadValue ="1000";
					}
					linearList.add(opt);
					Linear linear = new Linear();
					if (i==0){
						xplayload = payload;
						XplayloadValue = Integer.parseInt(payloadValue);
						
					}else
					{
						linear.add(XplayloadValue, xplayload);
						linear.add(-XplayloadValue, payload);
						problem.add(linear, opt, XplayloadValue);
						problem.setObjective(linear, OptType.MAX);
						problem.setVarType(payload, Integer.class);

						problem2.add(linear, opt, XplayloadValue);
						problem2.setObjective(linear, OptType.MIN);
						problem2.setVarType(payload, Integer.class);
					}			
					
					
					//lnr.add(linear);

				
				}

				Solver solver = factory.get(); // you should use this solver
												// only once for one problem
				net.sf.javailp.Result result = solver.solve(problem);

		 	//System.out.println("Key:"+ k + " ILP Cond: " + alphabet.ilpCondition);
		//		System.out.println(result);

				// Solver solver2 = factory.get(); // you should use this solver
				// only once for one problem
				net.sf.javailp.Result result2 = solver.solve(problem2);

				//System.out.println(result2);

			if (AllGreater(linearList)){
				MaxValue = Integer.MAX_VALUE;
				MinValue = result2.getObjective().intValue();
			//	alphabet.logGenerate = true;
				System.out.println("All Greater: Max " + MaxValue + " Min : "+ result2.getObjective().intValue());
			} 
			else if (AllLesser(linearList)){
				//alphabet.maxValue = result.getObjective().intValue();
				//Integer s = Integer.MIN_VALUE;
				//System.out.println(s);
				MinValue =  Integer.MIN_VALUE;
				MaxValue = result.getObjective().intValue();
			//	alphabet.logGenerate = true;
				System.out.println("AllLesser: Max " + MaxValue + " Min : "+ MinValue  );
			}
			else {
				if ((result == null) || (result2 == null)) {
				MaxValue = 0;
				MinValue =0;
				System.out.println("Key:  Result: Max " + result.getObjective().intValue() + " Min : "+ result2.getObjective().intValue());
				System.out.println("NullNull: Max " + MaxValue + " Min : "+ MinValue  );
				} else {
					MaxValue = result.getObjective().intValue();
					MinValue = result2.getObjective().intValue();
					//alphabet.logGenerate = true;
					System.out.println(" Result: Max " + result.getObjective().intValue() + " Min : "+ result2.getObjective().intValue());
				}
			} // not all greater
			//	abMapx.put(k, alphabet);

		//	} // end of isemty
	//	}
		
	}

	public static void CheckIlpConditions(LinkedHashMap<String, Alphabet> abMapx) {
		ArrayList<String> linearList = new ArrayList();
		boolean isGreater = false;
		for (Entry<String, Alphabet> activity : abMapx.entrySet()) {
			String k = activity.getKey();
			Alphabet alphabet = activity.getValue();

			if (alphabet.ilpCondition.isEmpty()) {
				alphabet.maxValue = 0;
				alphabet.minValue = 0;
				alphabet.logGenerate = true;
			} else {
				SolverFactory factory = new SolverFactoryLpSolve(); // use
																	// lp_solve
				factory.setParameter(Solver.VERBOSE, 0);
				factory.setParameter(Solver.TIMEOUT, 100); // set timeout to 100
															// seconds

				Problem problem = new Problem();
				Problem problem2 = new Problem();
				String[] res = alphabet.ilpCondition.split("::");// "x <= 2 :: x <= 6 :: x <= 3".split("::");
				// ArrayList<Linear> lnr = new ArrayList<Linear>();
				linearList.clear();
				for (int i = 0; i < res.length; i++) {
					String[] linearListValues = res[i].trim().split(" ");
					String payload = linearListValues[0].substring(1);
					String payloadValue = linearListValues[2].substring(0,
							linearListValues[2].length() - 1);
					String operater = linearListValues[1];
					if (operater.length() == 1) {
						operater = operater + "=";
					}

					linearList.add(operater);
					Linear linear = new Linear();
					linear.add(1, payload);
					// lnr.add(linear);

					problem.add(linear, operater,
							Integer.parseInt(payloadValue));
					problem.setObjective(linear, OptType.MAX);
					problem.setVarType(payload, Integer.class);

					problem2.add(linear, operater,
							Integer.parseInt(payloadValue));
					problem2.setObjective(linear, OptType.MIN);
					problem2.setVarType(payload, Integer.class);
				}

				Solver solver = factory.get(); // you should use this solver
												// only once for one problem
				net.sf.javailp.Result result = solver.solve(problem);

				System.out.println("Key:" + k + " ILP Cond: "
						+ alphabet.ilpCondition);
				// System.out.println(result);

				// Solver solver2 = factory.get(); // you should use this solver
				// only once for one problem
				net.sf.javailp.Result result2 = solver.solve(problem2);

				// System.out.println(result2);

				if (AllGreater(linearList)) {
					alphabet.maxValue = Integer.MAX_VALUE;
					alphabet.minValue = result2.getObjective().intValue();
					alphabet.logGenerate = true;
					System.out.println("All Greater: Max " + alphabet.maxValue
							+ " Min : " + result2.getObjective().intValue());
				} else if (AllLesser(linearList)) {
					alphabet.maxValue = result.getObjective().intValue();
					// Integer s = Integer.MIN_VALUE;
					// System.out.println(s);
					alphabet.minValue = Integer.MIN_VALUE;
					alphabet.maxValue = result.getObjective().intValue();
					alphabet.logGenerate = true;
					System.out.println("AllLesser: Max " + alphabet.maxValue
							+ " Min : " + alphabet.minValue);
				} else {
					if ((result == null) || (result2 == null)) {
						alphabet.maxValue = 0;
						alphabet.minValue = 0;
						alphabet.logGenerate = false;
						System.out.println("Key: " + k + " : Result : Null");
					} else {
						alphabet.maxValue = result.getObjective().intValue();
						alphabet.minValue = result2.getObjective().intValue();
						alphabet.logGenerate = true;
						System.out.println("Key: " + k + " Result: Max "
								+ result.getObjective().intValue() + " Min : "
								+ result2.getObjective().intValue());
					}
				} // not all greater
				abMapx.put(k, alphabet);

			} // end of isemty
		}

	}
	
	public static boolean AllLesser(ArrayList<String> linearList){
		for (int i = 0; i < linearList.size(); i++) {
			if (!linearList.get(i).substring(0, 1).equals("<")) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean AllGreater(ArrayList<String> linearList){
		for (int i = 0; i < linearList.size(); i++) {
			if (!linearList.get(i).substring(0, 1).equals(">")) {
				return false;
			}
		}
		return true;
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
}
