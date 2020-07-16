package window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;

import tools.ExcelReader;

public class ImportInfo extends JFrame {
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	private String filePath;
    private JTextField textField;

	public JTextField getTextField() {
		return textField;
	}
	public void setTextField(JTextField textField) {
		this.textField = textField;
	}
		

	    /**
	     * Create the frame.
	     */
	    public ImportInfo(JMenuItem jt) {
	        setResizable(false);
	        setVisible(true);
	        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        setBounds(800, 450, 501, 140);
	        setTitle(jt.getText());
	        
	        getContentPane().setLayout(null);
	         
	        JLabel label = new JLabel("文件路径");
	        label.setBounds(10, 10, 70, 15);
	        getContentPane().add(label);
	         
	        textField = new JTextField();
	        textField.setBounds(90, 7, 300, 21);
	        getContentPane().add(textField);
	        textField.setColumns(10);
	         
	        JButton button = new JButton("选择文件");
	        button.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                do_button_actionPerformed(e);
	            }
	        });
	        button.setBounds(400, 6, 93, 23);
	        getContentPane().add(button);
	         
	         
	        JButton button_2 = new JButton("确定");
	        button_2.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	switch(jt.getText()) {
	            	case "导入课程信息":
	            		if(getFilePath()!=null) {
	        				ExcelReader.CourseInfo=filePath;
	        				dispose();
	            		}
	            		break;
	            	case "导入学分需求信息":
	            		if(getFilePath()!=null) {
	        				ExcelReader.ArrangeInfo=filePath;
	        				dispose();
	            		}
	            		break;
	            	case "导入教师信息":
	            		if(getFilePath()!=null) {
	        				ExcelReader.TeacherInfo=filePath;
	        				dispose();
	            		}
	            		break;
	            	case "导入教室信息":
	            		if(getFilePath()!=null) {
	        				ExcelReader.ClassroomInfo=filePath;
	        				dispose();
	            		}
	            		break;
	            	}
	            }
	        });
	        button_2.setBounds(182, 69, 93, 23);
	        getContentPane().add(button_2);
	    }
	    protected void do_button_actionPerformed(ActionEvent e){
	        JFileChooser chooser=new JFileChooser();
	        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	        // 显示文件打开对话框
	        // 获取用户选择的文件对象
	        int returnValue = chooser.showOpenDialog(null);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
			    File selectedFile = chooser.getSelectedFile();
			    filePath = selectedFile.getAbsolutePath();
			}
	        
	        // 显示文件信息到文本框
	        textField.setText(filePath);
	    }

    }
    

 

