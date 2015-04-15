
import java.lang.Math;

public class Bakery implements Constrained, Comparable<Constrained>{

	private double e_max;  //total energy to consume
	private double p_max;  //amount of power can consume in 1 time step
	private double t_done; //when it has to finish by
	private double run;    //how long it has to run;

	private boolean running;
	private double e_curr;
	private double ag_factor;

	Bakery(double e, double p, double t){
		if(e < 0 || p < 0 || t < 0){
			System.out.println("illegal arguments to a new bakery");
			System.exit(1);
		}
		this.e_max = e;
		this.p_max = p;
		this.t_done = t;
		run = Math.ceil(e_max / p_max);
	
		ag_factor = 0;
		this.running = false;
		this.e_curr = 0;
	}
	private void start(){
		this.running = true;
	}
	private boolean isRunning(){
		return running;
	}
	public void consume(double p_dispatch){
		if(p_dispatch < p_max){
			System.out.println("Error - bakery didn't get enough power");
			System.exit(1);
		}
		if(this.isFinished()) return;
		if(!this.isRunning()) this.start();
		e_curr += p_dispatch;
		this.isFinished();
	}
	public boolean isFinished(){
		if(e_curr >= e_max - .0000001) {
			running = false;
			return true;
		}
		else return false;
	}

	public double agility(double t_curr){
		ag_factor = t_done - run - t_curr;
		return ag_factor;
	}

	public double forced(double t_curr){
		if (this.isFinished()) return 0;
		return (this.agility(t_curr) > 1) ? 0 : p_max;
	}
	
	public double getAgility(){
		return this.ag_factor;
	}
	
	public int compareTo(Constrained c){
		if(ag_factor - c.getAgility() > 0) return 1;
		else return -1;
	}
	public double getPower(){
		return p_max;
	}
}
