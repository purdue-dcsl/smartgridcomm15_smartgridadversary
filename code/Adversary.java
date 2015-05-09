import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


public class Adversary {
	private String type;
	private int to_attack;
	private int seed;
	
	Adversary(String type, int num, int seed){
		this.type = type;
		to_attack = num;
		this.seed = seed;
	}
	
	public void attack(Bakery[] bakeries){
		Random r = new Random(seed);
		
		/* Sort in order of agility factor */
		Arrays.sort(bakeries);      //least agile first
		
		/*Want to attack the most agile, decrease the overall flexibility */
		int num = bakeries.length;
		double sum = num*(num+1)/2;
		double[] prob = new double[num];
		//starts with least agile - which get the least weight.
		for(int i = 0; i < num; ++i){
			prob[i] = (i+1)/sum;       //these do sum to 1 :)
		}
		
		/*Now pick out who to actually attack */
		boolean[] attack = new boolean[num];
		Arrays.fill(attack, false);
		int num_attacked = 0;
		while(num_attacked < to_attack){
			double d = r.nextDouble();
			//who should we attack?
			boolean success = false;
			for(int i = 0; i < num; i++){
				if(d < prob[i]){
					//if not attacked already, mark them to attack
					if(!attack[i]) {
						attack[i] = true;
						success = true;
						break;
					}
					break;
				}
			}
			if(success){
				num_attacked++;
				success = false;
			}
		}
		
		for(int i = 0; i < num; i++){
			if(attack[i]){
				//jamming forces the utility back to historical data on load, leaves everything else the same
				//data is accurate +- 20%
				if(type.equals("jam")){
					double scale = r.nextDouble()*.4 + .8;  //uniform over .8 - 1.2
					double cur = bakeries[i].getE();
					//System.out.printf("cur: %f\tscale: %f\tnew: %f\n", cur, scale, cur*scale);
					bakeries[i].setE(cur*scale);
				}
				else if(type.equals("fdi-load")){
					double scale = 1.2;
					double cur = bakeries[i].getE();
					//System.out.printf("cur: %f\tscale: %f\tnew: %f\n", cur, scale, cur*scale);
					bakeries[i].setE(cur*scale);
				}
                else if(type.equals("fdi-time")){
                    bakeries[i].setTime(bakeries[i].getRun());
                }
				/* Other attack types go here */
			}
		}
	}
	public void attack_defend(Bakery[] bakeries){
		Random r = new Random(seed);
		
		/* Sort in order of agility factor */
		Arrays.sort(bakeries);      //least agile first
		
		/*Want to attack the most agile, decrease the overall flexibility */
		int num = bakeries.length;
		double sum = num*(num+1)/2;
		double[] prob = new double[num];
		//starts with least agile - which get the least weight.
		for(int i = 0; i < num; ++i){
			prob[i] = (i+1)/sum;       //these do sum to 1 :)
		}
		
		/*Now pick out who to defend */
		boolean[] def = new boolean[num];
		Arrays.fill(def, false);
		int num_def = 0;
		while(num_def < to_attack){
			double d = r.nextDouble();
			//who should we attack?
			boolean success = false;
			for(int i = 0; i < num; i++){
				if(d < prob[i]){
					//if not attacked already, mark them to attack
					if(!def[i]) {
						def[i] = true;
						success = true;
						break;
					}
					break;
				}
			}
			if(success){
				num_def++;
				success = false;
			}
		}
		
		/*Now pick out who to actually attack */
		boolean[] attack = new boolean[num];
		Arrays.fill(attack, false);
		int num_attacked = 0;
		while(num_attacked < to_attack){
			double d = r.nextDouble();
			//who should we attack?
			boolean success = false;
			for(int i = 0; i < num; i++){
				if(d < prob[i]){
					//if not attacked already, mark them to attack
					if(!attack[i] && !def[i]) {
						attack[i] = true;
						success = true;
						break;
					}
					break;
				}
			}
			if(success){
				num_attacked++;
				success = false;
			}
		}
		
		for(int i = 0; i < num; i++){
			if(attack[i]){
				//jamming forces the utility back to historical data on load, leaves everything else the same
				//data is accurate +- 20%
				if(type.equals("jam")){
					double scale = r.nextDouble()*.4 + .8;  //uniform over .8 - 1.2
					double cur = bakeries[i].getE();
					//System.out.printf("cur: %f\tscale: %f\tnew: %f\n", cur, scale, cur*scale);
					bakeries[i].setE(cur*scale);
				}
				else if(type.equals("fdi-load")){
					double scale = 1.2;
					double cur = bakeries[i].getE();
					//System.out.printf("cur: %f\tscale: %f\tnew: %f\n", cur, scale, cur*scale);
					bakeries[i].setE(cur*scale);
				}
                else if(type.equals("fdi-time")){
                    bakeries[i].setTime(bakeries[i].getRun());
                }
				/* Other attack types go here */
			}
		}
	}
}