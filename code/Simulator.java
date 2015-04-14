import java.io.*;

public class Simulator{

	int numBakery = 40;
	int numBattery = 40;
	int numBucket = 40;

	int power_mean = 10;
	int power_sdev = 2;
	
	double cust_sdev = .15;
	double cust_p_range = 10;

	public static void main(String[] args){
		if(args.length() != 2){
			System.println("Error - must specify a simulation length");
			System.exit();
		}

		int K = Integer.parseInt(args[1]);
		int Customers = numBakery + numBattery + numBucket;

		//initialize the power plant
		Plant p = new Plant(power_mean, power_sdev);

		//Constraints on customers power demand: want on average
		//to produce as much as consume, so balance is possible 
		//in theory
		int total_e = K*power_mean;

		//Assume customers energy requriements are gaussian in 
		//aggregate, with this mean and a stddev of 15%
		double mean_e = total_e / (double) Customers;
		double sdev_e = mean_e * cust_sdev;

		//need a random number generator for customer constraints / demands
		Random r = new Random(987654321);

		//Create arrays to hold all our customers
		Bakery[] bakeries = new Bakery[numBakery];
		Battery[] batteries = new Battery[numBattery];
		Bucket[] buckets = new Bucket[numBucket];

		/* Require E / P < K so can finish running in simulation duration 
		 * Fix E, K solve for min P, choose it randomly in [min, min+cust_p_range]*/
		for(int i = 0; i < numBakery; i++){
			double e_req = r.nextGaussian()*sdev_e + mean_e;
			double min_p = e_req / K;
			double p = r.nextDouble()*cust_p_range + min_p;
			int end_t = r.nextInt(K);
			
			bakeries[i] = new Bakery(e_req, p, end_t);
		}
		//batteries are bakeries - the constant power constraint
		for(int i = 0; i < numBattery; i++){
			double e_req = r.nextGaussian()*sdev_e + mean_e;
			double min_p = e_req / K;
			double p = r.nextDouble()*cust_p_range + min_p;
			int end_t = r.nextInt(K);
			
			batteries[i] = new Battery(e_req, p, end_t);
			
		}
		//implement the agile balancing algorithm
		for(int i = 0; i < K; i++){
			int forced;

		}
	}
}
