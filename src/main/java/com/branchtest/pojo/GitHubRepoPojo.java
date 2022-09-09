package com.branchtest.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class GitHubRepoPojo {
	
	private String name;
	@JsonIgnore
	private String fullName;
	@JsonIgnore
	private String updatedAtStr;
	
	public String getUrl() {
		return "https://github.com/" + this.fullName;
	}
}
