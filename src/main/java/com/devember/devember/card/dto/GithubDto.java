package com.devember.devember.card.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class GithubDto {

	private String name;
	private String login;
	private Long id;
	private String githubUrl;
	private Long followers;
	private Long following;
	private String location;
	private String company;
	private String imageUrl;
	private LocalDate recentCommitAt;
	private String recentCommitMessage;
}

