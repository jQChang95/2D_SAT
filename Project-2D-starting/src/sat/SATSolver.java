package sat;

import immutable.EmptyImList;
import immutable.ImList;
import sat.env.Environment;
import sat.env.Variable;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;

/**
 * A simple DPLL SAT solver. See http://en.wikipedia.org/wiki/DPLL_algorithm
 */
public class SATSolver {
    /**
     * Solve the problem using a simple version of DPLL with backtracking and
     * unit propagation. The returned environment binds literals of class
     * bool.Variable rather than the special literals used in clausification of
     * class clausal.Literal, so that clients can more readily use it.
     * 
     * @return an environment for which the problem evaluates to Bool.TRUE, or
     *         null if no such environment exists.
     */
    public static Environment solve(Formula formula) {
        // this calls the next function
    	ImList<Clause> clauses = formula.getClauses();
    	Environment env = new Environment();
    	return solve(clauses, env);
    }

    /**
     * Takes a partial assignment of variables to values, and recursively
     * searches for a complete satisfying assignment.
     * 
     * @param clauses
     *            formula in conjunctive normal form
     * @param env
     *            assignment of some or all variables in clauses to true or
     *            false values.
     * @return an environment for which all the clauses evaluate to Bool.TRUE,
     *         or null if no such environment exists.
     */
    private static Environment solve(ImList<Clause> clauses, Environment env) {
    	//If there are no clauses, the formula is trivially satisfiable.
    	//• If there is an empty clause, the clause list is unsatisfiable -- fail and backtrack. (use empty
    	//	clause to denote a clause evaluated to FALSE based on the variable binding in the
    	//	environment)
    	if (clauses.isEmpty()){
    		return env;
    	}
    	//empty clause
    	for (Clause clause : clauses){
    		if (clause.isEmpty()){
    			return null;
    		}
    	}

    	//• Otherwise, find the smallest clause (by number of literals).
    	Clause smallestClause = new Clause();
    	int smallestSize = 99999999;
    	for (Clause clause: clauses){
    		if (clause.size()<smallestSize){
    			smallestSize = clause.size();
    			smallestClause = clause;
    		}
    	}
    	
    	//	o If the clause has only one literal, bind its variable in the environment so that the
    	//	clause is satisfied, substitute for the variable in all the other clauses (using the
    	//	suggested substitute() method), and recursively call solve().
    	//	o Otherwise, pick an arbitrary literal from this small clause:
    	//		First try setting the literal to TRUE, substitute for it in all the clauses, then
    	//		solve() recursively.
    	//		If that fails, then try setting the literal to FALSE, substitute, and solve()
    	//		recursively.

    	Environment newEnv = new Environment(); //env to put the var as true
    	Literal literal = smallestClause.chooseLiteral();
    	Variable var = literal.getVariable();
    	Environment output = new Environment(); //output
    	ImList<Clause> newClauses = new EmptyImList<Clause>(); //Substituted list
    	if (smallestClause.isUnit()){
    		newEnv = env.putTrue(var);
    		newClauses = substitute(clauses, literal);
    		output = solve(newClauses, newEnv);
    	}else{
    		newEnv = env.putTrue(var);
    		newClauses = substitute(clauses, literal);
    		output = solve(newClauses, newEnv);
    		if (output == null){
    			newEnv = env.putFalse(var);
    			newClauses = substitute(clauses, literal);
    			output = solve(newClauses, newEnv);
    		}
    	}
    	return output;
    	
    }

    /**
     * given a clause list and literal, produce a new list resulting from
     * setting that literal to true
     * 
     * @param clauses
     *            , a list of clauses
     * @param l
     *            , a literal to set to true
     * @return a new list of clauses resulting from setting l to true
     */
    private static ImList<Clause> substitute(ImList<Clause> clauses,
            Literal l) {
        // Parse through all the clauses in the ImList and change the literal l to be true using reduce.
    	// If newClause comes out as not null (means still not true, because if true we can ignore) then add it to the new ImList
    	Clause newClause = new Clause();
    	ImList<Clause> outClauses = new EmptyImList<Clause>();
    	for (Clause clause : clauses){
    		newClause = clause.reduce(l);
    		if (newClause != null){
    			outClauses.add(newClause);
    		}
    	}
    	return outClauses;
    }

}
