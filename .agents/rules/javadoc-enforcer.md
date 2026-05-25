# Rule: Always Generate Javadoc Comments and Copyright Headers for Java Code

Whenever you write new Java files/methods or modify existing ones in this workspace, you must automatically:
1. Ensure the file has the standard copyright header.
2. Add or update Javadoc comments following standard Java guidelines.

## Requirements

### 1. English Only
- **All documentation, Javadocs, method descriptions, parameter descriptions, return value explanations, and file headers must be written entirely in English.**

### 2. File Copyright Header
At the start of every Java file, verify that the copyright header is present. If missing, prepended it to the top of the file.
- Replace `{current_year}` with the actual current year (e.g., 2026).
- Write a short summary description of the class's purpose in the middle paragraph (must be in English).

Template:
```java
/*
 * ==============================================================================================
 * Copyright (c) {current_year} J. Jose de Jesus Silva A.
 * Contact: jsilva210985@gmail.com
 * 
 * {description_of_the_class_functionality_in_english}
 * 
 * All rights reserved.
 * ==============================================================================================
 */
```

### 3. Method and Class Javadocs
Follow the `javadoc-generator` skill rules for all new or modified public, protected, and package-private classes, interfaces, and methods:
- Use `/** ... */` syntax.
- Start descriptions with third-person singular verbs in English (e.g., "Saves...", "Verifies...").
- Include `@param` tags for all parameters in English.
- Include `@return` tag if the method has a return type, in English.
- Include `@throws` tag if exceptions are declared or commonly thrown, in English.
- Update existing Javadocs to match changes instead of deleting or overwriting them.
