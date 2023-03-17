package sangmyung.chatprompt.Home.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {


    @GetMapping("/")
    public String test(Model model) {
//        Home home = new Home("test");
//        repository.save(home);

        return "editDefinition";
    }
}
