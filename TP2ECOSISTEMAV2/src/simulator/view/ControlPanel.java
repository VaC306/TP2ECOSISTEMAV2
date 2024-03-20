package simulator.view;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.misc.Utils;

class ControlPanel extends JPanel {
	private Controller _ctrl;
	private ChangeRegionsDialog _changeRegionsDialog;
	private JToolBar _toolaBar;
	private JFileChooser _fc;
	private boolean _stopped = true; // utilizado en los botones de run/stop
	private JButton _quitButton;

	// TODO añade más atributos aquí …
	ControlPanel(Controller ctrl) {
		_ctrl = ctrl;
		initGUI();
	}

	private void initGUI() {
		setLayout(new BorderLayout());
		_toolaBar = new JToolBar();
		add(_toolaBar, BorderLayout.PAGE_START);
		// TODO crear los diferentes botones/atributos y añadirlos a _toolaBar.
		// Todos ellos han de tener su correspondiente tooltip. Puedes utilizar
		// _toolaBar.addSeparator() para añadir la línea de separación vertical
		// entre las componentes que lo necesiten.
		// Quit Button
		_toolaBar.add(Box.createGlue()); // this aligns the button to the right
		_toolaBar.addSeparator();
		_quitButton = new JButton();
		_quitButton.setToolTipText("Quit");
		_quitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		_quitButton.addActionListener((e) -> ViewUtils.quit(this));
		_toolaBar.add(_quitButton);
		// TODO Inicializar _fc con una instancia de JFileChooser. Para que siempre
		// abre en la carpeta de ejemplos puedes usar:
		//
		// _fc.setCurrentDirectory(new File(System.getProperty("user.dir") +
		// "/resources/examples"));
		// TODO Inicializar _changeRegionsDialog con instancias del diálogo de cambio
		// de regiones
	}
	
	
	private void run_sim(int n, double dt) {
		if (n > 0 && !_stopped) {
		try {
		_ctrl.advance(dt);
		SwingUtilities.invokeLater(() -> run_sim(n - 1, dt));
		} catch (Exception e) {
		// TODO llamar a ViewUtils.showErrorMsg con el mensaje de error
		// que corresponda
		// TODO activar todos los botones
		_stopped = true;
		}
		} else {
		// TODO activar todos los botones
		_stopped = true;
		}
		}
}

