package tech.ing.authorization.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class LoginRequestDto {
    private String username;
    private String password;
}
