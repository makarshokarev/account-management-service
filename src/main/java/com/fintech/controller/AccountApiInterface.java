package com.fintech.controller;

import com.fintech.dto.AccountCreateRequest;
import com.fintech.dto.AccountResponse;
import com.fintech.dto.AccountUpdateRequest;
import com.fintech.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Account Management", description = "Operations for managing user accounts")
public interface AccountApiInterface {

    @Operation(
        summary = "Create a new account",
        description = "Creates a new user account with the provided name and phone number. " +
                     "Phone number must be unique and in E.164 international format."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Account successfully created",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AccountResponse.class),
                examples = @ExampleObject(
                    name = "Created Account",
                    value = """
                        {
                          "id": 1,
                          "name": "John Doe",
                          "phoneNr": "+1234567890",
                          "createdTime": "2025-08-25T10:30:00",
                          "modifiedTime": "2025-08-25T10:30:00"
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid input data or duplicate phone number",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Validation Error",
                        value = """
                            {
                              "errorCode": "VALIDATION_ERROR",
                              "message": "Phone number must be in E.164 format",
                              "timestamp": "2025-08-25T10:30:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Duplicate Phone",
                        value = """
                            {
                              "errorCode": "DUPLICATE_PHONE_NUMBER",
                              "message": "Phone number +1234567890 already exists",
                              "timestamp": "2025-08-25T10:30:00"
                            }
                            """
                    )
                }
            )
        )
    })
    ResponseEntity<AccountResponse> createAccount(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Account creation details",
            required = true,
            content = @Content(
                schema = @Schema(implementation = AccountCreateRequest.class),
                examples = @ExampleObject(
                    name = "Create Account Request",
                    value = """
                        {
                          "name": "John Doe",
                          "phoneNr": "+1234567890"
                        }
                        """
                )
            )
        )
        @Valid @RequestBody AccountCreateRequest request
    );

    @Operation(
        summary = "Get account by ID",
        description = "Retrieves a specific account by its unique identifier."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Account found and returned successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AccountResponse.class),
                examples = @ExampleObject(
                    name = "Account Details",
                    value = """
                        {
                          "id": 1,
                          "name": "John Doe",
                          "phoneNr": "+1234567890",
                          "createdTime": "2025-08-25T10:30:00",
                          "modifiedTime": "2025-08-25T10:30:00"
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Account not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "Account Not Found",
                    value = """
                        {
                          "errorCode": "ACCOUNT_NOT_FOUND",
                          "message": "Account not found with ID: 999",
                          "timestamp": "2025-08-25T10:30:00"
                        }
                        """
                )
            )
        )
    })
    ResponseEntity<AccountResponse> getAccountById(
        @Parameter(description = "Unique identifier of the account", example = "1", required = true)
        @PathVariable Long id
    );

    @Operation(
        summary = "Update an existing account",
        description = "Updates an existing account with new name and/or phone number. " +
                     "Phone number must be unique and in E.164 international format."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Account successfully updated",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AccountResponse.class),
                examples = @ExampleObject(
                    name = "Updated Account",
                    value = """
                        {
                          "id": 1,
                          "name": "John Updated",
                          "phoneNr": "+1234567890",
                          "createdTime": "2025-08-25T10:30:00",
                          "modifiedTime": "2025-08-25T12:45:00"
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid input data or duplicate phone number",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Validation Error",
                        value = """
                            {
                              "errorCode": "VALIDATION_ERROR",
                              "message": "Phone number must be in E.164 format",
                              "timestamp": "2025-08-25T12:45:00"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Duplicate Phone",
                        value = """
                            {
                              "errorCode": "DUPLICATE_PHONE_NUMBER",
                              "message": "Phone number +1987654321 already exists",
                              "timestamp": "2025-08-25T12:45:00"
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Account not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "Account Not Found",
                    value = """
                        {
                          "errorCode": "ACCOUNT_NOT_FOUND",
                          "message": "Account not found with ID: 999",
                          "timestamp": "2025-08-25T12:45:00"
                        }
                        """
                )
            )
        )
    })
    ResponseEntity<AccountResponse> updateAccount(
        @Parameter(description = "Unique identifier of the account to update", example = "1", required = true)
        @PathVariable Long id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Account update details",
            required = true,
            content = @Content(
                schema = @Schema(implementation = AccountUpdateRequest.class),
                examples = @ExampleObject(
                    name = "Update Account Request",
                    value = """
                        {
                          "name": "John Updated",
                          "phoneNr": "+1234567890"
                        }
                        """
                )
            )
        )
        @Valid @RequestBody AccountUpdateRequest request
    );

    @Operation(
        summary = "Delete an account",
        description = "Permanently deletes an account from the system by its unique identifier."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204", 
            description = "Account successfully deleted"
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Account not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "Account Not Found",
                    value = """
                        {
                          "errorCode": "ACCOUNT_NOT_FOUND",
                          "message": "Account not found with ID: 999",
                          "timestamp": "2025-08-25T13:00:00"
                        }
                        """
                )
            )
        )
    })
    ResponseEntity<Void> deleteAccount(
        @Parameter(description = "Unique identifier of the account to delete", example = "1", required = true)
        @PathVariable Long id
    );
}