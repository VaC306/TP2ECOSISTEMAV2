package simulator.factories;

import org.json.JSONObject;

import simulator.model.DefaultRegion;
import simulator.model.Region;

public class DefaultRegionBuilder extends Builder<Region>{
	
	public DefaultRegionBuilder()
	{
		super("default", "Infinite food supply");
	}

	@Override
	protected Region create_instance(JSONObject data) {
		return new DefaultRegion();
	}
	
	@Override
	protected void fill_in_data(JSONObject o) {
		o.put("factor","Food increase factor (optional with default 2.0)");
		o.put("food", "Initial amount of food (optional with default 1000.0");
	}
}
