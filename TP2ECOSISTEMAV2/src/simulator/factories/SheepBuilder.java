package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.model.Animal;
import simulator.model.SelectionStrategy;
import simulator.model.Sheep;

public class SheepBuilder extends Builder<Animal>{
	
	private Factory<SelectionStrategy> _selection_strategy_factory;
	
	public SheepBuilder(Factory<SelectionStrategy> selection_strategy_factory)
	{
		super("sheep","a"); //ver que poner en data
		_selection_strategy_factory = selection_strategy_factory;
	}

	@Override
	protected Animal create_instance(JSONObject data) {
		
		//valid data
		if(data == null)
			throw new IllegalArgumentException();
			
		SelectionStrategy _mate_strategy = null;
		SelectionStrategy _danger_strategy = null;
		
		if(data.has("mate_strategy"))
		{
			JSONObject _selection = data.getJSONObject("mate_strategy");
			_mate_strategy = _selection_strategy_factory.create_instance(_selection);
		}
		else
		{
			JSONObject _selection = new JSONObject();
			_selection.put("type", "first");
			_mate_strategy = _selection_strategy_factory.create_instance(_selection);
		}
		if(data.has("danger_strategy"))
		{
			JSONObject _selection = data.getJSONObject("danger_strategy");
			_danger_strategy = _selection_strategy_factory.create_instance(_selection);
		}
		else
		{
			JSONObject _selection = new JSONObject();
			_selection.put("type", "first");
			_danger_strategy = _selection_strategy_factory.create_instance(_selection);
		}
		
		Vector2D _pos = new Vector2D();
		
		
		if(!data.has("pos"))
		{
			_pos = null;
		}
		else
		{
			JSONObject pos = data.getJSONObject("pos");
			
			JSONArray _x_range = pos.getJSONArray("x_range");
			JSONArray _y_range = pos.getJSONArray("y_range");
				
			//se comrpueba q sean 2d
			if(_x_range.length()!=2 || _y_range.length()!=2 ) {
							
				throw new IllegalArgumentException();
			}
				
			double x = Utils._rand.nextDouble(_x_range.getDouble(0), _x_range.getDouble(1));
			double y = Utils._rand.nextDouble(_y_range.getDouble(0), _y_range.getDouble(1));
				
			_pos = new Vector2D(x,y);
				
			assert(_pos.getX() >= _x_range.getDouble(0) && _pos.getX() <= _x_range.getDouble(1));
			assert(_pos.getY() >= _y_range.getDouble(0) && _pos.getY() <= _y_range.getDouble(1));
		}
		
		return new Sheep(_mate_strategy, _danger_strategy, _pos);
	}

}
