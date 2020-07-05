package tools;

public class NumberFormatTrans {
	  /* public static String intToBinStr(int num, int digits) {
	        int value = 1 << digits | num;
	        String bs = Integer.toBinaryString(value); //0x20 | 这个是为了保证这个string长度是6位数
	        return  bs.substring(1);
	    }
	   public static byte[] strToBoolean(String str) { 
	      byte[] bool=new byte[str.length()]; 
	        for (int i = str.length()-1; i >= 0; i--) { 
	            if(str.charAt(i)=='0') {
	            	bool[i]=false;
	            }
	            else {
	            	bool[i]=true;
	            }
	        } 
	        return bool; 
	    }
	   public static boolean[] strToBoolean2(String str) { 
	        boolean[] bool=new boolean[str.length()]; 
	        boolean trigger=true;
	        int label=0;
	        for (int i =0; i < str.length()-1; i++) { 
	            if(str.charAt(i)=='1') {
	            		if(trigger==true) {
	    	            	label=i;
	    	            	trigger=false;
	            		}
	            	bool[i-label]=true;
	            }
	        } 
	        return bool; 
	    }
	    */
		
}
