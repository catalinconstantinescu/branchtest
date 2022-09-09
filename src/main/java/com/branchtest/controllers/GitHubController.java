package com.branchtest.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.branchtest.dto.GitHubRepo;
import com.branchtest.dto.GitHubUser;
import com.branchtest.error.ResourceLookupThrottleException;
import com.branchtest.error.ResourceNotFoundException;
import com.branchtest.feign.GitHubClient;
import com.branchtest.pojo.GitHubRepoPojo;
import com.branchtest.pojo.GitHubUserPojo;
import com.branchtest.services.GithubUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value="github")
@Slf4j
public class GitHubController {
	
	GithubUserService userService;
	
	public GitHubController(@Autowired GithubUserService userService) {
		this.userService = userService;
	}
	
	@RequestMapping(value="/{userId}")
	public ResponseEntity<String> getUser(@PathVariable String userId, 
			@RequestParam(name="after", required=false, defaultValue="1970-01-01T00:00:00Z") String reposUpdatedAfter) throws JsonProcessingException {
		try {
			return new ResponseEntity(userService.getUserAsString(userId, reposUpdatedAfter), HttpStatus.OK);
		} catch (FeignException.NotFound e) {
			throw new ResourceNotFoundException(userId, "gitHubUser", e.getMessage(), e);
		} catch (FeignException.Forbidden e) {
			throw new ResourceLookupThrottleException(userId, "gitHubUser", e.getMessage(), e);
		}

	}

}