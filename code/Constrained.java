
public interface Constrained {
	public double forced(double t_curr);
	public void consume(double p_dispatch);
	public double agility(double t_curr);
	public double getAgility();
	public double getPower();
}
