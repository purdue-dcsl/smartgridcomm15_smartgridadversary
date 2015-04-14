import java.io.*;

public class Simulator{

	int numBakery = 40;
	int numBattery = 40;
	int numBucket = 40;

	public static void main(String[] args){
		if(args.length() != 2){
			System.println("Error - must specify a simulation length");
			System.exit();
		}

		int K = Integer.parseInt(args[1]);

		//Initialize all our customers
		Bakery[] bakeries = new Bakery[numBakery];
		Battery[] batteries = new Battery[numBattery];
		Bucket[] buckets = new Bucket[numBucket];

		//implement the agile balancing algorithm
		for(int i = 0; i < K; i++){
			int forced;

		}
	}
}
