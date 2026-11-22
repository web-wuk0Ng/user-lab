package com.example.userlab;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestController
@RequestMapping("/api/ai")
public class AiController {
public record AskRequest(@NotBlank @Size(max=500) String question) {}
private final LlmService llm;
public AiController(LlmService llm) { this.llm=llm; }
@PostMapping("/ask")
@PreAuthorize("hasAuthority('ai:ask')")
public LlmService.Answer ask(@Valid @RequestBody AskRequest req) {
try { return llm.ask(req.question()); }
catch (InterruptedException e) {
Thread.currentThread().interrupt();
throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,"\u95ee\u7b54\u670d\u52a1\u6682\u4e0d\u53ef\u7528");
} catch (Exception e) {
throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,"\u95ee\u7b54\u670d\u52a1\u6682\u4e0d\u53ef\u7528");
}
}
}
