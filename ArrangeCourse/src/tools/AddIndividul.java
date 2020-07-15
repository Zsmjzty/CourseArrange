package tools;

import GA.ClassSolution;

public class AddIndividul {
	public static void add(ClassSolution cs,ClassSolution cn) {
		ClassSolution head=cs;
		while(head.next!=null) {
			head=head.next;
		}
		head.next=cn;
	}
}
