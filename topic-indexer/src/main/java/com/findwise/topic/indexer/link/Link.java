/*
 * Link data structure
 * 
 * Represents a link from one Wikipedia concept to another.
 * Is also used to filter out unwanted links with the isCorrect method
 * 
 */

package com.findwise.topic.indexer.link;

public class Link {
	private String from = "";
	private String to = "";

	public Link(String from, String to) {
		this.from = from;
		this.to = to;
	}

	public String getTo() {
		return to;
	}

	public String getFrom() {
		return from;
	}

	public String toString() {
		return from + "->" + to;
	}

	public boolean isCorrect() {

		// No linking to self
		if (to.equals(from))
			return false;

		// No empty link
		if (to == "" || from == "")
			return false;

		// Not starts with a year
		if (to.matches("[0-9][0-9][0-9][0-9].*$")
				|| from.matches("[0-9][0-9][0-9][0-9].*$"))
			return false;

		// Not start with a month
		String monthsPrefixes[] = { "January", "February", "March", "April",
				"May", "June", "July", "August", "September", "October",
				"November", "December" };

		for (String prefix : monthsPrefixes) {
			if ((to.startsWith(prefix) && to.matches(".*[0-9].*"))
					|| (from.startsWith(prefix) && from.matches(".*[0-9].*")))
				return false;
		}

		// Not any of these prefixes
		String wikipediaPrefixes[] = { "File:", "Wikt:", "Wiktionary:",
				"Wikipedia:", "User:", "Special:", "Template:", "Wikisource:",
				"Book:", "MediaWiki", "Media:", "Module:", "Portal:", "Help:",
				"Wikilivres:", "H:", "S:", "P:", "V:", "MOS:", "CAT:",
				"List of ", "Lists of ", "Index of ", "Category:" };

		for (String prefix : wikipediaPrefixes) {
			if (to.startsWith(prefix) || from.startsWith(prefix))
				return false;
		}
		return true;
	}
}
