package com.branchtest.dto;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties
public class GitHubUser {
	private String login;
	private String name;
	@JsonProperty(value="avatar_url")
	private String avatarUrl;
	private String location;
	private String email;
	@JsonIgnore
	private Instant createdAt;
	

	@JsonProperty(value="created_at")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss'Z'")
	public void setCreatedAt(String createdAt) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneId.of("Z"));
	    this.createdAt = ZonedDateTime.parse(createdAt,formatter).toInstant();
	}
}
