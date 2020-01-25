/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mergesortthread;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alex
 */
public class MergeSortThread extends Thread{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        File nr = new File("C:\\Users\\Alex\\Desktop\\numere.txt");
        
        MergeSortThread mergSort = new MergeSortThread();
        int[] list = mergSort.readFile(nr);
        mergSort.parallelSort(list);
    }
    
    private int[] readFile(File nr){
        List<Integer> listTmp = new LinkedList<Integer>();
        
        try {
            Scanner scanner = new Scanner(nr);
            while(scanner.hasNextInt()){
                listTmp.add(scanner.nextInt());
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MergeSortThread.class.getName()).log(Level.SEVERE, null, ex);
        } 
       
        int[] list = new int[listTmp.size()];
        
        for (int i = 0; i < listTmp.size(); i++) {
             list[i] = listTmp.get(i);
        }
         
        System.out.println("List "+ Arrays.toString(list));
        return list;
    }
    
    private void parallelSort(int[] list){ 
        
        //check list length
        if(list.length < 2)
            return;
        
        // split array in half
        int[] leftList = Arrays.copyOfRange(list, 0, list.length/2);
        int[] rightList = Arrays.copyOfRange(list, list.length/2 , list.length);
        
        Thread leftThread = new Thread(new MergeSortRunner(leftList));
        Thread rightThread = new Thread(new MergeSortRunner(rightList));
        leftThread.start();       
        rightThread.start();
        
        try {
            leftThread.join(); 
            rightThread.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(MergeSortThread.class.getName()).log(Level.SEVERE, null, ex);
        }   
        
        // merge them back together
        mergeList(leftList, rightList);
    }
    
    private void mergeList(int[] left, int[] right){

        System.out.println("Left "+ Arrays.toString(left)+"\nRight "+Arrays.toString(right));
        
        int first = 0;  // Next element to consider in the first array
        int second = 0;  // Next element to consider in the second array
        int j = 0; 

        int[]a = new int[left.length+right.length];
        
        while (first < left.length && second < right.length){ 
            if (left[first] < right[second]){ 
                   a[j] = left[first];
                   first++;
            }
            else{ 
                a[j] = right[second];
                second++;
            }
               j++;
          }

        while (first < left.length){ 
            a[j] = left[first]; 
             first++; j++;
            }
            // Copy any remaining entries of the second half
            while (second < right.length){ 
                a[j] = right[second]; 
                second++; j++;
            }

            System.out.println("Result" +Arrays.toString(a));
    }
}
