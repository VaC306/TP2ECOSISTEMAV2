package simulator.factories;

import org.json.JSONObject;

import simulator.model.DynamicSupplyRegion;
import simulator.model.Region;

public class DynamicSupplyRegionBuilder extends Builder<Region>{
	
	private Integer factor;
	private double food;
	
	public DynamicSupplyRegionBuilder()
	{
		super("dynamic", "Dynamic food supply");
	}

	@Override
	protected Region create_instance(JSONObject data) {
		
		if(data == null)
			throw new IllegalArgumentException();
		
		//valores por defecto
		factor = 2;
		food = 1000.0;
	
		if(data.has("factor"))
			factor = data.getInt("factor");
		if(data.has("food"))
			food = data.getDouble("food");
		
			
		
		return new DynamicSupplyRegion(food, factor);
	}
	
	@Override
	protected void fill_in_data(JSONObject o) {
		o.put("factor","Food increase factor (optional with default 2.0)");
		o.put("food", "Initial amount of food (optional with default 1000.0");
	}
	
}