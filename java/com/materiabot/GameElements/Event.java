package com.materiabot.GameElements;
import java.time.ZonedDateTime;
import java.util.LinkedList;

public class Event {
	public static class EventLink{
		public long eventId, linkId, order;
		public String name;
		public String url;
		
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
		public long getOrder() {
			return order;
		}
		public void setOrder(long order) {
			this.order = order;
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
	}
	
	public long id;
	public String name;
	public ZonedDateTime startDate, endDate;
	public LinkedList<EventLink> links = new LinkedList<EventLink>();
	
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

	public ZonedDateTime getStartDate() {
		return startDate;
	}
	public void setStartDate(ZonedDateTime startDate) {
		this.startDate = startDate;
	}
	public ZonedDateTime getEndDate() {
		return endDate;
	}
	public void setEndDate(ZonedDateTime endDate) {
		this.endDate = endDate;
	}
	public LinkedList<EventLink> getLinks() {
		return links;
	}
}