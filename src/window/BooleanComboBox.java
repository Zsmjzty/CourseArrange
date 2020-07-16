package window;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

public class BooleanComboBox extends AbstractListModel<String> implements ComboBoxModel<String>{
	String selectedItem=null;
	String[] test= {"true","false"};
	public String getElementAt(int index) {
		return test[index];
	}
	
	public int getSize() {
		return test.length;
	}
	public void setSelectedItem(Object item) {
		selectedItem=(String)item;
	}
	public Object getSelectedItem() {
		return selectedItem;
	}
}

