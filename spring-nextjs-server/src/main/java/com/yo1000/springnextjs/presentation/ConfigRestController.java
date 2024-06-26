package com.yo1000.springnextjs.presentation;

import com.yo1000.springnextjs.config.ApplicationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
public class ConfigRestController {
    private final ApplicationProperties props;

    public ConfigRestController(ApplicationProperties props) {
        this.props = props;
    }

    @GetMapping
    public ApplicationProperties.FrontendProperties get() {
        return props.getFrontend();
    }
}
