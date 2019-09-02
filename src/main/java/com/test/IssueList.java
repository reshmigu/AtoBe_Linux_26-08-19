package com.test;

import java.util.List;

public class IssueList {
	private String expand;
	private int startAt;
	private int maxResults;
	private int total;
	private List<Issues> issues;
	public String getExpand() {
		return expand;
	}
	public void setExpand(String expand) {
		this.expand = expand;
	}
	public int getStartAt() {
		return startAt;
	}
	public void setStartAt(int startAt) {
		this.startAt = startAt;
	}
	public int getMaxResults() {
		return maxResults;
	}
	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<Issues> getIssues() {
		return issues;
	}
	public void setIssues(List<Issues> issues) {
		this.issues = issues;
	}
	@Override
	public String toString() {
		return "IssueList [expand=" + expand + ", startAt=" + startAt + ", maxResults=" + maxResults + ", total="
				+ total + ", issues=" + issues + "]";
	}
	
}
