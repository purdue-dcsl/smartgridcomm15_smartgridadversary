import java.io.*;
import java.lang.Math;

public class Bakery {

	private double e_max;  //total energy to consume
	private double p_max;  //amount of power can consume in 1 time step
	private double t_done; //when it has to finish by
	private int run;    //how long it has to run;

	private boolean running;
	private int count;

	Bakery(double e, double p, double t){
		this.e_max = e;
		this.p_max = p;
		this.t_done = t;
		run = Math.floor(e_max / p_max);
		run = (e_max % p_max > 0) ? run + 1 : run;  //round up if e / p has a remainder
	
		this.running = false;
		this.count = 0;
	}
	public void start(){
		this.running = true;
	}
	public boolean isRunning(){
		return running;
	}
	public double getPower(){
		return p_max;
	}
	public void consume(){
		count++;
	}
	public boolean isFinished(){
		if(count >= run) {
			running = false;
			return true;
		}
		else return false;
	}

	public double agility(double t_curr){
		return t_done - run - t_curr;
	}

	public double forced(double t_curr){
		return (this.agility(t_curr) > 1) ? 0 : p_max;
	}
}
