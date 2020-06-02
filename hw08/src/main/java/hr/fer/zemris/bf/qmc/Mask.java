package hr.fer.zemris.bf.qmc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import hr.fer.zemris.bf.utils.Util;

/**
 * Stores minterms or don'tcares as byte mask. For example if function has 4
 * variables(A, B, C, D) and minterm's product is AC'D his mask will be 1201.
 * Number 1 means variable at that position is present, number 0 means variable
 * at that position is complemented, and number 2 means minterm doesn't depend
 * on that variable at that position.
 * 
 * @author Mihael Jaić
 *
 */

public class Mask {
	/**
	 * Mask.
	 */
	private byte[] mask;
	/**
	 * Minterms.
	 */
	private Set<Integer> indexes;
	/**
	 * If mask is don't care.
	 */
	private boolean dontCare;
	/**
	 * hashcode of mask.
	 */
	private final int hashCode;
	/**
	 * If mask was combined with other mask.
	 */
	private boolean combined;

	/**
	 * Constructor that accepts single minterm.
	 * 
	 * @param index
	 *            Index of minterm.
	 * @param numberOfVariables
	 *            Number of variables.
	 * @param dontCare
	 *            If expression is don't care value.
	 * @throws IllegalArgumentException
	 *             If number of variables isn't positive.
	 */

	public Mask(int index, int numberOfVariables, boolean dontCare) throws IllegalArgumentException {
		if (numberOfVariables < 1) {
			throw new IllegalArgumentException("Mask needs to have at least 1 variable.");
		}

		this.dontCare = dontCare;

		Set<Integer> temp = new TreeSet<>();
		temp.add(index);
		indexes = Collections.unmodifiableSet(temp);

		mask = Util.indexToByteArray(index, numberOfVariables);

		hashCode = Arrays.hashCode(mask);
	}

	/**
	 * Constructor that accepts set of minterms.
	 * 
	 * @param values
	 *            Mask.
	 * @param indexes
	 *            Set of minterms.
	 * @param dontCare
	 *            If expression is don't care value.
	 * @throws IllegalArgumentException
	 *             If values or indexes is null or set of minterms is empty.
	 */

	public Mask(byte[] values, Set<Integer> indexes, boolean dontCare) throws IllegalArgumentException {
		if (values == null || indexes == null || indexes.isEmpty()) {
			throw new IllegalArgumentException();
		}

		this.indexes = Collections.unmodifiableSet(new TreeSet<>(indexes));

		mask = new byte[values.length];
		for (int i = 0; i < values.length; i++) {
			mask[i] = values[i];
		}

		this.dontCare = dontCare;

		hashCode = Arrays.hashCode(mask);
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Mask)) {
			return false;
		}

		Mask other = (Mask) obj;
		if (hashCode != other.hashCode) {
			return false;
		}

		return Arrays.equals(mask, other.mask);
	}

	/**
	 * Returns true if mask was combined, false otherwise.
	 * 
	 * @return True if mask was combined, false otherwise.
	 */

	public boolean isCombined() {
		return combined;
	}

	/**
	 * Sets masked combined value.
	 * 
	 * @param combined
	 *            Combined value.
	 */

	public void setCombined(boolean combined) {
		this.combined = combined;
	}

	/**
	 * Returns true if mask is don't care value, false otherwise.
	 * 
	 * @return True if mask is don't care value, false otherwise.
	 */

	public boolean isDontCare() {
		return dontCare;
	}

	/**
	 * Gets set of minterms.
	 * 
	 * @return Set of minterms.
	 */

	public Set<Integer> getIndexes() {
		return indexes;
	}

	/**
	 * Counts number of bytes that are set to 1 in mask.
	 * 
	 * @return Number of bytes that are set to 1 in mask.
	 */

	public int countOfOnes() {
		int result = 0;

		for (byte b : mask) {
			if (b == 1) {
				result++;
			}
		}

		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (byte b : mask) {
			if (b == 2) {
				sb.append('-');
				continue;
			}
			sb.append(b == 1 ? "1" : "0");
		}

		sb.append(String.format(" %c %c ", dontCare ? 'D' : '.', combined ? '*' : ' '));
		sb.append(indexes.toString());

		return sb.toString();
	}

	/**
	 * Width of mask.
	 * 
	 * @return Width of mask.
	 */

	public int size() {
		return mask.length;
	}

	/**
	 * Gets value of byte at given position.
	 * 
	 * @param position
	 *            Position of byte in array.
	 * @return Value of byte at given position.
	 */

	public byte getValueAt(int position) {
		if (position < 0 || position > mask.length - 1) {
			throw new IllegalArgumentException("Position has to be interval [0, mask.length -1].");
		}

		return mask[position];
	}

	/**
	 * Combines 2 masks into 1 mask. Mask can be combined if they are of same
	 * length and they only differ in one byte value. However they can't differ
	 * in values in which one byte has value 2 and the other don't.
	 * 
	 * @param other
	 *            Other mask.
	 * @return New mask that is combination of 2 masks.
	 * @throws IllegalArgumentException
	 *             If masks aren't of same length.
	 */

	public Optional<Mask> combineWith(Mask other) throws IllegalArgumentException {
		if (other == null || other.mask.length != mask.length) {
			throw new IllegalArgumentException("Masks have to be same length.");
		}

		int differentIndex = 0;
		int numberOfDifferent = 0;
		for (int i = 0; i < mask.length; i++) {
			if (mask[i] != other.mask[i]) {
				if (mask[i] == 2 || other.mask[i] == 2) {
					return Optional.empty();
				}
				numberOfDifferent++;
				differentIndex = i;
			}
		}

		if (numberOfDifferent != 1) {
			return Optional.empty();
		}

		Set<Integer> combinedIndexes = new TreeSet<>(indexes);
		for (int index : other.indexes) {
			combinedIndexes.add(index);
		}

		byte[] newMask = new byte[mask.length];
		for (int i = 0; i < mask.length; i++) {
			if (i == differentIndex) {
				newMask[i] = 2;
				continue;
			}
			newMask[i] = mask[i];
		}

		return Optional.of(new Mask(newMask, combinedIndexes, dontCare && other.dontCare));
	}

}
