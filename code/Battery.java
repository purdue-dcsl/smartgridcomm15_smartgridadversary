
public class Battery implements Constrained, Comparable<Constrained>{
	private double e_max;
	private double p_max;
	private double t_done;

	private double e_curr;
	private double ag_factor;

	public Battery(double e_max, double p_max, double t_done){
		if(e_max < 0 || p_max < 0 || t_done < 0){
			System.out.println("illegal arguments to a new battery");
			System.exit(1);
		}
		this.e_max = e_max;
		this.p_max = p_max;
		this.t_done = t_done;

		ag_factor = 0;
		e_curr = 0;
	}

	public void consume(double p_dispatch){
		if(p_dispatch > p_max) {
			System.out.println("error - too much power to a battery");
			System.exit(1);
		}
		e_curr += p_dispatch;
	}

	public double agility(double t_curr){
		ag_factor = t_done - t_curr - (e_max - e_curr)/p_max;
		return ag_factor;
	}

	public double forced(double t_curr){
		double ag = this.agility(t_curr);

		if(ag > 1) return 0;
		else if(ag > 0) return p_max*(1-ag);
		else return p_max;
	}
	public double getAgility(){
		return this.ag_factor;
	}
	
	public int compareTo(Constrained c){
		if(ag_factor - c.getAgility() > 0) return 1;
		else if(ag_factor == c.getAgility()) return 0;
		else return -1;
	}
	
	public double getPower(){
		return p_max;
	}
}
