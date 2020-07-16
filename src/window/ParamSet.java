package window;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JTextField;

import tools.ExcelReader;

public class ParamSet extends JFrame{

	public JTextField getTextField() {
		return textField;
	}
	public void setTextField(JTextField textField) {
		this.textField = textField;
	}
	private JTextField textField;
    
    public ParamSet(JMenuItem jt) {
    	setLayout(null);
    	setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(800, 450, 350, 140);
        setTitle(jt.getText());
        
        getContentPane().setLayout(null);
         
        JLabel label = new JLabel("参数:");
        label.setBounds(90, 10, 70, 15);
        getContentPane().add(label);
         
        textField = new JTextField();
        textField.setBounds(150, 10, 100, 21);
        getContentPane().add(textField);
        textField.setColumns(10);
        JButton button_2 = new JButton("确定");
        button_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(getTextField().getText()!=null) {
            		switch(jt.getText()) {
            		case "设置班级个数":
            			ExcelReader.classNum=Integer.valueOf(getTextField().getText());
            			break;
            		case "设置班级人数":
            			ExcelReader.studentsNum=Integer.valueOf(getTextField().getText());
            			break;
            		case "设置交叉概率":
            			ExcelReader.crossoverPossibility=Double.valueOf(getTextField().getText());
            			break;
            		case "设置变异概率":
            			ExcelReader.mutatePossibility=Double.valueOf(getTextField().getText());
            			break;
            		default:
            			break;
            		}
            		dispose();
    			}
            }
        });
        button_2.setBounds(130, 69, 93, 23);
        getContentPane().add(button_2,BorderLayout.CENTER);
    }
    
}
