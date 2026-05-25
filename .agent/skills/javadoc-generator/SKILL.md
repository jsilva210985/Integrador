---
name: javadoc-generator
description: Generates, updates, and formats Javadoc comments for Java methods, classes, and interfaces, and enforces the standard copyright header.
---

# Javadoc Generator Skill

This skill ensures that all Java methods, classes, and interfaces are documented with clean, professional, and comprehensive Javadoc comments, and that files have the correct copyright and overview header.

## When to Use This Skill
- Whenever you are writing new Java code (methods, classes, interfaces).
- When modifying or refactoring existing Java code.
- When explicitly requested by the user to add documentation or headers to Java source files.

## Java File Header / Copyright Enforcer
Whenever you modify or create any Java source file, you must check if it starts with the standard copyright and class overview comment block. If it is missing, prepend it to the file. 

### Header Template
Replace `{current_year}` with the actual current year (e.g., 2026) and write a descriptive summary of the class in the middle paragraph. **The description must be written in English**:

```java
/*
 * ==============================================================================================
 * Copyright (c) {current_year} J. Jose de Jesus Silva A.
 * Contact: jsilva210985@gmail.com
 * 
 * {Description of the class written in English. For example, if it's an address service class:
 * "This service class provides various functionalities related to address management,
 * including retrieving Estafeta delivery frequencies, finding addresses by ID,
 * checking if an alias exists for a user, and saving address details."}
 * 
 * All rights reserved.
 * ==============================================================================================
 */
```

## Guidelines for Javadoc Generation
1. **Language**: All Javadoc comments, descriptions, parameter explanations, return details, and exception details must be written entirely in **English**.
2. **Target Elements**: Add Javadoc to all public, protected, and package-private classes, interfaces, and methods. Optional but recommended for private methods if they contain complex logic.
3. **First Sentence**: The first sentence of the Javadoc must be a brief summary of the element's purpose. Start with a third-person singular verb in English (e.g., "Calculates the total...", "Sends a request to...", "Represents an authenticated session...").
4. **Parameter Descriptions (`@param`)**: Document every parameter in the method signature in English. Format as:
   ```java
   * @param parameterName description of the parameter, starting with a lowercase letter and ending with a period.
   ```
5. **Return Value Description (`@return`)**: Document the return value for all non-void methods in English. Format as:
   ```java
   * @return description of what the method returns.
   ```
6. **Exceptions (`@throws` / `@exception`)**: Document checked exceptions and common runtime exceptions that the method might throw. Format as:
   ```java
   * @throws ExceptionType description of the conditions under which this exception is thrown.
   ```
7. **Formatting**: Use standard HTML tags (like `{@code ...}`, `{@link ...}`, `<p>`, `<ul>`, `<li>`) when necessary to format descriptions and preserve readability.

## Examples

### Good Method Javadoc
```java
    /**
     * Authenticates a user with the provided credentials.
     * <p>
     * This method sends a secure request to the authorization server and
     * retrieves a bearer token if authentication is successful.
     * @param username the username of the user attempting to log in.
     * @param password the password associated with the user's account.
     * @return the authentication token for the session.
     * @throws AuthenticationException if the credentials are invalid or the server is unreachable.
     */
    public String authenticate(String username, String password) throws AuthenticationException {
        // Implementation
    }
```
