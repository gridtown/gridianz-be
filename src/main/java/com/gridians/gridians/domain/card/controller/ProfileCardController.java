package com.gridians.gridians.domain.card.controller;

import com.gridians.gridians.domain.card.dto.ProfileCardDto;

import com.gridians.gridians.domain.card.entity.ProfileCard;
import com.gridians.gridians.domain.card.service.ProfileCardService;
import com.gridians.gridians.global.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@RequestMapping("/cards")
@RestController
@RequiredArgsConstructor
public class ProfileCardController {

	private final JwtUtils jwtUtils;
	private final ProfileCardService profileCardService;

	@Value("${custom.path.skill}")
	private String path;
	@Value("${custom.extension}")
	private String extension;

	@GetMapping("/{id}")
	public ResponseEntity<?> read(@PathVariable Long id) {
		return new ResponseEntity<>(profileCardService.readProfileCard(id), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<?> cardList(int page, int size) {
		log.info("CardList Read");
		return new ResponseEntity<>(profileCardService.allProfileCardList(page, size), HttpStatus.OK);
	}

	@GetMapping("/favorites")
	public ResponseEntity<?> favoriteCardList(@RequestHeader(name = "Authorization") String token, int page, int size) {
		String email = jwtUtils.getUserEmailFromToken(token);
		log.info("Favorite CardList Read");
		return new ResponseEntity<>(profileCardService.favoriteCardList(email, page, size), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestHeader(name = "Authorization") String token) {
		String email = jwtUtils.getUserEmailFromToken(token);
		ProfileCard pc = profileCardService.createProfileCard(email);
		log.info("[" + pc.getUser().getNickname() + "] Create Profile Card");
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{id}")
	public ResponseEntity<?> input(@PathVariable Long id, @RequestBody @Valid ProfileCardDto.Request request) throws IOException {
		profileCardService.input(id, request);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid ProfileCardDto.Request request) throws IOException {
		profileCardService.input(id, request);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		ProfileCard pc = profileCardService.deleteProfileCard(id);
		log.info("[" + pc.getUser().getNickname() + "] Delete Profile Card");
		return ResponseEntity.ok().build();
	}

	@GetMapping("/images/skills/{skill}")
	public ResponseEntity<Resource> getImage(@PathVariable String skill) throws IOException {
		// 실제 주소가 되어야 함

		String file = this.path + skill + extension;
		Path path = new File(file).toPath();
		FileSystemResource resource = new FileSystemResource(path);
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(path))).body(resource);
	}
}