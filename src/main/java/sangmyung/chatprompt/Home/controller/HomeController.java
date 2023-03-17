package sangmyung.chatprompt.Home.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sangmyung.chatprompt.Home.domain.Home;
import sangmyung.chatprompt.Home.repository.HomeRepository;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final HomeRepository repository;

    @GetMapping("/")
    public String test() {
//        Home home = new Home("test");
//        repository.save(home);

        return "editDefinition";
    }
}
