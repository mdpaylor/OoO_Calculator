package OoO_Calculator;

import java.io.*;
import java.util.*;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class Calculator extends OperatorFinders {
	private HashMap<Character, Integer> charToInt;
	private boolean checkComplex;
	private boolean checkANS;
	private boolean radian;
	private String phrase;
	private double answer;
	private double X;
	private double Y;
	private double W;
	private double Z;
	
	public Calculator() {
		charToInt = makeChar2IntMap();
		checkComplex = false;
		checkANS = false;
		radian = true;
		phrase = "";
		answer = 0.0;
		X = 0.0;
		Y = 0.0;
		W = 0.0;
		Z = 0.0;
	}
	
	public Calculator(File file) throws IOException {
		Scanner scnr = new Scanner(System.in);
		boolean go = true;
		while (go) {
			try {
				FileInputStream fis = new FileInputStream(file);
				Scanner s = new Scanner(fis);
				
				charToInt = makeChar2IntMap();
				checkComplex = false;
				checkANS = false;
				radian = Boolean.valueOf(s.nextLine());
				phrase = "";
				answer = 0;
				X = Double.valueOf(s.nextLine());
				Y = Double.valueOf(s.nextLine());
				W = Double.valueOf(s.nextLine());
				Z = Double.valueOf(s.nextLine());
				
				s.close();
				break;
			}
			catch (IOException e) {
				System.out.println("\nInvalid File\nWould you like to continue searching?\n1. YES\n\"ENTER\". NO");
				String decision = scnr.nextLine();
				if (!decision.equals("1")) break;
				file = getFile();
			}
			catch (InputMismatchException e) {
				System.out.println("\nInvalid File\nWould you like to continue searching?\n1. YES\n\"ENTER\". NO");
				String decision = scnr.nextLine();
				if (!decision.equals("1")) break;
				file = getFile();
			}
			catch (Exception e) {
				System.out.println("\nUnknown Error\nWould you like to continue searching?\n1. YES\n\"ENTER\". NO");
				String decision = scnr.nextLine();
				if (!decision.equals("1")) break;
				file = getFile();
			}
		}
		scnr.close();
	}
	
	private HashMap<Character, Integer> makeChar2IntMap() {
		HashMap<Character, Integer> map = new HashMap<>();
		map.put('%', 1);
		map.put('^', 2);
		map.put('!', 3);
		map.put('*', 4);
		map.put('+', 5);
		return map;
	}
	
	// Retrieves a file from the user via the JFileChooser
	private File getFile() {
		JFileChooser jfc = new JFileChooser();
		int result = jfc.showOpenDialog(new JFrame());
		
		// Requires the user to pick a file by using recursion
		if (result == JFileChooser.APPROVE_OPTION) {
			return jfc.getSelectedFile();
		}
		else {
			System.out.println("You have to choose a file.");
			return getFile();
		}
	}
	
	// Set Methods
	public void setX(double val1) {
		X = val1;
	}
	
	public void setY(double val1) {
		Y = val1;
	}
	
	public void setC(double val1) {
		W = val1;
	}
	
	public void setZ(double val1) {
		Z = val1;
	}
	
	// Accessory Methods
	public double getX() {
		return X;
	}
	
	public double getY() {
		return Y;
	}
	
	public double getW() {
		return W;
	}
	
	public double getZ() {
		return Z;
	}
	
	public void setRadian(boolean condition) {
		radian = condition;
	}
	
	public void setCheckComplex(boolean condition) {
		checkComplex = condition;
	}
	
	public void setCheckANS(boolean condition) {
		checkANS = condition;
	}
	
	public void setPhrase(String s) {
		phrase = s;
	}
	
	public void addToPhrase(String s) {
		phrase += s;
	}
	
	public void clearOperations() {
		phrase = "";
	}
	
	// Operator Methods
	private String addOrSub(String s) {
		char ch;
		double leftVal=0;
		double rightVal=0;
		for (int i=0;i<s.length(); i++) {
			ch = s.charAt(i);
			if (ch == '+' && i != 0) {
				String leftStringVal = getVal(s.substring(0,i),'r');
				if (leftStringVal.length() != 0) leftVal = Double.valueOf(leftStringVal);
				String rightStringVal = getVal(s.substring(i+1), 'l');
				if (rightStringVal.length() != 0) rightVal = Double.valueOf(rightStringVal);
				return s.substring(0,i-leftStringVal.length()) + (leftVal+rightVal) + s.substring(i+1+rightStringVal.length());
			}
			else if (ch =='-' && i != 0) {
				if (s.charAt(i-1) == 'E') continue;
				String leftStringVal = getVal(s.substring(0,i),'r');
				if (leftStringVal.length() != 0) leftVal = Double.valueOf(leftStringVal);
				String rightStringVal = getVal(s.substring(i+1), 'l');
				if (rightStringVal.length() != 0) rightVal = Double.valueOf(rightStringVal);
				return s.substring(0,i-leftStringVal.length()) + (leftVal-rightVal) + s.substring(i+1+rightStringVal.length());
			}
		}
		return s;
	}
	
	private String multOrDiv(String s) {
		char ch;
		double leftVal=1;
		double rightVal=1;
		for (int i=0; i<s.length(); i++) {
			ch = s.charAt(i);
			if (ch == '*') {
				String leftStringVal = getVal(s.substring(0,i),'r');
				if (leftStringVal.length() != 0) leftVal = Double.valueOf(leftStringVal);
				String rightStringVal = getVal(s.substring(i+1), 'l');
				if (rightStringVal.length() != 0) rightVal = Double.valueOf(rightStringVal);
				return s.substring(0,i-leftStringVal.length()) + (leftVal*rightVal) + s.substring(i+1+rightStringVal.length());
			}
			else if (ch == '/') {
				String leftStringVal = getVal(s.substring(0,i),'r');
				if (leftStringVal.length() != 0) leftVal = Double.valueOf(leftStringVal);
				String rightStringVal = getVal(s.substring(i+1), 'l');
				if (rightStringVal.length() != 0) rightVal = Double.valueOf(rightStringVal);
				return s.substring(0,i-leftStringVal.length()) + (leftVal/rightVal) + s.substring(i+1+rightStringVal.length());
			}
		}
		return s;
	}
	
	private String abs(String s) {
		String sub;
		double val = 0;
		for (int i=0; i<s.length() && s.length()-i>=3; i++) {
			sub = s.substring(i, i+3).toLowerCase();
			if (sub.equals("abs")) {
				String stringVal = getVal(s.substring(i+3),'l');
				if (stringVal.length() != 0) val = Math.abs(Double.valueOf(stringVal));
				String left = s.substring(0,i);
				String right = s.substring(i+3+stringVal.length());
				return left + val + right;
			}
		}
		return s;
	}
	
	private String percent(String s) {
		char ch;
		double val=0;
		for (int i=0; i<s.length(); i++) {
			ch = s.charAt(i);
			if (ch == '%') {
				String stringVal = getVal(s.substring(0,i),'r');
				if (stringVal.length() != 0) val = .01 * Double.valueOf(stringVal);
				String left = s.substring(0,i-stringVal.length());
				String right = s.substring(i+1);
				return left + val + right;
			}
		}
		return s;
	}
	
	private String mod(String s) {
		String sub;
		double leftVal = 0;
		double rightVal = 0;
		for (int i=s.length(); i>2; i--) {
			sub = s.substring(i-3,i);
			if (sub.equals("mod")) {
				String leftStringVal = getVal(s.substring(0,i-3),'r');
				if (leftStringVal.length() != 0) leftVal = Double.valueOf(leftStringVal);
				String rightStringVal = getVal(s.substring(i), 'l');
				if (rightStringVal.length() != 0) rightVal = Double.valueOf(rightStringVal);
				String left = s.substring(0,i-3-leftStringVal.length());
				String right = s.substring(i+rightStringVal.length());
				return left +(leftVal%rightVal) + right;
			}
		}
		return s;
	}
	
	// Factorial
	private String factorial(String s) {
		char ch;
		double val=0;
		int i=0;
		for (; i<s.length(); i++) {
			ch = s.charAt(i);
			if (ch == '!') break;
		}
		
		String stringVal = getVal(s.substring(0,i), 'r');
		if (stringVal.length() != 0) val = Double.valueOf(stringVal);
		double total = 1;
		for (double j=val; j > 1; j--) {
			total *= j;
		}
		return s.substring(0,i-stringVal.length()) + total + s.substring(i+1);
	}
	
	private String log(String s) {
		String sub;
		double val = 10;
		int len = s.length();
		for (int i=0; i<len && i+3<=len;i++) {
			sub = s.substring(i,i+3);
			if (sub.equals("log")) {
				String stringVal = getVal(s.substring(i+3), 'l');
				if (stringVal.length() != 0) val = Double.valueOf(stringVal);
				String left = s.substring(0,i);
				String right = s.substring(i+3+stringVal.length());
				return left + Math.log10(val) + right;
			}
		}
		return s;
	}
	
	// Log(e)
	private String loe(String s) {
		String sub;
		double val = Math.E;
		int len = s.length();
		for (int i=0; i<len && i+3<=len;i++) {
			sub = s.substring(i,i+3);
			if (sub.equals("loe")) {
				String stringVal = getVal(s.substring(i+3), 'l');
				if (stringVal.length() != 0) val = Double.valueOf(stringVal);
				String left = s.substring(0,i);
				String right = s.substring(i+3+stringVal.length());
				return left + Math.log(val) + right;
			}
		}
		return s;
	}
	
	private String sin(String s) {
		String sub;
		int len = s.length();
		double val = 0;
		String left = s.substring(0);
		String right = "";
		int i;
		for (i=0; i<len && i+3<=len;i++) {
			sub = s.substring(i,i+3);
			if (sub.equals("sin")) break;
		}
		
		String stringVal = getVal(s.substring(i+3), 'l');
		val = Double.valueOf(stringVal);
		left = s.substring(0,i);
		right = s.substring(i+3+stringVal.length());

		if (radian) val = Math.sin(val);
		else {
			val *= (Math.PI/180);
			Math.sin(val);
		}
		return left + val + right;
	}
	
	private String cos(String s) {
		String sub;
		int len = s.length();
		double val = 0;
		String left = s.substring(0);
		String right = "";
		int i;
		for (i=0; i<len && i+3<=len;i++) {
			sub = s.substring(i,i+3);
			if (sub.equals("cos")) break;
		}
		
		String stringVal = getVal(s.substring(i+3), 'l');
		val = Double.valueOf(stringVal);
		left = s.substring(0,i);
		right = s.substring(i+3+stringVal.length());

		if (radian) val = Math.cos(val);
		else {
			val *= (Math.PI/180);
			Math.cos(val);
		}
		return left + val + right;
	}
	
	private String tan(String s) {
		String sub;
		int len = s.length();
		double val = 0;
		String left = s.substring(0);
		String right = "";
		int i;
		for (i=0; i<len && i+3<=len;i++) {
			sub = s.substring(i,i+3);
			if (sub.equals("tan")) break;
		}
		
		String stringVal = getVal(s.substring(i+3), 'l');
		val = Double.valueOf(stringVal);
		left = s.substring(0,i);
		right = s.substring(i+3+stringVal.length());

		if (radian) val = Math.tan(val);
		else {
			val *= (Math.PI/180);
			Math.tan(val);
		}
		return left + val + right;
	}
	
	// asin
	private String asi(String s) {
		String sub;
		int len = s.length();
		double val = 0;
		String left = s.substring(0);
		String right = "";
		int i;
		for (i=0; i<len && i+3<=len;i++) {
			sub = s.substring(i,i+3);
			if (sub.equals("asi")) break;
		}
		
		String stringVal = getVal(s.substring(i+3), 'l');
		val = Double.valueOf(stringVal);
		left = s.substring(0,i);
		right = s.substring(i+3+stringVal.length());

		if (radian) val = Math.asin(val);
		else {
			val *= (Math.PI/180);
			Math.asin(val);
		}
		return left + val + right;
	}
	
	// acos
	private String aco(String s) {
		String sub;
		int len = s.length();
		double val = 0;
		String left = s.substring(0);
		String right = "";
		int i;
		for (i=0; i<len && i+3<=len;i++) {
			sub = s.substring(i,i+3);
			if (sub.equals("aco")) break;
		}
		
		String stringVal = getVal(s.substring(i+3), 'l');
		val = Double.valueOf(stringVal);
		left = s.substring(0,i);
		right = s.substring(i+3+stringVal.length());

		if (radian) val = Math.acos(val);
		else {
			val *= (Math.PI/180);
			Math.acos(val);
		}
		return left + val + right;
	}
	
	// atan
	private String ata(String s) {
		String sub;
		int len = s.length();
		double val = 0;
		String left = s.substring(0);
		String right = "";
		int i;
		for (i=0; i<len && i+3<=len;i++) {
			sub = s.substring(i,i+3);
			if (sub.equals("ata")) break;
		}
		
		String stringVal = getVal(s.substring(i+3), 'l');
		val = Double.valueOf(stringVal);
		left = s.substring(0,i);
		right = s.substring(i+3+stringVal.length());

		if (radian) val = Math.atan(val);
		else {
			val *= (Math.PI/180);
			Math.atan(val);
		}
		return left + val + right;
	}
	
	// sinh
	private String sih(String s) {
		String sub;
		int len = s.length();
		double val = 0;
		String left = s.substring(0);
		String right = "";
		int i;
		for (i=0; i<len && i+3<=len;i++) {
			sub = s.substring(i,i+3);
			if (sub.equals("sih")) break;
		}
		
		String stringVal = getVal(s.substring(i+3), 'l');
		val = Double.valueOf(stringVal);
		left = s.substring(0,i);
		right = s.substring(i+3+stringVal.length());

		if (radian) val = Math.sinh(val);
		else {
			val *= (Math.PI/180);
			Math.sinh(val);
		}
		return left + val + right;
	}
	
	// cosh
	private String coh(String s) {
		String sub;
		int len = s.length();
		double val = 0;
		String left = s.substring(0);
		String right = "";
		int i;
		for (i=0; i<len && i+3<=len;i++) {
			sub = s.substring(i,i+3);
			if (sub.equals("coh")) break;
		}
		
		String stringVal = getVal(s.substring(i+3), 'l');
		val = Double.valueOf(stringVal);
		left = s.substring(0,i);
		right = s.substring(i+3+stringVal.length());

		if (radian) val = Math.cosh(val);
		else {
			val *= (Math.PI/180);
			Math.cosh(val);
		}
		return left + val + right;
	}
	
	// tanh
	private String tah(String s) {
		String sub;
		int len = s.length();
		double val = 0;
		String left = s.substring(0);
		String right = "";
		int i;
		for (i=0; i<len && i+3<=len;i++) {
			sub = s.substring(i,i+3);
			if (sub.equals("tah")) break;
		}
		
		String stringVal = getVal(s.substring(i+3), 'l');
		val = Double.valueOf(stringVal);
		left = s.substring(0,i);
		right = s.substring(i+3+stringVal.length());

		if (radian) val = Math.tanh(val);
		else {
			val *= (Math.PI/180);
			Math.tanh(val);
		}
		return left + val + right;
	}
	
	// Square Root
	private String sqr(String s) {
		String sub;
		int len = s.length();
		for (int i=0; i<len && i+3<=len; i++) {
			sub = s.substring(i, i+3);
			if (sub.equals("sqr")) {
				String stringVal = getVal(s.substring(i+3), 'l');
				double val = Double.valueOf(stringVal);
				String left = s.substring(0,i);
				String right = s.substring(i+3+stringVal.length());
				return left + Math.sqrt(val) + right;
			}
		}
		return s;
	}
	
	// Invert
	private String inv(String s) {
		String sub;
		int len = s.length();
		for (int i=0; i<len && i+3<=len; i++) {
			sub = s.substring(i, i+3);
			if (sub.equals("inv")) {
				String stringVal = getVal(s.substring(i+3), 'l');
				double val = Double.valueOf(stringVal);
				String left = s.substring(0,i);
				String right = s.substring(i+3+stringVal.length());
				return left + 1/val + right;
			}
		}
		return s;
	}
	
	private String powerOf(String s) {
		char ch;
		for (int i=0; i<s.length(); i++) {
			ch = s.charAt(i);
			if (ch == '^') {
				String leftStringVal = getVal(s.substring(0,i),'r');
				double leftVal = Double.valueOf(leftStringVal);
				String rightStringVal = getVal(s.substring(i+1),'l');
				double rightVal = Double.valueOf(rightStringVal);
				String left = s.substring(0,i-leftStringVal.length());
				String right = s.substring(i+1+rightStringVal.length());
				return left + (Math.pow(leftVal, rightVal)) + right;
			}
		}
		return s;
	}
	
	public String equals() {
		if (!checkForNums(phrase)) return "SYNTAX ERROR";
		phrase.toLowerCase();
		fixInput();
		phrase = fixPlusMinus(phrase);
		checkForStored();
		if (checkANS) checkForANSAndE();
		try {
			answer = Double.valueOf(calculate(phrase));
			return String.valueOf(answer);
		}
		catch (Exception e) {
 			return "SYNTAX ERROR";
		}
	}
	
	private boolean checkForNums(String s) {
		for (int i=0; i<s.length(); i++) {
			char ch = s.charAt(i);
			if (ch=='1'||ch=='2'||ch=='3'||ch=='4'||ch=='5'||ch=='6'||ch=='7'||ch=='8'||ch=='9'||ch=='0') return true;
		}
		return false;
	}
	
	private void fixInput() {
		int open = openParenthesesCount(phrase);
		int closed = closedParenthesesCount(phrase);
		String sub;
		
		for (int i=0; i<open-closed;i++) {
			phrase += ")";
		}
		for (int i=0; i<phrase.length() && i+2<=phrase.length(); i++) {
			sub = phrase.substring(i,i+2);
			if (sub.equals("+-") || sub.equals("-+")) {
				phrase = phrase.substring(0,i) + "-" + phrase.substring(i+2);
			}
			else if (sub.equals("--")) {
				phrase = phrase.substring(0,i) + "+" + phrase.substring(i+2);
			}
		}
	}
	
	private String fixPlusMinus(String s) {
		String sub;
		for (int i=0; i<s.length() && i+2<=s.length(); i++) {
			sub = s.substring(i,i+2);
			if (sub.equals("+-") || sub.equals("-+")) {
				s = s.substring(0,i) + "-" + s.substring(i+2);
			}
			else if (sub.equals("--")) {
				s = s.substring(0,i) + "+" + s.substring(i+2);
			}
		}
		return s;
	}
	
	private void checkForANSAndE() {
		String sub;
		int len = phrase.length();
		for (int i=0; i<len && i+3<=len; i++) {
			sub = phrase.substring(i,i+3);
			if (sub.equals("ans")) {
				String left = phrase.substring(0,i);
				String right = phrase.substring(i+3);
				phrase = left + answer + right;
			}
			else if (sub.equals("lne")) {
				String left = phrase.substring(0,i);
				String right = phrase.substring(i+3);
				phrase = left + Math.E + right;
			}
		}
	}
	
	private void checkForStored() {
		int len = phrase.length();
		
		char ch = 's';
		for (int i=0; i<len; i++) {
			if (ch == 'x') insertValInClass(i,i,String.valueOf(X));
			else if (ch == 'y') insertValInClass(i,i,String.valueOf(Y));
			else if (ch == 'z') insertValInClass(i,i,String.valueOf(Z));
			else if (ch == 'w') insertValInClass(i,i,String.valueOf(W));
			
			len = phrase.length();
		}
	}
	
	private void insertValInClass(int start, int end, String s) {
		phrase = phrase.substring(0,start) + s + phrase.substring(end+1);
	}
	
	private String calculate(String s) {
		PriorityQueue<Integer> q = new PriorityQueue<>(s.length()/2);
		char ch;
		for (int i=0; i<s.length(); i++) {
			ch = s.charAt(i);
			if (ch == '(') {
				int endPar = findEndPar(s,i+1);
				String temp = String.valueOf(calculate(s.substring(i+1,endPar)));
				s = insertVal(i,endPar,s,temp, checkComplex);
				i--;
				s = fixPlusMinus(s);
			}
			else if (ch == '+' || ch == '-') q.add(charToInt.get('+'));
			else if (ch == '*' || ch == '/') q.add(charToInt.get('*'));
			else if (ch == '!') q.add(charToInt.get('!'));
			else if (ch == '^') q.add(charToInt.get('^'));
			else if (ch == '%') q.add(charToInt.get('%'));
		}
		
		String sub;
		if (checkComplex) {
			for (int i=s.length(); i>2; i--) {
				if (i > s.length()) i = s.length();
				sub = s.substring(i-3,i);
				if (sub.equals("abs")) s = abs(s);
				else if (sub.equals("sin")) s = sin(s);
				else if (sub.equals("mod")) s = mod(s);
				else if (sub.equals("cos")) s = cos(s);
				else if (sub.equals("tan")) s = tan(s);
				else if (sub.equals("log")) s = log(s);
				else if (sub.equals("loe")) s = loe(s);
				else if (sub.equals("aco")) s = aco(s);
				else if (sub.equals("asi")) s = asi(s);
				else if (sub.equals("ata")) s = ata(s);
				else if (sub.equals("inv")) s = inv(s);
				else if (sub.equals("sih")) s = sih(s);
				else if (sub.equals("coh")) s = coh(s);
				else if (sub.equals("tah")) s = tah(s);
				else if (sub.equals("sqr")) s = sqr(s);
				
				if (NaNCheck(s)) return "NaN";
			}
		}
		
		while (!q.isEmpty()) {
			int operation = q.poll();
			
			if (operation == 1) s = percent(s);
			else if (operation == 2) s = powerOf(s);
			else if (operation == 3) s = factorial(s);
			else if (operation == 4) s = multOrDiv(s);
			else s = addOrSub(s);
		}
		
		return s;
	}
	
	private boolean NaNCheck(String s) {
		String sub;
		for (int i=0; i<s.length() && i+3<=s.length(); i++) {
			sub = s.substring(i,i+3);
			if (sub.equals("NaN")) return true;
		}
		return false;
	}
}
