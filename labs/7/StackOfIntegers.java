import java.util.Stack;

public class StackOfIntegers {

	private Stack<Integer> stack;

	public StackOfIntegers() {
		stack = new Stack<>();
	}

	public void push(int i) {
		stack.push(i);
	}

	public void printStack() {
		while (!stack.isEmpty()) {
			System.out.print(stack.pop() + " ");
		}
		System.out.println();
	}
}
