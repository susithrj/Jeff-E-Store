package sample;

import Implementations.SearchOrderList;
import Implementations.SortOrderList;

import java.util.ArrayList;

public abstract class  CompanyAdministrator {
    private String name;
    private String email;
    private String password;

    public CompanyAdministrator(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public abstract boolean  login(CompanyAdministrator activeAdmin);

    protected int checkOrder(ArrayList ordersList,int id){
        SortOrderList.sortArray(ordersList);//sorting the array to search
        int index = SearchOrderList.searchOrder(ordersList,id);//searching and getting the relevant index
        return index;
    }
}
/*this is a abstract class having private variables , public getters and setters will be inherited to the concrete class*/