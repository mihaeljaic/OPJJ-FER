package hr.fer.zemris.java.custom.scripting.nodes;

import java.io.IOException;

/**
 * Root node in parser tree. Representing whole document.
 * 
 * @author Mihael Jaić
 *
 */

public class DocumentNode extends Node {
	@Override
	public String toString() {
		return "";
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DocumentNode)) {
			return false;
		}

		DocumentNode other = (DocumentNode) obj;

		if (numberOfChildren() != other.numberOfChildren()) {
			return false;
		}

		for (int i = 0; i < numberOfChildren(); i++) {
			if (!(getChild(i).equals(other.getChild(i)))) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void accept(INodeVisitor visitor) throws IOException {
		visitor.visitDocumentNode(this);
	}


}
