
import java.util.Random;
import java.util.Arrays;

public class Simulator{

	public static final int numBakery = 40;
	public static final int numBattery = 40;
	public static final int numBucket = 40;

	public static final int power_mean = 10;
	public static final int power_sdev = 2;
	
	public static final double cust_p_range = 10;
	public static final double cust_sdev = 2;

	public static double forced(Constrained[] cust, int num_cust, double time){
		double ret = 0;
		for(int i = 0; i < num_cust; i++){
			ret += cust[i].forced(time);
		}
		return ret;
	}
	
	public static void forced_dispatch(Constrained[] cust, int num_cust, double time){
		for(int i = 0; i < num_cust; i++){
			cust[i].consume(cust[i].forced(time));
		}
	}
	public static double reserve(Bucket[] buckets){
		double ret = 0;
		for(int i = 0; i < numBucket; i++){
			ret += buckets[i].reserve();
		}
		return ret;
	}
	public static double simulate(Bucket[] buckets, Battery[] batteries, Bakery[] bakeries, Plant p, int K){
		double imbalance = 0;
		//agile balancing algorithm from the paper
		for(int i = 0; i < K; i++){
			//how much power to dispatch
			double dispatch = p.produce();
			
			//how much power are you forced, by the constraints, to 
			//dispatch to bakeries / batteries
			double battery_f = forced(batteries, numBattery, i);
			double bakery_f = forced(bakeries, numBakery, i);
			double forced = battery_f + bakery_f;
			
			double bakery_d = 0;
			double battery_d = 0;
			if (forced > dispatch){
				forced_dispatch(batteries, numBattery, i);
				forced_dispatch(bakeries, numBakery, i);
				bakery_d = bakery_f;
				battery_d = battery_f;
			}
			else {
				//created a combined list of batteries / bakeries
				Constrained[] combined = new Constrained[numBattery + numBakery];
				int j = 0;
				for(; j < numBakery; j++){
					combined[j] = bakeries[j];
				}
				for(int k = 0; j < numBattery + numBakery; k++, j++){
					combined[j] = batteries[k];
				}
				
				//sort by agility
				Arrays.sort(combined);
				
				//distribute dispatch to the list until can't fulfill the next
				//request.  Do the book keeping on amount actually dispatched
				//to each category
				
			}
			double bucket_d = Math.min(reserve(buckets), dispatch - bakery_d - battery_d);
			
			//sort buckets by agility 
			
			//dispatch bucket_d to the buckets
			
			imbalance += dispatch - bucket_d - battery_d - bakery_d;
		}
		return imbalance;
	}
	
	public static void main(String[] args){
		if(args.length != 2){
			System.out.println("Error - must specify a simulation length");
			System.exit(1);
		}

		int K = Integer.parseInt(args[1]);
		int Customers = numBakery + numBattery + numBucket;

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
			double pow = r.nextDouble()*cust_p_range + min_p;
			int end_t = r.nextInt(K);
			
			bakeries[i] = new Bakery(e_req, pow, end_t);
		}
		//batteries are bakeries - the constant power constraint
		for(int i = 0; i < numBattery; i++){
			double e_req = r.nextGaussian()*sdev_e + mean_e;
			double min_p = e_req / K;
			double pow = r.nextDouble()*cust_p_range + min_p;
			int end_t = r.nextInt(K);
			
			batteries[i] = new Battery(e_req, pow, end_t);	
		}
		for(int i = 0; i < numBucket; i++){
			double e_req = r.nextGaussian()*sdev_e + mean_e;
			double min_p = e_req / K;
			double pow = r.nextDouble()*cust_p_range + min_p;
			
			buckets[i] = new Bucket(e_req, -e_req, pow, -pow);
		}
		
		//initialize the power plant
		Plant p = new Plant(power_mean, power_sdev);
		
		double result = simulate(buckets, batteries, bakeries, p, K);
		System.out.printf("%f\n", result);
	}
}
