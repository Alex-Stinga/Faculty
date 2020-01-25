/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alggenetici;

import java.util.Arrays;

public class Individ {
    private double fitness;
    private double probility;
    private int id;
    private int[] cromozom;
    private int nrGene;
    private boolean mutat;
    
   
    public Individ(int id, int nrGene,int[] cromozom,double fitness, double probility){
        this.id=id;
        this.nrGene=nrGene;
        this.fitness=fitness;
        this.probility = probility;
        this.cromozom = new int[nrGene];
        this.cromozom = cromozom;
        this.mutat =false;
    }
    
    public Individ(int nrGene, int[] cromozom){
        this.nrGene=nrGene;
        this.cromozom = cromozom;
    }
    
    public Individ(int id,float fitness, float probility){
        this.id=id;
        this.fitness=fitness;
        this.probility =probility;
    }
    
    public int getId() {
        return id;
    }
    
    public double getFitness(){
        return fitness;
    } 
    
    public double getProblty(){
        return probility;
    }
    
    public int getGene(){
        return nrGene;
    }
    
    public double setFitness(double value){
        fitness = value;
        return fitness;
    }
    
     public double setProb(double value){
        probility = value;
        return probility;
    }
     
     public int[] setCromozom(int[] vect){
        cromozom = vect;
        return vect;
    }
 
     public int[] getCromozom(){
        return cromozom;
    } 
     
    public String afisare(){
       return Arrays.toString(this.cromozom);
    } 
    
    public boolean getMutat(){
        return mutat;
    }
    
    public boolean setMutat(){
        return !mutat;
    }
}
