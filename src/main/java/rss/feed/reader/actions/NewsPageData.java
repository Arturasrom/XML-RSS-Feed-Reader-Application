package rss.feed.reader.actions;

import rss.feed.reader.io.FeedExtractor;
import rss.feed.reader.rss_feeds.RssFeed;

public class NewsPageData {

	private final String url;
	private final String feedName;

	public NewsPageData(String feedURL, String feedName) {
		this.url = feedURL;
		this.feedName = feedName;
	}

	public RssFeed doActions() {

		return getDataFromNewsPageRSS();
	}

	private RssFeed getDataFromNewsPageRSS() {

		return new FeedExtractor(url, feedName).getDataFromRSSFeed();

	}
}
