/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.List;

/**
 *
 * @author ISLEM
 */
public class Sat {

    private List<Clause> clauses;

    public Sat(List<Clause> clauses) {
        this.clauses = clauses;
    }

    public List<Clause> getClauses() {
        return clauses;
    }

    public void setClauses(List<Clause> clauses) {
        this.clauses = clauses;
    }

    public boolean satisfied(Ant solution) {
        int[] values = solution.getValues();
        boolean sat = false;
        for (Clause c : clauses) {
            sat = false;
            for(Litteral l : c.getLitterals()){
                if(l.getLitteralValue(values[l.getVar()-1]) == 1){
                    sat = true;
                }
            }
            if(!sat)
                return false;
        }
        return true;
    }
    public int satisfiedClauses(int values[]) {
        int satisfactions = 0;
        for (Clause c : clauses) {
            for(Litteral l : c.getLitterals()){
                if(l.getLitteralValue(values[l.getVar()-1]) == 1){
                    satisfactions ++;
                    break;
                }
            }
        }
        return satisfactions;
    }

}
