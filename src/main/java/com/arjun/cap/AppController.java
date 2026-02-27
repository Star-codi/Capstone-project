package com.arjun.cap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class
AppController
{
@GetMapping
(
"/hello"
)
public
String hello() {
return
"Hello from DevOps Pipeline! 🚀"
;
}
}