/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mergesortthread;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Alex
 */
public class MergeSortRunner implements Runnable{
    
    private int[] list;
    
    public MergeSortRunner(int[]  a) {
        this.list = a;
    }

    @Override
    public void run() {
        mergeSort(list, 0, list.length - 1);
    }

    private void mergeSort(int[] list, int from, int to) {

        if (from < to) {
            int mid = (from + to) / 2;
            
            // Sort the first and the second half
            mergeSort(list, from, mid);
            mergeSort(list, mid + 1, to);
            merge(list, from, mid, to);
        }
    }
    
    public void merge(int[] arr, int from, int mid, int to){
        
        int size1 = mid - from + 1; 
        int size2 = to - mid; 
  
        int leftList[] = new int [size1]; 
        int rightList[] = new int [size2]; 
  
        //copy data to temp arrays
        for (int i=0; i<size1; ++i) 
            leftList[i] = arr[from + i]; 
        for (int j=0; j<size2; ++j) 
            rightList[j] = arr[mid + 1+ j]; 
  
  
        // merge the temp arrays
        int i = 0, j = 0; 
  
        // Initial index of merged subarry array 
        int k = from; 
        while (i < size1 && j < size2){ 
            if (leftList[i] <= rightList[j]){ 
                arr[k] = leftList[i]; 
                i++; 
            } 
            else{ 
                arr[k] = rightList[j]; 
                j++; 
            } 
            k++; 
        } 
  
        // Copy remaining elements of left if any
        while (i < size1){ 
            arr[k] = leftList[i]; 
            i++; 
            k++; 
        } 
  
        // copy remaining elements of rigth if any
        while (j < size2){ 
            arr[k] = rightList[j]; 
            j++; 
            k++; 
        } 
        
        //System.out.println("merge "+Arrays.toString(arr));
    }
  
}
