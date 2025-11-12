import java.util.Arrays; //class
import java.util.List; // interface
import java.util.Comparator; // interface



public class DuckTest {
	public static void main(String[] args){
	    Duck[] ducks = { //ducks.length == 6
            new Duck("Daffy", 8),
            new Duck("Louie", 2),
            new Duck("Howard", 7),
            new Duck("Dewey", 2),
            new Duck("Donald", 10),
            new Duck("Huey", 4)
        };
	
	//정렬자리 
	List<Duck> duckList = Arrays.asList(ducks);
	System.out.println("정렬 전 : " +duckList);
	duckList.sort((d1, d2)->{
		System.out.println(d1 + ",  " + d2);
		return d1.getWeight() -d2.getWeight();
	});
	System.out.println("정렬 후 : " +duckList);


	}
	

	public static void display(Duck[] ducks){
		for(Duck d : ducks) {
			System.out.println(d);
		}
	}
}

class MyComparator implements Comparator<Duck>{
	@Override
   	 public int compare(Duck o1, Duck o2) {
        return o1.getWeight() - o2.getWeight();
	}
}


class Duck implements Comparable<Duck> {
	private String name;
	private int weight;

	public Duck(String name, int weight) {
	this.name = name;
	this.weight = weight;
	}
	@Override
	public String toString(){
		return name + ", 체중 : " + weight;
	}


	@Override
	public int compareTo(Duck o) {
		return weight - o.weight;
	}
public int getWeight() {
    return weight;
}
}