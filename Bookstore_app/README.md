
## **Lab 2-4** 
---

- design the solution for your problem using a CASE tool (use cases, class diagram, sequence diagram for each use case)
- use feature driven development
- layered architecture 
- data validation
- all functions will be documented and tested
- use Java 8 features (lambda expressions, streams etc); the program should be written without if statements and loops
- persistence: 
     - in memory
	 - text files 
     - xml 
     - db (jdbc); you may use any RDBMS, but we only offer support for PostgreSQL; MS SQL Server is forbidden


---

## **Book Store**

A book publisher manages information about books and clients.

**Create an application which allows to:**

- perform CRUD operations on books
- clients buy books
- filter entities based on various criteria
- reports: e.g. sort clients based on the spent amount of money

---

## *Iteration One:*

---

## Requirements

- two features:
      - E.G.: 
	      - Add Book
	      - List all books  
- java doc in html format (see the example from the group - project_root/doc/index.html - repository interface)
- only inmemory repository is enough

- focus on working with git
- working on only one branch (master/develop) is enough

---

## ** The project should be of type gradle **

---

## *Iteration two:*

---
## Requirements:
---

- Three Features

---

## *Iteration tree:*

---
## Requirements:
---

- All features

---

## ***Very Important***

*Architecture and coding style (5.5p):*

- layered architecture; proper operation/responsibility separation between layers; single responsibility principle etc

- using Java 8 language features - where applicable; note that there are some redundant ifs and loops in my example (actually there are some other issues as well); the application should be written without any ifs and loops (using any if or loop will have to be justified for each occurrence)

- guard against null - where applicable

- data validation

- custom exceptions

- proper exception handling: exceptions should be wrapped in custom exception classes, thrown further, caught in the ui, where a message is presented to the user; the application should never crash, it should always resume; exceptions may also be only logged (for now ex.printStanckTrace() would do), but this decision will have to be justified for each occurrence; having more than one exception in a method signature will have to be justified for each occurrence

- using try-with-resource where applicable

- using NIO.2 where applicable

- using custom generic classes

- the application must be structured as in the example: src/main/java, src/test/java - mixing the tests with the actual code is forbidden

- using java doc comments for the important parts of the application; generate java doc (intellij idea: tools -> generate java doc).

- **there will be a 0.5p penalty of each type of mistake (only applicable starting from I2 (week 3)).**

**Only in week 4 (I3 - the last iteration):**

- **if the file persistence is missing: 1p penalty**
- **if the xml persistence is missing: 2p penalty**
- **if the db persistence is missing: 3p penalty.**

---