package com.branchtest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GitHubRepo {
	String name;
	@JsonProperty(value="full_name")
	String fullName;
	@JsonProperty(value="updated_at")
	String updatedAtStr;
}
