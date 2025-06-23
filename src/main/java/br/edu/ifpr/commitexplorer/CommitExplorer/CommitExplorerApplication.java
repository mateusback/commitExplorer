package br.edu.ifpr.commitexplorer.CommitExplorer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class CommitExplorerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommitExplorerApplication.class, args);
	}

}
