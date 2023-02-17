package OoO_Calculator;

import java.util.Random;
import java.util.Scanner;

public class TestCalc {
	
	public static List list;
	
	public static void main(String args[]) {
		
		Scanner scnr = new Scanner(System.in);
		System.out.print("Which Type (Specific(s) or Mass(m)): ");
		//String type = scnr.nextLine();
		System.out.println("\n\n");
        String type = "m";
		scnr.close();
		
		///*
		if (type.equals("m")) {
			list = new List();
		
			//List l = new List();
		
			int synErr = 0;
			for (int i=0; i<6; i++) {
				synErr = TEST(100);
				System.out.println("Test "+ (i+1) +" Errors: "+ synErr);
				System.out.println("Percentage: "+ ((1-synErr/100.0)*100.0) +"%\n");
			}
		
			//l = list;
			System.out.println("-------------------------------------------------\n");
			int i=1;
			int listSize = list.size;
			while (list.size > 0) {
				Node prob = list.popHead();
				if (prob != null) System.out.println("Problem "+ (i) +":\t"+ prob.error);
				i++;
			}
			System.out.println("\n-------------------------------------------------");
			System.out.println("Total Errors: "+ listSize);
			System.out.println("Percentage Correct: "+ String.format("%.2f", ((1-listSize/600.0)*100.0)) +"%");
			System.out.println("-------------------------------------------------");
		}
		else {
		 
			Calculator calc = new Calculator();
			calc .setCheckComplex(true);
			//calc.setCheckANS(true);
			// String s1 = "200*525(7^3+2)2"; // 200 * 525(7^3 + 2)2
			// String s2 = "sin34*(4589+2^23*(56^2)+ata(3.1415926539/2)"; // sin(34) * 4589 + 2^23 * (56^2) + atan(3.1415926539/2)
			// String s3 = "34+3-4*(40!)%"; // 34 + 3 - 4 * (40!)%
			// String s4 = "sin3.1415926539+56*98+45mod6"; // sin(3.1415926539) + 56 * 98 + 45mod6
			// String s5 = "(2*10^6)/1.602*10^-19";
			// String s6 = "";
			// String s7 = "asi(1abs(6sih(539";
			// String s8 = "92(log(3-5(loe(00(sqr(8(log(0(abs(3-74";
			// String s9 = "5(tah(3^5(sqr(9853^231";
			String s = "1-01^5";
		
		
			calc.setPhrase(s);
			System.out.println(calc.equals());
		
		}
		
		/*
		for (int i=0; i<tests.length; i++) {
			calc.setPhrase(tests[i]);
			System.out.println(calc.equals());
		}
		*/
		
	}
	
	public static int TEST(int amt) {
		Calculator calc = new Calculator();
		calc .setCheckComplex(true);
		calc.setCheckANS(true);
		
		String[] tests = new String[amt];
		String[] answers = new String[amt];
		Random rand = new Random();
		
		String[] ops = {".", "(", ")", "!", "+", "-", "*", "/", "^", "%", "abs", "mod", "log",
				"loe", "sin", "cos", "tan", "asi", "aco", "ata", "sih", "coh", "tah", "sqr", "inv"};
		boolean needNum = true;
		int openParCount = 0;
		boolean doubleOp = false;
		boolean decimalPlaced = false;
		boolean needOp = true;
		String s = "";
		
		for (int i=0; i<amt; i++) {
			
			while (needNum || rand.nextBoolean() || needOp) {
				if (needNum) {
					s += String.valueOf(rand.nextInt(10));
					needNum = false;
					doubleOp = true;
				}
				
				if (rand.nextBoolean()) s += String.valueOf(rand.nextInt(10));
				else {
					String op = ops[rand.nextInt(ops.length)];
					
					if (openParCount == 0 && op.equals(")")) continue;
					if (!doubleOp && (op.equals("mod"))) continue;
					if (op.length() == 3) {
						if (!op.equals("mod")) op = "("+ op +"(";
						else continue;
						needOp = true;
						openParCount++;
						needNum = true;
						decimalPlaced = false;
					}
					
					if (op.equals(".") && !decimalPlaced) decimalPlaced = true;
					if (op.equals(".") && decimalPlaced) continue;;
					
					if (op.equals("(")) {
						needOp = true;
						openParCount++;
						s += op;
						needNum = true;
						doubleOp = false;
						decimalPlaced = false;
					}
					else {
						s += op;
						needNum = true;
						doubleOp = false;
						decimalPlaced = false;
						needOp = false;
					}
				}
			}
			tests[i] = s;
			needNum = true;
			openParCount = 0;
			s = "";
			needOp = true;
			doubleOp = false;
		}
		for (int i=0; i<amt; i++) {
			calc.setPhrase(tests[i]);
			answers[i] = calc.equals();
		}
		int syntaxErrors = 0;
		for (int i=0; i<amt; i++) {
			//System.out.println("Problem: "+ tests[i]);
			if (answers[i].equals("SYNTAX ERROR")) {
				list.add(new Node(tests[i]));
				//System.out.println("\t\t\t\t\t--- ERROR: INVALID ANSWER ---");
				syntaxErrors++;
			}
			//System.out.println("Answer: "+ answers[i]);
			//System.out.println("---------------------------------");
		}
		
		//System.out.println("\nErrors: "+ syntaxErrors);
		//System.out.println("Percentage: "+ (1 - syntaxErrors/100.0) +"%");
		return syntaxErrors;
	}
}

/* */
class Node {
	public String error;
	public Node next;
	
	public Node(String error) {
		this.error = error;
	}
}

class List {
	public Node head;
	public Node tail;
	public int size;
	
	public List() {
		head = null;
		tail = null;
		size = 0;
	}
	
	public void add(Node node) {
		if (head == null && tail == null) {
			head = tail = node;
			size++;
			return;
		}
		tail.next = node;
		tail = tail.next;
		size++;
	}
	
	public Node popHead() {
		if (head == tail) {
			Node temp = head;
			head = tail = null;
			size--;
			return temp;
		}
		Node temp = head;
		head = head.next;
		size--;
		return temp;
	}
}
