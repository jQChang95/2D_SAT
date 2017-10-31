package sat;

/*
import static org.junit.Assert.*;

import org.junit.Test;
*/

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import sat.env.*;
import sat.formula.*;


public class SATSolverTest {
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();
    
    
    
    
    // TODO: add the main method that reads the .cnf file and calls SATSolver.solve to determine the satisfiability
    public static void main(String args[]) throws IOException {
        FileReader fin = null;
        BufferedReader bin = null;
        
        try {
            fin=new FileReader("s8Sat.cnf"); //import file
            bin=new BufferedReader(fin);
            
            String line;
            bin.readLine();//remove the commented part
            String[] format=bin.readLine().split(" ");
            int NumberOfClauses=Integer.parseInt(format[3]);//get the number of clauses
            Formula f = new Formula(); //create and instance of the formula
            while (f.getSize()!=NumberOfClauses) {
                line=bin.readLine();
                if(line.length()>0){
                    String[] tempLine=line.split(" ");
                    Clause c = new Clause();
                    
                    for(String i:tempLine){
                        if(Integer.parseInt(i)==0){
                            break;
                        }
                        Literal literal = PosLiteral.make(Integer.toString(Math.abs(Integer.parseInt(i))));//makes literal instance
                        
                        
                        if((Integer.parseInt(i))<0){ //add the negated Integer to the clause if string is negative
                            c=c.add(literal.getNegation());
                        }
                    else if ((Integer.parseInt(i))>0){ //add the postitive Integer to the clause if string is positive
                            c=c.add(literal);
                        }
                    }
                    f=f.addClause(c); //add the clauses to the formula
                    
                    
                }
            }
            
            //TO BE REMOVED!!!!! WILL SLOW DOWN THE CODE===========================================
            if(NumberOfClauses==f.getSize()){
                System.out.println("Fomula generated Successfully!");
            }
            else{
                System.out.println("ExpectedNumberOfClauses: "+ NumberOfClauses);
                System.out.println("FormulaSize: "+ f.getSize());
            }
            //System.out.println(f.toString());
            //====================================================================================
            
        }finally {
            if (bin != null) {
                bin.close();
            }
        }
    }
    public void testSATSolver1(){
        // For example , (a v b):
        Environment e = SATSolver.solve(makeFm(makeCl(a,b)));
        /*
        assertTrue( "one of the literals should be set to true",
        Bool.TRUE == e.get(a.getVariable())
        || Bool.TRUE == e.get(b.getVariable())	);
        
        */
    }
    
    
    public void testSATSolver2(){
        // (~a)
        Environment e = SATSolver.solve(makeFm(makeCl(na)));
        /*
        assertEquals( Bool.FALSE, e.get(na.getVariable()));
        */
    }
    
    private static Formula makeFm(Clause... e) {
        Formula f = new Formula();
        for (Clause c : e) {
            f = f.addClause(c);
        }
        return f;
    }
    
    private static Clause makeCl(Literal... e) {
        Clause c = new Clause();
        for (Literal l : e) {
            c = c.add(l);
        }
        return c;
    }
    
    
    
}