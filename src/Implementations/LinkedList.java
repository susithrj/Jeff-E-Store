package Implementations;

public class LinkedList {
    private Node firstNode;
    private Node lastNode;
    private int num;

    public LinkedList() {
        this.firstNode = null;
        this.lastNode = null;
        this.num = 0;

    }
    public void addElement(String data){
        if(num == 0){//if its the first adding first node will be initialized
            lastNode =  new Node(data,null);
            firstNode = lastNode;
        }else {// else next node will be initialized
            Node previousNode = lastNode;
            lastNode = new Node(data,null);
            previousNode.setNext(lastNode);
        }
        num++;//will increase on every adding (to get how many elements)
    }
    public boolean hasNext(){
        return firstNode != null;
    }
    public String getData(){

        return firstNode.getValue();
    }

    public int getNum() {
        return num;
    }

    public void iterrate(){

        firstNode = firstNode.getNext();//to iterate though the list
    }
}
