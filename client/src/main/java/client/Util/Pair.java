package client.Util;

/**
 * Pair
 */
public class Pair<T, V> {

	private T first;
	private V second;

	public Pair() {

	}

	public T getFirst() {
		return first;
	}

	public V getSecond() {
		return second;
	}

	public void setFirst(T newFirst) {
		first = newFirst;
	}

	public void setSecond(V newSecond) {
		second = newSecond;
	}
}
