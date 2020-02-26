/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ISLEM
 */
public class ACS {

    public static List<Clause> clauses;
    public static List<Litteral> litterals;
    public static Litteral litteral;
    public static Clause clause;
    public static Sat sat;

    // Parametres SAT
    public static int VAR_NUM = 75; // nombre des Varriables
    public static int CLAUSE_NUMBER; // nombre des Clauses

    // Parametres ACS
    public static final double ALPHA = .3; // ALPHA
    public static final double BETA = .3; // BETA
    public static final double V_RATE = .1; // Taux d'evaporation
    public static final double I_PHEROMON = .1; // Pheromone initale
    public static final int MAX_ITERATIONS = 50000; // Max Iterations
    public static final int ANT_COUNT = 100; // Nombre des fourmis
    public static final double Q_0 = 0.005; // Q0

    public static long ACO_TOTAL_TIME = 0; // temps totale
    public static int[][] FITNESS; // matrice contiens le nombre des clauses satisfiable pour chaque literal

    public static List<Ant> ants; // liste des fourmis (Population)
    public static double[][] pheromon; // matrice du pheromone

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {

        sat = LoadSat("/res/uf75-325/uf75-01.cnf"); // chargement de l'instance
        
        

        pheromon = new double[VAR_NUM][2]; // declaration de la matrice du pheromone
        for (int i = 0; i < VAR_NUM; i++) { // initialisation du pheromone
            pheromon[i][0] = pheromon[i][1] = I_PHEROMON;
        }
        
        ACO_TOTAL_TIME = System.currentTimeMillis(); // start time

        for (int i = 0; i < MAX_ITERATIONS; i++) { // de 0 a MAX_IT

            ants = new ArrayList<>(); // crée la liste des fourmis
            for (int j = 0; j < ANT_COUNT; j++) {
                ants.add(new Ant()); // ajout des nouvelles fourmis
            }

            int best = 0; // best sat
            Ant bestAnt = null; // bes solution

            for (Ant ant : ants) {
                ant.begin(); // contruction d'une solution
                if (ant.getFitness() > best) { // evaluation 
                    bestAnt = ant; // mis a jours de la meilleur fourmis
                    best = ant.getFitness();
                }
            }

            System.out.println("" + bestAnt.getFitness());

            for (int j = 0; j < ACS.VAR_NUM; j++) { // offline update
                if (bestAnt.getValues()[j] == 0) {
                    pheromon[j][0] = (1 - V_RATE) * pheromon[j][0] + V_RATE * (double) (FITNESS[j][0]);
                    pheromon[j][1] = (1 - V_RATE) * pheromon[j][1];
                } else {
                    pheromon[j][0] = (1 - V_RATE) * pheromon[j][0];
                    pheromon[j][1] = (1 - V_RATE) * pheromon[j][1] + V_RATE * (double) (FITNESS[j][1]);
                }
            }
            if (bestAnt.getFitness() == 325) { // si SAT = 325 clauses
                System.out.print("Iteration " + i + " : "+bestAnt.getFitness());
                break; // arret
            }
        }
        
        System.out.println(" time = "+(System.currentTimeMillis()-ACO_TOTAL_TIME)+" ms");
        
    }

    // Chargement d'un fichier
    public static Sat LoadSat(String fileName) {

        //Initilisation des Listes
        clauses = new ArrayList<>();

        //Juste une varriable tmp
        int tmpParseInt;

        try {
            InputStream is = new FileInputStream(System.getProperty("user.dir") + fileName);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            String[] lit;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("c") || line.startsWith("%") || line.startsWith("0") || line.equals("")) {
                } else {
                    if (line.startsWith("p")) {
                        line = line.replace("p cnf ", "");
                        line = line.replaceFirst(" ", "");
                        lit = line.split(" ");
                        VAR_NUM = Integer.parseInt(lit[0]);
                        CLAUSE_NUMBER = Integer.parseInt(lit[1]);
                        FITNESS = new int[VAR_NUM][2];
                        continue;
                    }
                    litterals = new ArrayList<Litteral>();
                    if (first) {
                        line = line.replaceFirst(" ", "");
                        first = false;
                    }
                    lit = line.split(" ");
                    for (int i = 0; i < 3; i++) {
                        tmpParseInt = Integer.parseInt(lit[i]);
                        if (tmpParseInt > 0) {
                            FITNESS[tmpParseInt - 1][1]++;
                            litteral = new Litteral(tmpParseInt, 1);
                        } else {
                            FITNESS[-(tmpParseInt) - 1][0]++;
                            litteral = new Litteral(-tmpParseInt, -1);
                        }
                        litterals.add(litteral);
                    }
                    clause = new Clause(litterals);
                    clauses.add(clause);
                }
            }
            br.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return new Sat(clauses);

    }

}
