package hr.fer.zemris.bf.qmc;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;

import hr.fer.zemris.bf.model.BinaryOperatorNode;
import hr.fer.zemris.bf.model.ConstantNode;
import hr.fer.zemris.bf.model.Node;
import hr.fer.zemris.bf.model.UnaryOperatorNode;
import hr.fer.zemris.bf.model.VariableNode;
import hr.fer.zemris.bf.utils.Constants;

/**
 * Class that minimizes given boolean expression using Quine-McCluskey
 * algorithm.
 * 
 * @author Mihael Jaić
 *
 */

public class Minimizer {
	/**
	 * Set of minterms.
	 */
	private Set<Integer> mintermSet;
	/**
	 * Set of don't cares.
	 */
	private Set<Integer> dontCareSet;
	/**
	 * List of variables.
	 */
	private List<String> variables;
	/**
	 * Minimal form.
	 */
	private List<Set<Mask>> minimalForms;
	/**
	 * Logger.
	 */
	private final Logger log = Logger.getLogger("hr.fer.zemris.bf.qmc");

	/**
	 * And operator strategy.
	 */
	private static final BinaryOperator<Boolean> andOperator;
	/**
	 * Or operator strategy.
	 */
	private static final BinaryOperator<Boolean> orOperator;
	/**
	 * Not operator strategy.
	 */
	private static final UnaryOperator<Boolean> notOperator;

	static {
		andOperator = (a, b) -> a && b;
		orOperator = (a, b) -> a || b;
		notOperator = a -> !a;
	}

	/**
	 * Gets mintermset, don't care set and variables and performs minimization.
	 * 
	 * @param mintermSet
	 *            Minterm set.
	 * @param dontCareSet
	 *            Don't care set.
	 * @param variables
	 *            Variables.
	 */

	public Minimizer(Set<Integer> mintermSet, Set<Integer> dontCareSet, List<String> variables) {
		if (mintermSet == null || dontCareSet == null || variables == null) {
			throw new IllegalArgumentException("null values are not allowed");
		}

		int maxIndex = (1 << variables.size()) - 1;

		for (int index : mintermSet) {
			if (dontCareSet.contains(index)) {
				throw new IllegalArgumentException("set of minterms and don't cares is not disjunct.");
			}
			if (index < 0 || index > maxIndex) {
				throw new IllegalArgumentException("minterm index is out of interval.");
			}
		}

		for (int index : dontCareSet) {
			if (index < 0 || index > maxIndex) {
				throw new IllegalArgumentException("don't care index is out of interval.");
			}
		}

		this.mintermSet = mintermSet;
		this.dontCareSet = dontCareSet;
		this.variables = variables;

		// If function is tautology or contradiction minimization will not be
		// processed.
		if ((mintermSet.size() != 0) && (mintermSet.size() + dontCareSet.size() != (1 << variables.size()))) {
			Set<Mask> primaryImplicants = findPrimaryImplicants();
			minimalForms = chooseMinimalCover(primaryImplicants);
		}
	}

	/**
	 * Gets minimal form in form of masks.
	 * 
	 * @return Minimal form.
	 */

	public List<Set<Mask>> getMinimalForms() {
		return minimalForms;
	}

	/**
	 * Gets minimal form as Node expressions.
	 * 
	 * @return Gets minimal form as node expressions.
	 */

	public List<Node> getMinimalFormsAsExpressions() {
		List<Node> minimalFormExpression = new ArrayList<>();

		if (mintermSet.size() == 0) {
			minimalFormExpression.add(new ConstantNode(false));
			return minimalFormExpression;
		}

		if (mintermSet.size() + dontCareSet.size() == (1 << variables.size())) {
			minimalFormExpression.add(new ConstantNode(true));
			return minimalFormExpression;
		}

		for (Set<Mask> masks : minimalForms) {
			List<Node> orChildren = new ArrayList<>();
			for (Mask mask : masks) {
				List<Node> andChildren = new ArrayList<>();

				for (int i = 0; i < variables.size(); i++) {
					if (mask.getValueAt(i) == 0) {
						VariableNode variable = new VariableNode(variables.get(i));
						andChildren.add(new UnaryOperatorNode(Constants.NOT_STRING, variable, notOperator));

					} else if (mask.getValueAt(i) == 1) {
						andChildren.add(new VariableNode(variables.get(i)));
					}
				}

				if (andChildren.size() == 1) {
					orChildren.add(andChildren.get(0));

				} else {
					orChildren.add(new BinaryOperatorNode(Constants.AND_STRING, andChildren, andOperator));
				}
			}

			if (orChildren.size() == 1) {
				minimalFormExpression.add(orChildren.get(0));
			} else {
				minimalFormExpression.add(new BinaryOperatorNode(Constants.OR_STRING, orChildren, orOperator));
			}
		}

		return minimalFormExpression;
	}

	/**
	 * Gets minimal form as string.
	 * 
	 * @return Minimal form as string.
	 */

	public List<String> getMinimalFormsAsString() {
		List<String> minimalString = new ArrayList<>();

		if (mintermSet.size() == 0) {
			minimalString.add("Function is contradiction");
			return minimalString;
		}

		if (mintermSet.size() + dontCareSet.size() == (1 << variables.size())) {
			minimalString.add("Function is tautology.");
			return minimalString;
		}

		for (Set<Mask> masks : minimalForms) {
			StringBuilder sb = new StringBuilder();
			for (Mask mask : masks) {
				for (int i = 0; i < variables.size(); i++) {
					if (mask.getValueAt(i) == 0) {
						sb.append(String.format("%s %s %s ", Constants.NOT_STRING, variables.get(i),
								Constants.AND_STRING));

					} else if (mask.getValueAt(i) == 1) {
						sb.append(String.format("%s %s ", variables.get(i), Constants.AND_STRING));
					}
				}

				// Removes last "and " string.
				sb.setLength(sb.length() - 4);

				sb.append(String.format("%s ", Constants.OR_STRING));
			}
			// Removes last "or " string.
			sb.setLength(sb.length() - 3);
			minimalString.add(sb.toString());
		}

		return minimalString;
	}

	/**
	 * Finds primary implicants.
	 * 
	 * @return Primary implicants.
	 */

	private Set<Mask> findPrimaryImplicants() {
		List<Set<Mask>> column = createFirstColumn();

		Set<Mask> primaryImplicants = new LinkedHashSet<>();

		while (!column.isEmpty()) {
			List<Set<Mask>> newColumn = new ArrayList<>();

			for (int i = 0; i < column.size() - 1; i++) {
				Set<Mask> firstGroup = column.get(i);
				Set<Mask> secondGroup = column.get(i + 1);

				Set<Mask> newGroup = null;

				for (Mask mask1 : firstGroup) {
					for (Mask mask2 : secondGroup) {
						Optional<Mask> combined = mask1.combineWith(mask2);
						if (combined.isPresent()) {
							mask1.setCombined(true);
							mask2.setCombined(true);

							if (newGroup == null) {
								newGroup = new LinkedHashSet<>();
							}
							newGroup.add(combined.get());
						}
					}
				}

				if (newGroup != null) {
					newColumn.add(newGroup);
				}
			}

			boolean finerLoggable = log.isLoggable(Level.FINER);

			if (finerLoggable) {
				log.finer("Table column:");
				log.finer("=================================");
			}
			
			Set<Mask> newPrimaryImplicants = new LinkedHashSet<>();
			
			for (int i = 0; i < column.size(); i++) {
				if (i > 0 && finerLoggable) {
					log.finer("-------------------------------");
				}
				for (Mask mask : column.get(i)) {
					if (finerLoggable) {
						log.finer(mask.toString());
					}
					// Adds new primary implicant.
					if (!mask.isCombined() && !mask.isDontCare()) {
						newPrimaryImplicants.add(mask);
					}
				}
			}

			if (finerLoggable) {
				log.finer("");
			}

			if (log.isLoggable(Level.FINEST)) {
				for (Mask mask : newPrimaryImplicants) {
					log.finest(String.format("Found primary implicant: %s", mask));
				}

				log.finest("");
			}

			primaryImplicants.addAll(newPrimaryImplicants);
			column = newColumn;
		}

		if (log.isLoggable(Level.FINE)) {
			log.fine("");
			log.fine("All primary implicants:");

			for (Mask mask : primaryImplicants) {
				log.fine(mask.toString());
			}
		}

		return primaryImplicants;
	}

	/**
	 * Creates first column in table.
	 * 
	 * @return First column in table.
	 */

	private List<Set<Mask>> createFirstColumn() {
		List<Set<Mask>> columnList = new ArrayList<>();

		@SuppressWarnings("unchecked")
		Set<Mask>[] onesCount = new LinkedHashSet[variables.size() + 1];

		for (int i = 0; i <= variables.size(); i++) {
			onesCount[i] = new LinkedHashSet<>();
		}

		for (int index : mintermSet) {
			Mask mintermMask = new Mask(index, variables.size(), false);
			onesCount[mintermMask.countOfOnes()].add(mintermMask);
		}

		for (int index : dontCareSet) {
			Mask dontCareMask = new Mask(index, variables.size(), true);
			onesCount[dontCareMask.countOfOnes()].add(dontCareMask);
		}

		for (Set<Mask> oneCount : onesCount) {
			columnList.add(oneCount);
		}

		return columnList;
	}

	/**
	 * Chooses only minimal primary implicants that can cover function values.
	 * 
	 * @param primCover
	 *            Prim cover.
	 * @return Minimal forms.
	 */

	private List<Set<Mask>> chooseMinimalCover(Set<Mask> primCover) {
		Mask[] implicants = primCover.toArray(new Mask[primCover.size()]);
		Integer[] minterms = mintermSet.toArray(new Integer[mintermSet.size()]);

		Map<Integer, Integer> mintermToColumnMap = new HashMap<>();
		for (int i = 0; i < minterms.length; i++) {
			Integer index = minterms[i];
			mintermToColumnMap.put(index, i);
		}

		boolean[][] table = buildCoverTable(implicants, minterms, mintermToColumnMap);
		boolean[] coveredMinterms = new boolean[minterms.length];

		Set<Mask> importantSet = selectImportantPrimaryImplicants(implicants, mintermToColumnMap, table,
				coveredMinterms);
		logImportantPrimaryImplicants(importantSet);

		List<Set<BitSet>> pFunction = buildPFunction(table, coveredMinterms);

		logPFunction(pFunction);
		Set<BitSet> minset = findMinimalSet(pFunction);

		logMinimalCovering(minset);
		List<Set<Mask>> minimalForms = new ArrayList<>();

		if (minset.isEmpty()) {
			minimalForms.add(importantSet);
			logMinimalForms(minimalForms);
			return minimalForms;
		}

		for (BitSet bs : minset) {
			Set<Mask> set = new LinkedHashSet<>(importantSet);
			bs.stream().forEach(i -> set.add(implicants[i]));
			minimalForms.add(set);
		}

		logMinimalForms(minimalForms);

		return minimalForms;
	}

	/**
	 * Logs minimal forms.
	 * 
	 * @param minimalForms
	 *            Minimal forms.
	 */

	private void logMinimalForms(List<Set<Mask>> minimalForms) {
		if (!log.isLoggable(Level.FINE)) {
			return;
		}

		log.fine("");
		log.fine("Minimal forms of function are:");

		for (int i = 0; i < minimalForms.size(); i++) {
			log.fine(String.format("%d. %s", i + 1, minimalForms.get(i)));
		}
	}

	/**
	 * Logs minimal covering.
	 * 
	 * @param minset
	 *            Set of minimal expressions.
	 */

	private void logMinimalCovering(Set<BitSet> minset) {
		if (!log.isLoggable(Level.FINER)) {
			return;
		}

		log.finer("");
		log.finer("Minimal coverings still needed:");
		log.finer(minset.toString());
	}

	/**
	 * Logs important primary implicants.
	 * 
	 * @param importantSet
	 *            Importan primary implicants.
	 */

	private void logImportantPrimaryImplicants(Set<Mask> importantSet) {
		if (!log.isLoggable(Level.FINE)) {
			return;
		}

		log.fine("");
		log.fine("Important primary implicants are:");
		log.fine(importantSet.toString());
	}

	/**
	 * Logs p-function.
	 * 
	 * @param pFunction
	 *            P-function.
	 */

	private void logPFunction(List<Set<BitSet>> pFunction) {
		if (!log.isLoggable(Level.FINER)) {
			return;
		}

		log.finer("");
		log.finer("p function is:");
		log.finer(pFunction.toString());
	}

	/**
	 * Finds minimal set.
	 * 
	 * @param pFunction
	 *            P-function.
	 * @return Minimal set.
	 */

	private Set<BitSet> findMinimalSet(List<Set<BitSet>> pFunction) {
		if (pFunction.isEmpty()) {
			return new LinkedHashSet<>();
		}

		Set<BitSet> minimalSet = new LinkedHashSet<>(pFunction.get(0));

		for (int i = 1; i < pFunction.size(); i++) {
			Set<BitSet> newSet = new LinkedHashSet<>();

			for (BitSet bitSet1 : minimalSet) {
				for (BitSet bitSet2 : pFunction.get(i)) {
					BitSet newBitSet = new BitSet(bitSet1.size());
					newBitSet.or(bitSet1);
					newBitSet.or(bitSet2);

					newSet.add(newBitSet);
				}
			}

			minimalSet = newSet;
		}

		logPFunctionConversion(minimalSet);

		int minCardinality = Integer.MAX_VALUE;
		for (BitSet bitSet : minimalSet) {
			minCardinality = Math.min(minCardinality, bitSet.cardinality());
		}

		// Removes all bitsets that contain more members than minimal.
		for (Iterator<BitSet> it = minimalSet.iterator(); it.hasNext();) {
			if (it.next().cardinality() > minCardinality) {
				it.remove();
			}
		}

		return minimalSet;
	}

	/**
	 * Logs p-function conversion.
	 * 
	 * @param minimalSet
	 *            Minimal set.
	 */

	private void logPFunctionConversion(Set<BitSet> minimalSet) {
		if (!log.isLoggable(Level.FINER)) {
			return;
		}

		log.finer("");
		log.finer("After conversion of p-function into sum of products:");
		log.finer(minimalSet.toString());
	}

	/**
	 * Builds p-function.
	 * 
	 * @param table
	 *            Table.
	 * @param coveredMinterms
	 *            Covered minterms.
	 * @return P-function.
	 */

	private List<Set<BitSet>> buildPFunction(boolean[][] table, boolean[] coveredMinterms) {
		List<Set<BitSet>> pFunction = new ArrayList<>();

		for (int i = 0; i < coveredMinterms.length; i++) {
			if (coveredMinterms[i]) {
				continue;
			}

			Set<BitSet> implicantBitSet = new LinkedHashSet<>();

			for (int j = 0; j < table.length; j++) {
				if (table[j][i]) {
					BitSet bitSet = new BitSet(table.length);
					bitSet.set(j);

					implicantBitSet.add(bitSet);
				}
			}

			pFunction.add(implicantBitSet);
		}

		return pFunction;
	}

	/**
	 * Selects important primary implicants.
	 * 
	 * @param implicants
	 *            Implicants.
	 * @param mintermToColumnMap
	 *            Minterm column map.
	 * @param table
	 *            Table.
	 * @param coveredMinterms
	 *            Covered minterms.
	 * @return Important primary implicants.
	 */

	private Set<Mask> selectImportantPrimaryImplicants(Mask[] implicants, Map<Integer, Integer> mintermToColumnMap,
			boolean[][] table, boolean[] coveredMinterms) {
		Set<Mask> importantPrimaryImplicants = new LinkedHashSet<>();

		for (int i = 0; i < coveredMinterms.length; i++) {
			int numberOfCovering = 0;
			int indexOfCovering = 0;
			for (int j = 0; j < implicants.length; j++) {
				if (table[j][i]) {
					indexOfCovering = j;
					numberOfCovering++;
				}
			}

			if (numberOfCovering == 1) {
				importantPrimaryImplicants.add(implicants[indexOfCovering]);
				for (int minterm : implicants[indexOfCovering].getIndexes()) {
					Integer index = mintermToColumnMap.get(minterm);
					if (index != null) {
						coveredMinterms[index] = true;
					}
				}
			}
		}

		return importantPrimaryImplicants;
	}

	/**
	 * Builds cover table.
	 * 
	 * @param implicants
	 *            Implicants.
	 * @param minterms
	 *            Minterms.
	 * @param mintermToColumnMap
	 *            Minterm to column map.
	 * @return Cover table.
	 */

	private boolean[][] buildCoverTable(Mask[] implicants, Integer[] minterms,
			Map<Integer, Integer> mintermToColumnMap) {
		boolean[][] coverTable = new boolean[implicants.length][minterms.length];

		for (int i = 0; i < implicants.length; i++) {
			for (int minterm : implicants[i].getIndexes()) {
				Integer index = mintermToColumnMap.get(minterm);
				if (index != null) {
					coverTable[i][index] = true;
				}
			}
		}

		return coverTable;
	}

}
