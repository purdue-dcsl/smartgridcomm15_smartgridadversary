
import java.util.Random;

/*Simulates all of the renewable power sources, which
 * when lumped together produce a normally distributed
 * amount of power
 * */

public class Plant {
	private int mean;
	private int stddev;

	private Random r;

	/*Using a fixed seed for now for debugging */
	Plant(int mean, int stddev, int seed){
		this.mean = mean;
		this.stddev = stddev;
		r = new Random(seed);
	}

	public double produce(){
		double tmp = r.nextGaussian()*stddev+mean;
		if (tmp >= 0) return tmp;
		else return -tmp;
	}
}
