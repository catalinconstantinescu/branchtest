package com.branchtest.pojo;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({ "user_name", "display_name", "avatar", "geo_location", "email", "url", "created_at" })
public class GitHubUserPojo {
	@JsonProperty(value="user_name")
	private String login;
	@JsonProperty(value="display_name")
	private String name;
	@JsonProperty(value="avatar")
	private String avatarUrl;
	@JsonProperty(value="geo_location")
	private String location;
	private String email;
	@JsonIgnore
	private Instant createdAt;
	@JsonIgnore
	private boolean completeRepoList = false;
	@JsonIgnore
	private String lastRepoCreatedAtStr;
	private List<GitHubRepoPojo> repos = new ArrayList<>();
	
	@JsonProperty(value="created_at")
	public String getCreated() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Z"));
		return formatter.format(createdAt);
	}
}
