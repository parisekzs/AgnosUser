# AgnosUser
This maven project implements the user repository of the Agnos BI system. It stores user attributes in property files. The name of the directory that contains these properties files is "auth", and the path of this directory must be specified.
This directory will contain the following files:
- application-users.properties: It stores the attributes of users. The keys in this property file are the names of users and the values are JSON strings that describing the other attributes of the users.
- application-roles.properties: It stores the attributes of the roles. The keys in this property file are the names of the roles and the values are JSON strings that describing the other attributes of the roles.
- application-users-roles.properties: It contains the roles of users. The keys in this property file are the names of users and the values are JSON strings that containing a list of roles names belonging to the users.

You can have an overview of our class structure with the diagram below:

![AgnosUser](https://user-images.githubusercontent.com/41894108/114186241-e4c7ec80-9946-11eb-974a-6f14a621a521.png)

The package structure of the project:
- entity: the User, the Role, and the UserRole entities which are persisted in the property files.
- repository: the repository classes, each implements the Spring CrudRepository interface, they persist the entity classes.
- model: you can find the DTO entities here
- converter: contains the converter classes between DTO and DAO entities.

