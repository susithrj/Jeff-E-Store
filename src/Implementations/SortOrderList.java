package Implementations;

import sample.Order;

import java.util.ArrayList;

public class SortOrderList {


    public static void sortArray(ArrayList<Order> unsortedArray) {
        Order arrList[] = new Order[unsortedArray.size()];
        for (int j = 0;j < arrList.length;j++){
            arrList[j] = unsortedArray.get(j);
        }
        unsortedArray.clear();
        for (int i = 0; i < arrList.length - 1; i++) {
            int assumedMinimumIndex = i;//first we assume the minimum as first index and goes on
            for (int k = i + 1; k < arrList.length; k++) {
                if (arrList[assumedMinimumIndex].getOrderId() > arrList[k].getOrderId()) {
                    assumedMinimumIndex = k;//if a small value found in the inner loop the assume minimum is changed

                }

            }
            if (i != assumedMinimumIndex) {//if new assume minimum found swap the values
                Order tempVariable = arrList[i];//temporary variable to hold the reference
                arrList[i] =arrList[assumedMinimumIndex];//swap value
                arrList[assumedMinimumIndex]=tempVariable;//swap value
            }

        }

        for (int j = 0;j < arrList.length;j++){
            unsortedArray.add(arrList[j]);//adding the items after sorting
        }


    }


}
