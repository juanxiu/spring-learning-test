package cholog.di;

import org.springframework.stereotype.Component;

@Component
public class InjectionBean {
    private InjectionBean injectionBean;


    public String hello() {
        return "Hello";
    }
}
