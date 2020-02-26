
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ISLEM
 */
public class Ant {

    public static final double I_PHEROMON = .000005; // initial pheromone
    private int[] values; // solution
    private int fitness; // nombre de clauses satisfaites

    public Ant() { // initialisation par une solution vide (-1)
        values = new int[ACS.VAR_NUM];
        for (int i = 0; i < ACS.VAR_NUM; i++) {
            values[i] = -1;
        }
    }

    public void begin() { // construction de la solution
        for (int i = 0; i < ACS.VAR_NUM; i++) {
            step(i); // step ou etape
            onlineUpdate(i); // online update
        }
        fitness = ACS.sat.satisfiedClauses(values); // evaluation
    }

    public void step(int step) {

        double t[] = new double[2];
        double n[] = new double[2];
        double proba;
        double sum = 0;

        for (int i = 0; i < 2; i++) { // calcule des probas
            t[i] = Math.pow(ACS.pheromon[step][i], ACS.ALPHA);
            n[i] = Math.pow(ACS.FITNESS[step][i], ACS.BETA);
            sum += t[i] * n[i];
        }

        proba = t[0] * n[0] / sum;
        if (Math.random() > ACS.Q_0) { // parametre Q0
            if (Math.random() <= proba) { // folow the best
                values[step] = 0;
            } else {
                values[step] = 1;
            }
        } else {
            if(proba >= 0.5){ // choix d'une etape Xi ou /Xi
                values[step] = 0;
            } else {
                values[step] = 1;
            }
        }

    }

    private void onlineUpdate(int i) { // online update
        ACS.pheromon[i][values[i]] = ACS.pheromon[i][values[i]] * (1 - ACS.V_RATE) + ACS.V_RATE * I_PHEROMON;
    }

    public int[] getValues() {
        return values;
    }

    public int getFitness() {
        return fitness;
    }

}
