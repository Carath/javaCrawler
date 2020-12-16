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


	public static MetaData searchMetaData(String content, SearchQuery query)
	{
		MetaData metaData = new MetaData(query.getQueryName());

		String startTag = query.getStartTag(), endTag = query.getEndTag();
		int startTagLength = startTag.length(), endTagLength = endTag.length();

		if (startTagLength == 0 || endTagLength == 0) {
			System.out.printf("\nOne tag is empty ('%s', '%s'). Stopping the search.\n", startTag, endTag);
			return metaData;
		}

		int startIndex = 0;

		while (true)
		{
			int startTagIndex = content.indexOf(startTag, startIndex);

			if (startTagIndex == -1) {
				// System.out.printf("\nEnd of file. Stopping the search.\n", endTag);
				break;
			}

			int dataIndex = startTagIndex + startTagLength;
			int endTagIndex = content.indexOf(endTag, dataIndex);

			if (endTagIndex == -1) {
				// System.out.printf("\nEnding tag '%s' not found. Stopping the search.\n", endTag);
				break;
			}

			// Adding the new data to the data list, if not already found:
			String newData = query.getPrefix() + content.substring(dataIndex, endTagIndex) + query.getSuffix();

			if (! metaData.dataList.contains(newData)) {
				metaData.dataList.add(newData);
				// System.out.println(newData);
			}

			startIndex = endTagIndex + endTagLength;
		}

		return metaData;
	}


	public static void main(String[] args)
	{
		String content = "azertyhelloworldpoiuyhellowxcvbworld";
		System.out.println("\nContent: " + content);
		SearchQuery query = new SearchQuery("test", "hello", "world", "<<hello_", "_world>>");
		query.print();
		MetaData metaData = searchMetaData(content, query);
		metaData.print();
	}
}
