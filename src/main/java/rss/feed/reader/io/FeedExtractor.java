package rss.feed.reader.io;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

import rss.feed.reader.rss_feeds.Article;
import rss.feed.reader.rss_feeds.RssFeed;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class FeedExtractor {

	private final URL url;
	private final String feedName;

	public FeedExtractor(String feedURL, String feedName) {
		try {
			this.url = new URL(feedURL);
			this.feedName = feedName;
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public RssFeed getDataFromRSSFeed() {

		return getDataFromXML();
	}

	private RssFeed getDataFromXML() {
		final String ITEM = "item";
		final String TITLE = "title";
		final String LINK = "link";
		final String PUB_DATE = "pubDate";

		try {
			String feedTitle = "";
			String feedLink = "";
			String dateFromFeed = "Mon, 01 Jan 2000 00:00:01 +0300";

			// StAX -- https://habr.com/post/339716/ ---- http://java-course.ru/begin/xml/
			// --- https://www.ibm.com/developerworks/ru/library/x-stax1/

			// Creating new XMLInputFactory
			XMLInputFactory factory = XMLInputFactory.newInstance();

			// Creating event
			final XMLEventReader eventReader = factory.createXMLEventReader(url.openStream(), "UTF-8");

			// Creating feed Object
			RssFeed rssFeed = new RssFeed(feedName, url);

			XMLEvent event;

			// Getting Publication date
			while (eventReader.hasNext()) {

				event = eventReader.nextEvent();

				if (event.isStartElement() && event.asStartElement().getName().getLocalPart().equals(PUB_DATE)) {
					dateFromFeed = getCharacterData(event, eventReader);
					break;
				}
			}

			// Setting Publication date
			rssFeed.setUpdateTime(getDate(dateFromFeed));

			// Getting RSS <item> data
			while (eventReader.hasNext()) {

				event = eventReader.nextEvent();

				if (event.isStartElement() && event.asStartElement().getName().getLocalPart().equals(ITEM)) {

					while (true) {

						if (event.isStartElement() && event.asStartElement().getName().getLocalPart().equals(TITLE)) {
							feedTitle = getCharacterData(event, eventReader);
						}

						if (event.isStartElement() && event.asStartElement().getName().getLocalPart().equals(LINK)) {
							feedLink = getCharacterData(event, eventReader);
						}

						if (event.isEndElement() && event.asEndElement().getName().getLocalPart().equals(ITEM)) {
							break;
						}
						event = eventReader.nextEvent();
					}

					// Setting RSS data (Article Title & Link to the Object
					rssFeed.getArticles().add(new Article(feedTitle, feedLink));
				}
			}

			eventReader.close();

			return rssFeed;

		} catch (XMLStreamException e) {
			return null;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private Date getDate(String dateFromFeed) {

		return new DateFormatConverter(dateFromFeed).getDate();
	}

	private String getCharacterData(XMLEvent event, XMLEventReader eventReader) throws XMLStreamException {

		String result = "";

		event = eventReader.nextEvent();

		if (event instanceof Characters) {
			result = event.asCharacters().getData();
		}
		return result;
	}
}
