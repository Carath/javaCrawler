package main.java.javacrawler;

import java.util.ArrayList;


public class Crawler
{
	public static final Boolean ADAPTATIVE_WAITING = true;
	public static final double COOLDOWN = 1.0; // in seconds.

	public static final SearchQuery LANG_QUERY = new SearchQuery("lang", "lang=\"", "\"", "", "");
	public static final SearchQuery PAGE_TITLE_QUERY = new SearchQuery("pageTitle", "<title>", "</title>", "", "");
	public static final SearchQuery LINKS_QUERY = new SearchQuery("links", "\"http", "\"", "http", "");


	// Waits for the given amount of time. Values in seconds.
	public static void waiting(double cooldown)
	{
		double timeToWait = Math.max(0, cooldown);
		System.out.printf("Waiting for: %.3f s\n\n", timeToWait);
		long usedCooldown = (long) (timeToWait * 1000); // in milliseconds

		try {
			Thread.sleep(usedCooldown);
		}
		catch (InterruptedException e) {
			System.out.println("An error happened while waiting...");
			e.printStackTrace();
		}
	}


	public static String cleanupContent(String content)
	{
		return content.replace('\'', '\"');
	}


	// Removes things like "\/" or ending '/' to avoid redundancies.
	public static String cleanupUrl(String url)
	{
		url = url.replace("\\", "");

		if (url.charAt(url.length() - 1) == '/') {
			url = url.substring(0, url.length() - 1);
		}

		return url;
	}


	// Can be sometimes too strict, but better than nothing.
	public static Boolean isFile(String url)
	{
		int offset_1 = url.indexOf("//");
		int offset_2 = url.indexOf("/", offset_1 + 2);
		int offset_3 = url.indexOf(".", offset_2 + 1);
		return offset_1 != -1 && offset_2 != -1 && offset_3 != -1;
	}


	public static ArrayList<PageData> crawl(String url, int maxWebPagesToVisit, ArrayList<SearchQuery> userQueryList)
	{
		System.out.printf("\nCrawling starts:\n\n");

		ArrayList<SearchQuery> queryList = new ArrayList<>();
		queryList.add(LANG_QUERY);
		queryList.add(PAGE_TITLE_QUERY);
		queryList.add(LINKS_QUERY);
		queryList.addAll(userQueryList);

		// Indexes depend on the query order above!
		int langQueryIndex = 0, pageTitleQueryIndex = 1, linksQueryIndex = 2;

		ArrayList<PageData> pageDataList = new ArrayList<PageData>();

		ArrayList<String> urlArray = new ArrayList<String>();
		urlArray.add(cleanupUrl(url));

		int visitedPageIndex = 0, urlIndex = 0;
		for (; visitedPageIndex < maxWebPagesToVisit && urlIndex < urlArray.size(); ++urlIndex)
		{
			long startTime = System.nanoTime();

			String currentUrl = urlArray.get(urlIndex);

			if (isFile(currentUrl)) {
				System.out.printf("Not crawling the file: '%s'\n\n", currentUrl);
				continue;
			}

			System.out.printf("> Searching the page (rank %d): '%s'\n", urlIndex, currentUrl);
			PageData pageData = new PageData(currentUrl); // fetching the page content!
			// System.out.println(pageData.getHtmlContent());

			if (! pageData.getUrlValidity()) { // Invalid url. Don't continue on 404!
				continue;
			}

			String cleanContent = cleanupContent(pageData.getHtmlContent());

			// Doing all the searches:
			for (int i = 0; i < queryList.size(); ++i)
			{
				MetaData metaData = MetaData.searchMetaData(cleanContent, queryList.get(i));
				pageData.getMetaDataList().add(metaData);
			}

			ArrayList<String> langDataList = pageData.getMetaDataList().get(langQueryIndex).getDataList();
			ArrayList<String> pageTitleDataList = pageData.getMetaDataList().get(pageTitleQueryIndex).getDataList();
			ArrayList<String> foundUrlList = pageData.getMetaDataList().get(linksQueryIndex).getDataList();

			// Remark only the previous fields are checked to see if the page is relevant, user ones are ignored.
			// Furthermore, maybe the lang or pageTitle whole data need not to be saved (?)

			if (langDataList.size() > 0 || pageTitleDataList.size() > 0 || foundUrlList.size() > 0) // page contains relevant data.
			{
				String lang = langDataList.size() > 0 ? langDataList.get(0) : "";
				String pageTitle = pageTitleDataList.size() > 0 ? pageTitleDataList.get(0) : "";

				pageData.setMissingFields(lang, pageTitle);
				pageDataList.add(pageData);

				// Cleaning all found urls, and storing the new ones in 'urlArray':
				for (int i = 0; i < foundUrlList.size(); ++i) {
					String foundUrl = cleanupUrl(foundUrlList.get(i));
					foundUrlList.set(i, foundUrl);

					if (! urlArray.contains(foundUrl)) {
						urlArray.add(foundUrl);
					}
				}
			}

			++visitedPageIndex;

			if (maxWebPagesToVisit > 1) {
				long endTime = System.nanoTime();
				double elapsedTime = (endTime - startTime) / 1e9;
				// System.out.printf("\nElapsed time: %.3f s\n", elapsedTime);
				double cooldown = ADAPTATIVE_WAITING ? COOLDOWN - elapsedTime : COOLDOWN;
				waiting(cooldown);
			}
		}

		System.out.printf("\nCrawling done. Number of web pages visited: %d.\n", visitedPageIndex);
		return pageDataList;
	}


	public static void main(String[] args) {

		// String givenUrl = "http://www.example.com/";
		// String givenUrl = "http://www.example.com/test"; // 404 test
		String givenUrl = "https://en.wikipedia.org/wiki/Main_Page";

		//////////////////////////////////////////////////////////////////
		// Printing the page content:

		// String content = PageData.getURLcontent(givenUrl);
		// System.out.println(content);

		//////////////////////////////////////////////////////////////////
		// Cleanup tests:

		String someUrl = "https:\\/\\/en.wikipedia.org\\/wiki\\/Main_Page/";
		System.out.println("\n" + cleanupUrl(someUrl));

		//////////////////////////////////////////////////////////////////
		// Crawling test:

		// int maxWebPagesToVisit = 3;

		// ArrayList<SearchQuery> userQueryList = new ArrayList<SearchQuery>();
		// userQueryList.add(new SearchQuery("title", "title=\"", "\"", "", "")); // optional, for wikipedia.

		// ArrayList<PageData> pageDataList = crawl(givenUrl, maxWebPagesToVisit, userQueryList);

		// for (PageData pageData : pageDataList) {
		// 	pageData.print();
		// }

		//////////////////////////////////////////////////////////////////
	}
}
