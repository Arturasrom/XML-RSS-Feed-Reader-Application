package rss.feed.reader.actions;

import rss.feed.reader.rss_feeds.IRssFeed;

public class Actions {
	private final String feedURL;
	private final String feedName;

	public Actions(String feedURL, String feedName) {
		this.feedURL = feedURL;
		this.feedName = feedName;
	}

	public IRssFeed startApp() {

		return doActions();
	}

	private IRssFeed doActions() {

		IRssFeed rssFeed = new NewsPageData(feedURL, feedName).doActions();

		return rssFeed;
	}
}
