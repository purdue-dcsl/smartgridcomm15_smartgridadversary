

public class Bucket{
	private double e_max;   //max energy stored
	private double e_min;   //min allowed energy (can be negative)
	private double p_max;   //max power to consume
	private double p_min;   //min power to consume

	private double e_curr;  //current energy state

	Bucket(double etop, double ebot, double ptop, double pmin){
		e_max = etop;
		e_min = ebot;
		p_max = ptop;
		p_mix = pbot;
		e_curr = 0;
	}

	public double agility(){
		return (e_max - e_curr) / p_max;
	}

	public double reserve(){
		return Math.min(p_max, e_max - e_curr);
	}

	public void consume(double p_dispatch){
		if(p_dispatch > p_max){
			System.out.println("error - too much power to a bucket");
			System.exit();
		}
		e_curr += p_dispatch;
	}
}
