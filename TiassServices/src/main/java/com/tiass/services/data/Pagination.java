package com.tiass.services.data;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
 
/**
 * OBJECT FOR PAGINATION
 * 
 * @author JIMMY BAHOLE
 *
 */
@XmlRootElement public class Pagination extends MobileData { 
	private int						pageSet;
	private int						skip; 
	private List<PaginationFilter>	filters;  
	private List<PaginationSorting>	sorts;

	@XmlElement
	public int getPageSet() {
		return pageSet;
	} 
	public void setPageSet(int pageSet) {
		this.pageSet = pageSet;
	}

	@XmlElement
	public int getSkip() {
		return skip;
	}

	public void setSkip(int skip) {
		this.skip = skip;
	}

	/**
	 * the Fields to filter,
	 */
	@XmlElement
	public List<PaginationFilter> getFilters() {
		return filters;
	}

	public void setFilters(List<PaginationFilter> filters) {
		this.filters = filters;
	}

	/**
	 * the Fields to Sort,
	 */
	@XmlElement
	public List<PaginationSorting> getSorts() {
		return sorts;
	}

	public void setSorts(List<PaginationSorting> sorts) {
		this.sorts = sorts;
	}

}
