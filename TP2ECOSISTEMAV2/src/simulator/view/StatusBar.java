package simulator.view;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

class StatusBar extends JPanel implements EcoSysObserver {
	// TODO A�adir los atributos necesarios.
	StatusBar(Controller ctrl) {
	initGUI();
	// TODO registrar this como observador
	}
	private void initGUI() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBorder(BorderFactory.createBevelBorder(1));
		// TODO Crear varios JLabel para el tiempo, el n�mero de animales, y la
		// dimensi�n y a�adirlos al panel. Puedes utilizar el siguiente c�digo
		// para a�adir un separador vertical:
		//
		// JSeparator s = new JSeparator(JSeparator.VERTICAL);
		// s.setPreferredSize(new Dimension(10, 20));
		// this.add(s);
		}
		// TODO el resto de m�todos van aqu�
	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		
		
	}
	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		
		
	}
	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		
		
	}
	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		
		
	}
	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		
		
	}
}