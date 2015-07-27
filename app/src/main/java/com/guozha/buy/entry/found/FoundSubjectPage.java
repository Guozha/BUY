package com.guozha.buy.entry.found;

import java.util.List;

public class FoundSubjectPage {

	private int totalCount;
	private int pageCount;
	
	private List<FoundSubject> subjectList;

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public List<FoundSubject> getSubjectList() {
		return subjectList;
	}

	public void setSubjectList(List<FoundSubject> subjectList) {
		this.subjectList = subjectList;
	}
}
