/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proiectalg;

import java.util.Arrays;

public class Individ {
    private float fitness;
    private float probility;
    private int id;
    private int[] cromozom;
    private int nrGene;

    public Individ(int id, int nrGene,int[] cromozom,float fitness, float probility){
        this.id=id;
        this.nrGene=nrGene;
        this.fitness=fitness;
        this.probility = probility;
        this.cromozom = new int[nrGene];
        this.cromozom = cromozom;
    }
    
    public int getId() {
        return id;
    }
    
    public float getFitness(){
        return fitness;
    } 
    
    public float getProblty(){
        return probility;
    }
    
    public int getGene(){
        return nrGene;
    }
    
    public int[] getCromozom(){
        return cromozom;
    } 
    
    public float setFitness(float value){
        this.fitness = value;
        return fitness;
    }
   
     public float setProb(float value){
        this.probility = value;
        return probility;
    }
     
     public int[] setCromozom(int[] vect){
        this.cromozom = vect;
        return vect;
    }
 
    public String afisare(){
       return Arrays.toString(this.cromozom);
    } 
   
}
