package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import simulator.control.Controller;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

class StatusBar extends JPanel implements EcoSysObserver {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// TODO Añadir los atributos necesarios.
	JLabel _timeLabel;
	JLabel _numberAnimals;
	JLabel _dimension;
	
	StatusBar(Controller ctrl) {
	initGUI();
	// TODO registrar this como observador
	ctrl.addObserver(this);
	}
	private void initGUI() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBorder(BorderFactory.createBevelBorder(1));
		// TODO Crear varios JLabel para el tiempo, el número de animales, y la
		// dimensión y añadirlos al panel. Puedes utilizar el siguiente código
		// para añadir un separador vertical:
		//
		// JSeparator s = new JSeparator(JSeparator.VERTICAL);
		// s.setPreferredSize(new Dimension(10, 20));
		// this.add(s);
		_timeLabel = new JLabel("Time: 0.0");
		add(_timeLabel);
		
		JSeparator s = new JSeparator(JSeparator.VERTICAL);
		s.setPreferredSize(new Dimension(10, 20));
		this.add(s);
		
		_numberAnimals = new JLabel("Total Animals: 0");
		add(_numberAnimals);
		
		JSeparator s1 = new JSeparator(JSeparator.VERTICAL);
		s1.setPreferredSize(new Dimension(10, 20));
		this.add(s1);
		
		_dimension = new JLabel("Dimension: ");
		add(_dimension);
		
		}
		// TODO el resto de métodos van aquí…
	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		String timeString = String.format("%.3f", time);
		_timeLabel.setText("Time: " + timeString);
		_numberAnimals.setText("Total Animals: " + animals.size());
		_dimension.setText("Dimension: " + map.get_width() + "x" + map.get_height() + " " + map.get_rows() + "x" + map.get_cols());
	}
	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		String timeString = String.format("%.3f", time);
		_timeLabel.setText("Time: " + timeString);
		_numberAnimals.setText("Total Animals: " + animals.size());
		_dimension.setText("Dimension: " + map.get_width() + "x" + map.get_height() + " " + map.get_rows() + "x" + map.get_cols());
	}
	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		_numberAnimals.setText("Total Animals: " + animals.size());
		
	}
	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		
		
	}
	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		String timeString = String.format("%.3f", time);
		_timeLabel.setText("Time: " + timeString);
		_numberAnimals.setText("Total Animals: " + animals.size());
		_dimension.setText("Dimension: " + map.get_width() + "x" + map.get_height() + " " + map.get_rows() + "x" + map.get_cols());
	}
}