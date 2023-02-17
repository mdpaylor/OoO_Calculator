package OoO_Calculator;

public class OperatorFinders {
	
	protected String getVal(String s, char ch) {
		@SuppressWarnings("unused")
		double val = 0.0;
		String strVal = "";
		int len = s.length();
		if (infinityCheck(s) && !containsInterruption(s)) return s;
		if (ch == 'l') {
			for (int i=0; i<len; i++) {
				if (s.charAt(i) == '.') continue;
				if (s.charAt(i) == '-' && i == 0) continue;
				if (s.charAt(i) =='-' && Character.toLowerCase(s.charAt(i-1)) == 'e') continue;
				else if (Character.toLowerCase(s.charAt(i)) == 'e') {
					strVal = s.substring(0,i+1);
					continue;
				}
				try {
					val = Double.valueOf(s.substring(0,i+1));
					strVal = s.substring(0,i+1);
				}
				catch (Exception e) {
					char temp = strVal.charAt(0);
					if (temp == '+') strVal = strVal.substring(1);
					return strVal;
				}
			}
		}
		else {
			int i;
			for (i=len-1; i>0; i--) {
				if ((s.charAt(i) == '.' || s.charAt(i) =='-') && Character.toLowerCase(s.charAt(i-1)) == 'e') continue;
				else if (Character.toLowerCase(s.charAt(i)) == 'e') {
					strVal = "E" + s.substring(i+1);
					continue;
				}
				try {
					val = Double.valueOf(s.substring(i));
					strVal = s.substring(i);
					
				}
				catch (Exception e) {
					char temp = strVal.charAt(0);
					if (temp == '+' || temp == '-') strVal = strVal.substring(1);
					return strVal;
				}
			}
			//return s;
		}
		if (s.length() > 0 && strVal.length() == 0) return s;
		else return strVal;
	}
	
	protected int openParenthesesCount(String phrase) {
		int openCount = 0;
		for (int i=0; i<phrase.length(); i++) {
			if (phrase.charAt(i) == '(') openCount++;
		}
		return openCount;
	}
	
	protected int closedParenthesesCount(String phrase) {
		int closedCount = 0;
		for (int i=0; i<phrase.length(); i++) {
			if (phrase.charAt(i) == ')') closedCount++;
		}
		return closedCount;
	}
	
	protected int findEndPar(String s, int start) {
		int skips = 0;
		for (int i=start; i<s.length();i++) {
			if (s.charAt(i) == '(') skips++;
			else if (s.charAt(i) == ')' && skips != 0) skips--;
			else if (s.charAt(i) == ')' && skips == 0) return i;
		}
		return s.length()-1;
	}
	
	protected String insertVal(int start, int end, String original, String val, boolean checkComplex) {
		String left = original.substring(0,start);
		char chL = '+';
		
		if (left.length() != 0) chL = left.charAt(left.length()-1);
		if (chL != '+' && chL != '-' && chL != '*' && chL != '/' && chL != '^' && chL != '(' && chL != '%') {
			if (checkComplex && (chL != 'n' && chL != 'd' && chL != 's' && chL !='g' && chL != 'e' && chL != 'a' && chL 
					!= 'o' && chL != 'v' && chL != 'h' && chL != 'i' && chL != 'r')) left += "*";
			else if (!checkComplex) left += "*";
		}
		
		String right = original.substring(end+1);
		char chR = '+';
		
		if (right.length() != 0) chR = right.charAt(0);
		if (chR != '+' && chR != '-' && chR != '*' && chR != '/' && chR != '^' && chR != ')' && chR != '%') right = "*" + right;
		
		return left + val + right;
	}

	protected String insertComplexVal(int start, int end, String original, String val) {
		String left = original.substring(0,start);
		char chL = 'N';
		
		if (left.length() != 0) chL = left.charAt(left.length()-1);
		if ((chL != 'N') && (chL == '0' || chL == '1' || chL == '2' || chL == '3' || chL == '4' || chL == '5' 
				|| chL == '6' || chL == '7' || chL == '8' || chL == '9')) left += "*";
		
		String right = original.substring(end+1);
		char chR = 'N';
		
		if (right.length() != 0) chR = right.charAt(0);
		if ((chR != 'N') && (chR == '0' || chR == '1' || chR == '2' || chR == '3' || chR == '4' || chR == '5' 
				|| chR == '6' || chR == '7' || chR == '8' || chR == '9')) left = "*" + left;
		
		return left + val + right;
	}
	
	protected boolean infinityCheck(String s) {
		String sub = "";
		for (int i=0; i<s.length()-7; i++) {
			sub = s.substring(i,i+8);
			if (sub.equals("Infinity")) return true;
		}
		return false;
	}
	
	private boolean containsInterruption(String s) {
		for (int i=0; i<s.length(); i++) {
			char ch = s.charAt(i);
			if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^' || ch == '!' || ch == '%') return true;
		}
		return false;
	}
}
