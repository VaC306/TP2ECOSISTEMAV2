package simulator.control;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.Simulator;
import simulator.view.SimpleObjectViewer;
import simulator.view.SimpleObjectViewer.ObjInfo;

public class Controller {
	
	protected Simulator _sim;
	
	public Controller(Simulator sim)
	{
		_sim = sim;
	}
	
	private static List<ObjInfo> to_animals_info(List<? extends AnimalInfo> animals) {
		List<ObjInfo> ol = new ArrayList<>(animals.size());
		for (AnimalInfo a : animals)
		ol.add(new ObjInfo(a.get_genetic_code(),
		(int) a.get_position().getX(),
		(int) a.get_position().getY(),(int)Math.round(a.get_age())+2));
		return ol;
		}
	
	public void load_data(JSONObject data) {
		
		load_regions(data);
		
		JSONArray _animales = data.getJSONArray("animals");
		
		for(int i = 0; i < _animales.length(); ++i)
		{
			JSONObject _animal = _animales.getJSONObject(i);
			int cantidad = _animal.getInt("amount");
			
			for(int j = 0; j < cantidad; ++j)
			{
				JSONObject spec = _animal.getJSONObject("spec");
				_sim.add_animal(spec);
			}
			
		}
		
	}
	
	private void load_regions(JSONObject data)
	{
		if(data.has("regions")) {
			JSONArray _regiones = data.getJSONArray("regions");
			
			for(int i = 0; i < _regiones.length(); ++i)
			{
				JSONObject _region = _regiones.getJSONObject(i);
				
				JSONArray _row = _region.getJSONArray("row");
				int rf = _row.getInt(0);
				int rt = _row.getInt(1);
				
				JSONArray _col = _region.getJSONArray("col");
				int cf = _col.getInt(0);
				int ct = _col.getInt(1);
				
				JSONObject spec = _region.getJSONObject("spec");
				
				for (int R = rf; R <= rt; R++) {
	                for (int C = cf; C <= ct; C++) {
	               
	                    _sim.set_region(R, C, spec);
	                    
	                }
	            }	
			}
			
		}
	}
	
	public void run(double t, double dt, boolean sv, OutputStream out)
	{
		PrintStream p = new PrintStream(out);
		JSONObject init_state = new JSONObject(); 
		JSONObject final_state = new JSONObject();
		SimpleObjectViewer view = null;
		
		if(sv)
		{
			view = new SimpleObjectViewer("[ECOSYSTEM]", _sim.get_map_info().get_width(), _sim.get_map_info().get_height(), 
					_sim.get_map_info().get_cols(), _sim.get_map_info().get_rows());
			view.update(to_animals_info(_sim.get_animals()), _sim.get_time(), dt);
		}
		
		
		init_state.put("in", _sim.as_JSON());
		while(_sim.get_time() < t)
		{
			_sim.advance(dt);
			if (sv) view.update(to_animals_info(_sim.get_animals()), _sim.get_time(), dt);
		}
		final_state.put("out", _sim.as_JSON());
		
		p.println("{");
		p.println(init_state + ", ");
		p.println(final_state);
		p.println("}");

	}
	
	public void reset(int cols, int rows, int width, int height)
	{
		_sim.reset(cols, rows, width, height);
	}
	
	public void set_regions(JSONObject rs)
	{
		load_regions(rs);
	}
	
	public void advance(double dt)
	{
		_sim.advance(dt);
	}
	
	public void addObserver(EcoSysObserver o)
	{
		_sim.addObserver(o);
	}
	
	public void removeObserver(EcoSysObserver o)
	{
		_sim.removeObserver(o);
	}
}
