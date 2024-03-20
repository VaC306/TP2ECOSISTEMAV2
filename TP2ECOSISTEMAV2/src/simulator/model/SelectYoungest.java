package simulator.model;

import java.util.List;

public class SelectYoungest implements SelectionStrategy{

	@Override
	public Animal select(Animal a, List<Animal> as) {
		double youngestAge = Double.MAX_VALUE;
		
		if(as.isEmpty())
			return null;
		
		for (Animal animal : as) {
            if (animal.get_age() < youngestAge) {
                youngestAge = animal.get_age();
                a = animal;
            }
        }
		
		return a;
	}

}
