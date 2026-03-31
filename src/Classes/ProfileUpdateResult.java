package Classes;

public enum ProfileUpdateResult {
    SUCCESS,
    EMPTY_FIELDS,
    INVALID_EMAIL,
    INVALID_PHONE,
    USERNAME_EXISTS,
    PASSWORD_FAILED,
    IO_ERROR
}
