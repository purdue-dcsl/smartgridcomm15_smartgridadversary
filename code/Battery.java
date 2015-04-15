

public class Battery{
	private double e_max;
	private double p_max;
	private double t_done;

	private double e_curr;

	public Battery(double e_max, double p_max, double t_done){
		this.e_max = e_max;
		this.p_max = p_max;
		this.t_done = t_done;

		e_curr = 0;
	}

	public void consume(double p_dispatch){
		if(p_dispatch > p_max) {
			System.out.println("error - too much power to a battery");
			System.exit(1);
		}
		e_curr += p_dispatch;
	}

	public double agility(int t_curr){
		return t_done - t_curr - (e_max - e_curr)/p_max;
	}

	public double forced(int t_curr){
		double ag = this.agility(t_curr);

		if(ag > 1) return 0;
		else if(ag > 0) return p_max*(1-ag);
		else return p_max;
	}
}
