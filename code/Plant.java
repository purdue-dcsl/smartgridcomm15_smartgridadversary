
import java.util.Random;

public class Plant {
	private int mean;
	private Random r;

	Plant(int mean){
		this.mean = mean;
		r = new Random(123456789);
	}

	public int produce(){
		//mu = mean, stddev = 1
		return r.nextGaussian()+mean;  
	}
}
