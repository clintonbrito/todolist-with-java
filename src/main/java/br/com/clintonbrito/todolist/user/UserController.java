package br.com.clintonbrito.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

/*
 * Modificadores:
 * Public
 * Private
 * Protected
 */

@RestController
@RequestMapping("/users")

public class UserController {
  /*
 * String (texto)
 * Integer (int)
 * Float (float) números 0.00, 6-9 dígitos de precisão
 * Double (double) números 0.0000, 15-17 dígitos de precisão
 * Char (caracteres) A, B, C
 * Date (data)
 * void (usado quando não quero usar nenhum retorno)
 */

  @Autowired
  private IUserRepository userRepository;

  @PostMapping("/")
  public ResponseEntity create(@RequestBody UserModel userModel) {
    var user = this.userRepository.findByUsername(userModel.getUsername());

    if(user != null) {
      // Error message to user
      // Status code
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This user already exists");
    }

    var passwordHashred = BCrypt.withDefaults()
      .hashToString(12, userModel.getPassword().toCharArray());
    
    userModel.setPassword(passwordHashred);

    var userCreated = this.userRepository.save(userModel);
    return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
  }
}
