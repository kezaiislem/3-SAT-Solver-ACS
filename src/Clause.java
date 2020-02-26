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
public class Clause {

    private List<Litteral> litterals;

    public Clause() {

    }

    public Clause(List<Litteral> litterals) {
        this.litterals = litterals;
    }

    public List<Litteral> getLitterals() {
        return litterals;
    }

    public void setLitterals(List<Litteral> litterals) {
        this.litterals = litterals;
    }

    /*public boolean isSatisfied() {
        for (Litteral l : litterals) {
            if (l.getLitteralValue() == 1) {
                return true;
            }
        }
        return false;
    }*/

}
