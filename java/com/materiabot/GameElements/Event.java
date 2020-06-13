package com.materiabot.GameElements;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

public class Event {
	public static class EventLink{
		private long eventId, linkId;
		private String text, type;
		private String url;
		
		public long getEventId() {
			return eventId;
		}
		public void setEventId(long eventId) {
			this.eventId = eventId;
		}
		public long getLinkId() {
			return linkId;
		}
		public void setLinkId(long linkId) {
			this.linkId = linkId;
		}

		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
	}
	
	public long id;
	public String name, region;
	public List<String> units = new LinkedList<String>();
	public Timestamp startDate, endDate;
	public List<EventLink> links = new LinkedList<EventLink>();
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}

	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	public Timestamp getEndDate() {
		return endDate;
	}
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
	public List<String> getUnits() {
		return units;
	}
	public List<EventLink> getLinks() {
		return links;
	}
}