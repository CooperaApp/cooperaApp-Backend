package com.coopera.cooperaApp.dtos.requests;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class PasswordResetRequest {

        private String newPassword;
        private String confirmPassword;
        private String token;
}
