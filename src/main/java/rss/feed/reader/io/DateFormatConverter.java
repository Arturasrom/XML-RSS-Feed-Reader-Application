package rss.feed.reader.io;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateFormatConverter {

	private String dateFromFeed;
	private Date rawDate;

	public DateFormatConverter(String dateFromFeed) {
		this.dateFromFeed = dateFromFeed;
	}

	public DateFormatConverter(Date rawDate) {
		this.rawDate = rawDate;
	}

	public Date getDate() {

		return convertDate();
	}

	public String getFormatedDate() {

		return formatDate();
	}

	private Date convertDate() {
		try {
			DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.UK);
			dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

			return dateFormat.parse(dateFromFeed);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	private String formatDate() {

		DateFormat dateFormat = new SimpleDateFormat("yyyy.MMM.dd HH:mm:ss", Locale.UK);
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

		return dateFormat.format(rawDate);
	}
}
