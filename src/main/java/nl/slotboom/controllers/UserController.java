package nl.slotboom.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1")
public class UserController {

}



//
//@Controller // This means that this class is a Controller
//@RequestMapping(path = "/user") // This means URL's start with /user (after Application path)
//public class UserController {
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private JwtTokenUtil jwtTokenUtil;
//
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
//        System.out.println(username);
//        System.out.println(password);
//        User user = userRepository.findByUsername(username);
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
//        }
//        if (!new BCryptPasswordEncoder().matches(password, user.getPassword())) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
//        }
//        String token = jwtTokenUtil.generateToken(user.getUsername());
//        return ResponseEntity.ok(token);
//    }
//
//    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestParam String username, @RequestParam String password) {
//        User existingUser = userRepository.findByUsername(username);
//        if (existingUser != null) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with same username already exists");
//        }
//        User newUser = new User();
//        newUser.setUsername(username);
//        newUser.setPassword(password);
//        newUser.setCreatedAt(new Date());
//        newUser.setUpdatedAt(new Date());
//        userRepository.save(newUser);
//        return ResponseEntity.ok("User created successfully");
//    }
//}

//    @Autowired // This means to get the bean called userRepository
//    // Which is auto-generated by Spring, we will use it to handle the data
//    private UserRepository userRepository;
//
//    @PostMapping(path="/register") // Map ONLY POST Requests
//
////    public @ResponseBody String addNewUser (@RequestParam String name
////            , @RequestParam String email, @RequestParam String password) {
//
//    public @ResponseBody String addNewUser (@RequestBody User user) {
//        // @ResponseBody means the returned String is the response, not a view name
//        // @RequestParam means it is a parameter from the GET or POST request
//
////        User n = new User();
////        n.setName(name);
////        n.setEmail(email);
////        n.setPassword(password);
//
//        userRepository.save(user);
//        return "Saved";
//    }
//
//    @GetMapping(path="/all")
//    public @ResponseBody Iterable<User> getAllUsers() {
//        // This returns a JSON or XML with the users
//        return userRepository.findAll();
//    }

