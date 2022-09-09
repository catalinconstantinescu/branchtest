package com.branchtest.feign;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.branchtest.dto.GitHubRepo;
import com.branchtest.dto.GitHubUser;

@FeignClient(name="githubApi", url="${github.api.protoandhost:https://api.github.com}")
public interface GitHubClient {
	@RequestMapping(method = RequestMethod.GET, value = "/users/{userId}")
	GitHubUser getUser(@PathVariable String userId);
	
	@RequestMapping(method = RequestMethod.GET, 
			value = "/users/{userId}/repos?per_page={pageSize}&page={fromPage}&since={since}&sort=updated&direction=asc")
	List<GitHubRepo> getRepos(@PathVariable String userId, @RequestParam int pageSize, @RequestParam int fromPage, @RequestParam String since);
}