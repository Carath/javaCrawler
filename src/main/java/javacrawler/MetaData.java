package main.java.javacrawler;

import java.util.ArrayList;

public class MetaData
{
	private String dataTitle;
	private ArrayList<String> dataList;


	public MetaData(String dataTitle) {
		this.dataTitle = dataTitle;
		this.dataList = new ArrayList<String>();
	}

	public String getDataTitle() {
		return this.dataTitle;
	}

	public ArrayList<String> getDataList() {
		return this.dataList;
	}

	public void print()
	{
		System.out.printf("\n> MetaData:\n\n- Data title: %s\n- Data list (length = %d):\n\n",
			this.dataTitle, this.dataList.size());

		for (String data : this.dataList) {
			System.out.println(data);
		}
	}

	// Simplify with indexOf ?
	public static MetaData searchMetaData(String content, SearchQuery query)
	{
		MetaData metaData = new MetaData(query.getQueryName());

		String startTag = query.getStartTag(), endTag = query.getEndTag();
		int contentLength = content.length(), startTagLength = startTag.length(), endTagLength = endTag.length();
		int endBound = contentLength - endTagLength, startIndex = 0;

		while (startIndex < contentLength)
		{
			// Seeking the first delimiting tag:
			if (! content.startsWith(startTag, startIndex)) {
				++startIndex;
				continue;
			}

			int endIndex = startIndex + startTagLength;

			// Seeking the ending delimiting tag:
			while (endIndex <= endBound && ! content.startsWith(endTag, endIndex)) {
				++endIndex;
			}

			if (endIndex > endBound) {
				System.out.printf("\nEnding tag '%s' not found! Stopping the search.\n", endTag);
				return metaData;
			}

			// Adding the new data to the data list, if not already found:
			int offset = startIndex + startTagLength;
			String newData = query.getPrefix() + content.substring(offset, endIndex) + query.getSuffix();

			if (! metaData.dataList.contains(newData)) {
				metaData.dataList.add(newData);
				// System.out.println(newData);
			}

			startIndex = endIndex + endTagLength;
		}

		return metaData;
	}
}
