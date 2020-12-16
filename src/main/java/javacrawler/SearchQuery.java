package main.java.javacrawler;

public class SearchQuery
{
	private final String queryName;
	private final String startTag;
	private final String endTag;
	private final String prefix;
	private final String suffix;

	public SearchQuery(String queryName, String startTag, String endTag, String prefix, String suffix) {
		this.queryName = queryName;
		this.startTag = startTag;
		this.endTag = endTag;
		this.prefix = prefix;
		this.suffix = suffix;
	}

	public String getQueryName() {
		return this.queryName;
	}

	public String getStartTag() {
		return this.startTag;
	}

	public String getEndTag() {
		return this.endTag;
	}

	public String getPrefix() {
		return this.prefix;
	}

	public String getSuffix() {
		return this.suffix;
	}

	public void print() {
		System.out.printf("\n> SearchQuery:\n\n- Query name: %s\n- Start tag: %s\n- End tag: %s\n- Prefix: %s\n" +
			"- Suffix: %s\n\n", this.queryName, this.startTag, this.endTag, this.prefix, this.suffix);
	}
}
