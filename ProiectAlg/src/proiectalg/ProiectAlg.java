/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proiectalg;

import java.util.Random;

/**
 *
 * @author Alex
 */
public class ProiectAlg {
    
   private final float pC = (float) 0.25;
   private final float pM =  (float) 0.01;
   private final float d1Min =(float) -3.0;
   private final float d2Min = (float) 4.1;
   private final float d1Max = (float) 12.1;
   private final float d2Max = (float) 5.8;
   private final float epsilon =  (float) Math.pow(10, -4);
   
   private int setNrPop(){
        Random rand = new Random();
        int nrPop = rand.nextInt(200) + 2 ;
        System.out.println("Nr ind "+ nrPop);
        return nrPop;
    }
    
   private float fMax(float x1, float x2){
       float x =(float) (21.5 + x1 +Math.sin(4*Math.PI*x1)+ x2*Math.sin(20 * Math.PI*x2));
       return x;
   }
   
   private float setD1(){
       float x1 = d1Max - d1Min;
       float d1 = x1/epsilon;
       System.out.println("Dom 1 "+ x1 +" "+d1);
       return d1;
   }
   
   private float setD2(){
       float x2 = d2Max - d2Min;
       float d2 = x2/epsilon;
       System.out.println("Dom 2 "+ x2 +" "+d2+"\n");
       return d2;
   }
    
   private int getNrBits(float n){
        int nr = (int) n, count=0;
        while(nr > 0){
            nr/=2;
            count++;
        }
        System.out.println(count);
        return count;
    }
    
   private int getNr(){
       Random rand = new Random();
       int n = rand.nextInt(2);
       return n;
   }
   
   private int[] setCrom( int nrGene){
       int[] cromozom = new int[nrGene];
 
       for (int i = 0; i < cromozom.length; i++) {
           cromozom[i] = getNr();
       }
       return cromozom;
   }
   
   public long binaryToInteger(int[] sir) {
        long result = 0;

        for(int i=sir.length - 1; i>=0; i--){
                result += (Math.pow(2,i)* sir[i]);
        }
        return result;
   }
   
   private void afisarePop(Individ[] pop){
        for (int i = 0; i < pop.length; i++) {
            System.out.println(pop[i].getId()+ " "+pop[i].afisare()+ " "+ pop[i].getFitness()+ " "+ pop[i].getProblty());
        }   
    }
    
   private Individ ind(int id,int nrGene, int x1, int x2){//2.2 + 5
       int[] crom = setCrom(nrGene);
  
       int i=0,j;
       int[] s1 = new int[x1];
       int[] s2 = new int[x2];
       
       for (i = 0; i < s1.length; i++) { s1[i] = crom[i];  //  System.out.print(s1[i]+" ");
       }
       
       for (j = 0; j < s2.length; j++) {    
           s2[j] = crom[i++];   
        //   System.out.print(s2[j]+" ");
       }
       
       long dec =  binaryToInteger(s1);
       float up = d1Max -d1Min;
       int down = (int)(Math.pow(2, x1)-1);
       
       float  x12 = d1Min + (dec * (up/down));
      // System.out.println(x12);
       
       long dec2 =  binaryToInteger(s2);
       float up2 = d2Max -d2Min;
       int down2 = (int)(Math.pow(2, x1)-1);
       
       float  x22 = d2Min + (dec2 * (up2/down2));
       //System.out.println(x22);

       float fit = fMax(x12,x22);
       Individ ins = new Individ(id,nrGene, crom,fit,0);

       return ins;
   }
   
   private Individ[] init(int n,int nrGene, int x1, int x2){
        Individ populatie[] = new Individ[n];
        int id = 1;
        
        for (int i = 0; i < n; i++) {
            populatie[i] = ind(id++, nrGene, x1, x2);      
        }   
        
        return populatie;
    }
   
   private float fitTotal(Individ[] pop){//6
        float sum =0;
        for(int i=0;i<pop.length;i++)
            sum+=pop[i].getFitness();
        
        return sum;
    }
    
   private Individ[] probSel(Individ[] pop){
       float fitTot = (float) fitTotal(pop);
       for(int i=0;i<pop.length;i++){
           float probSl = pop[i].getFitness()/fitTot;
           pop[i].setProb(probSl);
         
       }
        afisarePop(pop);
        System.out.println("Fitness total "+ fitTot);
        return pop;
    }
   
    private float[] sumCumulata(Individ[] pop){
        float[] sumCum = new float[pop.length-1];
        float sum=0, crt =pop[0].getProblty();
        
        int j=0;
        for (int i = 1; i < pop.length; i++) {
             sum=crt+pop[i].getProblty();
             crt = sum;
             sumCum[j++] =sum;
        }

//        System.out.println("Axa pt suma cumulata ");
//        for (int i = 0; i < sumCum.length; i++) {
//            System.out.print(String.format("%.2f", sumCum[i])+" ");
//        }
//        System.out.println();
        
        return sumCum;
    }
    
    private Individ[] selectie(Individ[] pop, float[] sumaCum){
      
        System.out.println("Axa pt suma cumulata ");
        for (int i = 0; i < sumaCum.length; i++) {
            System.out.print(String.format("%.2f", sumaCum[i])+" ");
        }
        System.out.println();
        
        Individ[] selected = new Individ[pop.length];
        int sel=0;

        System.out.println("-----------------SELECTIE------------------- ");
        for (int i = 0; i < pop.length; i++) {
          float  a = (float) Math.random(); 
            System.out.print(String.format("%.2f", a )+" ");
          
            int j=0;
            while(a >= sumaCum[j]){
                j++;
            }
            
            System.out.println(pop[j].getId()+" "+String.format("%.2f", sumaCum[j] ));
            selected[sel++] = pop[j];
        }
 
        afisarePop(selected);
        return  selected;
    }
   
    private Individ[] incrucisare(Individ[] pop,int lungCrom ){
 
        System.out.println("-----------------INCRUCISARE-------------------");//de la 1
        
        Individ[] finalvect = pop;
        int[] vect = new int[pop.length];
        int nrSel=0;
            
        System.out.println("a --------Nr indice in vect ");
        for (int i = 0; i < finalvect.length; i++) {
           float a = (float) Math.random();
           if(a < pC){     
               System.out.println(a+" "+ (++i));
               vect[nrSel++] = i;
           }
        }
        
        if(nrSel%2==1)
            nrSel--;
     
        if (verficare(nrSel)) {
            System.out.println("Se pot incrucisa "+nrSel+" indivizi deci "+nrSel/2 +" puncte de taietura");
            
            int[] vector = new int[nrSel];
            System.arraycopy(vect, 0, vector, 0, nrSel);
 
        int pctIncMax = lungCrom-2;  
        for (int i = 0; i <vector.length; i+=2) {

            int taiet = (int) (Math.random() * pctIncMax+1);
            System.out.println("Taietura "+taiet);
            System.out.println("Perechile de indivizii ce vor fi incrucisati "+  vector[i] +" " + vector[i+1]);
            int k;

            try {
            int[] v1 = finalvect[vector[i]-1].getCromozom();
            int[] v2 = finalvect[vector[i+1]-1].getCromozom();
           
            System.out.println("ID "+ finalvect[vector[i]-1].getId()+ "  "+  finalvect[vector[i]-1].afisare()+" \n"
                              +"ID "+ finalvect[vector[i+1]-1].getId()+" "+  finalvect[vector[i+1]-1].afisare());
           
            //incrucisarea in sine
            int[] tmpV1 = new int[v1.length];
            int[] tmpV2 = new int[v2.length];
            
            for (k = 0; k < taiet; k++) {
                tmpV1[k] = v1[k];
                tmpV2[k] = v2[k];
            }

            for (int l = k; l < v1.length; l++) {
               tmpV1[l] =v2[l];
               tmpV2[l] =v1[l];
            }
            
            //afisare vectori incrucisati
            for (int l = 0; l < tmpV2.length; l++) {
                System.out.print(tmpV1[l]+" ");
            }
            System.out.println();
             for (int l = 0; l < tmpV2.length; l++) {
                System.out.print(tmpV2[l]+" ");
            }
            System.out.println();
            
            finalvect[vector[i]-1].setCromozom(tmpV1);
            finalvect[vector[i+1]-1].setCromozom(tmpV2);
            
            } catch (ArrayIndexOutOfBoundsException e) {
                finalvect[0].getCromozom();
            }
        } 
        
        
        System.out.println();
        afisarePop(finalvect);
        }
        
        if (verficare(nrSel)) {
            return finalvect;
        }else{
            theBest(finalvect);
            return null;
        }      
    }
    
    private Individ[] mutatia(int lungCrom, Individ[] pop){
         
        System.out.println("---------------------------MUTATIA--------------------------------");
        int nrTot = lungCrom* pop.length;
        int[] total = new int[nrTot];
        int modificati=0,tot=0;
        Individ[] mutati = pop;
        
        //umplere vector biti
        for (int i = 0; i < mutati.length; i++) {        
            for (int j = 0; j < mutati[i].getCromozom().length; j++) {
                total[tot++] =  mutati[i].getCromozom()[j];     
            } 
        }
        
      //  for (int i = 0; i < total.length; i++) {if(i%33==0) System.out.println();  System.out.print(total[i]+" "); }
        
        //mutatie bit
        System.out.println("\nNr aleatoare "+nrTot);
        for (int i = 0; i < total.length; i++) {
           float rand =  (float) Math.random();
           if(rand < pM){ 
               modificati++;
               
               try {
               if(total[i-1]==0)
                    total[i-1] = 1;
               else{
               if(total[i-1]==1)
                    total[i-1] = 0;
               }
                } catch (ArrayIndexOutOfBoundsException e) {
               }
               
               int count=0, copy = i;
               while(copy >0 ){
                   copy-=lungCrom;count++;
               }
               System.out.println("Bitul ales(rand < pM) aleator "+ i +" din "+"cromozomul cu nr "+ count+" din vector "+"Range-ul "+((count*lungCrom-lungCrom)+"-"+count*lungCrom));
              
           }        
        }
        
       // for (int i = 0; i < total.length; i++) {if(i%33==0) System.out.println(); System.out.print(total[i]+" ");  }
        
        tot=0;
        for (int i = 0; i < mutati.length; i++) {
         int[] tmp = new int[lungCrom];
            for (int j = 0; j < tmp.length; j++) {
                tmp[j]= total[tot++]; 
            }
            mutati[i].setCromozom(tmp);
        }

        System.out.println("\nNr biti modificati "+modificati+"\n");
        afisarePop(mutati);
      
        
        if(modificati!=0){
          return mutati;
        }
        else{
            theBest(mutati);
            return null;
        }
    }
     
    private boolean verficare(int n){
       return (n>=2);
     }
     
    private void theBest(Individ[] pop){
         Individ best= pop[0];
         for (int i = 0; i < pop.length; i++) {
            if(pop[i].getFitness() > best.getFitness())
                best = pop[i];
         }  
         System.out.println("Cel mai bun individ: "+ best.getId()+" "+ best.afisare()+" "+best.getFitness());
     }
    
    private boolean NuAcelasi(Individ[] pop){
        int count=0;
        for (int i = 0; i < pop.length-1; i++) {
          if(pop[i].getId()== pop[i+1].getId())
              count++;        
        }
        
        if(count==pop.length-1){
            theBest(pop);
             return false;
        }
        else  
            return true;
    }
    
    void sf(Individ[] pop,int lungCrom, float[] sumaCum){
        
        int gen=0;
        Individ[] generatii = pop;  
    
        do{
        System.out.println("--------------------------GENERATIA------------------------------ "+ gen);
        Individ[] select = selectie(generatii, sumaCum);
        Individ[] incruc = incrucisare(select, lungCrom);
        if(incruc!=null)
            generatii = mutatia(lungCrom, incruc); 
        else
            break;
        gen++;
        }while (generatii!=null && NuAcelasi(generatii)==true);
 
     }
   
   public static void main(String[] args) {
        // TODO code application logic here
        
        ProiectAlg alg = new ProiectAlg();
        
        float d1 = alg.setD1();
        float d2 = alg.setD2();
        int s1 = alg.getNrBits(d1);
        int s2 = alg.getNrBits(d2);
        int lungCrom = s1+s2;
        
        //initialzare pop
        int nrPop = alg.setNrPop();
        Individ[] pop  = alg.init(nrPop, lungCrom, s1, s2);
        alg.probSel(pop);
        float[] sumaCm = alg.sumCumulata(pop);
        alg.sf(pop,lungCrom,sumaCm);
        
    }
    
    
}
