package window;

import javax.swing.JProgressBar;

public class timeProgress extends JProgressBar implements Runnable{
	
	public void changeValue(int value) {
		setValue(value);
		setVisible(true);
	}

	@Override
	public void run() {
		// TODO 自动生成的方法存根
		
	}
}
