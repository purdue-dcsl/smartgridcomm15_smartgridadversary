

public class Bucket{
	private double e_max;   //max energy stored
	private double e_min;   //min allowed energy (can be negative)
	private double p_max;   //max power to consume
	private double p_min;   //min power to consume

	private double e_curr;  //current energy state

	Bucket(double etop, double ebot, double ptop, double pbot){
		e_max = etop;
		e_min = ebot;
		p_max = ptop;
		p_min = pbot;
		e_curr = 0;
	}

	//how "agile" is the bucket -> bigger numbers better
	public double agility(){
		return (e_max - e_curr) / p_max;
	}

	//how much can the bucket store
	public double reserve(){
		return Math.min(p_max, e_max - e_curr);
	}

	//how much can the bucket put back into the system
	public double balance(){
		return e_max - e_curr;
	}

	//consume the power dispatched to the bucket.  Dispatch is negative
	//if the bucket is being tapped for power
	public void consume(double p_dispatch){
		if(p_dispatch > p_max){
			System.out.println("error - too much power to a bucket");
			System.exit(1);
		}
		else if (e_curr + p_dispatch > e_max || e_curr + p_dispatch < e_min){
			System.out.println("error - violating bucket energy constraints");
			System.exit(1);
		}
		e_curr += p_dispatch;
	}
}
