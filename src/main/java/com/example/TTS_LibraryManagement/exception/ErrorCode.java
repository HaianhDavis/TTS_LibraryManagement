package com.example.TTS_LibraryManagement.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;


@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION("FAILED","Uncategorized error!", HttpStatus.INTERNAL_SERVER_ERROR),
    BOOK_EXISTED("BOOK EXISTED","Book existed!" , HttpStatus.BAD_REQUEST),
    CATEGORY_EXISTED("CATEGORY EXISTED","Category existed!" , HttpStatus.BAD_REQUEST),
    NOT_BLANK("BLANK","Cannot be left blank!" , HttpStatus.BAD_REQUEST),
    INVALID_TITLE("INVALID TITLE","Title must be between 1 and 100 characters", HttpStatus.BAD_REQUEST ),
    INVALID_AUTHORS("INVALID AUTHORS","Authors must not exceed 100 characters!" , HttpStatus.BAD_REQUEST),
    INVALID_PUBLISHER("INVALID PUBLISHER","Publisher must not exceed 100 characters!" , HttpStatus.BAD_REQUEST),
    INVALID_PRINT_TYPE("INVALID PRINT_TYPE","Print type must be 'BOOK'!" , HttpStatus.BAD_REQUEST),
    INVALID_LANGUAGE("INVALID LANGUAGE","Language type must be either 'vi' or 'en'!" , HttpStatus.BAD_REQUEST),
    INVALID_DESCRIPTION("INVALID DESCRIPTION","Description must not exceed 500 characters!" , HttpStatus.BAD_REQUEST),
    INVALID_CATEGORY("INVALID CATEGORY","At least one category is required if provided!" , HttpStatus.BAD_REQUEST),
    API_NO_RESPONSE("API NO RESPONSE","No response received from Google Books API!" , HttpStatus.BAD_REQUEST),
    API_NO_DATA("API NO DATA","No data found from Google API!" , HttpStatus.BAD_REQUEST),
    BOOK_NOT_FOUND("BOOK NOT FOUND","Book not found!" , HttpStatus.NOT_FOUND),
    CATEGORY_NOT_FOUND("CATEGORY NOT FOUND","Categories not found!"  , HttpStatus.NOT_FOUND),
    TITLE_EXISTED("TITLE EXISTED","Title existed!" , HttpStatus.BAD_REQUEST),
    USERNAME_EXISTED("USERNAME EXISTED","Username existed!" , HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("USER NOT FOUND","User not found!" , HttpStatus.NOT_FOUND),
    ROLE_EXISTED("ROLE EXISTED","Role existed!"  , HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND("ROLE NOT FOUND","Role not found!" , HttpStatus.NOT_FOUND),
    ROLE_NOT_CHANGED("ROLE NOT CHANGED","Role has not changed!" , HttpStatus.BAD_REQUEST),
    PAGE_NO_ERROR("PAGE NO ERROR","PageNo must be greater than 0!" , HttpStatus.BAD_REQUEST),
    PAGE_SIZE_ERROR("PAGE SIZE ERROR","PageSize must be greater than 0!" , HttpStatus.BAD_REQUEST),
    IDENTITY_NUMBER_EXISTED("IDENTITY NUMBER EXISTED","Identity Number existed!" , HttpStatus.BAD_REQUEST),
    AGE_NO_EXISTED("AGE NO EXISTED","No one in this age group!" , HttpStatus.BAD_REQUEST),
    PERMISSION_EXISTED("PERMISSION EXISTED","Permission existed!" , HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_FOUND("PERMISSION NOT FOUND","Permission not found!" , HttpStatus.NOT_FOUND),
    INVALID_PHONE_NUMBER("INVALID PHONE NUMBER","Phone number must have 9 numbers!" , HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_CHANGED("PERMISSION NOT CHANGED","Permission has not changed!" , HttpStatus.BAD_REQUEST),
    BOOK_NOT_CHANGED("BOOK NOT CHANGED","Book has not changed!" , HttpStatus.BAD_REQUEST),
    USER_NOT_CHANGED("USER NOT CHANGED","User has not changed!" , HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_CHANGED("CATEGORY NOT CHANGED","Category has not changed!" , HttpStatus.BAD_REQUEST),
    BORROW_RETURN_BOOK_NOT_CHANGED("BORROW RETURN BOOK NOT CHANGED","Borrow return book has not changed!" , HttpStatus.BAD_REQUEST),
    ROLE_NOT_DELETED("ROLE NOT DELETED","Role has not deleted!" , HttpStatus.BAD_REQUEST),
    USER_NOT_DELETED("USER NOT DELETED","User has not deleted!" , HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_DELETED("CATEGORY NOT DELETED","Category has not deleted!"  , HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_DELETED("PERMISSION NOT DELETED","Permission has not deleted!" , HttpStatus.BAD_REQUEST),
    BOOK_NOT_DELETED("BOOK NOT DELETED","Book has not deleted!" , HttpStatus.BAD_REQUEST),
    BORROW_RETURN_BOOK_NOT_DELETED("BORROW RETURN BOOK NOT DELETED NOT DELETED","Borrow return book has not deleted!" , HttpStatus.BAD_REQUEST),
    FAILED_TO_IMPORT_EXCEL("FAILED TO IMPORT EXCEL","Failed to import excel file!" , HttpStatus.BAD_REQUEST),
    FAILED_TO_EXPORT_EXCEL("FAILED TO EXPORT EXCEL","Failed to export excel file!" , HttpStatus.BAD_REQUEST),
    BORROW_RETURN_BOOK_NOT_FOUND("BORROW RETURN BOOK NOT FOUND","Borrow return book not found!" , HttpStatus.NOT_FOUND),
    RETURN_DATE_NOT_VALID("RETURN DATE NOT VALID","Return date must be greater than borrow date!" , HttpStatus.BAD_REQUEST),
    BOOK_NOT_AVAILABLE("BOOK NOT AVAILABLE","Book not available!" , HttpStatus.BAD_REQUEST),
    BOOK_ALREADY_BORROWED("BOOK ALREADY BORROWED","You already borrowed this book!" , HttpStatus.BAD_REQUEST),
    BORROW_RETURN_BOOK_STATUS_NOT_VALID("BORROW RETURN BOOK STATUS NOT VALID","Borrow return book status must be 0 or 1!" , HttpStatus.BAD_REQUEST),
    POST_NOT_FOUND("POST NOT FOUND","Post not found!" , HttpStatus.NOT_FOUND),
    POST_NOT_DELETED("POST NOT DELETED","Post has not deleted!" , HttpStatus.BAD_REQUEST),
    POST_NOT_CHANGED("POST NOT CHANGED","Post has not changed!" , HttpStatus.BAD_REQUEST),
    COMMENT_NOT_FOUND("COMMENT NOT FOUND","Comment not found!" , HttpStatus.NOT_FOUND),
    COMMENT_NOT_DELETED("COMMENT NOT DELETED","Comment has not deleted!" , HttpStatus.BAD_REQUEST),
    COMMENT_NOT_CHANGED("COMMENT NOT CHANGED","Comment has not changed!" , HttpStatus.BAD_REQUEST),
    POST_LIKE_EXISTED("POST LIKE EXISTED","Post like existed!" , HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED("UNAUTHENTICATED","User is not authenticated!" , HttpStatus.UNAUTHORIZED),
    INVALID_INPUT("INVALID INPUT", "Invalid input data", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("UNAUTHORIZED","User do not have permission!" , HttpStatus.FORBIDDEN)
    ;
    private String code;
    private String message;
    private HttpStatusCode statusCode;
    ErrorCode(String code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
