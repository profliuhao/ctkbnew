package org.haoai.medixhub.ctkb.utils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class NumberManipulateUtil {


    public static <N extends Number> Map<N, Integer> countFreq(List<N> arr)
    {
        Map<N, Integer> mp = new HashMap<N, Integer>();

        // Traverse through array elements and
        // count frequencies
        for (N i : arr)
        {

            if (mp.containsKey(i))
            {
                mp.put(i, mp.get(i) + 1);
            }
            else
            {
                mp.put(i, 1);
            }
        }
        // Traverse through map and print frequencies
//        for (Map.Entry<N, Integer> entry : mp.entrySet())
//        {
//            System.out.println(entry.getKey() + " " + entry.getValue());
//        }
        return mp;
    }


    public static <T> Collection<List<T>> partitionBasedOnSize(List<T> inputList, int size) {
        final AtomicInteger counter = new AtomicInteger(0);
        return inputList.stream()
                .collect(Collectors.groupingBy(s -> counter.getAndIncrement()/size))
                .values();
    }


    // Function select an element base on index
    // and return an element
    public static <T> T getRandomElement(List<T> list)
    {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }


    public static void main(String [] args ) {

        ArrayList<Double> arr = new ArrayList<>();

        arr.add((double)3);
        arr.add(4.0);
        arr.add((double)3);
        arr.add(5.0);
        arr.add((double)3);
        arr.add(2.0);
        arr.add((double)30);
        arr.add(1.0);

        Map<Double, Integer> mp = countFreq(arr);

        mp.remove(3.0);
        System.out.println(mp.get(3.0) + "not exist");

        Collection<List<Double>>  arrayList = partitionBasedOnSize(arr,3);
        System.out.println(arrayList);

        for(List<Double> dlist: arrayList){

            System.out.println(dlist);

        }


    }



}
