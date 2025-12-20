public static String authenticate(Authentication auth) {
    return switch (auth) {
        case TokenAuth t -> "Token authentication: " + t;
        case BiometricAuth b -> "Biometric auth data length: " + b.fingerprintData().length;
        case PasswordAuth p when p.password().length() < 8 ->
            "Weak password";
        case PasswordAuth p ->
            "Password login for " + p.username();
        //
    };
}
