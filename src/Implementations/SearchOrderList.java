package Implementations;

import sample.Order;

import java.util.ArrayList;

public class SearchOrderList {
    public static int searchOrder(ArrayList<Order> orderList,int searchValue ){
        Order array[] = new Order[orderList.size()];
        for (int j = 0;j < array.length;j++){
            array[j] = orderList.get(j);
        }
        int locationIndex = -1;//if no element found return -1
        int lowEndIndex = 0;
        int highEndIndex = array.length -1;
        while(lowEndIndex <= highEndIndex && locationIndex == -1){
            int middleElementIndex =( lowEndIndex + highEndIndex + 1 )/2;
            if(searchValue ==array[middleElementIndex].getOrderId()){
                locationIndex = middleElementIndex;//if the search element in middle that index will be returned
            }else if(searchValue < array[middleElementIndex].getOrderId()){
                highEndIndex = middleElementIndex -1;//if the searching element is the low part of the array high part is neglected
            }else {
                lowEndIndex = middleElementIndex +1;//if the searching element is the high part of the array low part is neglected
            }

        }
        return locationIndex;//return index or -1
    }

}
