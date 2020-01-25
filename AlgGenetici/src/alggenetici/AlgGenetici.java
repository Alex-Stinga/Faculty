/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alggenetici;

import java.util.Arrays;
import java.util.Random;

public class AlgGenetici {

    /**
     * @param args the command line arguments
     */
    
   private final double pC = 0.25;
   private final double pM =  0.01;
   private final double d1Min =-3.0;
   private final double d2Min = 4.1;
   private final double d1Max = 12.1;
   private final double d2Max = 5.8;
   private final double epsilon =  Math.pow(10, -4);
   
    private int setNrPop(){
        Random rand = new Random();
        int nrPop = rand.nextInt(50) + 1 ;
        System.out.println("Nr ind "+ nrPop);
        return nrPop;
    }
    
    private double getX1(){
        double x1 = d1Max - Math.random()* (d1Max +d1Min);
        System.out.println(x1);
        return x1;
    }
    
    private double getX2(){
        double x2 = d2Min + Math.random()* (d2Max -d2Min);
        System.out.println(x2);
        return x2;
    }
   
   private double fMax(double x1, double x2){
       double x =21.5 + x1 +Math.sin(4*Math.PI*x1)+ x2*Math.sin(20 * Math.PI*x2);
       return x;
   }
   
   private double setD1(){
       double x1 = d1Max - d1Min;
       double d1 = x1/epsilon;
       System.out.println("Dom 1 "+ x1 +" "+d1);
       
       System.out.println("");
       return d1;
   }
   
    private double setD2(){
       double x2 = d2Max - d2Min;
       double d2 = x2/epsilon;
       System.out.println("Dom 2 "+ x2 +" "+d2);
       return d2;
   }
    
    private int getMax(double n){
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
   
   private Individ ind(int id,int nrGene, int x1, int x2){//2.2 + 5
       int[] crom = setCrom(nrGene);
  
       int i=0,j;
       int[] s1 = new int[x1];
       int[] s2 = new int[x2];
       
       for (i = 0; i < s1.length; i++) {
           s1[i] = crom[i];     
         //  System.out.print(s1[i]+" ");
       }
       
       for (j = 0; j < s2.length; j++) {    
           s2[j] = crom[i++];   
        //   System.out.print(s2[j]+" ");
       }
       
       long dec =  binaryToInteger(s1);
       double up = d1Max -d1Min;
       int down = (int)(Math.pow(2, x1)-1);
       
       double  x12 = d1Min + (dec * (up/down));
      // System.out.println(x12);
       
       long dec2 =  binaryToInteger(s2);
       double up2 = d2Max -d2Min;
       int down2 = (int)(Math.pow(2, x1)-1);
       
       double  x22 = d2Min + (dec2 * (up2/down2));
       //System.out.println(x22);

       double fit = fMax(x12,x22);
       Individ ind = new Individ(id,nrGene, crom,fit,0);

       return ind;
   }
   
    private Individ[] init(int n,int nrGene, int x1, int x2){
        Individ populatie[] = new Individ[n];
        int id = 1;
        
        for (int i = 0; i < n; i++) {
            populatie[i] = ind(id++, nrGene, x1, x2);      
        }   
    //    afisarePop(populatie);
        return populatie;
    }
    
    private double fitTotal(Individ[] pop){//6
        double sum =0;
        for(int i=0;i<pop.length;i++)
            sum+=pop[i].getFitness();
        System.out.println("Fitness total: "+ sum);
        return sum;
    }
    
    private Individ[] probSel(Individ[] pop){
       double fitTot = fitTotal(pop);
       for(int i=0;i<pop.length;i++){
           double probSl = pop[i].getFitness()/fitTot;
           pop[i].setProb(probSl);
         
       }
      //  afisarePop(pop);
        return pop;
    }
   
    private void afisarePop(Individ[] pop){
        for (int i = 0; i < pop.length; i++) {
            System.out.println(pop[i].getId()+ " "+pop[i].afisare()+ " "+ pop[i].getFitness()+ " "+ pop[i].getProblty());
        }   
    }
    
    private double[] sumCumulata(Individ[] pop){
        double[] sumCum = new double[pop.length-1];
        double sum=0, crt =pop[0].getProblty();
        
        int j=0;
        for (int i = 1; i < pop.length; i++) {
             sum=crt+pop[i].getProblty();
             crt = sum;
             sumCum[j++] =sum;
        }

       // for (int i = 0; i < sumCum.length; i++) {//System.out.println(String.format("%.2f", sumCum[i])); }
        System.out.println("Pobabilitatea totala "+String.format("%.2f", sumCum[sumCum.length-1]));
        return sumCum;
    }
    
    private Individ[] selectie(Individ[] pop, double[] sumaCum){
        Individ[] selected = new Individ[pop.length];
        int nr  = pop.length,sel=0;
        double nrRand;
        
        for (int i = 0; i < nr; i++) {
            nrRand = Math.random();  //System.out.print(nrRand+" ");
          
            int j=0;
            while(nrRand > sumaCum[j] && j<sumaCum.length){
                j++;
            }
        //    System.out.println(pop[j].getId()+" "+String.format("%.2f", sumaCum[j] ));
            selected[sel++] = pop[j];
        }
        
      //  for (int i = 0; i < selected.length; i++) {         
            //System.out.println(selected[i].getId());
      //  } 
      
        System.out.println("\nIndivizii selectati "+ selected.length);
        afisarePop(selected);
        return  selected;
    }
    
    private Individ[] incrucisare(Individ[] pop,int lungCrom){
        Individ[] incr = new Individ[pop.length];
        
        Individ[] finalvect = new Individ[pop.length];
        System.arraycopy(pop, 0, finalvect, 0, pop.length);
        int j=0;
        int pctIncMax = lungCrom ;
            
        for (int i = 0; i < pop.length; i++) {
           double rand = Math.random();
           if(rand < pC){
               incr[j++] = pop[i];
               //System.out.println(pop[i].getId());
           }
        }
        
        Individ[] incr2 = new Individ[j];
        System.arraycopy(incr, 0, incr2, 0, incr2.length);
        
        System.out.println("indivizi pt incrucisare: "+ j+" "+incr2.length);
       
        
        if(j==0|| j==1){
            System.out.println("Nr selectati 0");
          
        }
        else{
        for (int i = 0; i < j-1; i++) {

            int taiet = (int) (Math.random() * pctIncMax-1);
            System.out.println("Taietura "+taiet);
        
            Individ individ1 = incr[i];
            Individ individ2 = incr[i+1];
            int k=0;
            
            System.out.println("Ind: "+ individ1.getId());
            
            int[] v1 = individ1.getCromozom();
            int[] v2 = individ2.getCromozom();
            
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
            
            individ1.setCromozom(tmpV1);
            individ2.setCromozom(tmpV2);
            finalvect[individ1.getId()] = individ1;
            finalvect[individ2.getId()] = individ2;
            
        }
        }
     
        return finalvect;
    }
    
    private Individ[] mutatia(int lungCrom, Individ[] pop){
        int nrAleat = lungCrom* pop.length;
        int[] total = new int[nrAleat];
        int i,ales=0;
        Individ[] ind = new Individ[pop.length];
       
        for (i = 0; i < pop.length; i++) {
            int[] tmp = pop[i].getCromozom();
            
            for (int j = 0; j < tmp.length; j++) {
                total[i] = tmp[j];  
             //   System.out.print(total[i]);
            }
        }
        
        System.out.println("Nr aleatoare "+nrAleat);

        for (int r = 0; r < nrAleat; r++) {
           double rand =  Math.random();
           if(rand < pM){
               if(total[i]==0)
                    total[i] = 1;
               else
                    total[i] = 0;
               
               int count =0, copy = r;
               while(copy >0 ){
                   copy-=lungCrom;count++;
               }
               System.out.println(r+" "+ (count));
               Individ indvd  = pop[count];
               indvd.setMutat();
               ind[ales++]=indvd;
           }   
           
        }
        
        Individ[] mutati = new Individ[ales];
        System.arraycopy(ind, 0, mutati, 0, ales);
        System.out.println("Indivizi mutati "+ales);
        return mutati;
    }
    
    private boolean isMutated(Individ[] ind){
        int count=0;
        for (int i = 0; i < ind.length; i++) {
           if(ind[i].getMutat()==true)
               count++;
        }
        
        if(count < 2)
            return true;
        else
            return false;
    }
    
    
    public static void main(String[] args) {
        // TODO code application logic here
               
        AlgGenetici alg = new AlgGenetici();  

        double d1 = alg.setD1();
        double d2 = alg.setD2();
        
        int max1 = alg.getMax(d1);
        int max2 = alg.getMax(d2);

        int lungCrom = max1+max2;
        int nrPop = alg.setNrPop();
        Individ[] pop  = alg.init(nrPop, lungCrom, max1, max2);
        
//        double fitness= alg.fitTotal(pop);
//        Individ[] popSel = alg.probSel(pop);
//       
       // double[] sumC = alg.sumCumulata(pop);
       // Individ[] selected=alg.selectie(pop,sumC);
        
//        Individ[] pop = new Individ[nrPop];
//        pop = alg.init(nrPop, lungCrom);
        
//        Individ ind= alg.setPop(5);
//        int[] crom = ind.getCromozom();
//        alg.mutatie(crom);
        //alg.afis(poplat);
  
       // int[] crom = alg.setCrom(5);
        
        
//        float pM = alg.genPm();
//        alg.setCrom(pop);
       // alg.mutatie(pop, crom, pM);
        
        
//        Individ p1 = pop[0];
//        Individ p2 = pop[1];
//        alg.incrDiscreta(p1, p2);
        
//         
//         AlgGenetici alg = new AlgGenetici();
//         int nrPop =alg.setNrPop();
//        
//         Individ[] pop = alg.initPop(nrPop);
//         float fitGlob = alg.sumFitness(pop);
//        
//         System.out.println("Fitness total: "+fitGlob);
//         
//         pop = alg.setProbSel(pop);
//         alg.afisare(pop);
//         
//          System.out.print("introduceti un nr: ");
//          Scanner in = new Scanner(System.in);int num=in.nextInt();
//         
//          while(num < pop.length){
//            System.out.print("introduceti alt nr: ");
//            num = in.nextInt();
//          }
//          
//         int[] vect =alg.setVect(pop,num);
//          alg.selection(vect);
//


//         int n=4;
//         Individ[] pop  =new Individ[n];
//         pop[0] = new Individ(1, 10, 0);
//         pop[1] = new Individ(2, 10, 0);
//         pop[2] = new Individ(3, 15, 0);
//         pop[3] = new Individ(4, 25, 0);
//         
//         AlgGenetici alg = new AlgGenetici();
//         float fitGlob = alg.sumFitness(pop);
//        
//         System.out.println("Fitness total: "+fitGlob);
//         
//          pop = alg.setProbSel(pop);
//          alg.afisare(pop);
//          
//          
//          System.out.print("introduceti un nr: ");
//          Scanner in = new Scanner(System.in);int num=in.nextInt();
//         
//          while(num < pop.length){
//            System.out.print("introduceti alt nr: ");
//            num = in.nextInt();
//          }
//          int[] popSelect =  alg.setVect(pop, num);
//          alg.selection(popSelect);


            
                
    }
    
}
