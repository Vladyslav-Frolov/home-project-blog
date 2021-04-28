package com.softserveinc.ita.homeprojectblog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

import static com.softserveinc.ita.homeprojectblog.util.Constants.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private BigDecimal id;

    @NotBlank
    private String name;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;


    @NotBlank
    private String email;

    @Pattern(regexp = PASSWORD_REGEXP,
            message = WRONG_PASSWORD_PATTERN)
    @Size(min = 8, max = 255,
            message = WRONG_PASSWORD_SIZE)
    private String password;

//    private String createOn;

    /**
     * This is the level of user access to various functions
     */
    public enum RoleEnum {
        GUEST("guest"),

        USER("user"),

        MODERATOR("moderator"),

        ADMIN("admin"),

        EXPERT("expert");

        private String value;

        RoleEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

    }

    private com.softserveinc.ita.homeprojectblog.entity.UserEntity.RoleEnum role;
}