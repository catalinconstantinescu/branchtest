package com.branchtest.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.branchtest.dto.GitHubRepo;
import com.branchtest.dto.GitHubUser;
import com.branchtest.error.ResourceSerializationException;
import com.branchtest.feign.GitHubClient;
import com.branchtest.pojo.GitHubRepoPojo;
import com.branchtest.pojo.GitHubUserPojo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GitHubUserServiceImpl implements GithubUserService {
	
	private GitHubClient githubClient;
	
	private ObjectMapper objectMapper;
	
	int reposPerPage;
	
	private ModelMapper modelMapper = new ModelMapper();
	
	public GitHubUserServiceImpl(@Autowired GitHubClient githubClient, 
			@Autowired ObjectMapper objectMapper, @Value("${github.user.repos.pagesize:100}") int reposPerPage) {
		this.githubClient = githubClient;
		this.objectMapper = objectMapper;
		this.reposPerPage = reposPerPage;
	}
	
	@Override
	@Cacheable("githubUsers")
	public String getUserAsString(String userId, String reposUpdatedAfter){
		log.info("getting user for {}", userId);
		GitHubUser userAtSource = githubClient.getUser(userId);
		
		List<GitHubRepo> reposAtSource = getReposForUser(userId, reposPerPage, 1, reposUpdatedAfter);
		GitHubUserPojo user = modelMapper.map(userAtSource, GitHubUserPojo.class);
		List<GitHubRepoPojo> repos = reposAtSource.stream().map(r -> modelMapper.map(r, GitHubRepoPojo.class)).toList();
		user.setRepos(repos);
		try {
			return objectMapper.writeValueAsString(user);	
		} catch (JsonProcessingException e) {
			log.error("GitHub User " + userId + " cannot be serialized. Message: " + e.getMessage(), e);
			throw new ResourceSerializationException(userId, "gitHubUser", e.getMessage(), e);
		}
	}
	
	protected List<GitHubRepo> getReposForUser(String userId, int pageSize, int fromPage, String fromTimestamp) {
		List<GitHubRepo> repos = 
				githubClient.getRepos(userId, pageSize, fromPage, fromTimestamp==null?"1970-01-01T00:00:00Z":fromTimestamp);
		if (repos.size()==pageSize) {
			repos.addAll(getReposForUser(userId, pageSize, fromPage+1, fromTimestamp));
		}
		return repos;
	}

}
