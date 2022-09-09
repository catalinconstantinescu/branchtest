package com.branchtest;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.branchtest.controllers.GitHubController;
import com.branchtest.feign.GitHubClient;

@SpringBootTest
class BranchtestApplicationTests {
	
	@Autowired
	private GitHubController githubControler; 
	
	@Autowired
	private GitHubClient githubClient;

	@Test
	void contextLoads() throws Exception {
		assertThat(githubControler).isNotNull();
		assertThat(githubClient).isNotNull();
	}

}
