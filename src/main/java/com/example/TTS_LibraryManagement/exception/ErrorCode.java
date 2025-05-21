package com.example.TTS_LibraryManagement.exception;

import lombok.Getter;


@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION("FAILED","Uncategorized error!"),
    BOOK_EXISTED("BOOK EXISTED","Book existed!"),
    CATEGORY_EXISTED("CATEGORY EXISTED","Category existed!"),
    NOT_BLANK("BLANK","Cannot be left blank!"),
    INVALID_TITLE("INVALID TITLE","Title must be between 1 and 100 characters!"),
    INVALID_AUTHORS("INVALID AUTHORS","Authors must not exceed 100 characters!"),
    INVALID_PUBLISHER("INVALID PUBLISHER","Publisher must not exceed 100 characters!"),
    INVALID_PRINT_TYPE("INVALID PRINT_TYPE","Print type must be 'BOOK'!"),
    INVALID_LANGUAGE("INVALID LANGUAGE","Language type must be either 'vi' or 'en'!"),
    INVALID_DESCRIPTION("INVALID DESCRIPTION","Description must not exceed 500 characters!"),
    INVALID_CATEGORY("INVALID CATEGORY","At least one category is required if provided!"),
    API_NO_RESPONSE("API NO RESPONSE","No response received from Google Books API!"),
    API_NO_DATA("API NO DATA","No data found from Google API!"),
    BOOK_NOT_FOUND("BOOK NOT FOUND","Book not found!"),
    CATEGORY_NOT_FOUND("CATEGORY NOT FOUND","Categories not found!"),
    TITLE_EXISTED("TITLE EXISTED","Title existed!"),
    USERNAME_EXISTED("USERNAME EXISTED","Username existed!"),
    USER_NOT_FOUND("USER NOT FOUND","User not found!"),
    ROLE_EXISTED("ROLE EXISTED","Role existed!"),
    ROLE_NOT_FOUND("ROLE NOT FOUND","Role not found!"),
    ROLE_NOT_CHANGED("ROLE NOT CHANGED","Role has not changed!"),
    PAGE_NO_ERROR("PAGE NO ERROR","PageNo must be greater than 0!"),
    PAGE_SIZE_ERROR("PAGE SIZE ERROR","PageSize must be greater than 0!"),
    IDENTITY_NUMBER_EXISTED("IDENTITY NUMBER EXISTED","Identity Number existed!"),
    AGE_NO_EXISTED("AGE NO EXISTED","No one in this age group!"),
    PERMISSION_EXISTED("PERMISSION EXISTED","Permission existed!"),
    PERMISSION_NOT_FOUND("PERMISSION NOT FOUND","Permission not found!"),
    INVALID_PHONE_NUMBER("INVALID PHONE NUMBER","Phone number must have 9 numbers!"),
    PERMISSION_NOT_CHANGED("PERMISSION NOT CHANGED","Permission has not changed!"),
    BOOK_NOT_CHANGED("BOOK NOT CHANGED","Book has not changed!"),
    USER_NOT_CHANGED("USER NOT CHANGED","User has not changed!"),
    CATEGORY_NOT_CHANGED("CATEGORY NOT CHANGED","Category has not changed!"),
    BORROW_RETURN_BOOK_NOT_CHANGED("BORROW RETURN BOOK NOT CHANGED","Borrow return book has not changed!"),
    ROLE_NOT_DELETED("ROLE NOT DELETED","Role has not deleted!"),
    USER_NOT_DELETED("USER NOT DELETED","User has not deleted!"),
    CATEGORY_NOT_DELETED("CATEGORY NOT DELETED","Category has not deleted!"),
    PERMISSION_NOT_DELETED("PERMISSION NOT DELETED","Permission has not deleted!"),
    BOOK_NOT_DELETED("BOOK NOT DELETED","Book has not deleted!"),
    BORROW_RETURN_BOOK_NOT_DELETED("BORROW RETURN BOOK NOT DELETED NOT DELETED","Borrow return book has not deleted!"),
    FAILED_TO_IMPORT_EXCEL("FAILED TO IMPORT EXCEL","Failed to import excel file!"),
    FAILED_TO_EXPORT_EXCEL("FAILED TO EXPORT EXCEL","Failed to export excel file!"),
    BORROW_RETURN_BOOK_NOT_FOUND("BORROW RETURN BOOK NOT FOUND","Borrow return book not found!"),
    RETURN_DATE_NOT_VALID("RETURN DATE NOT VALID","Return date must be greater than borrow date!"),
    BOOK_NOT_AVAILABLE("BOOK NOT AVAILABLE","Book not available!"),
    BOOK_ALREADY_BORROWED("BOOK ALREADY BORROWED","You already borrowed this book!"),
    BORROW_RETURN_BOOK_STATUS_NOT_VALID("BORROW RETURN BOOK STATUS NOT VALID","Borrow return book status must be 0 or 1!"),
    POST_NOT_FOUND("POST NOT FOUND","Post not found!"),
    POST_NOT_DELETED("POST NOT DELETED","Post has not deleted!"),
    POST_NOT_CHANGED("POST NOT CHANGED","Post has not changed!"),
    COMMENT_NOT_FOUND("COMMENT NOT FOUND","Comment not found!"),
    COMMENT_NOT_DELETED("COMMENT NOT DELETED","Comment has not deleted!"),
    COMMENT_NOT_CHANGED("COMMENT NOT CHANGED","Comment has not changed!"),
    POST_LIKE_EXISTED("POST LIKE EXISTED","Post like existed!"),
    ;
    private String code;
    private String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
