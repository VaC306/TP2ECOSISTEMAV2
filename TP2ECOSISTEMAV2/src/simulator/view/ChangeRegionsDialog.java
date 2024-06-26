package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.control.Controller;
import simulator.launcher.Main;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

class ChangeRegionsDialog extends JDialog implements EcoSysObserver {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DefaultComboBoxModel<String> _regionsModel;
	private DefaultComboBoxModel<Integer> _fromRowModel;
	private DefaultComboBoxModel<Integer> _toRowModel;

	// TODO en caso de ser necesario, a�adir los atributos aqu�
	private int _status = 1;
	private DefaultComboBoxModel<Integer> _fromColModel;
	private DefaultComboBoxModel<Integer> _toColModel;
	private DefaultTableModel _dataTableModel;
	private Controller _ctrl;
	private List<JSONObject> _regionsInfo;
	JComboBox<String> _regionsCombo;
	private String[] _headers = { "Key", "Value", "Description" };
	private JButton _ok;
	private JButton _cancel;
	
	private int _selectedRegionIndex;
	
	
	ChangeRegionsDialog(Controller ctrl) {
		super((Frame)null, true);
		_ctrl = ctrl;
		initGUI();
		// TODO registrar this como observer;
		_ctrl.addObserver(this);
	}
	
	
	private void initGUI() {
		setTitle("Change Regions");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);
		
		// TODO crea varios paneles para organizar los componentes visuales en el
		// dialogo, y a�adelos al mainpanel. P.ej., uno para el texto de ayuda,
		// uno para la tabla, uno para los combobox, y uno para los botones.
		JPanel _label_panel = new JPanel();
		mainPanel.add(_label_panel);
		
		JPanel _table_panel = new JPanel();
		_table_panel.setLayout(new BoxLayout(_table_panel, BoxLayout.Y_AXIS));
		mainPanel.add(_table_panel);
		
		JPanel _combo_panel = new JPanel();
		mainPanel.add(_combo_panel);
		
		JPanel _buttons_panel = new JPanel();
		mainPanel.add(_buttons_panel);
		
		// TODO crear el texto de ayuda que aparece en la parte superior del di�logo y
		// a�adirlo al panel correspondiente di�logo (Ver el apartado Figuras)
		// _regionsInfo se usar� para establecer la informaci�n en la tabla
		
		//texto de ayuda
		JLabel _info1 = new JLabel("Select a region type, the rows/cols interval, and provide values for the parameters in the Value Column (default values\n");
		JLabel _info2 = new JLabel("are used for parameters with no value)\n");
		_label_panel.add(_info1);
		_label_panel.add(_info2);
		
		
		//info de la tabla
		_regionsInfo = Main._regions_factory.get_info();
		
		// _dataTableModel es un modelo de tabla que incluye todos los par�metros de
		// la region
		_dataTableModel = new DefaultTableModel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO hacer editable solo la columna 1
				return column == 1;
			}
		};
		
		_dataTableModel.setColumnIdentifiers(_headers);
		
		// TODO crear un JTable que use _dataTableModel, y a�adirlo al di�logo
		// _regionsModel es un modelo de combobox que incluye los tipos de regiones		
		JTable _table = new JTable(_dataTableModel);
		_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_table_panel.add(_table);
		
		JScrollPane _dataTableScroll = new JScrollPane(_table);
		_dataTableScroll.setVisible(true); // scroll panel
		
		_table_panel.add(_dataTableScroll);
		
		_regionsModel = new DefaultComboBoxModel<>();
		
		// TODO a�adir la descripci�n de todas las regiones a _regionsModel, para eso
		// usa la clave �desc� o �type� de los JSONObject en _regionsInfo,
		for (JSONObject info : _regionsInfo)
        {
            _regionsModel.addElement(info.getString("type"));     
        }
		
		// ya que estos nos dan informaci�n sobre lo que puede crear la factor�a.
		// TODO crear un combobox que use _regionsModel y a�adirlo al di�logo.
		_regionsCombo = new JComboBox<>(_regionsModel);
		JLabel _regionsLabel = new JLabel("Region type: ");
		_regionsLabel.setAlignmentX(CENTER_ALIGNMENT);
		_combo_panel.add(_regionsLabel);
		_combo_panel.add(_regionsCombo);
		
		_regionsCombo.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				_selectedRegionIndex = _regionsCombo.getSelectedIndex();
				JSONObject info = _regionsInfo.get(_selectedRegionIndex);
				JSONObject data = info.getJSONObject("data");
				_dataTableModel.setRowCount(0);
				for(String key : data.keySet())
				{
					String value = data.getString(key);
		            _dataTableModel.addRow(new Object[] { key, "", value });
				}
			}
		});
		
		// TODO crear 4 modelos de combobox para _fromRowModel, _toRowModel,
		// _fromColModel y _toColModel.
		_fromRowModel = new DefaultComboBoxModel<>();
		_toRowModel = new DefaultComboBoxModel<>();
		_fromColModel = new DefaultComboBoxModel<>();
		_toColModel = new DefaultComboBoxModel<>();
		
		// TODO crear 4 combobox que usen estos modelos y a�adirlos al di�logo.
		JComboBox<Integer> _fromRowCombo = new JComboBox<>(_fromRowModel);
		JComboBox<Integer> _toRowCombo = new JComboBox<>(_toRowModel);
		JComboBox<Integer> _fromColCombo = new JComboBox<>(_fromColModel);
		JComboBox<Integer> _toColCombo = new JComboBox<>(_toColModel);
		
		JLabel _rowLabel = new JLabel("Row from/to: ");
		_rowLabel.setAlignmentX(CENTER_ALIGNMENT);
		_combo_panel.add(_rowLabel);
		_combo_panel.add(_fromRowCombo);
		_combo_panel.add(_toRowCombo);
		
		JLabel _colLabel = new JLabel("Column from/to: ");
		_colLabel.setAlignmentX(CENTER_ALIGNMENT);
		_combo_panel.add(_colLabel);
		_combo_panel.add(_fromColCombo);
		_combo_panel.add(_toColCombo);
		
		// TODO crear los botones OK y Cancel y a�adirlos al di�logo.
		//boton cancel
		_cancel = new JButton("CANCEL");
		_cancel.addActionListener((e) -> {
			_status = 0;
			setVisible(false);
		});
		
		//boton TODO ok terminarlo
		_ok = new JButton("OK");
		_ok.addActionListener((e) -> {
			
			//Crear el JSONObject retornado general
			JSONObject ret = new JSONObject();
			
			//crear el JSONObject regions
			JSONObject regions = new JSONObject();
			JSONArray array = new JSONArray();
			
			//Crear el JSONObject spec que contiene el type y data
			JSONObject spec = new JSONObject();
			
			JSONObject region_data = new JSONObject(get_json());
			JSONObject region_type =  _regionsInfo.get(_regionsCombo.getSelectedIndex()); 
			
			spec.put("type", region_type.get("type"));
			spec.put("data", region_data);
			
			//Crear el JSONObject regions que contiene row, col, spec
			JSONArray row = new JSONArray();
			JSONArray col = new JSONArray();
			
			int row_from = (int) _fromRowCombo.getSelectedItem();
			int row_to = (int) _toRowCombo.getSelectedItem();
			int col_from = (int) _fromColCombo.getSelectedItem();
			int col_to = (int) _toColCombo.getSelectedItem();
			
			row.put(0, row_from);
			row.put(1, row_to);
			
			col.put(0, col_from);
			col.put(1, col_to);
			
			
			ret.put("row", row);
			ret.put("col", col);
			ret.put("spec", spec);
			
			array.put(ret);
			regions.put("regions", array);
			
			// pasalo a _ctrl.set_regions para cambiar las regiones
			//_ctrl.set_regions(region_type);
			_ctrl.set_regions(regions);
			
			// cambiar el estado y ocultar el dialogo
			_status = 1;
			setVisible(false);
		});		
		
		
		_buttons_panel.add(_ok);
		_buttons_panel.add(_cancel);
		
		setPreferredSize(new Dimension(700, 400)); // puedes usar otro tama�o
		pack();
		setResizable(false);
		setVisible(false);
	}
	
	public String get_json() {
		StringBuilder s = new StringBuilder();
		s.append('{');
		for (int i = 0; i < _dataTableModel.getRowCount(); i++) {
			String k = _dataTableModel.getValueAt(i, 0).toString();
			String v = _dataTableModel.getValueAt(i, 1).toString();
			if (!v.isEmpty()) {
				s.append('"');
				s.append(k);
				s.append('"');
				s.append(':');
				s.append(v);
				s.append(',');
			}
		}

		if (s.length() > 1)
			s.deleteCharAt(s.length() - 1);
		s.append('}');

		return s.toString();
	}
	
	public void open(Frame parent) {
		setLocation(//
		parent.getLocation().x + parent.getWidth() / 2 - getWidth() / 2, //
		parent.getLocation().y + parent.getHeight() / 2 - getHeight() / 2);
		pack();
		setVisible(true);
	}
	// TODO el resto de m�todos van aqu�

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		
		//_regionsModel.removeAllElements();
		_fromRowModel.removeAllElements();
		_toRowModel.removeAllElements();
		_fromColModel.removeAllElements();
		_toRowModel.removeAllElements();
		
		
		for (int i = 0; i < map.get_rows(); i++)
        {
            _fromRowModel.addElement(i);
            _toRowModel.addElement(i);
        }
		
		for (int i = 0; i < map.get_cols(); i++)
        {
            _fromColModel.addElement(i);
            _toColModel.addElement(i);
        }
	}


	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		_regionsModel.removeAllElements();
		_fromRowModel.removeAllElements();
		_toRowModel.removeAllElements();
		_fromColModel.removeAllElements();
		_toRowModel.removeAllElements();
		
		for (int i = 0; i < map.get_rows(); i++)
        {
            _fromRowModel.addElement(i);
            _toRowModel.addElement(i);
        }
		
		for (int i = 0; i < map.get_cols(); i++)
        {
            _fromColModel.addElement(i);
            _toColModel.addElement(i);
        }
		
		for (JSONObject info : _regionsInfo)
        {
            _regionsModel.addElement(info.getString("type"));
            JSONObject data = info.getJSONObject("data");
            for(String key : data.keySet())
            {
            	String value = data.getString(key);
                _dataTableModel.addRow(new Object[] { key, "", value });
            }
        }
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

