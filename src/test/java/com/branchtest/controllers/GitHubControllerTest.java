package com.branchtest.controllers;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.http.HttpRequest;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.branchtest.dto.GitHubRepo;
import com.branchtest.dto.GitHubUser;
import com.branchtest.error.RestResponseEntityExceptionHandler;
import com.branchtest.feign.GitHubClient;
import com.branchtest.services.GitHubUserServiceImpl;
import com.branchtest.services.GithubUserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.FeignException;
import feign.Request;
import feign.Request.HttpMethod;

@SpringBootTest
@AutoConfigureMockMvc
public class GitHubControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private GitHubClient githubClient;
	
	
	//@BeforeEach
	public void setup() {
		GithubUserService userService = new GitHubUserServiceImpl(githubClient, new ObjectMapper(), 100);
		mockMvc = MockMvcBuilders.standaloneSetup(new GitHubController(userService)).build();
	}
	
	@Test
	public void getUserHappy() throws Exception {
		GitHubUser user = new GitHubUser();
		user.setLogin("aLogin");
		user.setName("aName");
		user.setLocation("aLocation");
		user.setEmail("anEmail");
		user.setAvatarUrl("anAvatar");
		user.setCreatedAt("2001-01-01T13:20:54Z");
		
		GitHubRepo repo1 = new GitHubRepo();
		repo1.setFullName("aFirstFullName");
		repo1.setName("aFirstName");
		
		GitHubRepo repo2 = new GitHubRepo();
		repo2.setFullName("aSecondFullName");
		repo2.setName("aSecondName");
		
		when(githubClient.getUser("aLogin")).thenReturn(user);
		when(githubClient.getRepos("aLogin", 100, 1, "1970-01-01T00:00:00Z")).thenReturn(Lists.list(repo1, repo2));
		
		this.mockMvc.perform(get("/github/aLogin"))
		.andDo(print()).andExpect(status().isOk())
		.andExpect(jsonPath("user_name").value("aLogin"))
		.andExpect(jsonPath("avatar").value("anAvatar"))
		.andExpect(jsonPath("geo_location").value("aLocation"))
		.andExpect(jsonPath("email").value("anEmail"))
		.andExpect(jsonPath("created_at").value("2001-01-01 13:20:54"))
		.andExpect(jsonPath("repos[0].name").value("aFirstName"))
		.andExpect(jsonPath("repos[0].url").value("https://github.com/aFirstFullName"))
		.andExpect(jsonPath("repos[1].name").value("aSecondName"))
		.andExpect(jsonPath("repos[1].url").value("https://github.com/aSecondFullName"));
	}
	
	@Test
	public void getUserNotFound() throws Exception{
		
		doThrow(new FeignException.NotFound("clientDidNotFind", Request.create(HttpMethod.GET, "aUrl", new HashMap<>(), null,
                Charset.defaultCharset(), null), null, null )).when(githubClient).getUser("aLogin");
		
		this.mockMvc.perform(get("/github/aLogin"))
		.andDo(print()).andExpect(status().isNotFound())
		.andExpect(jsonPath("resourceId").value("aLogin"))
		.andExpect(jsonPath("resourceType").value("gitHubUser"))
		.andExpect(jsonPath("url").value("http://localhost/github/aLogin"))
		.andExpect(jsonPath("errorMessage").value("Resource not found"));
	}
	
	@Test
	public void getUserThrottleReached() throws Exception {
		doThrow(new FeignException.Forbidden("clientDidNotFind", Request.create(HttpMethod.GET, "aUrl", new HashMap<>(), null,
                Charset.defaultCharset(), null), null, null )).when(githubClient).getUser("aLogin");
		
		this.mockMvc.perform(get("/github/aLogin"))
		.andDo(print()).andExpect(status().isForbidden())
		.andExpect(jsonPath("resourceId").value("aLogin"))
		.andExpect(jsonPath("resourceType").value("gitHubUser"))
		.andExpect(jsonPath("url").value("http://localhost/github/aLogin"))
		.andExpect(jsonPath("errorMessage").value("Temporary lookup throttle limit reached."));		
	}
}
