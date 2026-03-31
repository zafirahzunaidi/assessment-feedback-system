package Classes;

public enum PasswordChangeResult {
    SUCCESS,
    EMPTY_FIELDS,
    WRONG_CURRENT_PASSWORD,
    WEAK_PASSWORD,
    SAME_AS_OLD,
    CONFIRM_MISMATCH,
    IO_ERROR    
}
