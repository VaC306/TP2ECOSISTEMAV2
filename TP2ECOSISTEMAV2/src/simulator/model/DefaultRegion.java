package simulator.model;

public class DefaultRegion extends Region{

	@Override
	public void update(double dt) {
		
	}

	@Override
	public double get_food(Animal a, double dt) {
		int n=0;
		for(Animal b: getAnimals()) {
			if(b.get_diet()== Diet.HERBIVORE) {
				n++;
			}
		}
		
		if(a.get_diet() == Diet.CARNIVORE)
			return 0.0;
		else
			return 60.0*Math.exp(-Math.max(0,n-5.0)*2.0)*dt;
			
	}
	
	public String toString()
	{
		return "Default Region";
	}
	
}
