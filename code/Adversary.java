import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.ArrayList;


public class Adversary {
    private String type;
    private int to_attack;
    private int seed;
    
    Adversary(String type, int num, int seed){
        this.type = type;
        to_attack = num;
        this.seed = seed;
    }
    
    private boolean[] select(Random r, ArrayList<Bucket> buckets, ArrayList<Battery> batteries, ArrayList<Bakery> bakeries, boolean naieve, boolean[] def){
        boolean[] attack;
        
        if(naieve){
            int num = bakeries.size() + batteries.size() + buckets.size();
            attack = new boolean[num];
            Arrays.fill(attack, false);
            int num_attacked = 0;
            while(num_attacked < to_attack){
                int i = r.nextInt(num);
                if(!attack[i]){
                    attack[i] = true;
                    num_attacked++;
                }
            }
        }
        else if(def != null){
            /* Sort in order of agility factor */
            Collections.sort(bakeries);      //least agile first
            
            /*Want to attack the most agile, decrease the overall flexibility */
            int num;
            if(type.equals("fdi-class")) num = buckets.size();
            else num = bakeries.size();
            double sum = num*(num+1)/2;
            double[] prob = new double[num];
            //starts with least agile - which get the least weight.
            for(int i = 0; i < num; ++i){
                prob[i] = (i+1)/sum;       //these do sum to 1 :)
            }
            
            /*Now pick out who to actually attack */
            attack = new boolean[num];
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
        }
        else{
            /*Now pick out who to actually attack */
            int num;
            if(type.equals("fdi-class")) num = buckets.size();
            else num = bakeries.size();
            double sum = num*(num+1)/2;
            double[] prob = new double[num];
            //starts with least agile - which get the least weight.
            for(int i = 0; i < num; ++i){
                prob[i] = (i+1)/sum;       //these do sum to 1 :)
            }
            attack = new boolean[num];
            int num_attacked = 0;
            while(num_attacked < to_attack){
                double d = r.nextDouble();
                //who should we attack?
                boolean success = false;
                for(int i = 0; i < num; i++){
                    if(d < prob[i]){
                        //if not attacked already or defended, mark them to attack
                        if(!attack[i] && !def[i]) {
                            attack[i] = true;
                            success = true;
                            break;
                        }
                        //if defended but not attacked attack fails
                        else if(!attack[i]){
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
        }
        
        return attack;
    }
    public void attack(Random r,ArrayList<Bucket> buckets, ArrayList<Battery> batteries, ArrayList<Bakery> bakeries, int K, boolean naieve){
        
        boolean[] attack = select(r, buckets, batteries, bakeries, naieve, null);
        
        int bat = batteries.size();
        int buck = buckets.size();
        
        for(int i = attack.length - 1; i >= 0; i--){
            if(attack[i]){
                //jamming forces the utility back to historical data on load, leaves everything else the same
                //data is accurate +- 20%
                if(type.equals("jam") && !naieve){
                    double scale = r.nextDouble()*.4 + .8;  //uniform over .8 - 1.2
                    double cur = bakeries.get(i).getE();
                    //System.out.printf("cur: %f\tscale: %f\tnew: %f\n", cur, scale, cur*scale);
                    bakeries.get(i).setE(cur*scale);
                }
                else if(type.equals("jam") && naieve){
                    //battery
                    if(i < bat){
                        double scale = r.nextDouble()*.4 + .8;  //uniform over .8 - 1.2
                        double cur = batteries.get(i).getE();
                        //System.out.printf("cur: %f\tscale: %f\tnew: %f\n", cur, scale, cur*scale);
                        batteries.get(i).setE(cur*scale);
                    }
                    //bucket
                    else if( i < bat + buck){
                        double scale = r.nextDouble()*.4 + .8;  //uniform over .8 - 1.2
                        double cur = buckets.get(i).getE();
                        //System.out.printf("cur: %f\tscale: %f\tnew: %f\n", cur, scale, cur*scale);
                        buckets.get(i).setE(cur*scale);
                    }
                    //bakery
                    else{
                        double scale = r.nextDouble()*.4 + .8;  //uniform over .8 - 1.2
                        double cur = bakeries.get(i).getE();
                        //System.out.printf("cur: %f\tscale: %f\tnew: %f\n", cur, scale, cur*scale);
                        bakeries.get(i).setE(cur*scale);
                    }
                }
                else if(type.equals("fdi-load") && !naieve){
                    double scale = 1.2;
                    double cur = bakeries.get(i).getE();
                    //System.out.printf("cur: %f\tscale: %f\tnew: %f\n", cur, scale, cur*scale);
                    bakeries.get(i).setE(cur*scale);
                }
                else if(type.equals("fdi-load") && naieve){
                    //battery
                    if(i < bat){
                        double scale = 1.2;
                        double cur = batteries.get(i).getE();
                        //System.out.printf("cur: %f\tscale: %f\tnew: %f\n", cur, scale, cur*scale);
                        batteries.get(i).setE(cur*scale);
                    }
                    //bucket
                    else if( i < bat + buck){
                        double scale = 1.2;
                        double cur = buckets.get(i).getE();
                        //System.out.printf("cur: %f\tscale: %f\tnew: %f\n", cur, scale, cur*scale);
                        buckets.get(i).setE(cur*scale);
                    }
                    //bakery
                    else{
                        double scale = 1.2;
                        double cur = bakeries.get(i).getE();
                        //System.out.printf("cur: %f\tscale: %f\tnew: %f\n", cur, scale, cur*scale);
                        bakeries.get(i).setE(cur*scale);
                    }
                }
                else if(type.equals("fdi-time") && !naieve){
                    bakeries.get(i).setTime(bakeries.get(i).getRun());
                }
                else if(type.equals("fdi-time") && naieve){
                    //battery
                    if(i < bat){
                        batteries.get(i).setTime(batteries.get(i).getTime());
                    }
                    //no notion of time for buckets
                    else if(i < bat + buck) continue;
                    else{
                        bakeries.get(i).setTime(bakeries.get(i).getRun());
                    }
                }
                else if(type.equals("fdi-class")){
                    double e = buckets.get(i).getE();
                    double pow = buckets.get(i).getPow();
                    double t = (Math.ceil(e / pow) > K) ? K : Math.ceil(e/pow);
                    Bakery b = new Bakery(e, pow, t);
                    bakeries.add(b);
                    buckets.remove(i);
                }
                
                /* Other attack types go here */
            }
        }
    }
    public void attack_defend(Random r, ArrayList<Bucket> buckets, ArrayList<Battery> batteries, ArrayList<Bakery> bakeries, int K, boolean naieve){
        
        boolean[] def = select(r, buckets, batteries, bakeries, naieve, null);
        
        boolean[] attack = select(r, buckets, batteries, bakeries, naieve, def);
        
        int bat = batteries.size();
        int buck = buckets.size();
        
        for(int i = attack.length - 1; i >= 0; i--){
            if(attack[i]){
                //jamming forces the utility back to historical data on load, leaves everything else the same
                //data is accurate +- 20%
                if(type.equals("jam") && !naieve){
                    double scale = r.nextDouble()*.4 + .8;  //uniform over .8 - 1.2
                    double cur = bakeries.get(i).getE();
                    //System.out.printf("cur: %f\tscale: %f\tnew: %f\n", cur, scale, cur*scale);
                    bakeries.get(i).setE(cur*scale);
                }
                else if(type.equals("jam") && naieve){
                    //battery
                    if(i < bat){
                        double scale = r.nextDouble()*.4 + .8;  //uniform over .8 - 1.2
                        double cur = batteries.get(i).getE();
                        //System.out.printf("cur: %f\tscale: %f\tnew: %f\n", cur, scale, cur*scale);
                        batteries.get(i).setE(cur*scale);
                    }
                    //bucket
                    else if( i < bat + buck){
                        double scale = r.nextDouble()*.4 + .8;  //uniform over .8 - 1.2
                        double cur = buckets.get(i).getE();
                        //System.out.printf("cur: %f\tscale: %f\tnew: %f\n", cur, scale, cur*scale);
                        buckets.get(i).setE(cur*scale);
                    }
                    //bakery
                    else{
                        double scale = r.nextDouble()*.4 + .8;  //uniform over .8 - 1.2
                        double cur = bakeries.get(i).getE();
                        //System.out.printf("cur: %f\tscale: %f\tnew: %f\n", cur, scale, cur*scale);
                        bakeries.get(i).setE(cur*scale);
                    }
                }
                else if(type.equals("fdi-load") && !naieve){
                    double scale = 1.2;
                    double cur = bakeries.get(i).getE();
                    //System.out.printf("cur: %f\tscale: %f\tnew: %f\n", cur, scale, cur*scale);
                    bakeries.get(i).setE(cur*scale);
                }
                else if(type.equals("fdi-load") && naieve){
                    //battery
                    if(i < bat){
                        double scale = 1.2;
                        double cur = batteries.get(i).getE();
                        //System.out.printf("cur: %f\tscale: %f\tnew: %f\n", cur, scale, cur*scale);
                        batteries.get(i).setE(cur*scale);
                    }
                    //bucket
                    else if( i < bat + buck){
                        double scale = 1.2;
                        double cur = buckets.get(i).getE();
                        //System.out.printf("cur: %f\tscale: %f\tnew: %f\n", cur, scale, cur*scale);
                        buckets.get(i).setE(cur*scale);
                    }
                    //bakery
                    else{
                        double scale = 1.2;
                        double cur = bakeries.get(i).getE();
                        //System.out.printf("cur: %f\tscale: %f\tnew: %f\n", cur, scale, cur*scale);
                        bakeries.get(i).setE(cur*scale);
                    }
                }
                else if(type.equals("fdi-time") && !naieve){
                    bakeries.get(i).setTime(bakeries.get(i).getRun());
                }
                else if(type.equals("fdi-time") && naieve){
                    //battery
                    if(i < bat){
                        batteries.get(i).setTime(batteries.get(i).getTime());
                    }
                    //no notion of time for buckets
                    else if(i < bat + buck) continue;
                    else{
                        bakeries.get(i).setTime(bakeries.get(i).getRun());
                    }
                }
                else if(type.equals("fdi-class")){
                    double e = buckets.get(i).getE();
                    double pow = buckets.get(i).getPow();
                    double t = (Math.ceil(e / pow) > K) ? K : Math.ceil(e/pow);
                    Bakery b = new Bakery(e, pow, t);
                    bakeries.add(b);
                    buckets.remove(i);
                }
                
                /* Other attack types go here */
            }
        }
    }
}
